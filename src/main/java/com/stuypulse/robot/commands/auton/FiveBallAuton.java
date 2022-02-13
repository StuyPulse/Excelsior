/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.Constants.LimelightSettings;
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
 */

public class FiveBallAuton extends SequentialCommandGroup {
    // Time it takes for the intake to go down
    private static final double INTAKE_FALL_DOWN = 0.1;
    // Time it takes for the shooter to reach the target speed
    private static final int SHOOTER_INITIALIZE_DELAY = 1;
    // Time it takes for the robot.conveyor to give the shooter the ball
    private static final int CONVEYOR_TO_SHOOTER = 1;
    // Time we want to give the robot.drivetrain to align
    private static final int DRIVETRAIN_ALIGN_TIME = 2;

    private static final int HUMAN_WAIT_TIME = 2;

    private static final String FIVE_BALL_TO_SECOND_BALL =
            "FiveBallAuton/output/FiveBallAcquireSecondBall.wpilib.json";
    private static final String FIVE_BALL_TO_TERMINAL =
            "FiveBallAuton/output/FiveBallGetTerminalBalls.wpilib.json";
    private static final String FIVE_BALL_TERMINAL_TO_SHOOT =
            "FiveBallAuton/output/FiveBallShootTerminalBalls.wpilib.json";
    private static final String FIVE_BALL_TO_WALL_BALL =
            "FiveBallAuton/output/FiveBallShootWallBall.wpilib.json";

    /** Creates a new FiveBallAuton. */
    public FiveBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.YELLOW_SOLID),
                new ShooterRingShotCommand(robot.shooter),
                new IntakeExtendCommand(robot.intake),
                new WaitCommand(INTAKE_FALL_DOWN),
                new IntakeAcquireCommand(robot.intake),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY));

        // Tarmac to first ball
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.ORANGE_SOLID),
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TO_SECOND_BALL).robotRelative(),
                new DrivetrainAlignCommand(robot.drivetrain, LimelightSettings.RING_SHOT_DISTANCE)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        // First ball to terminal to RingShot
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.RED_SOLID),
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TO_TERMINAL).fieldRelative());
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.BLUE_SOLID),
                new WaitCommand(HUMAN_WAIT_TIME));

        // Return to Ring to shoot
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.GREEN_SOLID),
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TERMINAL_TO_SHOOT).fieldRelative(),
                new DrivetrainAlignCommand(robot.drivetrain, LimelightSettings.RING_SHOT_DISTANCE)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        // Pick up and shoot fifth ball
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.YELLOW_SOLID),
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TO_WALL_BALL).fieldRelative(),
                new DrivetrainAlignCommand(robot.drivetrain, LimelightSettings.RING_SHOT_DISTANCE)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));
    }
}
