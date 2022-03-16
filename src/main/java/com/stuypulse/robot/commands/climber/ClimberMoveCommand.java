/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

import com.stuypulse.robot.subsystems.Climber;

import com.stuypulse.robot.constants.Settings.Climber.Encoders;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberMoveCommand extends ClimberMoveToPositionCommand {

    public ClimberMoveCommand(Climber climber, double speed, boolean movingUp) {
        super(climber, speed, (movingUp ? Encoders.MAX_EXTENSION.get() : 0));
    }

}
