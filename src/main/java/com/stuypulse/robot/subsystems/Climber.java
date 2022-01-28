/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import java.io.Console;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.Constants;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * Climbs at end of match
 *
 * Contains:
 *      - Change tilt of climber
 *      - Move climber via motor
 *      - Different tilt angles
 *      - Encoder + Solenoid used for stopping
 *
 * @author independence106(Jason Zhou)
 * @author Ca7Ac1(Ayan Chowdhury)
 * @author marcjiang7(Marc Jiang)
 * @author ambers7(Amber Shen)
 * @author Souloutz(Howard Kong)
 * @author jiayuyan0501(Jiayu Yan)
 * @author ijiang05(Ian Jiang)
 * @author TraceyLin (Tracey Lin)
 * @author annazheng14(Anna Zheng)
 * @author lonelydot(Raymond Zhang)
 * @author andylin2004 (Andy Lin)
 */
public class Climber extends SubsystemBase {

    private Solenoid solenoidLong;
    private Solenoid solenoidShort;

    private Solenoid stopper;

    private RelativeEncoder encoder;

    private CANSparkMax climber;

    public Climber() {
        climber = new CANSparkMax(Constants.Ports.Climber.MOTOR);

        solenoidLong = new Solenoid(Constants.Ports.Climber.SOLENOID_LONG);
        solenoidShort = new Solenoid(Constants.Ports.Climber.SOLENOID_SHORT);
        stopper = new Solenoid(Constants.Ports.Climber.STOPPER);

        encoder = climber.getEncoder();
        encoder.setPositionConversionFactor(Constants.ClimberSettings.CIRCUMFERENCE * Constants.ClimberSettings.GEAR_RATIO);

        climber.setInverted(Constants.ClimberSettings.MOTOR_INVERTED);

    }
    
    private void moveMotor(double speed) {
        climber.set(speed);
    }

    public void liftUp() {
        moveMotor(Constants.ClimberSettings.CLIMBER_DEFAULT_SPEED.get());
    }

    public void liftDown() {
        moveMotor(-Constants.ClimberSettings.CLIMBER_DEFAULT_SPEED.get());
    }

    public void stop() {
        moveMotor(0.0);
    }
    
    public void liftUpSlow() {
        moveMotor(Constants.ClimberSettings.CLIMBER_SLOW_SPEED.get());
    }

    public void liftDownSlow() {
        moveMotor(-Constants.ClimberSettings.CLIMBER_SLOW_SPEED.get());
    }

    public void extendLong() {
        solenoidLong.set(true);
    }
    
    public void retractLong() {
        solenoidLong.set(false);
    }
    
    public void extendShort() {
        solenoidShort.set(true);
    }

    private void retractShort() {
        solenoidShort.set(false);
    }

    public void fullyRetract() {
        retractShort();
        retractLong();

    }

    public void fullyExtend() {
        extendLong();
        extendShort();
    }

    public boolean getLongExtended() {
        return solenoidLong.get();
    }

    public boolean getShortExtended() {
        return solenoidShort.get();
    }

    public double getEncoderDistance() {
        return encoder.getPosition();
    }

    public void reset() {
        encoder.setPosition(0.0);
    }

    public void lockClimber() {
        stopper.set(true);
    }

    public void unlockClimber() {
        stopper.set(false);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putBoolean("Climber/LongSolenoid Extended", solenoidLong.get());
            SmartDashboard.putBoolean("Climber/ShortSolenoid Extended", solenoidLong.get());
            SmartDashboard.putNumber("Climber/DistanceEncoder Traveled", getEncoderDistance());
        } 
    }
}
