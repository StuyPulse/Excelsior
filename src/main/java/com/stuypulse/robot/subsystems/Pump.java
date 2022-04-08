/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Settings.LED;
import com.stuypulse.stuylib.network.SmartBoolean;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.stuypulse.robot.constants.Settings.Pump.*;

/*-
 * Pumps the robot full of air
 *
 * Contains:
 *      - network boolean for controlling state of compressor
 *      - compressor pneumatics module
 *
 * @author Myles Pasetsky
 * @author SE
 */
public class Pump extends SubsystemBase {

    private final SmartBoolean enabled;
    private final Compressor compressor;

    public Pump() {
        enabled = new SmartBoolean("Pump/Compressor Enabled", true);
        compressor = new Compressor(PneumaticsModuleType.CTREPCM);

        stop();
    }

    public boolean getCompressing() {
        return compressor.enabled();
    }

    // Start Compressing the Robot
    public void compress() {
        this.set(true);
    }

    // Stop Compressing
    public void stop() {
        this.set(false);
    }

    // Set the compressor to on or off
    public void set(boolean compressing) {
        enabled.set(compressing);
    }

    @Override
    public void periodic() {
        final double time = DriverStation.getMatchTime();
        boolean autoCompress = time > LED.MIN_MATCH_TIME && time < START_COMPRESSING;

        if (enabled.get() || autoCompress) {
            compressor.enableDigital();
        } else {
            compressor.disable();
        }
    }
}
