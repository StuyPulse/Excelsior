/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import java.io.Console;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.Constants;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * Climbs at end of match
 *
 * Contains:
 *      - Code for Climber
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

    private CANSparkMax climber;

    private boolean longExtended;
    private boolean shortExtended;

    public Climber() {
        //Ports not config
        solenoidLong = new Solenoid(Constants.Ports.Climber.SOLENOID_LONG);
        solenoidShort = new Solenoid(Constants.Ports.Climber.SOLENOID_SHORT);
        climber = new CANSparkMax(Constants.Ports.Climber.MOTOR, MotorType.kBrushless);

        climber.setInverted(Constants.ClimberSettings.MOTOR_REVERTED);

    }

    public void liftUp() {
        moveMotor(Constants.ClimberSettings.CLIMBER_DEFAULT_SPEED);
    }

    public void liftDown() {
        moveMotor(-Constants.ClimberSettings.CLIMBER_DEFAULT_SPEED);
    }

    public void stop() {
        moveMotor(0.0);
    }
    
    public void liftUpSlow() {
        moveMotor(Constants.ClimberSettings.CLIMBER_SLOW_SPEED);
    }

    public void liftDownSlow() {
        moveMotor(-Constants.ClimberSettings.CLIMBER_SLOW_SPEED);
    }

    public double getSpeed() {
        return climber.get();
    }

    public void extendLong() {
        solenoidLong.set(true);
        longExtended = true;
    }
    
    public void retractLong() {
        solenoidLong.set(false);
        longExtended = false;
    }
    
    public void extendShort() {
        solenoidShort.set(true);
        shortExtended = true;
    }

    public void retractShort() {
        solenoidShort.set(false);
        shortExtended = false;
    }

    private void moveMotor(double speed) {
        climber.set(speed);
    }

    public void fullyRetract() {
        retractShort();
        retractLong();

    }

    public void fullyExtend() {
        extendLong();
        extendShort();
    }

    public boolean longExtended() {
        return longExtended;
    }

    public boolean shortExtended() {
        return shortExtended;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putBoolean("Long/Solenoid Extended", longExtended());
            SmartDashboard.putBoolean("Short/Solenoid Extended", shortExtended());
        }
       
    }
}
