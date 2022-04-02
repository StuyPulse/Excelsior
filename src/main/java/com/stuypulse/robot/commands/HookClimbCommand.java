/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import com.stuypulse.robot.constants.Settings.Climber.Auto;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.climber.ClimberMaxTiltCommand;
import com.stuypulse.robot.commands.climber.ClimberMoveDistanceCommand;
import com.stuypulse.robot.commands.climber.ClimberNoTiltCommand;
import com.stuypulse.robot.commands.climber.ClimberPullUp;
import com.stuypulse.robot.commands.climber.ClimberReset;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This is a helpful mechanism where it can hook onto the rung 
 * (Convenience)
 */
public class HookClimbCommand extends SequentialCommandGroup {
    public HookClimbCommand(RobotContainer robot) {

        /**
         * pulls the robot up until passive hooks clear the mid bar
         */
        addCommands(
            new ClimberPullUp(robot.climber)
        );
        
    }
}
