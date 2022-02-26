/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistanceCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainRamseteCommand;
import com.stuypulse.robot.commands.intake.IntakeAcquireForeverCommand;
import com.stuypulse.robot.commands.intake.IntakeExtendCommand;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.LEDController.LEDColor;
import com.stuypulse.robot.util.TrajectoryLoader;

import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
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

public class AltFourBallAuton extends SequentialCommandGroup {
    // Time it takes for the intake to go down
    private static final double INTAKE_FALL_DOWN = 0.1;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 2.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 3.0;
    // Time it takes for human player to roll ball to intake
    private static final double HUMAN_WAIT_TIME = 1.5;

    private static final String ALT_FOUR_BALL_GET_SECOND_BALL =
            "AltFourBallAuton/output/AltFourBallAutonGetSecondBall.wpilib.json";
    private static final String ALT_FOUR_BALL_GET_TERMINAL_BALLS =
            "AltFourBallAuton/output/AltFourBallAutonGetTerminalBalls.wpilib.json";
    private static final String ALT_FOUR_BALL_SHOOT_TERMINAL_BALLS =
            "AltFourBallAuton/output/AltFourBallAutonShootTerminalBalls.wpilib.json";

    /** Creates a new AltFourBallAuton. */
    public AltFourBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.YELLOW_PULSE),

            new ShooterRingShotCommand(robot.shooter),
            new IntakeExtendCommand(robot.intake),
            new WaitCommand(INTAKE_FALL_DOWN),
            new IntakeAcquireForeverCommand(robot.intake),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        // Tarmac to first ball
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_SOLID),
            new DrivetrainRamseteCommand(robot.drivetrain, ALT_FOUR_BALL_GET_SECOND_BALL).robotRelative());

        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_PULSE),
            new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                .withTimeout(DRIVETRAIN_ALIGN_TIME / 2));
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
            new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER)
        );

        // First ball to terminal to RingShot
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.BLUE_SOLID),

            new DrivetrainRamseteCommand(robot.drivetrain, ALT_FOUR_BALL_GET_TERMINAL_BALLS)
                .fieldRelative()
        );

        addCommands(
            new DrivetrainDriveDistanceCommand(robot.drivetrain, Units.feetToMeters(-1))
                .fieldRelative(),

            new LEDSetCommand(robot.leds, LEDColor.WHITE_PULSE),

            // new WaitCommand(HUMAN_WAIT_TIME).withInterrupt(() -> robot.conveyor.isFull()),
            new WaitCommand(HUMAN_WAIT_TIME).withInterrupt(() -> robot.conveyor.isFull())
        );

        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.PURPLE_SOLID),

            new DrivetrainRamseteCommand(robot.drivetrain, ALT_FOUR_BALL_SHOOT_TERMINAL_BALLS)
                    .fieldRelative());
    
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.PURPLE_PULSE),
            new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                    .withTimeout(DRIVETRAIN_ALIGN_TIME / 2.0)
        );

        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
            new ConveyorShootCommand(robot.conveyor).perpetually()
        );

    }
}
