/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DrivetrainHighGearCommand extends InstantCommand {
    private Drivetrain drivetrain;

    public DrivetrainHighGearCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        addRequirements(this.drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.setHighGear();
    }
}
