/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.climber.ClimberMaxTilt;
import com.stuypulse.robot.commands.climber.ClimberMoveDown;
import com.stuypulse.robot.commands.climber.ClimberMoveUp;
import com.stuypulse.robot.commands.climber.ClimberNoTilt;
import com.stuypulse.robot.commands.intake.IntakeDeacquireForever;
import com.stuypulse.robot.commands.intake.IntakeRetract;
import com.stuypulse.robot.commands.shooter.ShooterStop;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoClimb extends SequentialCommandGroup {
    public AutoClimb(RobotContainer robot) {

        addCommands(
                // robot setup
                new ShooterStop(robot.shooter),
                new IntakeDeacquireForever(robot.intake),
                new IntakeRetract(robot.intake),

                // fully collapse climber
                new WaitCommand(0.0),
                new ClimberMoveDown(robot.climber),

                // fully extend climber
                new WaitCommand(0.0),
                new ClimberMoveUp(robot.climber),

                // fully collapse piston
                new WaitCommand(0.0),
                new ClimberMaxTilt(robot.climber),

                // fully collapse climber
                new WaitCommand(0.0),
                new ClimberMoveDown(robot.climber),

                // full extend piston
                new WaitCommand(0.0),
                new ClimberNoTilt(robot.climber));
    }
}
