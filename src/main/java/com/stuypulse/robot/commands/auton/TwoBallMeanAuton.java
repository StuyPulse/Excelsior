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
import com.stuypulse.robot.commands.intake.IntakeDeacquire;
import com.stuypulse.robot.commands.intake.IntakeDeacquireForever;
import com.stuypulse.robot.commands.intake.IntakeExtend;
import com.stuypulse.robot.commands.leds.LEDSet;
import com.stuypulse.robot.commands.shooter.ShooterRingShot;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*-
 * @author Vincent Wang (vinowang921@gmail.com)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Shaurya Sen (shauryasen12@gmail.com)
 */

public class TwoBallMeanAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 1.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 3.0;
    // Time it takes for the robot to deacquire two balls
    private static final double INTAKE_DEACQUIRE_TIME = 3.0;

    private static final String TWO_BALL_TO_SECOND_BALL = "TwoBallMeanAuton/output/TwoBallGetSecondBall.wpilib.json";
    private static final String TWO_BALL_GET_OTHER_OPPONENT_BALL = "TwoBallMeanAuton/output/TwoBallGetOtherOpponentBall.wpilib.json";
    private static final String TWO_BALL_GET_WALL_BALL = "TwoBallMeanAuton/output/TwoBallGetWallBall.wpilib.json";
    private static final String TWO_BALL_EJECT_WALL_BALL = "TwoBallMeanAuton/output/TwoBallEjectWallBall.wpilib.json";
    private static final String TWO_BALL_TO_TELEOP_STARTING_POSITION = "TwoBallMeanAuton/output/TwoBallEjectWallBallInverse.wpilib.json";

    public TwoBallMeanAuton(RobotContainer robot) {

        addCommands(
                new LEDSet(robot.leds, LEDColor.RED));

        // Starting up subsystems
        addCommands(
            new LEDSet(robot.leds, LEDColor.YELLOW),
            new IntakeExtend(robot.intake),
            new IntakeAcquireForever(robot.intake),
            new ShooterRingShot(robot.shooter),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        // Shoot Two Balls
        addCommands(
                new LEDSet(robot.leds, LEDColor.GREEN),
                new DrivetrainRamsete(robot.drivetrain, TWO_BALL_TO_SECOND_BALL)
                        .robotRelative(),

                new LEDSet(robot.leds, LEDColor.GREEN.pulse()),
                new DrivetrainAlign(robot.drivetrain, robot.camera)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),

                new LEDSet(robot.leds, LEDColor.RAINBOW),
                new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER)
        );

        // Get Wall Blue Ball
        addCommands(
                new LEDSet(robot.leds, LEDColor.BLUE),
                new DrivetrainRamsete(robot.drivetrain, TWO_BALL_GET_OTHER_OPPONENT_BALL)
                        .fieldRelative());
        addCommands(
                new LEDSet(robot.leds, LEDColor.PURPLE),
                new DrivetrainRamsete(robot.drivetrain, TWO_BALL_GET_WALL_BALL)
                        .fieldRelative());
        addCommands(
                new LEDSet(robot.leds, LEDColor.PINK),
                new DrivetrainRamsete(robot.drivetrain, TWO_BALL_EJECT_WALL_BALL)
                        .fieldRelative(),
          
                new IntakeDeacquireForever(robot.intake).withTimeout(INTAKE_DEACQUIRE_TIME));
                
        addCommands(
                new LEDSet(robot.leds, LEDColor.AQUA),
                new DrivetrainRamsete(robot.drivetrain, TWO_BALL_TO_TELEOP_STARTING_POSITION)
                        .fieldRelative());

        addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
    }
}