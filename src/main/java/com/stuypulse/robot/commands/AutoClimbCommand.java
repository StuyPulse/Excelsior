/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.climber.ClimberMaxTiltCommand;
import com.stuypulse.robot.commands.climber.ClimberMoveDownCommand;
import com.stuypulse.robot.commands.climber.ClimberMoveUpCommand;
import com.stuypulse.robot.commands.climber.ClimberNoTiltCommand;
import com.stuypulse.robot.commands.intake.IntakeDeacquireForeverCommand;
import com.stuypulse.robot.commands.intake.IntakeRetractCommand;
import com.stuypulse.robot.commands.shooter.ShooterStopCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoClimbCommand extends SequentialCommandGroup {
    public AutoClimbCommand(RobotContainer robot) {

        // Stage 1 (robot setup for climb)
        addCommands(
            // robot setup
            new ShooterStopCommand(robot.shooter),
            new IntakeDeacquireForeverCommand(robot.intake),
            new IntakeRetractCommand(robot.intake)
        );

        // Stage 2 (climber to mid and hook onto passive hooks)
        addCommands(
            // fully extend climber
            new ClimberMoveUpCommand(robot.climber),

            // fully collapse climber
            new WaitCommand(0.1),
            new ClimberMoveDownCommand(robot.climber)
        );

        // Stage 3 (climber to high and hook onto passive hooks)
        addCommands(
            // Tilt

        );

    }
}
