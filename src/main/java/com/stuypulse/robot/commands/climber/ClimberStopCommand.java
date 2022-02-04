/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.InstantCommand;

import com.stuypulse.robot.subsystems.Climber;

public class ClimberStopCommand extends InstantCommand {

    private final Climber climber;

    public ClimberStopCommand (Climber climber) {
            this.climber = climber;
            addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setMotorStop();
        climber.setClimberLocked();
    }
}