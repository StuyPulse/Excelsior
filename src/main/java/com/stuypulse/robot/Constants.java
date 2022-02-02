/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot;

import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

import java.nio.file.Path;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    Path DEPLOY_DIRECTORY = Filesystem.getDeployDirectory().toPath();

    SmartBoolean DEBUG_MODE = new SmartBoolean("Debug Mode", false);

    public interface Ports {

        public interface Gamepad {
            int DRIVER = 0;
            int OPERATOR = 1;
            int DEBUGGER = 2;
        }

        public interface Drivetrain {
            int LEFT_TOP = 7;
            int LEFT_BOTTOM = 6;

            int RIGHT_TOP = 4;
            int RIGHT_BOTTOM = 3;

            int LEFT_ENCODER_A = 0;
            int LEFT_ENCODER_B = 1;
            int RIGHT_ENCODER_A = 2;
            int RIGHT_ENCODER_B = 3;

            int GEAR_SHIFT = 0;
        }

        public interface Shooter {}

        public interface Climber {
            int SOLENOID_LONG = -1;
            int SOLENOID_SHORT = -1;
            int SOLENOID_STOPPER = -1;

            int MOTOR = 50;
            
            int BOTTOM_LIMIT_SWITCH = -1;
            int TOP_LIMIT_SWITCH = -1;
        }

        public interface Intake {}
    }
    public interface ClimberSettings {
        SmartNumber CLIMBER_DEFAULT_SPEED = new SmartNumber("Climber/Default Speed", -2);
        SmartNumber CLIMBER_SLOW_SPEED = new SmartNumber("Climber/Slow Speed", -2);

        boolean MOTOR_INVERTED = false;
        /*
        //36 : 1

        double GEAR_RATIO = 1.0 / 20.0;


        double DIAMETER = 1.0; //encoder to motor 
        double CIRCUMFERENCE = DIAMETER * Math.PI;
        double CONVERSION_FACTOR = GEAR_RATIO * CIRCUMFERENCE;
        */


    }
}
