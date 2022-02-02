/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import java.io.Console;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.Constants;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
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
 * @author TraceyLin(Tracey Lin)
 * @author annazheng14(Anna Zheng)
 * @author lonelydot(Raymond Zhang)
 * @author andylin2004(Andy Lin)
 * @author hwang30git(Hui Wang)
 */
public class Climber extends SubsystemBase {

    public enum Tilt {
        // first param is shorter, second is longer 
        MAX_TILT(true, true),
        NO_TILT(false, false),
        PARTIAL_TILT(true, false);

        private final boolean shorterExtended;
        private final boolean longerExtended;

        private Tilt(boolean shorterExtended, boolean longerExtended) {
            this.shorterExtended = shorterExtended;
            this.longerExtended = longerExtended;
        }  
    }

    private Solenoid long;
    private Solenoid short;

    private DigitalInput bottomLimitSwitch;
    private DigitalInput topLimitSwitch;

    private Solenoid stopper;

    private CANSparkMax climber;

    public Climber() {
        climber = new CANSparkMax(Constants.Ports.Climber.MOTOR, MotorType.kBrushless);
        climber.setInverted(Constants.ClimberSettings.MOTOR_INVERTED);

        long = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Ports.Climber.SOLENOID_LONG);
        short = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Ports.Climber.SOLENOID_SHORT);
        stopper = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Ports.Climber.SOLENOID_STOPPER);

        bottomLimitSwitch = new DigitalInput(Constants.Ports.Climber.BOTTOM_LIMIT_SWITCH);
        topLimitSwitch = new DigitalInput(Constants.Ports.Climber.TOP_LIMIT_SWITCH);
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

    public boolean getLongExtended() {
        return long.get();
    }

    public boolean getShortExtended() {
        return short.get();
    }

    public boolean topReached() {
        return topLimitSwitch.get(); 
    }

    public boolean bottomReached() {
        return bottomLimitSwitch.get();
    }

    public void setTilt(Tilt tilt) {
        short.set(tilt.shorterExtended);
        long.set(tilt.longerExtended);
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
            SmartDashboard.putBoolean("Climber/LongSolenoid Extended", long.get());
            SmartDashboard.putBoolean("Climber/ShortSolenoid Extended", long.get());
            SmartDashboard.putBoolean("Climber/StopperSolenoid Active", stopper.get());
        } 
    }
}
