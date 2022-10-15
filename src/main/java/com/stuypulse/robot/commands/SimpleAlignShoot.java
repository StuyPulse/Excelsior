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
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SimpleAlignShoot extends CommandBase {

    // subsystems
    private final Camera camera;
    private final Conveyor conveyor;
    private final Drivetrain drivetrain;
    private final Shooter shooter;

    // angle control
    private final Controller angleController;
    private final IFuser angleError;

    // determine when to switch between shooting and aligning
    private final BStream readyToShoot;

    public SimpleAlignShoot(RobotContainer robot) {
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
        

        // handle errors
        this.angleController = Alignment.Angle.getController();

        // figure out when ready to shoot
        readyToShoot =
                BStream.create(camera::hasAnyTarget)
                        .and(shooter::isReady)
                        .and(
                                () ->
                                        Math.abs(drivetrain.getVelocity())
                                                < Limelight.MAX_VELOCITY.get())
                        .and(() -> angleController.isDone(Limelight.MAX_ANGLE_ERROR.get()))
                        .filtered(new BDebounceRC.Rising(Limelight.DEBOUNCE_TIME));

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.setLowGear();
        shooter.retractHood();

        angleError.initialize();
    }

    private double getTurn() {
        return angleController.update(angleError.get());
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(0, getTurn());

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
