/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.IDrivetrain;
import com.stuypulse.robot.subsystems.IDrivetrain.Gear;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DrivetrainLowGear extends InstantCommand {

    private IDrivetrain drivetrain;

    public DrivetrainLowGear(IDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.setGear(Gear.LOW);
    }
}
