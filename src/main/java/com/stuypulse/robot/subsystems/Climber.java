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
 *      - TBD
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
 */
public class Climber extends SubsystemBase {

    private Solenoid solenoid;
    private CANSparkMax motor;

    // double solenoid

    public Climber() {
        //Ports not config
        solenoid = new Solenoid(Constants.Ports.Climber.SOLENOID);
        motor = new CANSparkMax(Constants.Ports.Climber.MOTOR, MotorType.kBrushless);

        motor.setInverted(true);

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
        return motor.get();
    }

    private void moveMotor(double speed) {
        motor.set(speed);
    }





    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Speed Of Climber Motor", this.getSpeed());
        }
    }
}
