/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShoot;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlign;
import com.stuypulse.robot.commands.drivetrain.DrivetrainRamsete;
import com.stuypulse.robot.commands.intake.IntakeAcquireForever;
import com.stuypulse.robot.commands.intake.IntakeExtend;
import com.stuypulse.robot.commands.leds.LEDSet;
import com.stuypulse.robot.commands.shooter.ShooterRingShot;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBallMysteryAuton extends SequentialCommandGroup {
    // Initial delay for the auton
    private static final double START_DELAY = 1.0;

    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 1.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 3.0;

    
    private static final String START =
            "ThreeBallMysteryAuton/output/ThreeMysteryBallGetSecondBall.wpilib.json";
    private static final String GET_MYSTERY_BALL = 
            "ThreeBallMysteryAuton/output/GetMysteryBall.wpilib.json";
    private static final String EVACUATE =
            "ThreeBallMysteryAuton/output/Evacuate.wpilib.json";

    public ThreeBallMysteryAuton(RobotContainer robot) {

        addCommands(
                new LEDSet(robot.leds, LEDColor.RED), new WaitCommand(START_DELAY));

        // Starting up subsystems
        addCommands(
                new LEDSet(robot.leds, LEDColor.YELLOW),
                new IntakeExtend(robot.intake),
                new IntakeAcquireForever(robot.intake),
                new ShooterRingShot(robot.shooter),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        addCommands(
                new LEDSet(robot.leds, LEDColor.GREEN),
                new DrivetrainRamsete(robot.drivetrain, START).robotRelative(),
                new DrivetrainAlign(robot.drivetrain, robot.camera)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        addCommands(
                new LEDSet(robot.leds, LEDColor.PURPLE),
                new DrivetrainRamsete(robot.drivetrain, GET_MYSTERY_BALL).fieldRelative(),
                new DrivetrainAlign(robot.drivetrain, robot.camera)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER)
        );

        addCommands(
                new LEDSet(robot.leds, LEDColor.BLUE),
                new DrivetrainRamsete(robot.drivetrain, EVACUATE).fieldRelative()
        );

        addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
    }
}
