/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShooterFenderShot extends SequentialCommandGroup {

    public ShooterFenderShot(IShooter shooter) {
        addCommands(new ShooterExtendHood(shooter));
        addCommands(new ShooterSetRPM(shooter, Settings.Shooter.FENDER_RPM));
    }
}
