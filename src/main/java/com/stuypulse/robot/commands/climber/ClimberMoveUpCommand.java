/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.stuypulse.robot.subsystems.Climber;
import com.stuypulse.robot.Constants;

public class ClimberMoveUpCommand extends ClimberMoveCommand {

    public ClimberMoveUpCommand(Climber climber) {
        super(climber, Constants.ClimberSettings.CLIMBER_DEFAULT_SPEED, true);
    }
    
}