/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;

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

    public static double speed;

    private final CANSparkMax motor;
    private final DoubleSolenoid solenoid;

    private final ColorSensor colorSensor;

    public Intake(ColorSensor colorSensor) {
        this.motor = new CANSparkMax(Ports.Intake.MOTOR, MotorType.kBrushless);
        Motors.INTAKE.configure(motor);

        this.solenoid =
                new DoubleSolenoid(
                        PneumaticsModuleType.CTREPCM,
                        Ports.Intake.SOLENOID_FORWARD,
                        Ports.Intake.SOLENOID_REVERSE);

        this.colorSensor = colorSensor;

        speed = 0.0;
    }

    /*** Extend / Retract ***/
    public void extend() {
        solenoid.set(Value.kForward);
    }

    public void retract() {
        solenoid.set(Value.kReverse);
    }

    /*** Acquire / Deaqcuire ***/
    public void setMotor(double newSpeed) {
        speed = newSpeed;
    }

    public void stop() {
        speed = 0.0;
    }

    public void acquire() {
        speed = +Settings.Intake.MOTOR_SPEED.get();
    }

    public void deacquire() {
        speed = -Settings.Intake.MOTOR_SPEED.get();
    }

    /*** Color Sensor Information ***/
    private boolean getShouldStop() {
        return colorSensor.isConnected() && colorSensor.hasAllianceBall();
    }

    /*** Debug Information ***/
    @Override
    public void periodic() {
        if (0.0 <= speed && getShouldStop()) {
            motor.set(speed * Settings.Intake.LOCKED_SPEED.get());
        } else {
            motor.set(speed);
        }

        if (Settings.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Debug/Intake/Motor Speed", motor.get());
            SmartDashboard.putString("Debug/Intake/Solenoid", solenoid.get().name());
            SmartDashboard.putBoolean("Debug/Intake/Extended", solenoid.get() == Value.kReverse);
        }
    }
}
