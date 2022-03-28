/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.streams.IFuser;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.Target;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainAlignCommand extends CommandBase {

    private final Drivetrain drivetrain;

    private final Debouncer finished;

    private IFilter speedAdjFilter;

    private IFuser angleError;
    private IFuser distanceError;
    private final Number targetDistance;

    protected final Controller angleController;
    protected final Controller distanceController;

    public DrivetrainAlignCommand(Drivetrain drivetrain, Number targetDistance, double debounceTime) {
        this.drivetrain = drivetrain;

        // find angle error based on offset to limelight and gyroscope
        angleError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> Target.getXAngle().toDegrees(),
                        () -> drivetrain.getRawGyroAngle());

        // find distance error based on calculated distance and encoders
        this.targetDistance = targetDistance;
        enableDistance();

        // distanceError =
        //         new IFuser(
        //                 Alignment.FUSION_FILTER,
        //                 () -> targetDistance.doubleValue() - Target.getDistance(),
        //                 () -> drivetrain.getDistance());

        // handle errors
        speedAdjFilter = new LowPassFilter(Alignment.SPEED_ADJ_FILTER);
        this.angleController = Alignment.Angle.getController();
        this.distanceController = Alignment.Speed.getController();

        // finish optimally
        finished = new Debouncer(debounceTime, DebounceType.kRising);

        addRequirements(drivetrain);
    }

    public DrivetrainAlignCommand disableDistance() {
        // private static final IFuser kNoDistance = 
            // new IFuser(0.0, () -> 0.0, () -> 0.0);
        this.distanceError = new IFuser(0.0, () -> 0.0, () -> 0.0);
        return this;
    }

    public DrivetrainAlignCommand enableDistance() {
        this.distanceError = new IFuser(
            Alignment.FUSION_FILTER,
            () -> targetDistance.doubleValue() - Target.getDistance(),
            () -> drivetrain.getDistance()
        );
        return this;
    }

    public DrivetrainAlignCommand(Drivetrain drivetrain, Number targetDistance) {
        this(drivetrain, targetDistance, Limelight.DEBOUNCE_TIME);
    }

    @Override
    public void initialize() {
        Target.enable();
        drivetrain.setLowGear();

        speedAdjFilter = new LowPassFilter(Alignment.SPEED_ADJ_FILTER);

        angleError.initialize();
        distanceError.initialize();
    }

    private double getSpeedAdjustment() {
        double error = angleError.get() / Limelight.MAX_ANGLE_FOR_MOVEMENT.get();
        return speedAdjFilter.get(Math.exp(-error * error));
    }

    private double getSpeed() {
        double speed = distanceController.update(distanceError.get());
        return speed * getSpeedAdjustment();
    }

    private double getTurn() {
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
        return finished.calculate(
                Target.hasTarget()
                        && drivetrain.getVelocity() < Limelight.MAX_VELOCITY.get()
                        && angleController.isDone(Limelight.MAX_ANGLE_ERROR.get())
                        && distanceController.isDone(Limelight.MAX_DISTANCE_ERROR.get()));
    }
}