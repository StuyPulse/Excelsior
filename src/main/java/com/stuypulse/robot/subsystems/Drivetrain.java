/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.DrivetrainSettings;
import com.stuypulse.robot.Constants.DrivetrainSettings.Stalling;
import com.stuypulse.robot.Constants.Ports;
import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.math.SLMath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
        HIGH(
            // Value.kForward, 
            true,
            DrivetrainSettings.Encoders.HIGH_GEAR_DISTANCE_PER_ROTATION
        ),
        
        LOW(
            // Value.kReverse, 
            false,
            DrivetrainSettings.Encoders.LOW_GEAR_DISTANCE_PER_ROTATION
        );

        private final boolean value;
        private final double ratio;

        private Gear(boolean value, double ratio) {
            this.value = value;
            this.ratio = ratio;
        }
    }

    // An array of motors on the left and right side of the drive train
    private final CANSparkMax[] leftMotors;
    private final CANSparkMax[] rightMotors;

    // An encoder for each side of the drive train
    private final RelativeEncoder leftNEO;
    private final RelativeEncoder rightNEO;

    private final Encoder leftGrayhill;
    private final Encoder rightGrayhill;

    // DifferentialDrive and Gear Information
    private Gear gear;
    // private final DoubleSolenoid gearShift;
    private final Solenoid gearShift;
    private final DifferentialDrive drivetrain;

    // NAVX for Gyro
    private final AHRS navx;

    // Odometry
    private final DifferentialDriveOdometry odometry;
    private final Field2d field;

    public Drivetrain() {
        // Add Motors to list
        leftMotors =
                new CANSparkMax[] {
                    new CANSparkMax(Ports.Drivetrain.LEFT_TOP, MotorType.kBrushless),
                    // new CANSparkMax(Ports.Drivetrain.LEFT_MIDDLE, MotorType.kBrushless),
                    new CANSparkMax(Ports.Drivetrain.LEFT_BOTTOM, MotorType.kBrushless)
                };

        rightMotors =
                new CANSparkMax[] {
                    new CANSparkMax(Ports.Drivetrain.RIGHT_TOP, MotorType.kBrushless),
                    // new CANSparkMax(Ports.Drivetrain.RIGHT_MIDDLE, MotorType.kBrushless),
                    new CANSparkMax(Ports.Drivetrain.RIGHT_BOTTOM, MotorType.kBrushless)
                };

        // Create list of encoders based on motors
        leftNEO = leftMotors[0].getEncoder();
        rightNEO = rightMotors[0].getEncoder();
        setNEODistancePerRotation(
                Constants.DrivetrainSettings.Encoders.HIGH_GEAR_DISTANCE_PER_ROTATION);

        leftGrayhill =
                new Encoder(Ports.Drivetrain.Encoders.LEFT_A, Ports.Drivetrain.Encoders.LEFT_B);

        rightGrayhill =
                new Encoder(Ports.Drivetrain.Encoders.RIGHT_A, Ports.Drivetrain.Encoders.RIGHT_B);
        setGrayhillDistancePerPulse(
                Constants.DrivetrainSettings.Encoders.GRAYHILL_DISTANCE_PER_PULSE);

        // Make differential drive object
        drivetrain =
                new DifferentialDrive(
                        new MotorControllerGroup(leftMotors),
                        new MotorControllerGroup(rightMotors));

        // Add Gear Shifter
        gearShift =
                new Solenoid(
                        PneumaticsModuleType.CTREPCM, 
                        Ports.Drivetrain.GEAR_SHIFT_A);
        
        // gearShift = 
        //     new DoubleSolenoid(
        //                 PneumaticsModuleType.CTREPCM,
        //                 Ports.Drivetrain.GEAR_SHIFT_A,
        //                 Ports.Drivetrain.GEAR_SHIFT_B);

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
        setSmartCurrentLimit(DrivetrainSettings.CURRENT_LIMIT_AMPS);
        setIdleMode(IdleMode.kBrake);
        setHighGear();

        // Save Motor Settings
        burnFlash();
    }

    /***********************
     * MOTOR CONFIGURATION *
     ***********************/

    private void burnFlash() {
        for (CANSparkMax motor : leftMotors) {
            motor.burnFlash();
        }

        for (CANSparkMax motor : rightMotors) {
            motor.burnFlash();
        }
    }

    // Set the distance traveled in one rotation of the motor
    private void setNEODistancePerRotation(double distancePerRotation) {
        leftNEO.setPositionConversionFactor(distancePerRotation);
        leftNEO.setVelocityConversionFactor(
                (1.0 / 60.0) * distancePerRotation); // Convert RPM -> rotations/s -> m/s
        leftNEO.setPosition(0);

        rightNEO.setPositionConversionFactor(distancePerRotation);
        rightNEO.setVelocityConversionFactor(
                (1.0 / 60.0) * distancePerRotation); // Convert RPM -> rotations/s -> m/s
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
        leftGrayhill.setReverseDirection(leftSide);
        for (CANSparkMax motor : leftMotors) {
            motor.setInverted(leftSide);
        }

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
        if (this.gear != gear) {
            this.gear = gear;
            gearShift.set(this.gear.value);
            setNEODistancePerRotation(this.gear.ratio);
            reset();
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

    /***************
     * ROBOT ANGLE *
     ***************/

    private boolean usingGyro() {
        return DrivetrainSettings.USING_GYRO;
    }

    // Gets current Angle of the Robot as a double (contiuous / not +-180)
    public double getRawGyroAngle() {
        return navx.getAngle();
    }

    // Gets current Angle of the Robot
    public Angle getGyroAngle() {
        return Angle.fromDegrees(getRawGyroAngle());
    }

    private void resetNavX() {
        navx.reset();
    }

    // Angle
    private double getEncoderRadians() {
        double distance = getLeftDistance() - getRightDistance();
        return distance / DrivetrainSettings.TRACK_WIDTH;
    }

    public Angle getEncoderAngle() {
        return Angle.fromRadians(getEncoderRadians());
    }

    public Angle getAngle() {
        return usingGyro() ? getGyroAngle() : getEncoderAngle();
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

    /*******************
     * STALL DETECTION *
     *******************/

    private double getLeftCurrentAmps() {
        double amps = 0.0;

        for (CANSparkMax motor : leftMotors) {
            amps += Math.abs(motor.getOutputCurrent());
        }

        return amps / leftMotors.length;
    }

    private double getRightCurrentAmps() {
        double amps = 0.0;

        for (CANSparkMax motor : rightMotors) {
            amps += Math.abs(motor.getOutputCurrent());
        }

        return amps / rightMotors.length;
    }

    private boolean isLeftStalling() {
        boolean gear = getGear() == Gear.HIGH;
        boolean current = getLeftCurrentAmps() > Stalling.CURRENT_THRESHOLD;
        boolean output = Math.abs(leftMotors[0].get()) > Stalling.DUTY_CYCLE_THRESHOLD;
        boolean velocity = Math.abs(getLeftVelocity()) < Stalling.VELOCITY_THESHOLD;
        return gear && (current || output) && velocity;
    }

    private boolean isRightStalling() {
        boolean gear = getGear() == Gear.HIGH;
        boolean current = getRightCurrentAmps() > Stalling.CURRENT_THRESHOLD;
        boolean output = Math.abs(rightMotors[0].get()) > Stalling.DUTY_CYCLE_THRESHOLD;
        boolean velocity = Math.abs(getRightVelocity()) < Stalling.VELOCITY_THESHOLD;
        return gear && (current || output) && velocity;
    }

    public boolean isStalling() {
        return isLeftStalling() || isRightStalling();
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

            SmartDashboard.putData("Debug/Drivetrain/Field", field);
            SmartDashboard.putString(
                    "Debug/Drivetrain/Current Gear",
                    getGear().equals(Gear.HIGH) ? "High Gear" : "Low Gear");
            SmartDashboard.putNumber("Debug/Drivetrain/Odometer X Position (m)", getPose().getX());
            SmartDashboard.putNumber("Debug/Drivetrain/Odometer Y Position (m)", getPose().getY());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Odometer Rotation (deg)",
                    getPose().getRotation().getDegrees());

            SmartDashboard.putNumber("Debug/Drivetrain/Motor Voltage Left (V)", getLeftVoltage());
            SmartDashboard.putNumber("Debug/Drivetrain/Motor Voltage Right (V)", getRightVoltage());

            SmartDashboard.putNumber(
                    "Debug/Drivetrain/NEO Distance Left (m)", leftNEO.getPosition());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/NEO Distance Right (m)", rightNEO.getPosition());

            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Grayhill Distance Left (m)", rightGrayhill.getDistance());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Grayhill Distance Right (m)", leftGrayhill.getDistance());

            SmartDashboard.putNumber("Debug/Drivetrain/Distance Traveled (m)", getDistance());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Distance Traveled Left (m)", getLeftDistance());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Distance Traveled Right (m)", getRightDistance());

            SmartDashboard.putNumber("Debug/Drivetrain/Velocity (m per s)", getVelocity());
            SmartDashboard.putNumber("Debug/Drivetrain/Velocity Left (m per s)", getLeftVelocity());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Velocity Right (m per s)", getRightVelocity());

            SmartDashboard.putNumber("Debug/Drivetrain/Current Left (amps)", getLeftCurrentAmps());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Current Right (amps)", getRightCurrentAmps());

            SmartDashboard.putNumber("Debug/Drivetrain/Angle NavX (deg)", getAngle().toDegrees());
            SmartDashboard.putNumber(
                    "Debug/Drivetrain/Encoder Angle (deg)", getEncoderAngle().toDegrees());
        }
    }
}
