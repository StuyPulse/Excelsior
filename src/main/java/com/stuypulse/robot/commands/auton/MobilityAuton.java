/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistance;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveForever;
import com.stuypulse.robot.commands.leds.LEDSet;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class MobilityAuton {

    public static class NoEncoders extends SequentialCommandGroup {
        private static final double START_DELAY = 1.0;

        public NoEncoders(RobotContainer robot) {
            addCommands(
                    new LEDSet(robot.leds, LEDColor.RED),
                    new WaitCommand(START_DELAY));

            addCommands(
                    new LEDSet(robot.leds, LEDColor.GREEN),
                    new DrivetrainDriveForever(robot.drivetrain, 0.3).withTimeout(3));

            addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
        }
    }

    public static class WithEncoders extends SequentialCommandGroup {
        // Distance from start point to Ring (in meters)
        private static final double DISTANCE_TO_RING = 3.0;
        private static final double START_DELAY = 1.0;

        public WithEncoders(RobotContainer robot) {
            addCommands(
                    new LEDSet(robot.leds, LEDColor.RED),
                    new WaitCommand(START_DELAY));

            addCommands(
                    new LEDSet(robot.leds, LEDColor.GREEN),
                    new DrivetrainDriveDistance(robot.drivetrain, DISTANCE_TO_RING));

            addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
        }
    }
}
