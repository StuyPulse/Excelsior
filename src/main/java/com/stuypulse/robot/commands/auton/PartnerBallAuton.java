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
import com.stuypulse.robot.commands.shooter.ShooterFenderShot;
import com.stuypulse.robot.commands.shooter.ShooterRingShot;
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
            new LEDSet(robot.leds, LEDColor.YELLOW),
            new IntakeExtend(robot.intake),
            new IntakeAcquireForever(robot.intake),
            new ShooterFenderShot(robot.shooter),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        // Tarmac to first ball
        addCommands(
            new LEDSet(robot.leds, LEDColor.GREEN),
            new WaitCommand(ACQUIRE_PARTNER_BALL_TIME).withInterrupt(robot.conveyor::isFull),
            new ConveyorShoot(robot.conveyor)
        );

        addCommands(
            new ShooterRingShot(robot.shooter),
            new DrivetrainRamsete(robot.drivetrain, PARTNER_BALL_TO_RING_BALL)
                    .robotRelative(),
            new WaitCommand(SHOOTER_TO_RING_DELAY)
        );
        
        addCommands(
            new LEDSet(robot.leds, LEDColor.GREEN.pulse()),
            new DrivetrainAlign(robot.drivetrain, robot.camera)
                    .withTimeout(DRIVETRAIN_ALIGN_TIME)
        );
        
        addCommands(
            new LEDSet(robot.leds, LEDColor.RAINBOW),
            new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER)
        );

        addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
    }
}
