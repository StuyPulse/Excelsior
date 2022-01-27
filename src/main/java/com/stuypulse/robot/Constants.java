/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot;

import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import java.nio.file.Path;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public interface Constants {

    public static Path DEPLOY_DIRECTORY = Filesystem.getDeployDirectory().toPath();

    public static SmartBoolean DEBUG_MODE = new SmartBoolean("Debug Mode", false);

    public interface Ports {

        public interface Gamepad {
            int DRIVER = 0;
            int OPERATOR = 1;
            int DEBUGGER = 2;
        }

        public interface Drivetrain {
            int LEFT_TOP = 10;
            int LEFT_MIDDLE = 11;
            int LEFT_BOTTOM = 12;

            int RIGHT_TOP = 13;
            int RIGHT_MIDDLE = 14;
            int RIGHT_BOTTOM = 15;

            int GEAR_SHIFT = -1;
        }

        public interface Shooter {
            int SHOOTER = 20;
            int SHOOTER_FOLLOWER = 21;
            int FEEDER = 22;

            int HOOD_SOLENOID = -1;
        }

        public interface Climber {}

        public interface Intake {
            int MOTOR= -1;
            int SOLENOID_A = -1;
            int SOLENOID_B = -1;
        }

        public interface Conveyor {
            int TOP_CONVEYOR_MOTOR = 31;
            int GANDALF_MOTOR = 30;
            
            int COLOR_SENSOR = -1;
            int IR_SENSOR = -1;
        }

        I2C.Port COLOR_SENSOR = I2C.Port.kOnboard;
    }

    public interface ColorSensorSettings {
<<<<<<< HEAD
        public interface BallColor {
            Color RED = new Color(0.5432, 0.3401, 0.1169);
            Color BLUE = new Color(0.1826, 0.42505, 0.3982);
        }
        
=======
>>>>>>> cb19a16 (Move Ball Colors to CurrentBall enum)
        SmartNumber MIN_CONFIDENCE = new SmartNumber("Color Sensor/Confidence", 0.8);
    }

    public interface IntakeSettings {
        // TODO: test with intake
        double MOTOR_SPEED = 0.8;
    }

    public interface ShooterSettings {

        SmartNumber RING_RPM = new SmartNumber("Shooter/Ring RPM", 3900);
        SmartNumber FENDER_RPM = new SmartNumber("Shooter/Fender RPM", 3000);
        SmartNumber FEEDER_MULTIPLER = new SmartNumber("Shooter/Fender Multipler", 1.0);

        public interface ShooterPID {
            double kP = 0.0;
            double kI = 0.0;
            double kD = 0.0;
            double kF = 0.0;
        }

        public interface FeederPID {
            double kP = 0.0;
            double kI = 0.0;
            double kD = 0.0;
            double kF = 0.0;
        }
    }

    public interface DrivetrainSettings {
        // If speed is below this, use quick turn
        SmartNumber BASE_TURNING_SPEED = new SmartNumber("Driver Settings/Base Turn Speed", 0.4);

        // Low Pass Filter and deadband for Driver Controls
        SmartNumber SPEED_DEADBAND = new SmartNumber("Driver Settings/Speed Deadband", 0.0);
        SmartNumber ANGLE_DEADBAND = new SmartNumber("Driver Settings/Turn Deadband", 0.0);

        SmartNumber SPEED_POWER = new SmartNumber("Driver Settings/Speed Power", 1.0);
        SmartNumber ANGLE_POWER = new SmartNumber("Driver Settings/Turn Power", 1.0);

        SmartNumber SPEED_FILTER = new SmartNumber("Driver Settings/Speed Filtering", 0.2);
        SmartNumber ANGLE_FILTER = new SmartNumber("Driver Settings/Turn Filtering", 0.01);

        // Current Limit for the motors
        int CURRENT_LIMIT = 30; // TODO: figure out SMART current limit

        // If the motors are inverted
        boolean IS_INVERTED = true;

        // Width of the robot
        double TRACK_WIDTH = Units.inchesToMeters(26.9); // SEAN PROMISED !

        interface Motion {

            DifferentialDriveKinematics KINEMATICS = new DifferentialDriveKinematics(TRACK_WIDTH);

            SimpleMotorFeedforward MOTOR_FEED_FORWARD =
                    new SimpleMotorFeedforward(FeedForward.S, FeedForward.V, FeedForward.A);

            interface FeedForward {
                double S = -1; // TODO: characterize
                double V = -1; // TODO: characterize
                double A = -1; // TODO: characterize
            }

            interface PID {
                double P = -1; // TODO: characterize
                double I = -1; // TODO: characterize
                double D = -1; // TODO: characterize
            }
        }

        public interface Odometry {
            Translation2d STARTING_TRANSLATION = new Translation2d();
            Rotation2d STARTING_ANGLE = new Rotation2d();

            Pose2d STARTING_POSITION = new Pose2d(STARTING_TRANSLATION, STARTING_ANGLE);
        }

        // Encoder Constants
        public interface Encoders {
            double WHEEL_DIAMETER = Units.inchesToMeters(4);
            double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

            double LOW_GEAR_DISTANCE_PER_ROTATION = WHEEL_CIRCUMFERENCE * (1.0 / 16.71);
            double HIGH_GEAR_DISTANCE_PER_ROTATION = WHEEL_CIRCUMFERENCE * (1.0 / 4.55);
        }
        
    public interface ConveyorSettings {
        SmartNumber TOP_MOTOR_SPEED = new SmartNumber("Conveyor/Top Belt Speed", 0.6);
        SmartNumber REJECT_SPEED = new SmartNumber("Conveyor/Reject Speed", -0.6);
        SmartNumber ACCEPT_SPEED = new SmartNumber("Conveyor/Accept Speed", 0.6);
    }
}
