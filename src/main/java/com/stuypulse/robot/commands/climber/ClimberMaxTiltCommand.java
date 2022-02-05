/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ClimberMaxTiltCommand extends InstantCommand {

    private final Climber climber;

    public ClimberMaxTiltCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setTilt(Climber.Tilt.MAX_TILT);
    }
}
