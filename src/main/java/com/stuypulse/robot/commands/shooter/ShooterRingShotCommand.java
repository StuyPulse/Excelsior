/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.commands.conveyor.ConveyorIndexCommand;
import com.stuypulse.robot.commands.conveyor.modes.ConveyorShotMode;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShooterRingShotCommand extends SequentialCommandGroup {

    public ShooterRingShotCommand(Shooter shooter, Conveyor conveyor) {
        addCommands(new ShooterRetractHoodCommand(shooter));
        addCommands(new ShooterSetRPMCommand(shooter, Settings.Shooter.RING_RPM));
        addCommands(new ConveyorIndexCommand(conveyor, ConveyorShotMode.FENDER));
    }
}
