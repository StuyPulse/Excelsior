/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.math.SLMath;

import com.stuypulse.robot.Constants.LimelightSettings;
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
                        LimelightSettings.Alignment.FUSION_FILTER,
                        () -> Target.getXAngle().toDegrees(),
                        () -> drivetrain.getAngle().toDegrees());

        distanceError =
                new IFuser(
                        LimelightSettings.Alignment.FUSION_FILTER,
                        () -> Target.getDistance() - targetDistance.doubleValue(),
                        () -> drivetrain.getDistance());

        angleController = LimelightSettings.Alignment.Angle.getController();
        distanceController = LimelightSettings.Alignment.Speed.getController();

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Target.enable();

        drivetrain.setLowGear();

        angleError.initialize();
        distanceError.initialize();
    }

    public double getSpeed() {
        double speed = distanceController.update(distanceError.get());
        double maxAngleError = LimelightSettings.MAX_ANGLE_ERROR.get();

        return speed * Math.exp(-SLMath.fpow(angleError.get() / maxAngleError, 2));
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
        return angleController.isDone(LimelightSettings.MAX_ANGLE_ERROR.get())
                && distanceController.isDone(LimelightSettings.MAX_DISTANCE_ERROR.get());
    }
}
