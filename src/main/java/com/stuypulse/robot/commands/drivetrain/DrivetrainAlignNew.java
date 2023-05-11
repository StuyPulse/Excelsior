/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.streams.IFuser;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounceRC;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import com.stuypulse.robot.commands.ThenShoot;
import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.Camera;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainAlignNew extends CommandBase {

    private final Drivetrain drivetrain;
    private final Camera camera;

    private final BStream finished;

    private final Controller angleController;
    private final Controller speedController;

    public DrivetrainAlignNew(Drivetrain drivetrain, Camera camera) {
        this.drivetrain = drivetrain;
        this.camera = camera;

        // handle errors
        this.angleController = new PIDController(0.0366, 0, 0.0034);
        this.speedController = new PIDController(0.5, 0, 0.05);

        // finish optimally
        finished = BStream.create(camera::hasTarget)
                .and(() -> Math.abs(drivetrain.getVelocity()) < Limelight.MAX_VELOCITY.get())
                .and(() -> angleController.isDone(Limelight.MAX_ANGLE_ERROR.get()))
                .filtered(new BDebounceRC.Rising(Limelight.DEBOUNCE_TIME));

        addRequirements(drivetrain);
    }

    
    @Override
    public void initialize() {
        drivetrain.setLowGear();
    }

    @Override
    public void execute() {
        if (camera.hasAnyTarget()) {
            drivetrain.arcadeDrive(
                speedController.update(Settings.Limelight.RING_DISTANCE.get(), camera.getDistance()),
                angleController.update(0, camera.getXAngle().toDegrees()));
        }
    }

    
    @Override
    public boolean isFinished() {
        return finished.get();
    }

    public Command thenShoot(Conveyor conveyor) {
        return new ThenShoot(this, conveyor, ConveyorMode.SHOOT);
    }
}
