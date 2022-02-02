/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.Ports;
import com.stuypulse.robot.Constants.ShooterSettings;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
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
    
    private final SmartNumber targetRPM;
    private final static CANSparkMax.ControlType MODE = CANSparkMax.ControlType.kVelocity;

    // Motors
    private final CANSparkMax shooterMotor;
    private final CANSparkMax shooterFollower;
    private final CANSparkMax feederMotor;

    // Encoders
    private final RelativeEncoder shooterEncoder;
    private final RelativeEncoder feederEncoder;

    // PID
    private final SparkMaxPIDController shooterPIDController;
    private final SparkMaxPIDController feederPIDController;

    // Hood Solenoid
    private final Solenoid hoodSolenoid;

    public Shooter() {
        targetRPM = new SmartNumber("Shooter/Target", 0.0);

        shooterMotor = new CANSparkMax(Ports.Shooter.SHOOTER, MotorType.kBrushless);
        shooterFollower = new CANSparkMax(Ports.Shooter.SHOOTER_FOLLOWER, MotorType.kBrushless);
        feederMotor = new CANSparkMax(Ports.Shooter.FEEDER, MotorType.kBrushless);

        shooterFollower.follow(shooterMotor, true);

        shooterEncoder = shooterMotor.getEncoder();
        feederEncoder = feederMotor.getEncoder();

        shooterPIDController = shooterMotor.getPIDController();
        feederPIDController = feederMotor.getPIDController();

        shooterPIDController.setP(ShooterSettings.ShooterPID.kP);
        shooterPIDController.setI(ShooterSettings.ShooterPID.kI);
        shooterPIDController.setD(ShooterSettings.ShooterPID.kD);
        shooterPIDController.setFF(ShooterSettings.ShooterPID.kF);

        feederPIDController.setP(ShooterSettings.FeederPID.kP);
        feederPIDController.setI(ShooterSettings.FeederPID.kI);
        feederPIDController.setD(ShooterSettings.FeederPID.kD);
        feederPIDController.setFF(ShooterSettings.FeederPID.kF);

        shooterMotor.burnFlash();
        feederMotor.burnFlash();

        hoodSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Shooter.HOOD_SOLENOID);
    }

    public void setShooterRPM(SmartNumber speed) {
        targetRPM.set(speed);
    }

    public void setShooterRPM(double speed) {
        targetRPM.set(speed);
    }

    public double getShooterRPM() {
        return Math.abs(shooterEncoder.getVelocity());    
    }

    public double getFeederRPM() {
        return Math.abs(feederEncoder.getVelocity());
    }

    public void extendHoodSolenoid() {
        hoodSolenoid.set(true);
    }

    public void retractHoodSolenoid() {
        hoodSolenoid.set(false);
    }

    public void setDefaultSolenoidPosition() {
        retractHoodSolenoid();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        double feederMultipler = ShooterSettings.FEEDER_MULTIPLER.get();
        shooterPIDController.setReference(targetRPM.get(), MODE);
        feederPIDController.setReference(targetRPM.get() * feederMultipler, MODE);

        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Shooter/Shooter RPM", getShooterRPM());
            SmartDashboard.putNumber("Shooter/Feeder RPM", getFeederRPM());
        }
    }
}
