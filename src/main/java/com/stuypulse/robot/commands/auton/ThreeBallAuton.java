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
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.subsystems.LEDController.LEDColor;

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

public class ThreeBallAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final int SHOOTER_INITIALIZE_DELAY = 1;
    // Time it takes for the conveyor to give the shooter the ball
    private static final int CONVEYOR_TO_SHOOTER = 1;
    // Time we want to give the drivetrain to align
    private static final int DRIVETRAIN_ALIGN_TIME = 2;

    // Timeouts for Pathweaver paths
    private static final int THREE_BALL_START_TIMEOUT = 10;
    private static final int THREE_BALL_TO_TERMINAL_TIMEOUT = 10;
    private static final int THREE_BALL_SHOOT_TERMINAL_BALLS_TIMEOUT = 10;

    private static final String THREE_BALL_START =
            "ThreeBallAuton/output/ThreeBallAutonGetSecondBall.wpilib.json";
    private static final String THREE_BALL_TO_TERMINAL =
            "ThreeBallAuton/output/ThreeBallAutonGetTerminalBalls.wpilib.json";
    private static final String THREE_BALL_SHOOT_TERMINAL_BALLS = 
            "ThreeBallAuton/output/FourBalAutonShootTerminalBalls.wpilib.json";

    /** Creates a new ThreeBallAuton. */
    public ThreeBallAuton(RobotContainer robot, double ringShot) {
        addCommands(
                new ShooterRingShotCommand(robot.shooter),
                new IntakeExtendCommand(robot.intake),
                new IntakeAcquireForeverCommand(robot.intake),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY),
                new LEDSetCommand(robot.leds, LEDColor.YELLOW_PULSE));
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, THREE_BALL_START).robotRelative()
                        .withTimeout(THREE_BALL_START_TIMEOUT),
                new DrivetrainAlignCommand(robot.drivetrain, ringShot)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER),
                new LEDSetCommand(robot.leds, LEDColor.ORANGE_PULSE));

        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, THREE_BALL_TO_TERMINAL).fieldRelative()
                        .withTimeout(THREE_BALL_TO_TERMINAL_TIMEOUT)
        );
        
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, THREE_BALL_SHOOT_TERMINAL_BALLS).fieldRelative()
                        .withTimeout(THREE_BALL_SHOOT_TERMINAL_BALLS_TIMEOUT),
                new DrivetrainAlignCommand(robot.drivetrain, ringShot)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER),
                new LEDSetCommand(robot.leds, LEDColor.CONFETTI));
    }
}
