/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.math.SLMath;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.DrivetrainSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * Javadoc Comments must start with /*-
 *
 * Moves the robot around
 *
 * Contains:
 *      - 3 motors on left side
 *      - 3 motors on right side
 *      - Encoders for both sides
 *      - Gear shifting solonoid
 *      - NavX / Gyroscope
 *
 * @author Myles Pasetsky
 * @author Sam Belliveau
 * @author Samuel Chen
 * @author Andrew Liu
 * @author Ryan Zhou
 * @author Jack Zheng
 * @author Daniel Chang
 * @author Benjamin Beloster
 * @author Russell Goyachev
 * @author James Li
 * @author Zhi Ming Xu
 */
public class Drivetrain extends SubsystemBase {

    // Enum used to store the state of the gear
    public static enum Gear {
        HIGH,
        LOW
    };

    // An array of motors on the left and right side of the drive train
    private CANSparkMax[] leftMotors;
    private CANSparkMax[] rightMotors;

    // An encoder for each side of the drive train

    private RelativeEncoder leftNEO;
    private RelativeEncoder rightNEO;

    private Encoder leftGrayhill;
    private Encoder rightGrayhill;

    // DifferentialDrive and Gear Information
    private Gear gear;
    private Solenoid gearShift;
    private DifferentialDrive drivetrain;

    // NAVX for Gyro
    private AHRS navx;

    // Odometry
    private DifferentialDriveOdometry odometry;
    private Field2d field;

    public Drivetrain() {
        // Add Motors to list
        leftMotors =
                new CANSparkMax[] {
                    new CANSparkMax(Ports.Drivetrain.LEFT_TOP, MotorType.kBrushless),
                    new CANSparkMax(Ports.Drivetrain.LEFT_MIDDLE, MotorType.kBrushless),
                    new CANSparkMax(Ports.Drivetrain.LEFT_BOTTOM, MotorType.kBrushless)
                };

        rightMotors =
                new CANSparkMax[] {
                    new CANSparkMax(Ports.Drivetrain.RIGHT_TOP, MotorType.kBrushless),
                    new CANSparkMax(Ports.Drivetrain.RIGHT_MIDDLE, MotorType.kBrushless),
                    new CANSparkMax(Ports.Drivetrain.RIGHT_BOTTOM, MotorType.kBrushless)
                };

        // Create list of encoders based on motors
        leftNEO = leftMotors[0].getEncoder();
        rightNEO = rightMotors[0].getEncoder();
        setNEODistancePerRotation(Constants.DrivetrainSettings.Encoders.HIGH_GEAR_DISTANCE_PER_ROTATION);

        leftGrayhill = new Encoder(
            Ports.Drivetrain.Encoders.LEFT_A, 
            Ports.Drivetrain.Encoders.LEFT_B
        );

        rightGrayhill = new Encoder(
            Ports.Drivetrain.Encoders.RIGHT_A,
            Ports.Drivetrain.Encoders.RIGHT_B
        );
        setGrayhillDistancePerPulse(Constants.DrivetrainSettings.Encoders.GRAYHILL_DISTANCE_PER_PULSE);

        // TODO: this might not mean what I think it means (based on my understanading 
        // of the docs, it is used for velocity, and not position) and 4 might not
        // be  enough/ too little  
        // leftGrayhill.setVelocitySamples(4);
        // rightGrayhill.setVelocitySamples(4);


        // Make differential drive object
        drivetrain =
                new DifferentialDrive(
                        new MotorControllerGroup(leftMotors),
                        new MotorControllerGroup(rightMotors));

        // Add Gear Shifter
        gearShift = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Drivetrain.GEAR_SHIFT);

        // Initialize NAVX
        navx = new AHRS(SPI.Port.kMXP);

        // Initialize Odometry
        odometry =
                new DifferentialDriveOdometry(
                        DrivetrainSettings.Odometry.STARTING_ANGLE,
                        DrivetrainSettings.Odometry.STARTING_POSITION);
        field = new Field2d();

        // Configure Motors and Other Things
        setInverted(DrivetrainSettings.IS_INVERTED, !DrivetrainSettings.IS_INVERTED);
        setSmartCurrentLimit(DrivetrainSettings.CURRENT_LIMIT);
        setIdleMode(IdleMode.kBrake);
        setHighGear();

        // Add Children to Subsystem
        addChild("Gear Shift", gearShift);
        addChild("Differential Drive", drivetrain);
        addChild("NavX", navx);
        addChild("Field Map", field);
    }

    /***********************
     * MOTOR CONFIGURATION *
     ***********************/

    // Set the distance traveled in one rotation of the motor
    private void setNEODistancePerRotation(double distance) {
        leftNEO.setPositionConversionFactor(distance);
        leftNEO.setPosition(0);

        rightNEO.setPositionConversionFactor(distance);
        rightNEO.setPosition(0);
    }

    private void setGrayhillDistancePerPulse(double distance) {
        rightGrayhill.setDistancePerPulse(distance);
        rightGrayhill.reset();

        leftGrayhill.setDistancePerPulse(distance);
        leftGrayhill.reset();
    }

    // Set the smart current limit of all the motors
    public void setSmartCurrentLimit(int limit) {
        for (CANSparkMax motor : leftMotors) {
            motor.setSmartCurrentLimit(limit);
        }

        for (CANSparkMax motor : rightMotors) {
            motor.setSmartCurrentLimit(limit);
        }
    }

    // Set the idle mode of the all the motors
    public void setIdleMode(IdleMode mode) {
        for (CANSparkMax motor : leftMotors) {
            motor.setIdleMode(mode);
        }

        for (CANSparkMax motor : rightMotors) {
            motor.setIdleMode(mode);
        }
    }

    // Set isInverted of all the motors
    public void setInverted(boolean leftSide, boolean rightSide) {
        leftNEO.setInverted(leftSide);
        leftGrayhill.setReverseDirection(leftSide);
        for (CANSparkMax motor : leftMotors) {
            motor.setInverted(leftSide);
        }

        rightNEO.setInverted(rightSide);
        rightGrayhill.setReverseDirection(rightSide);
        for (CANSparkMax motor : rightMotors) {
            motor.setInverted(rightSide);
        }
    }

    /*****************
     * Gear Shifting *
     *****************/

    // Gets the current gear the robot is in
    public Gear getGear() {
        return gear;
    }

    // Sets the current gear the robot is in
    public void setGear(Gear gear) {
        // TODO: just note here, reset is the biggest change

        if (this.gear != gear) {
            this.gear = gear;
            if (this.gear == Gear.HIGH) {
                gearShift.set(true);
                setNEODistancePerRotation(
                        DrivetrainSettings.Encoders.HIGH_GEAR_DISTANCE_PER_ROTATION);
                reset();
            } else {
                gearShift.set(false);
                setNEODistancePerRotation(
                        DrivetrainSettings.Encoders.LOW_GEAR_DISTANCE_PER_ROTATION);
                reset();
            }
        }
    }

    // Sets robot into low gear
    public void setLowGear() {
        setGear(Gear.LOW);
    }

    // Sets robot into high gear
    public void setHighGear() {
        setGear(Gear.HIGH);
    }

    /********
     * NAVX *
     ********/

    // Gets current Angle of the Robot as a double (contiuous / not +-180)
    public double getRawAngle() {
        return navx.getAngle();
    }

    // Gets current Angle of the Robot
    public Angle getAngle() {
        return Angle.fromDegrees(getRawAngle());
    }

    private void resetNavX() {
        navx.reset();
    }

    /*********************
     * ENCODER FUNCTIONS *
     *********************/

    private boolean getUsingGrayhills() {
        return Constants.DrivetrainSettings.USING_GRAYHILLS;
    }

    // Distance
    public double getLeftDistance() {
        return getUsingGrayhills() ? leftGrayhill.getDistance() : leftNEO.getPosition();
    }

    public double getRightDistance() {
        return getUsingGrayhills() ? rightGrayhill.getDistance() : rightNEO.getPosition();
    }

    public double getDistance() {
        return (getLeftDistance() + getRightDistance()) / 2.0;
    }

    // Velocity
    public double getLeftVelocity() {
        return getUsingGrayhills() ? leftGrayhill.getRate() : leftNEO.getVelocity();
    }

    public double getRightVelocity() {
        return getUsingGrayhills() ? rightGrayhill.getRate() : rightNEO.getVelocity();
    }

    public double getVelocity() {
        return (getLeftVelocity() + getRightVelocity()) / 2.0;
    }

    // Angle
    private double getEncoderRotations() {
        // this distance is in meters (converted from rotations by the encoder)
        double distance = getLeftDistance() - getRightDistance();

        // undo the conversion done by the encoder to get rotations
        return distance / DrivetrainSettings.TRACK_WIDTH;
    }

    public Angle getEncoderAngle() {
        return Angle.fromRotations(getEncoderRotations());
    }

    /**********************
     * ODOMETRY FUNCTIONS *
     **********************/

    private void updateOdometry() {
        odometry.update(getRotation2d(), getLeftDistance(), getRightDistance());
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
    }

    public Rotation2d getRotation2d() {
        // TODO: check if this needs to be negative
        return getAngle().negative().getRotation2d();
    }

    public Pose2d getPose() {
        updateOdometry();
        return odometry.getPoseMeters();
    }

    public Field2d getField() {
        return field;
    }

    /************************
     * OVERALL SENSOR RESET *
     ************************/

    public void reset(Pose2d location) {
        resetNavX();

        leftGrayhill.reset();
        rightGrayhill.reset();
        leftNEO.setPosition(0);
        rightNEO.setPosition(0);

        odometry.resetPosition(location, getAngle().getRotation2d());
    }

    public void reset() {
        reset(getPose());
    }

    /*********************
     * VOLTAGE FUNCTIONS *
     *********************/

    public double getBatteryVoltage() {
        return RobotController.getBatteryVoltage();
    }

    public double getLeftVoltage() {
        return leftMotors[0].get() * getBatteryVoltage();
    }

    public double getRightVoltage() {
        return rightMotors[0].get() * getBatteryVoltage();
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        for (MotorController motor : leftMotors) {
            motor.setVoltage(leftVolts);
        }

        for (MotorController motor : rightMotors) {
            motor.setVoltage(rightVolts);
        }

        drivetrain.feed();
    }

    /********************
     * DRIVING COMMANDS *
     ********************/

    // Stops drivetrain from moving
    public void stop() {
        drivetrain.stopMotor();
    }

    // Drives using tank drive
    public void tankDrive(double left, double right) {
        drivetrain.tankDrive(left, right, false);
    }

    // Drives using arcade drive
    public void arcadeDrive(double speed, double rotation) {
        drivetrain.arcadeDrive(speed, rotation, false);
    }

    // Drives using curvature drive algorithm
    public void curvatureDrive(double xSpeed, double zRotation, double baseTS) {
        // Clamp all inputs to valid values;
        xSpeed = SLMath.clamp(xSpeed, -1.0, 1.0);
        zRotation = SLMath.clamp(zRotation, -1.0, 1.0);
        baseTS = SLMath.clamp(baseTS, 0.0, 1.0);

        // Find the amount to slow down turning by.
        // This is proportional to the speed but has a base value
        // that it starts from (allows turning in place)
        double turnAdj = Math.max(baseTS, Math.abs(xSpeed));

        // Find the speeds of the left and right wheels
        double lSpeed = xSpeed + zRotation * turnAdj;
        double rSpeed = xSpeed - zRotation * turnAdj;

        // Find the maximum output of the wheels, so that if a wheel tries to go > 1.0
        // it will be scaled down proportionally with the other wheels.
        double scale = Math.max(1.0, Math.max(Math.abs(lSpeed), Math.abs(rSpeed)));

        lSpeed /= scale;
        rSpeed /= scale;

        // Feed the inputs to the drivetrain
        drivetrain.tankDrive(lSpeed, rSpeed, false);
    }

    // Drives using curvature drive algorithm with automatic quick turn
    public void curvatureDrive(double xSpeed, double zRotation) {
        this.curvatureDrive(xSpeed, zRotation, DrivetrainSettings.BASE_TURNING_SPEED.get());
    }

    /*********************
     * DEBUG INFORMATION *
     *********************/

    @Override
    public void periodic() {
        updateOdometry();
        field.setRobotPose(getPose());

        // Smart Dashboard Information

        if (Constants.DEBUG_MODE.get()) {

            SmartDashboard.putData("Drivetrain/Field", field);
            SmartDashboard.putString(
                    "Drivetrain/Current Gear",
                    getGear().equals(Gear.HIGH) ? "High Gear" : "Low Gear");
            SmartDashboard.putNumber("Drivetrain/Odometer X Position (m)", getPose().getX());
            SmartDashboard.putNumber("Drivetrain/Odometer Y Position (m)", getPose().getY());
            SmartDashboard.putNumber(
                    "Drivetrain/Odometer Rotation (deg)", getPose().getRotation().getDegrees());

            SmartDashboard.putNumber("Drivetrain/Motor Voltage Left (V)", getLeftVoltage());
            SmartDashboard.putNumber("Drivetrain/Motor Voltage Right (V)", getRightVoltage());

            SmartDashboard.putNumber("Drivetrain/Distance Traveled (m)", getDistance());
            SmartDashboard.putNumber("Drivetrain/Distance Traveled Left (m)", getLeftDistance());
            SmartDashboard.putNumber("Drivetrain/Distance Traveled Right (m)", getRightDistance());

            SmartDashboard.putNumber("Drivetrain/Velocity (m per s)", getVelocity());
            SmartDashboard.putNumber("Drivetrain/Velocity Left (m per s)", getLeftVelocity());
            SmartDashboard.putNumber("Drivetrain/Velocity Right (m per s)", getRightVelocity());

            SmartDashboard.putNumber("Drivetrain/Angle NavX (deg)", getAngle().toDegrees());
            SmartDashboard.putNumber(
                    "Drivetrain/Encoder Angle (deg)", getEncoderAngle().toDegrees());
        }
    }
}
