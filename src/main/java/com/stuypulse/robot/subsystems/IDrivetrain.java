package com.stuypulse.robot.subsystems;

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

        public final Value value;

        private Gear(Value value) {
            this.value = value;
        }
    }

    // Gets the current gear the robot is in
    public abstract Gear getGear();
    // Sets the current gear the robot is in
    public abstract void setGear(Gear gear) ;

    public abstract boolean isStalling();
    
    /*********************
     * ENCODER FUNCTIONS *
     *********************/


    public abstract double getDistance();

    public abstract double getVelocity();

    /***************
     * ROBOT ANGLE *
     ***************/

     
    public abstract double getRawGyroAngle();

    public abstract Angle getAngle();

    /**********************
     * ODOMETRY FUNCTIONS *
     **********************/



    public abstract DifferentialDriveWheelSpeeds getWheelSpeeds();

    public abstract Pose2d getPose();

    /************************
     * OVERALL SENSOR RESET *
     ************************/

    public abstract void reset(Pose2d location);

    public abstract void reset();

    /*********************
     * VOLTAGE FUNCTIONS *
     *********************/


    public abstract void stop();

    // Drives using tank drive
    public abstract void tankDrive(double leftVelocity, double rightVelocity);

    // Drives using arcade drive
    public abstract void arcadeDrive(double forwardVelocity, double omegaRotation);

    // Drives using curvature drive algorithm
    // public abstract void curvatureDrive(double velocity, double curvature, double baseTS);

    // Drives using curvature drive algorithm with automatic quick turn
    // public abstract void curvatureDrive(double xSpeed, double zRotation);

}
