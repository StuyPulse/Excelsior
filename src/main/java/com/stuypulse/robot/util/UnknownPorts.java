/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

/*-
 * Contains:
 *      - Way to easily generate unique port numbers for
 *        simulation while making it clear that the port
 *        is only temporary.
 *
 *      - getFakeSolenoid()
 *          - returns fake port number for a solenoid
 *      - getFakeSensor()
 *          - returns fake port number for a sensor
 *
 * @author Sam Belliveau
 */
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
