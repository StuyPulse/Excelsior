/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

public class UnknownPorts {

    private static int solenoid = 0;
    private static int sensor = 0;

    public static int getFakeSolenoid() {
        return solenoid++;
    }

    public static int getFakeSensor() {
        return sensor++;
    }
}
