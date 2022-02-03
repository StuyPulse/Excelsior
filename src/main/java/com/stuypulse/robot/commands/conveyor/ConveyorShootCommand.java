/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.conveyor;

import com.stuypulse.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ConveyorShootCommand extends CommandBase {
    private final Conveyor conveyor;

    /** Creates a new ConveyorShootCommand. */
    public ConveyorShootCommand(Conveyor conveyor) {
        this.conveyor = conveyor;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(conveyor);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        conveyor.setShoot(true);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        conveyor.setShoot(false);
    }
}
