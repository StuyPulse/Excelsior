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
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.LEDController.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*-
 * @author Ivan Chen (ivanchen07@gmail.com)
 */

public class AltThreeBallAuton extends SequentialCommandGroup {
    // Time it takes for the intake to go down
    private static final double INTAKE_FALL_DOWN = 0.1;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 2.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 3.0;

    private static final String ALT_THREE_BALL_ACQUIRE_SECOND_BALL =
            "AltThreeBall/output/AltThreeBallAcquireSecondBall.wpilib.json";
    private static final String ALT_THREE_BALL_ACQUIRE_THIRD_BALL =
            "AltThreeBall/output/AltThreeBallAcquireThirdBall.wpilib.json";

    /** Creates a new FourBallAuton. */
    public AltThreeBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.YELLOW_PULSE),

            new ShooterRingShotCommand(robot.shooter),
            new IntakeExtendCommand(robot.intake),
            new WaitCommand(INTAKE_FALL_DOWN),
            new IntakeAcquireForeverCommand(robot.intake),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        // Tarmac to second ball
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_SOLID),
            new DrivetrainRamseteCommand(robot.drivetrain, ALT_THREE_BALL_ACQUIRE_SECOND_BALL).robotRelative()
        );

        // Align and shoot first and second balls
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_PULSE),
            new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                .withTimeout(DRIVETRAIN_ALIGN_TIME / 2));
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
            new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER)
        );

        // Acquire third ball
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.BLUE_SOLID),
            new DrivetrainRamseteCommand(robot.drivetrain, ALT_THREE_BALL_ACQUIRE_THIRD_BALL)
                .fieldRelative()
        );
        
        // Align and shoot third ball
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
