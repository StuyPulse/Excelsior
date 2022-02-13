/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.util.StopWatch;

import com.stuypulse.robot.Constants.LEDSettings;
import com.stuypulse.robot.Constants.Ports;
import com.stuypulse.robot.RobotContainer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * Contains:
 *      - setColor() : sets color of LEDs for short time
 *      - getDefaultColor() : determines LED color if it is not set
 *
 * @author Sam Belliveau
 */
public class LEDController extends SubsystemBase {

    // Enum that represents and calculates values for
    public enum LEDColor {
        RAINBOW(-0.97, false),
        SINELON(-0.77, false),
        CONFETTI(-0.87, false),
        BEAT(-0.67, false),
        WAVE(-0.43, false),

        WHITE_SOLID(0.93, false),
        PINK_SOLID(0.57, false),
        RED_SOLID(0.61, false),
        ORANGE_SOLID(0.65, false),
        GREEN_SOLID(0.77, false),
        LIME_SOLID(0.73, false),
        BLUE_SOLID(0.87, false),
        PURPLE_SOLID(0.91, false),
        YELLOW_SOLID(0.69, false),

        WHITE_PULSE(0.93, true),
        PINK_PULSE(0.57, true),
        RED_PULSE(0.61, true),
        ORANGE_PULSE(0.65, true),
        GREEN_PULSE(0.77, true),
        LIME_PULSE(0.73, true),
        BLUE_PULSE(0.87, true),
        PURPLE_PULSE(0.91, true),
        YELLOW_PULSE(0.69, true),

        OFF(0.99, false);

        private final double color;
        private final boolean pulse;

        LEDColor(double color, boolean pulse) {
            this.color = color;
            this.pulse = pulse;
        }

        double get() {
            // Variables for detecting if we should be blinking or not
            double cT = Timer.getFPGATimestamp() % (LEDSettings.BLINK_TIME);
            double oT = (0.5 * LEDSettings.BLINK_TIME);

            // Detect if the color should be on or off
            if (pulse && cT >= oT) {
                return LEDColor.OFF.color;
            } else {
                return this.color;
            }
        }
    }

    // Motor that controlls the LEDs
    private final PWMSparkMax controller;

    // Stopwatch to check when to start overriding manual updates
    private final StopWatch lastUpdate;

    // The robot container to get information from
    private final RobotContainer robotContainer;

    // The current color to set the LEDs to
    private LEDColor manualColor;

    public LEDController(RobotContainer container) {
        this.controller = new PWMSparkMax(Ports.LEDController.PWM_PORT);
        this.lastUpdate = new StopWatch();
        this.robotContainer = container;

        setColor(LEDColor.OFF);
    }

    public void setColor(LEDColor color) {
        manualColor = color;
        lastUpdate.reset();
    }

    public LEDColor getDefaultColor() {
        // TODO: Add if statements that check sensor values to update LEDController
        return LEDColor.OFF;
    }

    @Override
    public void periodic() {
        // If we called .setColor() recently, use that value
        if (DriverStation.isAutonomous() || lastUpdate.getTime() < LEDSettings.MANUAL_UPDATE_TIME) {
            controller.set(manualColor.get());
        }

        // Otherwise use the default color
        else {
            controller.set(getDefaultColor().get());
        }
    }
}
