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
    private StopWatch lastUpdateTwo;

    // The robot container to get information from
    private final RobotContainer robot;

    // The current color to set the LEDs to
    private LEDColor manualColor;

    public LEDController(RobotContainer container) {
        this.controller = new PWMSparkMax(Ports.LEDController.PWM_PORT);
        this.lastUpdate = new StopWatch();
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

         
        
        if (robot.conveyor.isFull()) return LEDColor.GREEN_SOLID;

        if (robot.colorSensor.hasBall()) {
            if (robot.colorSensor.getCurrentBall() == CurrentBall.BLUE_BALL) {
                this.lastUpdateTwo = new StopWatch();
               
               while (lastUpdateTwo.getTime() < .75){
                    return LEDColor.BLUE_SOLID;
               }

            } else {
                while (lastUpdateTwo.getTime() < .75){
                    return LEDColor.ORANGE_SOLID;
               };
            }
        }

        if (Math.abs(robot.shooter.getShooterRPM() - Settings.Shooter.RING_RPM.get()) < 200) {
            return LEDColor.RED_PULSE;
        }
        if (Math.abs(robot.shooter.getShooterRPM() - Settings.Shooter.FENDER_RPM.get()) < 200) {
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
