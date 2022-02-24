/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.math.SLMath;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.IFilterGroup;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Drivetrain.Stalling;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainDriveCommand extends CommandBase {

    private Drivetrain drivetrain;
    private Gamepad driver;

    private Debouncer stallingFilter = new Debouncer(Stalling.DEBOUNCE_TIME, DebounceType.kBoth);

    private IFilter speedFilter =
            new IFilterGroup(
                    x -> SLMath.deadband(x, Settings.Drivetrain.SPEED_DEADBAND.get()),
                    x -> SLMath.spow(x, Settings.Drivetrain.SPEED_POWER.get()),
                    new LowPassFilter(Settings.Drivetrain.SPEED_FILTER));

    private IFilter angleFilter =
            new IFilterGroup(
                    x -> SLMath.deadband(x, Settings.Drivetrain.ANGLE_DEADBAND.get()),
                    x -> SLMath.spow(x, Settings.Drivetrain.ANGLE_POWER.get()),
                    new LowPassFilter(Settings.Drivetrain.ANGLE_FILTER));

    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad driver) {
        this.drivetrain = drivetrain;
        this.driver = driver;

        addRequirements(drivetrain);
    }

    private boolean isStalling() {
        return stallingFilter.calculate(drivetrain.isStalling());
    }

    public void execute() {
        double speed = driver.getRightTrigger() - driver.getLeftTrigger();
        double angle = driver.getLeftX();

        if (driver.getRawRightButton() || (Stalling.STALL_DETECTION.get() && isStalling())) {
            drivetrain.setLowGear();
            drivetrain.arcadeDrive(speedFilter.get(speed), angleFilter.get(angle));
        } else {
            drivetrain.setHighGear();
            drivetrain.curvatureDrive(speedFilter.get(speed), angleFilter.get(angle));
        }
    }

    public boolean isFinished() {
        return false;
    }
}
