/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.stuylib.control.Controller;

import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.IFuser;
import com.stuypulse.robot.util.Target;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainAlignCommand extends CommandBase {

    private final Drivetrain drivetrain;

    private final IFuser angleError;
    private final IFuser distanceError;

    private final Controller angleController;
    private final Controller distanceController;

    public DrivetrainAlignCommand(Drivetrain drivetrain, Number targetDistance) {
        this.drivetrain = drivetrain;

        angleError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> Target.getXAngle().toDegrees(),
                        () -> drivetrain.getRawGyroAngle());

        distanceError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> Target.getDistance() - targetDistance.doubleValue(),
                        () -> drivetrain.getDistance());

        angleController = Alignment.Angle.getController();
        distanceController = Alignment.Speed.getController();

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Target.enable();

        drivetrain.reset();
        drivetrain.setLowGear();

        angleError.initialize();
        distanceError.initialize();
    }

    private double getSpeedAdjustment() {
        double error = angleError.get() / Limelight.MAX_ANGLE_FOR_MOVEMENT.get();
        return Math.exp(-error * error);
    }

    public double getSpeed() {
        double speed = distanceController.update(distanceError.get());
        return speed * getSpeedAdjustment();
    }

    public double getTurn() {
        return angleController.update(angleError.get());
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(getSpeed(), getTurn());
    }

    @Override
    public void end(boolean interrupted) {
        Target.disable();
    }

    @Override
    public boolean isFinished() {
        return Target.hasTarget()
                && angleController.isDone(Limelight.MAX_ANGLE_ERROR.get())
                && distanceController.isDone(Limelight.MAX_DISTANCE_ERROR.get());
    }
}
