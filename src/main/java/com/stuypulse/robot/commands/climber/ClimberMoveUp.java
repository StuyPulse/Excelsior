/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Climber;

public class ClimberMoveUp extends ClimberMove {

    public ClimberMoveUp(Climber climber) {
        super(climber, Settings.Climber.DEFAULT_SPEED, true);
    }
}
