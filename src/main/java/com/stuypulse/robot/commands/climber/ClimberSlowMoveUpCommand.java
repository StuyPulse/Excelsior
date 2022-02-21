/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Climber;

public class ClimberSlowMoveUpCommand extends ClimberMoveCommand {

    public ClimberSlowMoveUpCommand(Climber climber) {
        super(climber, Settings.Climber.CLIMBER_SLOW_SPEED, true);
    }
}
