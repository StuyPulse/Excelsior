/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.ClimberSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
        MAX_TILT(Value.kForward, Value.kForward),
        NO_TILT(Value.kReverse, Value.kReverse),
        PARTIAL_TILT(Value.kForward, Value.kReverse);

        private final Value shorterExtended;
        private final Value longerExtended;

        private Tilt(Value shorterExtended, Value longerExtended) {
            this.shorterExtended = shorterExtended;
            this.longerExtended = longerExtended;
        }
    }

    // private final DoubleSolenoid longSolenoid;
    // private final DoubleSolenoid shortSolenoid;

    // private final DigitalInput bottomLimitSwitch;
    // private final DigitalInput topLimitSwitch;

    // private final Solenoid stopper;

    // private final CANSparkMax climber;

    public Climber() {
        // climber = new CANSparkMax(Ports.Climber.MOTOR, MotorType.kBrushless);
        // climber.setInverted(ClimberSettings.MOTOR_INVERTED);

        // stopper = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Climber.SOLENOID_STOPPER);

        // if (ClimberSettings.ENABLE_TILT) {
        //     longSolenoid =
        //             new DoubleSolenoid(
        //                     PneumaticsModuleType.CTREPCM,
        //                     Ports.Climber.SOLENOID_LONG_FORWARD,
        //                     Ports.Climber.SOLENOID_LONG_REVERSE);
        //     shortSolenoid =
        //             new DoubleSolenoid(
        //                     PneumaticsModuleType.CTREPCM,
        //                     Ports.Climber.SOLENOID_LONG_FORWARD,
        //                     Ports.Climber.SOLENOID_LONG_REVERSE);
        // } else {
        //     longSolenoid = null;
        //     shortSolenoid = null;
        // }

        // bottomLimitSwitch = new DigitalInput(Ports.Climber.BOTTOM_LIMIT_SWITCH);
        // topLimitSwitch = new DigitalInput(Ports.Climber.TOP_LIMIT_SWITCH);
    }

    public void setMotor(double speed) {
        // if (stopper.get()) {
        //     DriverStation.reportWarning("Climber attempted to run while lock was enabled!", true);
        //     setMotorStop();
        // } else {
        //     climber.set(speed);
        // }
    }

    public void setMotorStop() {
        // climber.stopMotor();
    }

    public boolean getTopReached() {
        return true; // topLimitSwitch.get();
    }

    public boolean getBottomReached() {
        return true; // bottomLimitSwitch.get();
    }

    public void setTilt(Tilt tilt) {
        // if (ClimberSettings.ENABLE_TILT) {
        //     shortSolenoid.set(tilt.shorterExtended);
        //     longSolenoid.set(tilt.longerExtended);
        // } else {
        //     DriverStation.reportWarning(
        //             "Climber attempted to tilt while solenoids are disabled!", true);
        // }
    }

    public void setClimberLocked() {
        // setMotorStop();
        // stopper.set(true);
    }

    public void setClimberUnlocked() {
        // stopper.set(false);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // if (Constants.DEBUG_MODE.get()) {
        //     if (ClimberSettings.ENABLE_TILT) {
        //         SmartDashboard.putString(
        //                 "Debug/Climber/Long Extended", longSolenoid.get().toString());
        //         SmartDashboard.putString(
        //                 "Debug/Climber/Short Extended", shortSolenoid.get().toString());
        //     }
        //     SmartDashboard.putBoolean("Debug/Climber/Stopper Active", stopper.get());
        //     SmartDashboard.putNumber("Debug/Climber/Climber Speed", climber.get());
        // }
    }
}
