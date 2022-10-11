/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.IDrivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainDriveForever extends CommandBase {
    private final IDrivetrain drivetrain;
    private final double speed;

    public DrivetrainDriveForever(IDrivetrain drivetrain, double speed) {
        this.drivetrain = drivetrain;
        this.speed = speed;

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.curvatureDrive(speed, 0);
    }
    
}
