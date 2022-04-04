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
 * Auto starts with active hook above mid rung
 * 
 * Starts by lowering active hook until passive takes over (transition command)
 * Raises active hook to above the shooter bar
 * Tilt hook
 * Raise active hook to high bar
 * Pause for input then tilt
 * Transition
 * Raise active hook to traversal bar
 * Pause for input then tilt
 */
public class AutoClimbCommand extends SequentialCommandGroup {
    public AutoClimbCommand(RobotContainer robot) {

        /**
         * pulls the robot up until passive hooks clear the mid bar
         * prepares robot for transition to high bar (reset encoder)
         */
        addCommands(
            new ClimberPullUp(robot.climber, robot.leds),
            new ClimberReset(robot.climber)
        );

        /**
         * move climber from above mid bar to the high bar
         * requires tilting inbetwixt (checks encoders for this)
         */
        addCommands(
            new ClimberMoveDistanceCommand(robot.climber, Auto.PASSIVE_TO_TILT),
            new ClimberMaxTiltCommand(robot.climber),
            
            new ClimberMoveDistanceCommand(robot.climber, Auto.TILT_TO_HIGH),
            //PAUSE
            new ClimberNoTiltCommand(robot.climber)
        );
        
        /**
         * pulls the robot up until passive hooks clear the high bar
         * prepares robot for transition to traversal bar (reset encoder)
         */
        addCommands(
            new ClimberPullUp(robot.climber, robot.leds),
            new ClimberReset(robot.climber)
        );

        /**
         * moves active hook up to traversal (no tilt required inbetwixt)
         * when at right right distance, tilts on to the traversal bar
         */
        addCommands(
            new ClimberMoveDistanceCommand(robot.climber, Auto.PASSIVE_TO_TRAVERSAL),
            // PAUSE
            new ClimberMaxTiltCommand(robot.climber)
        );

        /**
         * move climber down slightly to GRIP on to that traversal bar 
         */
        addCommands(
            new ClimberMoveDistanceCommand(robot.climber, Auto.HOOK_TRAVERSAL)
        );
    }
}
