package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.subsystems.drivetrain.Drivetrain.Gear;
import com.stuypulse.stuylib.math.Angle;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IDrivetrain extends SubsystemBase{
    
    public static enum Gear {
        HIGH(Value.kReverse),
        LOW(Value.kForward);

        private final Value value;

        private Gear(Value value) {
            this.value = value;
        }
    }

    // Gets the current gear the robot is in
    public abstract Gear getGear();
    // Sets the current gear the robot is in
    public abstract void setGear(Gear gear) ;

    
    // Sets robot into low gear
    public abstract void setLowGear();

    // Sets robot into high gear
    public abstract void setHighGear();

    /*********************
     * ENCODER FUNCTIONS *
     *********************/

    // Distance
    public abstract double getLeftDistance();

    public abstract double getRightDistance();

    public abstract double getDistance();

    // Velocity
    public abstract double getLeftVelocity();

    public abstract double getRightVelocity();

    public abstract double getVelocity();

    /***************
     * ROBOT ANGLE *
     ***************/

    // Gets current Angle of the Robot as a double (contiuous / not +-180)
    public abstract double getRawGyroAngle();
    
    // Gets current Angle of the Robot
    public abstract Angle getGyroAngle();

    // Gets current Angle of the Robot as a double [using encoders] (contiuous / not +-180)
    public abstract double getRawEncoderAngle();

    // Gets current Angle of the Robot [using encoders]
    public abstract Angle getEncoderAngle();

    public abstract Angle getAngle();

    public abstract Angle getRoll();

    /**********************
     * ODOMETRY FUNCTIONS *
     **********************/

    public abstract void updateOdometry();

    public abstract DifferentialDriveWheelSpeeds getWheelSpeeds();

    public abstract Rotation2d getRotation2d();

    public abstract Pose2d getPose();

    public abstract Field2d getField();

    /************************
     * OVERALL SENSOR RESET *
     ************************/

    public abstract void reset(Pose2d location);

    public abstract void reset();

    /*********************
     * VOLTAGE FUNCTIONS *
     *********************/

    public abstract double getBatteryVoltage();

    public abstract double getLeftVoltage();

    public abstract double getRightVoltage();

    public abstract void tankDriveVolts(double leftVolts, double rightVolts);

    /*******************
     * STALL DETECTION *
     *******************/

    public abstract double getLeftCurrentAmps();

    public abstract double getRightCurrentAmps();

    public abstract double getCurrentAmps();

    public abstract boolean isLeftStalling();

    public abstract boolean isRightStalling();

    public abstract boolean isStalling();

    /********************
     * DRIVING COMMANDS *
     ********************/

    // Stops drivetrain from moving
    public abstract void stop();

    // Drives using tank drive
    public abstract void tankDrive(double left, double right);

    // Drives using arcade drive
    public abstract void arcadeDrive(double speed, double rotation);

    // Drives using curvature drive algorithm
    public abstract void curvatureDrive(double xSpeed, double zRotation, double baseTS);

    // Drives using curvature drive algorithm with automatic quick turn
    public abstract void curvatureDrive(double xSpeed, double zRotation);

}
