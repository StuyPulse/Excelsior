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
import com.stuypulse.robot.commands.intake.IntakeDeacquireCommand;
import com.stuypulse.robot.commands.intake.IntakeExtendCommand;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*-
 * @author Vincent Wang (vinowang921@gmail.com)
 * @author Ivan Wei (ivanw8288@gmail.com)
 * @author Ivan Chen (ivanchen07@gmail.com)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Marc Jiang (mjiang05@gmail.com)
 * @author Ian Jiang (ijiang05@gmail.com)
 * @author Carmin Vuong (carminvuong@gmail.com)
 * @author Samuel Chen(samchen1738@gmail.com)
 */

public class TwoBallTwoMeanAuton extends SequentialCommandGroup {
    // Initial delay for the auton
    private static final double START_DELAY = 1.0;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 1.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 3.0;

    private static final String TWO_BALL_TO_SECOND_BALL = "TwoBallOneMeanAuton/output/TwoBallGetSecondBall.wpilib.json";
    private static final String TWO_BALL_GET_OTHER_OPPONENT_BALL = "TwoBallOneMeanAuton/output/TwoBallGetOtherOpponentBall.wpilib.json";
    private static final String TWO_BALL_GET_WALL_BALL = "TwoBallOneMeanAuton/output/TwoBallGetWallBall.wpilib.json";
    private static final String TWO_BALL_EJECT_WALL_BALL = "TwoBallOneMeanAuton/output/TwoBallEjectWallBall.wpilib.json";
    private static final String TWO_BALL_TO_TELEOP_STARTING_POSITION = "TwoBallOneMeanAuton/output/TwoBallTeleopStart.wpilib.json";


    public TwoBallTwoMeanAuton(RobotContainer robot) {

        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.RED), new WaitCommand(START_DELAY));

        // Starting up subsystems
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.YELLOW),
            new IntakeExtendCommand(robot.intake),
            new IntakeAcquireForeverCommand(robot.intake),
            new ShooterRingShotCommand(robot.shooter),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY));
        // Shoot Two Balls
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.GREEN),
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_TO_SECOND_BALL),
                new LEDSetCommand(robot.leds, LEDColor.GREEN.pulse()),
                new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));
        // Get Wall Blue Ball
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.BLUE),
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_GET_WALL_BALL));
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.PURPLE),
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_GET_OTHER_OPPONENT_BALL)
        );
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.PINK),
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_EJECT_WALL_BALL),
                new IntakeDeacquireCommand(robot.intake));

        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.AQUA),
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_TO_TELEOP_STARTING_POSITION));

        addCommands(new LEDSetCommand(robot.leds, LEDColor.WHITE.pulse()));
    }
}