/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main {
    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }
}
