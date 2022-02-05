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
    public AutoClimbCommand(RobotContainer robot){

        addCommands(
            // robot setup
            new ShooterStopCommand(robot.shooter),
            new IntakeDeacquireForeverCommand(robot.intake),
            new IntakeRetractCommand(robot.intake),

            // fully collapse climber
            new WaitCommand(0.0),
            new ClimberMoveDownCommand(robot.climber),

            // fully extend climber
            new WaitCommand(0.0),
            new ClimberMoveUpCommand(robot.climber),

            // fully collapse piston
            new WaitCommand(0.0),
            new ClimberMaxTiltCommand(robot.climber),

            // fully collapse climber
            new WaitCommand(0.0),
            new ClimberMoveDownCommand(robot.climber),

            // full extend piston
            new WaitCommand(0.0),
            new ClimberNoTiltCommand(robot.climber)
        );
    }
}
