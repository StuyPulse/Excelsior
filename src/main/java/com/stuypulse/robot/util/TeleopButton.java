/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.function.BooleanSupplier;

/** @author Myles Pasetsky (@selym3) */
public class TeleopButton extends Button {

    public TeleopButton(BooleanSupplier pressed) {
        super(() -> DriverStation.isTeleop() && pressed.getAsBoolean());
    }
}
