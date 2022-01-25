/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.Constants.Ports.Shooter.FIELD;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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


    private double SHOOTER_P = 0.0;
    private double SHOOTER_I = 0.0;
    private double SHOOTER_D = 0.0;
    private double SHOOTER_FF = 0.0;

    private double FEEDER_P = 0.0;
    private double FEEDER_I = 0.0;
    private double FEEDER_D = 0.0;
    private double FEEDER_FF = 0.0;

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

    // PID values taken from smart dashboard   
    double shooter_P, shooter_I, shooter_D, shooter_F, feeder_P, feeder_I, feeder_D, feeder_F;

    public Shooter () {
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
        shooterPIDController.setP(SHOOTER_P);
        shooterPIDController.setI(SHOOTER_I);
        shooterPIDController.setD(SHOOTER_D);
        shooterPIDController.setFF(SHOOTER_FF);

        feederPIDController.setP(FEEDER_P);
        feederPIDController.setI(FEEDER_I);
        feederPIDController.setD(FEEDER_D);
        feederPIDController.setFF(FEEDER_FF);

        // put the PID values for shooter and feder onto the Smartdashboard
        SmartDashboard.putNumber("Shooter P value" , SHOOTER_P);
        SmartDashboard.putNumber("Shooter I value" , SHOOTER_I);
        SmartDashboard.putNumber("Shooter D value" , SHOOTER_D);
        SmartDashboard.putNumber("Shooter F value" , SHOOTER_FF);

        SmartDashboard.putNumber("Feeder P value" , FEEDER_P);
        SmartDashboard.putNumber("Feeder I value" , FEEDER_I);
        SmartDashboard.putNumber("Feeder D value" , FEEDER_D);
        SmartDashboard.putNumber("Feeder F value" , FEEDER_FF);


    }

    public void setShooterRPM(double speed) {
        shooterMotor.set(speed);
        feederMotor.set(speed);
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
        

        // get the current value for p, i, and d from smart dashboard
        shooter_P = SmartDashboard.getNumber("Shooter P value" , SHOOTER_P);
        shooter_I = SmartDashboard.getNumber("Shooter I value" , SHOOTER_I);
        shooter_D = SmartDashboard.getNumber("Shooter D value" , SHOOTER_D);
        shooter_F = SmartDashboard.getNumber("Shooter F value" , SHOOTER_FF);
        
        feeder_P = SmartDashboard.getNumber("Feeder P value" , FEEDER_P);
        feeder_I = SmartDashboard.getNumber("Feeder I value" , FEEDER_I);
        feeder_D = SmartDashboard.getNumber("Feeder D value" , FEEDER_D);
        feeder_F = SmartDashboard.getNumber("Feeder F value" , FEEDER_FF);

        // If the value for PID changed on smart dashboard, change it here too
        if((shooter_P != SHOOTER_P)) { shooterPIDController.setP(shooter_P); SHOOTER_P = shooter_P; }
        if((shooter_I != SHOOTER_I)) { shooterPIDController.setP(shooter_I); SHOOTER_I = shooter_I; }
        if((shooter_D != SHOOTER_D)) { shooterPIDController.setP(shooter_D); SHOOTER_D = shooter_D; }
        if((shooter_D != SHOOTER_D)) { shooterPIDController.setP(shooter_D); SHOOTER_FF = shooter_F; }

        if((shooter_P != FEEDER_P)) { shooterPIDController.setP(shooter_P); FEEDER_P = shooter_P; }
        if((shooter_I != FEEDER_I)) { shooterPIDController.setP(shooter_I); FEEDER_I = shooter_I; }
        if((shooter_D != FEEDER_D)) { shooterPIDController.setP(shooter_D); FEEDER_D = shooter_D; }
        if((shooter_D != FEEDER_D)) { shooterPIDController.setP(shooter_D); FEEDER_FF = shooter_F; }


        shooterPIDController.setReference(FIELD.RING_SETPOINT, CANSparkMax.ControlType.kVelocity);
    }
}
