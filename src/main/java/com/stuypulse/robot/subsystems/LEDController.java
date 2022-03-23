/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.util.StopWatch;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.ColorSensor.BallColor;
import com.stuypulse.robot.util.LEDColor;
import com.stuypulse.robot.util.TeleopButton;

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
    private double manualTime;

    // The robot container to get information from
    private final RobotContainer robot;

    // The current color to set the LEDs to
    private LEDColor manualColor;

    public LEDController(RobotContainer container) {
        this.controller = new PWMSparkMax(Ports.LEDController.PWM_PORT);
        this.lastUpdate = new StopWatch();
        this.robot = container;

        setLEDConditions();
        setColor(LEDColor.OFF);
    }

    public void setColor(LEDColor color, double time) {
        manualColor = color;
        manualTime = time;
        lastUpdate.reset();
    }

    public void setColor(LEDColor color) {
        setColor(color, Settings.LED.MANUAL_UPDATE_TIME);
    }

    private void setLEDConditions() {
        new TeleopButton(robot.conveyor::isFull)
                .whenPressed(new LEDSetCommand(this, LEDColor.GREEN));
        new TeleopButton(() -> robot.colorSensor.hasBall(BallColor.RED_BALL))
                .whenPressed(new LEDSetCommand(this, LEDColor.RED_ORANGE));
        new TeleopButton(() -> robot.colorSensor.hasBall(BallColor.BLUE_BALL))
                .whenPressed(new LEDSetCommand(this, LEDColor.BLUE));
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
        if (robot.pump.getCompressing()) return LEDColor.HEARTBEAT;

        double shooterError =
                Math.abs(robot.shooter.getRawTargetRPM() - robot.shooter.getShooterRPM());

        if (robot.shooter.getRawTargetRPM() <= Settings.LED.RPM_ERROR_STEP) return LEDColor.OFF;
        else if (shooterError <= 1.0 * Settings.LED.RPM_ERROR_STEP) return LEDColor.GREEN;
        else if (shooterError <= 2.0 * Settings.LED.RPM_ERROR_STEP) return LEDColor.LIME;
        else if (shooterError <= 3.0 * Settings.LED.RPM_ERROR_STEP) return LEDColor.YELLOW;
        else if (shooterError <= 4.0 * Settings.LED.RPM_ERROR_STEP) return LEDColor.ORANGE;
        else if (shooterError <= 5.0 * Settings.LED.RPM_ERROR_STEP) return LEDColor.RED;
        else return LEDColor.RED.pulse();
    }

    @Override
    public void periodic() {
        // If we called .setColor() recently, use that value
        if (DriverStation.isAutonomous() || lastUpdate.getTime() < manualTime) {
            controller.set(manualColor.get());
        }

        // Otherwise use the default color
        else {
            controller.set(getDefaultColor().get());
        }
    }
}
