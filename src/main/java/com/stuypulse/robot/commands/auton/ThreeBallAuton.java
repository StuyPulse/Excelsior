/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainRamseteCommand;
import com.stuypulse.robot.commands.intake.IntakeAcquireForeverCommand;
import com.stuypulse.robot.commands.intake.IntakeExtendCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.Intake;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBallAuton extends SequentialCommandGroup {

    private static final String ACQUIRE_FIRST_BALL =
            "ThreeBallAuton/output/3BallAutonStartPath.wpilib.json";
    private static final String SECOND_SHOT_PATH =
            "ThreeBallAuton/output/ThreeBallAutonGetThirdBall.wpilib.json";

    private static final int SHOOTER_START_TIME = 1;
    private static final int SHOOTER_SHOOT_TIME = 2;
    private static final int LIMELIGHT_ALIGN_TIME = 3;

    /** Creates a new ThreeBallAuton. */
    public ThreeBallAuton(
            Drivetrain drivetrain,
            Intake intake,
            Shooter shooter,
            Conveyor conveyor,
            double ringShot) {
        addCommands(
                new ShooterRingShotCommand(shooter),
                new IntakeExtendCommand(intake),
                new IntakeAcquireForeverCommand(intake),
                new WaitCommand(SHOOTER_START_TIME));
        addCommands(
                new DrivetrainRamseteCommand(drivetrain, ACQUIRE_FIRST_BALL).robotRelative(),
                new DrivetrainAlignCommand(drivetrain, ringShot).withTimeout(LIMELIGHT_ALIGN_TIME),
                new ConveyorShootCommand(conveyor).withTimeout(SHOOTER_SHOOT_TIME));

        addCommands(
                new DrivetrainRamseteCommand(drivetrain, SECOND_SHOT_PATH).fieldRelative(),
                new DrivetrainAlignCommand(drivetrain, ringShot).withTimeout(LIMELIGHT_ALIGN_TIME),
                new ConveyorShootCommand(conveyor).withTimeout(SHOOTER_SHOOT_TIME));
    }
}
