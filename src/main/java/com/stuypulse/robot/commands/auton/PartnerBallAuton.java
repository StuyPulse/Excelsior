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
import com.stuypulse.robot.commands.shooter.ShooterFenderShotCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*-
 * @author Vincent Wang
 * @author Shaurya Sen
 */
 
public class PartnerBallAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 0.3;
    // Time it takes for shooter to go from Fender to Ring shot speed
    private static final double SHOOTER_TO_RING_DELAY = 0.2;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 3.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 3.0;
    // Time to acquire partner auton ball
    private static final double ACQUIRE_PARTNER_BALL_TIME = 5.0;

    private static final String PARTNER_BALL_TO_RING_BALL = "PartnerBallAuton/output/PartnerBallGetRingBall.wpilib.json";

    /** Creates a new FiveBallAuton. */
    public PartnerBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.YELLOW),
            new IntakeExtendCommand(robot.intake),
            new IntakeAcquireForeverCommand(robot.intake),
            new ShooterFenderShotCommand(robot.shooter),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        // Tarmac to first ball
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN),
            new WaitCommand(ACQUIRE_PARTNER_BALL_TIME).withInterrupt(() -> robot.conveyor.isFull()),
            new DrivetrainAlignCommand(robot.drivetrain, Limelight.FENDER_SHOT_DISTANCE),
            new ConveyorShootCommand(robot.conveyor));
        addCommands(
            new ShooterRingShotCommand(robot.shooter),
            new DrivetrainRamseteCommand(robot.drivetrain, PARTNER_BALL_TO_RING_BALL)
                    .robotRelative(),
            new WaitCommand(SHOOTER_TO_RING_DELAY));
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN.pulse()),
            new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                    .withTimeout(DRIVETRAIN_ALIGN_TIME));
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        addCommands(new LEDSetCommand(robot.leds, LEDColor.WHITE.pulse()));
    }
}
