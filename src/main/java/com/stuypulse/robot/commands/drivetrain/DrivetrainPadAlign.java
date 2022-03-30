/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.streams.IFuser;

import com.stuypulse.robot.commands.ThenShoot;
import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.Camera;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainPadAlign extends CommandBase {

    private final Drivetrain drivetrain;
    private final Camera camera;

    private final Debouncer finished;

    private final IFuser angleError;
    protected final Controller angleController;

    public DrivetrainPadAlign(Drivetrain drivetrain, Camera camera) {
        this.drivetrain = drivetrain;
        this.camera = camera;

        // find errors
        angleError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> camera.getXAngle().toDegrees(),
                        () -> drivetrain.getRawGyroAngle());

        // handle errors
        this.angleController = Alignment.Angle.getController();

        // finish optimally
        finished = new Debouncer(Limelight.DEBOUNCE_TIME, DebounceType.kRising);

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.setLowGear();

        angleError.initialize();
    }

    private double getTurn() {
        return angleController.update(angleError.get());
    }

    @Override
    public void execute() {
        double turn = 2.0 * getTurn();

        if (turn < 0) {
            drivetrain.tankDrive(turn, 0);
        } else {
            drivetrain.tankDrive(0, -turn);
        }
    }

    @Override
    public boolean isFinished() {
        return finished.calculate(
                camera.hasTarget() && angleController.isDone(Limelight.MAX_ANGLE_ERROR.get()));
    }

    public Command thenShoot(Conveyor conveyor) {
        return new ThenShoot(this, conveyor);
    }
}
