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

public class DrivetrainAlignCommand extends DrivetrainAlignAngleCommand {

    private final IFuser distanceError;

    private IFilter speedAdjFilter;
    private final Controller distanceController;

    public DrivetrainAlignCommand(Drivetrain drivetrain, Number targetDistance, double debounceTime) {
        super(drivetrain, debounceTime);

        // find distance errors
        distanceError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> targetDistance.doubleValue() - Target.getDistance(),
                        () -> drivetrain.getDistance());

        // handle distance errors
        speedAdjFilter = new LowPassFilter(Alignment.SPEED_ADJ_FILTER);
        this.distanceController = Alignment.Speed.getController();
    }

    public DrivetrainAlignCommand(Drivetrain drivetrain, Number targetDistance) {
        this(drivetrain, targetDistance, Limelight.DEBOUNCE_TIME);
    }

    @Override
    public void initialize() {
        super.initialize();
        distanceError.initialize();
    }

    private double getSpeedAdjustment() {
        double error = getAngleError() / Limelight.MAX_ANGLE_FOR_MOVEMENT.get();
        return speedAdjFilter.get(Math.exp(-error * error));
    }

    private double getSpeed() {
        double speed = distanceController.update(distanceError.get());
        return speed * getSpeedAdjustment();
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(getSpeed(), getTurn());
    }

    @Override
    public boolean isAlignmentDone() {
        return super.isAlignmentDone() && 
            drivetrain.getVelocity() < Limelight.MAX_VELOCITY.get() &&
            distanceController.isDone(Limelight.MAX_ANGLE_ERROR.get());
    }

}
