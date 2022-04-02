/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import edu.wpi.first.wpilibj.I2C;

/** This file contains the different ports of motors, solenoids and sensors */
public interface Ports {

    public interface Gamepad {
        int DRIVER = 0;
        int OPERATOR = 1;
        int DEBUGGER = 2;
    }

    public interface Climber {
        // Motors
        int MOTOR = 50;

        // Solenoids
        int TILTER_FORWARD = 6;
        int TILTER_REVERSE = 7;
    }

    public interface ColorSensor {
        I2C.Port COLOR_SENSOR = I2C.Port.kMXP;
        int BALL_IR_SENSOR = 4;
    }

    public interface Conveyor {
        // Motors
        int GANDALF_MOTOR = 30;
        int TOP_BELT_MOTOR = 31;

        // Sensors
        int TOP_BELT_IR_SENSOR = 5;
    }

    public interface Drivetrain {
        // Motors
        int LEFT_TOP = 10;
        int LEFT_MIDDLE = 11;
        int LEFT_BOTTOM = 12;

        int RIGHT_TOP = 13;
        int RIGHT_MIDDLE = 14;
        int RIGHT_BOTTOM = 15;

        // Soleniods
        int GEAR_SHIFT_FORWARD = 0;
        int GEAR_SHIFT_REVERSE = 1;
    }

    interface Grayhill {
        // Sensors
        int LEFT_A = 0;
        int LEFT_B = 1;

        int RIGHT_A = 2;
        int RIGHT_B = 3;
    }

    public interface Intake {
        // Motors
        int MOTOR = 40;

        // Soleniods
        int SOLENOID_FORWARD = 2;
        int SOLENOID_REVERSE = 3;
    }

    public interface LEDController {
        int PWM_PORT = 0;
    }

    public interface Pump {
        // Sensors
        int PRESSURE_SENSOR = 9;
    }

    public interface Shooter {
        // Motors
        int LEFT = 20;
        int RIGHT = 21;
        int FEEDER = 22;

        // Solenoids
        int HOOD_SOLENOID = 5;
    }
}
