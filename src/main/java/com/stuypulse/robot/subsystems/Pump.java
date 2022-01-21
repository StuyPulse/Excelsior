/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2021 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.network.SmartBoolean;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pump extends SubsystemBase {

    private final SmartBoolean enabled;

    public Pump() {
        enabled = new SmartBoolean("Pump/Compressor Enabled", false);
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
    }

    @Override
    public void periodic() {
    }
}
