/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.network.SmartBoolean;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

    private final Debouncer disable;

    public Pump() {
        enabled = new SmartBoolean("Pump/Compressor Enabled", true);
        compressor = new Compressor(PneumaticsModuleType.CTREPCM);

        disable = new Debouncer(1.0, DebounceType.kRising);

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

    // Determine if we should disable the pump
    private boolean shouldDisable() {
        return disable.calculate(
            enabled.get() &&
            !getCompressing()
        );
    }

    @Override
    public void periodic() {
        if (enabled.get()) {
            compressor.enableDigital();

            if (shouldDisable()) {
                stop();
            }
        } else {
            compressor.disable();
        }
    }
}
