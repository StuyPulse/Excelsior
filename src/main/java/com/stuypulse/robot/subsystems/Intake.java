/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.IntakeSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/*-
 * Intake balls into the robot
 *
 * Contains:
 *      - Double Solenoid to lift and lower intake
 *      - Motor to control rollers that intake the balls
 *
 * @author Elaine Ye
 * @author Ben Goldfisher
 * @author Allison Choi
 * @author Brandon Chao
 * @author Crystal Yang
 * @author Mandy Wang
 * @author Vincent Zheng
 * @author Naf Murtaza
 * @author Orion Xiang
 * @author Ethan Liu
 * @author Raymond Lian
 * @author Reya Miller
 */
public class Intake extends SubsystemBase {

    private final CANSparkMax motor;
    private final DoubleSolenoid solenoid;

    public Intake() {
        motor = new CANSparkMax(Ports.Intake.MOTOR, MotorType.kBrushless);
        solenoid =
                new DoubleSolenoid(
                        PneumaticsModuleType.CTREPCM,
                        Ports.Intake.SOLENOID_FORWARD,
                        Ports.Intake.SOLENOID_REVERSE);
    }

    /*** Extend / Retract ***/
    public void extend() {
        solenoid.set(Value.kReverse);
    }

    public void retract() {
        solenoid.set(Value.kForward);
    }

    /*** Acquire / Deaqcuire ***/
    public void setMotor(double speed) {
        motor.set(speed);
    }

    public void stop() {
        motor.stopMotor();
    }

    public void acquire() {
        setMotor(IntakeSettings.MOTOR_SPEED.get());
    }

    public void deacquire() {
        setMotor(-IntakeSettings.MOTOR_SPEED.get());
    }

    @Override
    public void periodic() {
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Debug/Intake/Motor Speed", motor.get());
            SmartDashboard.putString("Debug/Intake/Solenoid", solenoid.get().name());
            SmartDashboard.putBoolean("Debug/Intake/Extended", solenoid.get() == Value.kReverse);
        }
    }
}
