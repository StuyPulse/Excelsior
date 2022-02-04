/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.Constants.ShooterSettings;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class ShooterRingShotCommand extends ParallelCommandGroup {

    public ShooterRingShotCommand(Shooter shooter) {
        addCommands(new ShooterRetractHoodCommand(shooter));
        addCommands(new ShooterSetRPMCommand(shooter, ShooterSettings.RING_RPM));
    }
}
