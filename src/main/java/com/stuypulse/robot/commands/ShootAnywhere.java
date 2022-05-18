/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.stuylib.math.interpolation.CubicInterpolator;
import com.stuypulse.stuylib.math.interpolation.Interpolator;
import com.stuypulse.stuylib.math.interpolation.NearestInterpolator;
import com.stuypulse.stuylib.streams.IFuser;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounceRC;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;
import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistance;
import com.stuypulse.robot.commands.leds.LEDSet;
import com.stuypulse.robot.constants.RPMMap;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Ports.LEDController;
import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.Camera;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.Shooter;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShootAnywhere extends CommandBase {

    private final Conveyor conveyor;
    private final Drivetrain drivetrain;
    private final Shooter shooter;
    private final RobotContainer robot;

    private final IFuser angleError;
    private final IFuser distance;

    private final BStream readyToShoot;

    protected final Controller angleController;

    // private final double minDistance;
    // private final double backoffSpeed;

    public ShootAnywhere(RobotContainer robot) {
        this.conveyor = robot.conveyor;
        this.drivetrain = robot.drivetrain;
        this.shooter = robot.shooter;
        this.robot = robot;

        // find errors
        angleError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> robot.camera.getXAngle()
                                .add(Angle.fromDegrees(RPMMap.distanceToAngleOffset.interpolate(robot.camera.getDistance())))
                                .toDegrees(),
                        () -> drivetrain.getRawGyroAngle());

        distance =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> robot.camera.getDistance(),
                        () -> -drivetrain.getDistance());

        // handle errors
        this.angleController = Alignment.Angle.getController();

        // finish optimally
        readyToShoot =
                BStream.create(() -> shooter.isReady())
                        .and(() -> angleController.isDone(Limelight.MAX_ANGLE_ERROR.get()))
                        .and(() -> distance.get() > RPMMap.minDistance)
                        .filtered(new BDebounceRC.Rising(Limelight.DEBOUNCE_TIME));

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.setLowGear();
        shooter.retractHood();
        angleError.initialize();
        distance.initialize();
    }

    private double getTurn() {
        return angleController.update(angleError.get());
    }

    private double getTargetRPM() {
        return RPMMap.distanceToRPM.get(distance.get());
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(0.0, getTurn());
        shooter.setShooterRPM(getTargetRPM());

        if(readyToShoot.get()) {
            conveyor.setMode(ConveyorMode.SEMI_AUTO);
        } else {
            conveyor.setMode(ConveyorMode.DEFAULT);
        }

        while(distance.get() > RPMMap.minDistance){
            new LEDSet(robot.leds, LEDColor.PURPLE);
        }

        // Debug Info
        if (Settings.DEBUG_MODE.get()) {

            SmartDashboard.putNumber("Shooter/ShootAnywhere/TargetRPM", getTargetRPM());
            SmartDashboard.putNumber("Shooter/ShootAnywhere/Distace", distance.get());
            SmartDashboard.putNumber("Shooter/ShootAnywhere/CurrentRPM", shooter.getShooterRPM());

        }
        
    }

    @Override
    public boolean isFinished() {
        return conveyor.isEmpty();
    }
}
