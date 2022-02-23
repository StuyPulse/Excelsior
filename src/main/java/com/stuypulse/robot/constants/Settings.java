/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.util.Color;

import java.nio.file.Path;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public interface Settings {

    Path DEPLOY_DIRECTORY = Filesystem.getDeployDirectory().toPath();

    SmartBoolean DEBUG_MODE = new SmartBoolean("Debug Mode", true);

    public interface Climber {
        boolean ENABLE_TILT = false;

        SmartBoolean ENABLE_SWITCHES = new SmartBoolean("Climber/Enable Switches", false);

        SmartNumber CLIMBER_DEFAULT_SPEED = new SmartNumber("Climber/Default Speed", 1.0);
        SmartNumber CLIMBER_SLOW_SPEED = new SmartNumber("Climber/Slow Speed", 0.2);

        SmartNumber CLIMBER_DELAY = new SmartNumber("Climber/Delay", 0.1);
    }

    public interface ColorSensor {
        SmartBoolean ENABLED = new SmartBoolean("Color Sensor/Enabled", true);

        public interface BallColor {
            Color RED = new Color(0.331, 0.428, 0.241);
            Color BLUE = new Color(0.2, 0.432, 0.368);
        }

        SmartNumber PROXIMITY_THRESHOLD = new SmartNumber("Color Sensor/Proximity Threshold", 150);
    }

    public interface Conveyor {
        SmartNumber TOP_BELT_SPEED = new SmartNumber("Conveyor/Top Belt Speed", 1.0);
        SmartNumber ACCEPT_SPEED = new SmartNumber("Conveyor/Accept Speed", 1.0);
        SmartNumber REJECT_SPEED = new SmartNumber("Conveyor/Reject Speed", -1.0);

        SmartBoolean DISABLE_IR_SENSOR = new SmartBoolean("Conveyor/Disable IR Sensor", false);

        SmartBoolean AUTO_RETRACT = new SmartBoolean("Conveyor/Auto Retract", true);
    }

    public interface Drivetrain {
        // If speed is below this, use quick turn
        SmartNumber BASE_TURNING_SPEED = new SmartNumber("Driver Settings/Base Turn Speed", 0.25);

        // Low Pass Filter and deadband for Driver Controls
        SmartNumber SPEED_DEADBAND = new SmartNumber("Driver Settings/Speed Deadband", 0.05);
        SmartNumber ANGLE_DEADBAND = new SmartNumber("Driver Settings/Turn Deadband", 0.05);

        SmartNumber SPEED_POWER = new SmartNumber("Driver Settings/Speed Power", 1.0);
        SmartNumber ANGLE_POWER = new SmartNumber("Driver Settings/Turn Power", 1.0);

        SmartNumber SPEED_FILTER = new SmartNumber("Driver Settings/Speed Filtering", 0.2);
        SmartNumber ANGLE_FILTER = new SmartNumber("Driver Settings/Turn Filtering", 0.01);

        // Width of the robot
        double TRACK_WIDTH = Units.inchesToMeters(26.9); // SEAN PROMISED !

        boolean USING_GYRO = true;

        public interface Motion {

            DifferentialDriveKinematics KINEMATICS = new DifferentialDriveKinematics(TRACK_WIDTH);

            SimpleMotorFeedforward MOTOR_FEED_FORWARD =
                    new SimpleMotorFeedforward(FeedForward.kS, FeedForward.kV, FeedForward.kA);

            double MAX_VELOCITY = Units.feetToMeters(16.0);
            double MAX_ACCELERATION = Units.feetToMeters(8.0);

            public interface FeedForward {
                double kS = 0; // TODO: characterize
                double kV = 0; // TODO: characterize
                double kA = 0; // TODO: characterize
            }

            public interface PID {
                double kP = 0; // TODO: characterize
                double kI = 0; // TODO: characterize
                double kD = 0; // TODO: characterize
            }
        }

        public interface Odometry {
            Translation2d STARTING_TRANSLATION = new Translation2d();
            Rotation2d STARTING_ANGLE = new Rotation2d();

            Pose2d STARTING_POSITION = new Pose2d(STARTING_TRANSLATION, STARTING_ANGLE);
        }

        public interface Stalling {
            // Enable / Disable the Stall Detection
            SmartBoolean STALL_DETECTION =
                    new SmartBoolean("Driver Settings/Stall Detection", false);

            // Motor will hit current limit when stalling
            double CURRENT_THRESHOLD = Motors.Drivetrain.CURRENT_LIMIT_AMPS - 10;

            // If we are trying to go at full speed,
            // it doesnt matter if our current draw isnt that high
            double DUTY_CYCLE_THRESHOLD = 0.8;

            // This is about half the speed of low gear
            // High Gear should be able to reach this speed
            double VELOCITY_THESHOLD = Units.feetToMeters(1.2);

            // Debounce Time
            double DEBOUNCE_TIME = 1.0;
        }

        // Encoder Constants
        public interface Encoders {

            public interface GearRatio {

                public interface Stages {
                    double INITIAL_STAGE = (11.0 / 50.0);

                    double HIGH_GEAR_STAGE = (50.0 / 34.0);
                    double LOW_GEAR_STAGE = (24.0 / 60.0);

                    double GRAYHILL_STAGE = (12.0 / 36.0);

                    double THIRD_STAGE = (34.0 / 50.0);

                    double EXTERNAL_STAGE = (1.0 / 1.0);
                }

                /** = 0.22666 */
                double GRAYHILL_TO_WHEEL =
                        Stages.GRAYHILL_STAGE * Stages.THIRD_STAGE * Stages.EXTERNAL_STAGE;
            }

            double WHEEL_DIAMETER = Units.inchesToMeters(4);
            double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

            double GRAYHILL_PULSES_PER_REVOLUTION = 256;
            double GRAYHILL_DISTANCE_PER_PULSE =
                    (WHEEL_CIRCUMFERENCE / GRAYHILL_PULSES_PER_REVOLUTION)
                            * GearRatio.GRAYHILL_TO_WHEEL;
        }
    }

    public interface Intake {
        SmartNumber MOTOR_SPEED = new SmartNumber("Intake/Motor Speed", 1.0);
    }

    public interface LED {
        double MANUAL_UPDATE_TIME = 0.65;

        double BLINK_TIME = 0.5;
    }

    public interface Shooter {

        double MIN_RPM = 100.0;

        SmartNumber RING_RPM = new SmartNumber("Shooter/Ring RPM", 3900);
        SmartNumber FENDER_RPM = new SmartNumber("Shooter/Fender RPM", 3000);
        SmartNumber FEEDER_MULTIPLER = new SmartNumber("Shooter/Feeder Multipler", 1.2);

        public interface ShooterPID {
            double kP = 0.00025;
            double kI = 0.0;
            double kD = 0.0;
            double kF = 0.0001725;
        }

        public interface FeederPID {
            double kP = 0.00015;
            double kI = 0.0;
            double kD = 0.0;
            double kF = 0.0001825;
        }
    }

    public interface Limelight {
        double LIMELIGHT_HEIGHT = Units.inchesToMeters(39.135042);

        // if the intake is on the ring, distance of limelight to hub
        double CENTER_TO_HUB = Field.Hub.UPPER_RADIUS;
        double LIMELIGHT_TO_SHOOTER = Units.inchesToMeters(14);
        double RING_SHOT_DISTANCE =
                Units.inchesToMeters(128.5) - CENTER_TO_HUB - LIMELIGHT_TO_SHOOTER;

        double HEIGHT_DIFFERENCE = Field.Hub.HEIGHT - LIMELIGHT_HEIGHT;

        // TODO: Measure with ???
        SmartNumber LIMELIGHT_PITCH = new SmartNumber("Limelight/Pitch", 35.0);
        SmartNumber LIMELIGHT_YAW = new SmartNumber("Limelight/Yaw", 0);

        // Bounds for Distance
        double MIN_VALID_DISTANCE = Units.feetToMeters(2);
        double MAX_VALID_DISTANCE = Field.LENGTH / 2.0;

        // What angle error should make us start distance alignment
        SmartNumber MAX_ANGLE_FOR_MOVEMENT =
                new SmartNumber("Limelight/Max Angle For Distance", 2.0);

        SmartNumber MAX_ANGLE_ERROR = new SmartNumber("Limelight/Max Angle Error", 1.5);
        SmartNumber MAX_DISTANCE_ERROR = new SmartNumber("Limelight/Max Distance Error", 0.15);
    }

    public interface Alignment {

        SmartNumber SPEED_ADJ_FILTER = new SmartNumber("Drivetrain/Alignment/Speed Adj RC", 0.1);
        SmartNumber FUSION_FILTER = new SmartNumber("Drivetrain/Alignment/Fusion RC", 0.5);

        public interface Speed {
            SmartNumber kP = new SmartNumber("Drivetrain/Alignment/Speed/P", 0.8);
            SmartNumber kI = new SmartNumber("Drivetrain/Alignment/Speed/I", 0);
            SmartNumber kD = new SmartNumber("Drivetrain/Alignment/Speed/D", 0.06);

            SmartNumber ERROR_FILTER =
                    new SmartNumber("Drivetrain/Alignment/Speed/Error Filter", 0.0);
            SmartNumber OUT_FILTER =
                    new SmartNumber("Drivetrain/Alignment/Speed/Output Filter", 0.2);

            public static Controller getController() {
                return new PIDController(kP, kI, kD)
                        .setErrorFilter(new LowPassFilter(ERROR_FILTER))
                        .setOutputFilter(new LowPassFilter(OUT_FILTER));
            }
        }

        public interface Angle {
            SmartNumber kP = new SmartNumber("Drivetrain/Alignment/Angle/P", 0.022);
            SmartNumber kI = new SmartNumber("Drivetrain/Alignment/Angle/I", 0);
            SmartNumber kD = new SmartNumber("Drivetrain/Alignment/Angle/D", 0.0023);

            SmartNumber ERROR_FILTER =
                    new SmartNumber("Drivetrain/Alignment/Angle/Error Filter", 0.0);
            SmartNumber OUT_FILTER =
                    new SmartNumber("Drivetrain/Alignment/Angle/Output Filter", 0.06);

            public static Controller getController() {
                return new PIDController(kP, kI, kD)
                        .setErrorFilter(new LowPassFilter(ERROR_FILTER))
                        .setOutputFilter(new LowPassFilter(OUT_FILTER));
            }
        }
    }
}
