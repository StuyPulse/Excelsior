/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.util.StopWatch;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.ColorSensor.CurrentBall;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
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
 * @author Andrew Liu
 */
public class LEDController extends SubsystemBase {

    // Enum that represents and calculates values for
    public enum LEDColor {
        BEAT(-0.67, false),
        BREATH(-0.17, false),
        CONFETTI(-0.87, false),
        FIRE(0.57, false),
        HEARTBEAT(-0.25, false),
        SINELON(-0.77, false),
        TWINKLES(-0.53, false),
        WAVE(-0.43, false),
        RAINBOW(-0.99, false),

        BEAT_PULSE(-0.67, true),
        BREATH_PULSE(-0.17, true),
        CONFETTI_PULSE(-0.87, true),
        FIRE_PULSE(0.57, true),
        HEARTBEAT_PULSE(-0.25, true),
        RAINBOW_PULSE(-0.99, true),
        SINELON_PULSE(-0.77, true),
        TWINKLES_PULSE(-0.53, true),
        WAVE_PULSE(-0.43, true),

        AQUA(0.81, false),
        BLACK(0.99, false),
        BLUE(0.87, false),
        BLUE_GREEN(0.79, false),
        BLUE_VIOLET(0.89, false),
        DARK_BLUE(0.85, false),
        DARK_GRAY(0.97, false),
        DARK_GREEN(0.75, false),
        DARK_RED(0.59, false),
        GOLD(0.67, false),
        GRAY(0.95, false),
        GREEN(0.77, false),
        HOT_PINK(0.57, false),
        LAWN_GREEN(0.71, false),
        LIME(0.73, false),
        ORANGE(0.65, false),
        PINK(0.57, false),
        PURPLE(0.91, false),
        RED(0.61, false),
        RED_ORANGE(0.63, false),
        SKY_BLUE(0.83, false),
        VIOLET(0.91, false),
        WHITE(0.93, false),
        YELLOW(0.69, false),

        AQUA_PULSE(0.81, true),
        BLACK_PULSE(0.99, true),
        BLUE_GREEN_PULSE(0.79, true),
        BLUE_PULSE(0.87, true),
        BLUE_VIOLET_PULSE(0.89, true),
        DARK_BLUE_PULSE(0.85, true),
        DARK_GRAY_PULSE(0.97, true),
        DARK_GREEN_PULSE(0.75, true),
        DARK_RED_PULSE(0.59, true),
        GOLD_PULSE(0.67, true),
        GRAY_PULSE(0.95, true),
        GREEN_PULSE(0.77, true),
        HOT_PINK_PULSE(0.57, true),
        LAWN_GREEN_PULSE(0.71, true),
        LIME_PULSE(0.73, true),
        ORANGE_PULSE(0.65, true),
        PINK_PULSE(0.57, true),
        PURPLE_PULSE(0.91, true),
        RED_ORANGE_PULSE(0.63, true),
        RED_PULSE(0.61, true),
        SKY_BLUE_PULSE(0.83, true),
        VIOLET_PULSE(0.91, true),
        WHITE_PULSE(0.93, true),
        YELLOW_PULSE(0.69, true),

        OFF(0.99, false);

        private final double color;
        private final boolean pulse;

        LEDColor(double color) {
            this.color = color;
            this.pulse = false;
        }

        LEDColor(double color, boolean pulse) {
            this.color = color;
            this.pulse = pulse;
        }

        double get() {
            // Variables for detecting if we should be blinking or not
            double cT = Timer.getFPGATimestamp() % (Settings.LED.BLINK_TIME);
            double oT = (0.5 * Settings.LED.BLINK_TIME);

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
    private final RobotContainer robot;

    // The current color to set the LEDs to
    private LEDColor manualColor;

    // Debouncer for led persist delay
    private final Debouncer redBall;
    private final Debouncer blueBall;

    public LEDController(RobotContainer container) {
        this.controller = new PWMSparkMax(Ports.LEDController.PWM_PORT);
        this.lastUpdate = new StopWatch();
        this.redBall = new Debouncer(Settings.LED.DEBOUNCE_TIME, DebounceType.kFalling);
        this.blueBall = new Debouncer(Settings.LED.DEBOUNCE_TIME, DebounceType.kFalling);
        this.robot = container;

        setColor(LEDColor.OFF);
    }

    public void setColor(LEDColor color) {
        manualColor = color;
        lastUpdate.reset();
    }

    public LEDColor getDefaultColor() {
        /**
         * - In fender shot mode led white - Ring shot red
         *
         * <p>All other behavior overides it - When shooter is getting to speed not yet the rpm
         * flashing the color its suppose to be - When robot aligning solid yellow - When alignment
         * finished green .75 second - Pick up a ball and sensed in the color sensor flash color of
         * ball .75 second blue/orange - Two correct ball green
         */
        if (robot.pump.getCompressing()) return LEDColor.BREATH;

        if (robot.conveyor.isFull()) return LEDColor.GREEN;

        if (robot.colorSensor.hasBall()) {
            if (blueBall.calculate(robot.colorSensor.getCurrentBall() == CurrentBall.BLUE_BALL)) {
                return LEDColor.BLUE;
            }

            if (redBall.calculate(robot.colorSensor.getCurrentBall() == CurrentBall.RED_BALL)) {
                return LEDColor.RED_ORANGE;
            }
        }

        if (Math.abs(robot.shooter.getShooterRPM() - Settings.Shooter.RING_RPM.get()) < 100) {
            return LEDColor.FIRE;
        }
        if (Math.abs(robot.shooter.getShooterRPM() - Settings.Shooter.FENDER_RPM.get()) < 100) {
            return LEDColor.WHITE;
        }

        if (Math.abs(robot.shooter.getShooterRPM() - Settings.Shooter.RING_RPM.get()) < 500) {
            return LEDColor.FIRE_PULSE;
        }
        if (Math.abs(robot.shooter.getShooterRPM() - Settings.Shooter.FENDER_RPM.get()) < 500) {
            return LEDColor.WHITE_PULSE;
        }

        return LEDColor.OFF;
    }

    @Override
    public void periodic() {
        // If we called .setColor() recently, use that value
        if (DriverStation.isAutonomous()
                || lastUpdate.getTime() < Settings.LED.MANUAL_UPDATE_TIME) {
            controller.set(manualColor.get());
        }

        // Otherwise use the default color
        else {
            controller.set(getDefaultColor().get());
        }
    }
}
