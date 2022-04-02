/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShoot;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlign;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistance;
import com.stuypulse.robot.commands.intake.IntakeAcquireForever;
import com.stuypulse.robot.commands.intake.IntakeExtend;
import com.stuypulse.robot.commands.leds.LEDSet;
import com.stuypulse.robot.commands.shooter.ShooterRingShot;
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

public class OneBallAuton extends SequentialCommandGroup {

    // Auton start up delay
    private static final double START_DELAY = 0.0;
    /// Time it takes for the intake to go down
    private static final double INTAKE_FALL_DOWN = 0.1;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 1.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 2.0;
    // Distance from start point to Ring (in meters)
    private static final double DISTANCE_TO_RING = 3.0;

    public OneBallAuton(RobotContainer robot) {
        // Starting up subsystems
        addCommands(
                new LEDSet(robot.leds, LEDColor.RED), new WaitCommand(START_DELAY));

        addCommands(
                new LEDSet(robot.leds, LEDColor.YELLOW),
                new ShooterRingShot(robot.shooter),
                new IntakeExtend(robot.intake),
                new WaitCommand(INTAKE_FALL_DOWN),
                new IntakeAcquireForever(robot.intake),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY));

        addCommands(
                new LEDSet(robot.leds, LEDColor.GREEN),
                new DrivetrainDriveDistance(robot.drivetrain, DISTANCE_TO_RING),
                new DrivetrainAlign(robot.drivetrain, robot.camera)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME),
                new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
    }
}
