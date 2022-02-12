/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainRamseteCommand;
import com.stuypulse.robot.commands.intake.IntakeAcquireCommand;
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
 * @author many other software newbies who left before writing author tags
 */

public class FourBallAuton extends SequentialCommandGroup {

    // private static final int

    // Times it takes for the robot to run through

    // Time it takes for the shooter to reach the target speed
    private static final int SHOOTER_INITIALIZE_DELAY = 1;
    // Time it takes for the conveyor to give the shooter the ball
    private static final int CONVEYOR_TO_SHOOTER = 1;
    // Time we want to give the drivetrain to align
    private static final int DRIVETRAIN_ALIGN_TIME = 2;

    private static final int HUMAN_WAIT_TIME = 2;

    // we need to set these after testing
    private static final int FOUR_BALL_START_TIMEOUT = 5;
    private static final int FOUR_BALL_TO_TERMINAL_TIMEOUT = 5;

    private static final String FOUR_BALL_START =
            "FourBallAuton/output/FourBallStartPath.wpilib.json";
    private static final String FOUR_BALL_TO_TERMINAL =
            "FourBallAuton/output/FourBallTerminalPath.wpilib.json";

    /** Creates a new FourBallAuton. */
    public FourBallAuton(RobotContainer robot, double ringShot) {
        // Starting up subsystems
        addCommands(
                new ShooterRingShotCommand(robot.shooter),
                new IntakeExtendCommand(robot.intake),
                new IntakeAcquireCommand(robot.intake),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY), // 1 seconds
                new LEDSetCommand(robot.leds, LEDColor.YELLOW_PULSE));

        // Tarmac to first ball
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FOUR_BALL_START)
                        .withTimeout(FOUR_BALL_START_TIMEOUT),
                new DrivetrainAlignCommand(robot.drivetrain, ringShot)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME), // 2 seconds
                new ConveyorShootCommand(robot.conveyor)
                        .withTimeout(CONVEYOR_TO_SHOOTER), // 2 seconds
                new LEDSetCommand(robot.leds, LEDColor.ORANGE_PULSE));
        // First ball to terminal to RingShot
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FOUR_BALL_TO_TERMINAL)
                        .withTimeout(FOUR_BALL_TO_TERMINAL_TIMEOUT),
                new WaitCommand(HUMAN_WAIT_TIME),
                new DrivetrainAlignCommand(robot.drivetrain, ringShot)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME), // 2 seconds
                new ConveyorShootCommand(robot.conveyor)
                        .withTimeout(CONVEYOR_TO_SHOOTER), // 2 seconds
                new LEDSetCommand(robot.leds, LEDColor.CONFETTI));
    }
}
