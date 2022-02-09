// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.Intake;
import com.stuypulse.robot.subsystems.Shooter;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainRamseteCommand;
import com.stuypulse.robot.commands.intake.IntakeAcquireForeverCommand;
import com.stuypulse.robot.commands.intake.IntakeExtendCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBallAuton extends SequentialCommandGroup {

  private static final String ACQUIRE_FIRST_BALL = "ThreeBallAuto/output/3BallAutonStartPath.wpilib.json";
  private static final String SECOND_SHOT_PATH = "ThreeBallAuto/output/ThreeBallAutonGetThirdBall.wpilib.json";

/** Creates a new ThreeBallAuton. */
  public ThreeBallAuton(Drivetrain drivetrain, Intake intake, Shooter shooter, Conveyor conveyor, double ringShot) {
    addCommands(
      new IntakeExtendCommand(intake),
      new IntakeAcquireForeverCommand(intake),
      new ShooterRingShotCommand(shooter),
      new WaitCommand(1)
    );
    addCommands(
      new DrivetrainRamseteCommand(drivetrain, ACQUIRE_FIRST_BALL)
        .robotRelative(),
      new DrivetrainAlignCommand(drivetrain, ringShot),
      new ConveyorShootCommand(conveyor).withTimeout(1)
    );

    addCommands(
      new DrivetrainRamseteCommand(drivetrain, SECOND_SHOT_PATH)
        .fieldRelative(),
      new DrivetrainAlignCommand(drivetrain, ringShot),
      new ConveyorShootCommand(conveyor).withTimeout(1)
    );
  }
}
