/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistanceCommand;
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

public class TwoBallAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final int SHOOTER_INITIALIZE_DELAY = 1;
    // Distance from starting position to ring shot
    private static final int DISTANCE_TO_RING = 12;

    public TwoBallAuton(RobotContainer robot, double ringShot) {
        // Starting up subsystems
        addCommands(
                new ShooterRingShotCommand(robot.shooter),
                new IntakeExtendCommand(robot.intake),
                new IntakeAcquireCommand(robot.intake),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY),
                new LEDSetCommand(robot.leds, LEDColor.YELLOW_PULSE)
        );

        addCommands(
                new DrivetrainDriveDistanceCommand(
                        robot.drivetrain, DISTANCE_TO_RING),
                new DrivetrainAlignCommand(robot.drivetrain, ringShot),
                new ConveyorShootCommand(robot.conveyor),
                new LEDSetCommand(robot.leds, LEDColor.CONFETTI)
        );
    }
}
