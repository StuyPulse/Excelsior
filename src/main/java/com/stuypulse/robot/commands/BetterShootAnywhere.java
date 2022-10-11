/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.math.SLMath;
import com.stuypulse.stuylib.streams.IFuser;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounceRC;
import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.constants.ShotMap;
import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.Camera;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.IDrivetrain;
import com.stuypulse.robot.subsystems.Shooter;
import com.stuypulse.robot.subsystems.IDrivetrain.Gear;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class BetterShootAnywhere extends CommandBase {

    // subsystems
    private final Camera camera;
    private final Conveyor conveyor;
    private final IDrivetrain drivetrain;
    private final Shooter shooter;

    // angle control
    private final Controller angleController;
    private final IFuser angleError;

    // distance control
    private final Controller distanceController;
    private final IFuser distance;

    // determine when to switch between shooting and aligning
    private final BStream readyToShoot;

    public BetterShootAnywhere(RobotContainer robot) {
        this.camera = robot.camera;
        this.conveyor = robot.conveyor;
        this.drivetrain = robot.drivetrain;
        this.shooter = robot.shooter;

        // find angle error
        angleError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> camera.getXAngle()
                                .add(ShotMap.getAngle(camera.getDistance()))
                                .toDegrees(),
                        () -> drivetrain.getRawGyroAngle());
        
        // find fused distance
        distance =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> camera.getDistance(),
                        () -> -drivetrain.getDistance());

        // handle errors
        this.angleController = Alignment.Angle.getController();
        this.distanceController = Alignment.Speed.getController(angleError::get);

        // figure out when ready to shoot
        readyToShoot =
                BStream.create(camera::hasAnyTarget)
                        .and(shooter::isReady)
                        .and(
                                () ->
                                        Math.abs(drivetrain.getVelocity())
                                                < Limelight.MAX_VELOCITY.get())
                        .and(() -> angleController.isDone(Limelight.MAX_ANGLE_ERROR.get()))
                        .and(() -> distanceController.isDone(Limelight.MAX_DISTANCE_ERROR.get()))
                        .filtered(new BDebounceRC.Rising(Limelight.DEBOUNCE_TIME));

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.setGear(Gear.LOW);
        shooter.retractHood();

        angleError.initialize();
        distance.initialize();
    }

    private double getTargetDistance() {
        return SLMath.clamp(distance.get(), Limelight.RING_DISTANCE.get(), Limelight.PAD_DISTANCE.get());
    }

    private double getSpeed() {
        return distanceController.update(getTargetDistance(), distance.get());
    }

    private double getTurn() {
        return angleController.update(angleError.get());
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(getSpeed(), getTurn());
        shooter.setShooterRPM(ShotMap.getRPM(distance.get()));

        if (readyToShoot.get()) {
            conveyor.setMode(ConveyorMode.SEMI_AUTO);
        } else {
            conveyor.setMode(ConveyorMode.DEFAULT);
        }
    }

    @Override
    public boolean isFinished() {
        return conveyor.isEmpty();
    }

    @Override
    public void end(boolean interrupted) {
        conveyor.setMode(ConveyorMode.DEFAULT);
    }
}
