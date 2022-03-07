/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.util.StopWatch;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.ColorSensor.BallColor;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.DriverStation;
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
            if (blueBall.calculate(robot.colorSensor.getCurrentBall() == BallColor.BLUE_BALL)) {
                return LEDColor.BLUE;
            }

            if (redBall.calculate(robot.colorSensor.getCurrentBall() == BallColor.RED_BALL)) {
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
            return LEDColor.FIRE.pulse();
        }
        if (Math.abs(robot.shooter.getShooterRPM() - Settings.Shooter.FENDER_RPM.get()) < 500) {
            return LEDColor.WHITE.pulse();
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
