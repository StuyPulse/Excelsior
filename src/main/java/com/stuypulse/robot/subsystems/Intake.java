/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.IntakeSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
    private DigitalInput sensor;

    public Intake() {
        motor = new CANSparkMax(Ports.Intake.MOTOR, MotorType.kBrushless);
        solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Ports.Intake.SOLENOID_A, Ports.Intake.SOLENOID_B);
        sensor = new DigitalInput(Ports.Intake.SENSOR);

        addChild("Double Solenoid", solenoid);
        addChild("Sensor", sensor);
    }

    /*** Extend / Retract ***/
    public void extend() {
        solenoid.set(Value.kReverse);
    }

    public void retract() {
        solenoid.set(Value.kForward);
    }

    /*** Acquire / Deaqcuire ***/
    public void setMotor(final double speed) {
        motor.set(speed);
    }

    public void stop() {
        motor.stopMotor();
    }

    public void acquire() {
        setMotor(IntakeSettings.MOTOR_SPEED);
    }

    public void deacquire() {
        setMotor(-IntakeSettings.MOTOR_SPEED);
    }

    public boolean isBallDetected() {
        return !sensor.get();
    }

    @Override
    public void periodic() {
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putBoolean("Intake/Ball Detected", isBallDetected());
        }
    }
}
