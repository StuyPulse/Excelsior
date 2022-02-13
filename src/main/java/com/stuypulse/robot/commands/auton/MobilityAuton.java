/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistanceCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveForeverCommand;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.subsystems.LEDController.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class MobilityAuton {
    // Distance from start point to Ring (in meters)
    private static final int DISTANCE_TO_RING = 12;

    public static class NoEncoders extends SequentialCommandGroup {
        public NoEncoders(RobotContainer robot) {
            addCommands(new DrivetrainDriveForeverCommand(robot.drivetrain, 1.0).withTimeout(5));
        }
    }

    public static class WithEncoders extends SequentialCommandGroup {
        public WithEncoders(RobotContainer robot) {
            addCommands(
                    new DrivetrainDriveDistanceCommand(
                            robot.drivetrain, DISTANCE_TO_RING),
                    new LEDSetCommand(robot.leds, LEDColor.BEAT),
                    new WaitCommand(0.5),
                    new LEDSetCommand(robot.leds, LEDColor.CONFETTI),
                    new WaitCommand(0.5),
                    new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
                    new WaitCommand(0.5),
                    new LEDSetCommand(robot.leds, LEDColor.LIME_SOLID),
                    new WaitCommand(0.5),
                    new LEDSetCommand(robot.leds, LEDColor.ORANGE_PULSE),
                    new WaitCommand(0.5),
                    new LEDSetCommand(robot.leds, LEDColor.RED_PULSE));
        }
    }
}
