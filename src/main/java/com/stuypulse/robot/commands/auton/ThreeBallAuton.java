/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainRamseteCommand;
import com.stuypulse.robot.commands.intake.IntakeAcquireForeverCommand;
import com.stuypulse.robot.commands.intake.IntakeExtendCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;

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
    public ThreeBallAuton(RobotContainer robot, double ringShot) {
        addCommands(
                new ShooterRingShotCommand(robot.shooter),
                new IntakeExtendCommand(robot.intake),
                new IntakeAcquireForeverCommand(robot.intake),
                new WaitCommand(SHOOTER_START_TIME));
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, ACQUIRE_FIRST_BALL).robotRelative(),
                new DrivetrainAlignCommand(robot.drivetrain, ringShot)
                        .withTimeout(LIMELIGHT_ALIGN_TIME),
                new ConveyorShootCommand(robot.conveyor).withTimeout(SHOOTER_SHOOT_TIME));

        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, SECOND_SHOT_PATH).fieldRelative(),
                new DrivetrainAlignCommand(robot.drivetrain, ringShot)
                        .withTimeout(LIMELIGHT_ALIGN_TIME),
                new ConveyorShootCommand(robot.conveyor).withTimeout(SHOOTER_SHOOT_TIME));
    }
}
