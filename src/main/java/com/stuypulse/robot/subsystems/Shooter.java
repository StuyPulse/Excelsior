/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.Constants.Ports.Shooter.PID;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * Shoots balls out of the robot
 *
 * Contains:
 *      - Two shooter motors
 *      - One feeder motor
 *
 *      @author Vincent Lin
 *      @author Aharanish Dev
 *      @author Kevin Lio
 *      @author John Jay Wang
 *      @author Adeeb Khan
 *      @author Vicente Xia
 *      @author Julia Xue
 *      @author William Vongphanith
 *      @author Yuchen Pan
 *      @author Shaurya Sen
 *      @author Nicky Lin
 */
public class Shooter extends SubsystemBase {

    // motors
    private final CANSparkMax shooterMotor;
    private final CANSparkMax shooterFollower;
    private final CANSparkMax feederMotor;

    // encoders
    private final RelativeEncoder encoderA;
    private final RelativeEncoder encoderB;
    private final RelativeEncoder feederEncoder;

    // PID
    private final SparkMaxPIDController shooterPIDController;
    private final SparkMaxPIDController feederPIDController;

    public Shooter() {
        // initializing motors
        //// change ports /////
        shooterMotor = new CANSparkMax(-1, MotorType.kBrushless);
        shooterFollower = new CANSparkMax(-1, MotorType.kBrushless);
        feederMotor = new CANSparkMax(-1, MotorType.kBrushless);

        shooterFollower.follow(shooterMotor, true);

        // initializing encoders
        encoderA = shooterMotor.getEncoder();
        encoderB = shooterFollower.getEncoder();
        feederEncoder = feederMotor.getEncoder();

        // initializing PIDControllers
        shooterPIDController = shooterMotor.getPIDController();
        feederPIDController = shooterMotor.getPIDController();

        // set PID coefficients
        shooterPIDController.setP(PID.SHOOTER_P);
        shooterPIDController.setI(PID.SHOOTER_I);
        shooterPIDController.setD(PID.SHOOTER_D);
        shooterPIDController.setFF(PID.SHOOTER_FF);

        feederPIDController.setP(PID.FEEDER_P);
        feederPIDController.setI(PID.FEEDER_I);
        feederPIDController.setD(PID.FEEDER_D);
        feederPIDController.setFF(PID.FEEDER_FF);
    }

    public void setShooterRPM(double speed) {
        shooterMotor.set(speed);
    }

    public double getShooterRPM() {
        // units are RPM
        return (Math.abs(encoderA.getVelocity())
        + Math.abs(encoderB.getVelocity())) / 2;
    }

    public double getFeederRPM() {
        // units are RPM
        return Math.abs(feederEncoder.getVelocity());
    }
    


    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // for the ring shot
        shooterPIDController.setReference(PID.RING_SETPOINT, CANSparkMax.ControlType.kVelocity);
    }
}
