/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShooterPadShot extends SequentialCommandGroup {

    public ShooterPadShot(Shooter shooter) {
        addCommands(new ShooterRetractHood(shooter));
        addCommands(new ShooterSetRPM(shooter, Settings.Shooter.PAD_RPM));
    }
}
