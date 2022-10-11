/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.subsystems.IClimber;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ClimberStop extends InstantCommand {

    private final IClimber climber;

    public ClimberStop(IClimber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setMotorStop();
    }
}
