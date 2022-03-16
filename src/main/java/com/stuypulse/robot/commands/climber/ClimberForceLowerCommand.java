/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberForceLowerCommand extends CommandBase {

    private final Climber climber;

    public ClimberForceLowerCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        climber.forceLowerClimber();
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
    }
}
