/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.stuypulse.robot.subsystems.ColorSensor.CurrentBall;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.Ports;
import com.stuypulse.robot.Constants.ConveyorSettings;

/*-
 * The Conveyor subsystem is meant to transport team alliance balls from the intake to the shooter, while rejecting balls that are
 * of the opposing alliance's color.
 * 
 * Contains:
 * - A gandalf motor above a gap that will spin a wheel either out the back of the robot or upwards to the upper conveyor
 * - An upper conveyor (with a motor) that can hold a ball or transport balls to the shooter
 * - A color sensor that detects the color of the ball held in the gap near the gandalf motor
 * - An IR Sensor that detects the presence of a ball in the upper conveyor
 *`
 * @author Ivan Wei (ivanw8288@gmail.com)
 * @author Ivan Chen (ivanchen07@gmail.com)
 * @author Tony Chen (tchenpersonal50@gmail.com)
 * @author Gus Watkins (gus@guswatkins.net)
 * @author Kelvin Zhao (kzhao31@github)
 * @author Richie Xue (keobkeig/GlitchRich)
 * @author Rui Dong (ruidong0629@gmail.com)
 * @author Anthony Chen (achen318)
 * @author Joseph Mei (Gliese667Cc/SaggyTroy)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Carmin Vuong (carminvuong@gmail.com)
 * @author Jeff Chen (jeffc998866@gmail.com)
 * @author Sudipta Chakraborty (sudiptacc)
 * @author Andrew Che (andrewtheemerald@gmail.com)
 * @author Niki Chen (nikichen6769@gmail.com)
 * @author Vincent Wang (vinowang921@gmail.com)
 */
 
public class Conveyor extends SubsystemBase {
    private final CANSparkMax topMotor;
    private final CANSparkMax gandalfMotor;    

    private final ColorSensor colorSensor;
    private final DigitalInput gandalfIRSensor;
    private final DigitalInput topIRSensor;

    /**
     * Creates a Conveyor subsystem
     */
    public Conveyor() {
        topMotor = new CANSparkMax(Ports.Conveyor.TOP_CONVEYOR_MOTOR, MotorType.kBrushless);
        gandalfMotor = new CANSparkMax(Ports.Conveyor.GANDALF_MOTOR, MotorType.kBrushless);
        
        colorSensor = new ColorSensor();
        gandalfIRSensor = new DigitalInput(Ports.Conveyor.GANDALF_IR_SENSOR);
        topIRSensor = new DigitalInput(Ports.Conveyor.TOP_CONVEYOR_IR_SENSOR);
    }

    /**
     * Spins the Top Conveyor Belt, moving the ball up to the shooter
     */
    public void spinTopBelt() {
        topMotor.set(ConveyorSettings.TOP_MOTOR_SPEED.get());
    }
    
    /**
     * Accept ball - spin the gandalf motor upwards to the top conveyor
     * To be used when the ball is team alliance color
     */
    public void acceptBall() {
        gandalfMotor.set(ConveyorSettings.ACCEPT_SPEED.get());
    }
    
    /**
     * Eject ball - spin the gandalf motor outwards
     * To be used when the ball is opposing alliance color
     */
    public void rejectBall(){
        // If the ball is not of our alliance color, reject ball
        gandalfMotor.set(ConveyorSettings.REJECT_SPEED.get());
    }
    
    /**
     * Stops the Top Conveyor Belt
     */
    public void stopTopBelt() {
        topMotor.stopMotor();
    }
    
    // stub for perspicuity
    /**
     * Stops the Gandalf Motor
     */
    public void stopGandalf() {
        gandalfMotor.stopMotor();
    }

    /**
     * Stop both the Top Conveyor Belt and the Gandalf Motor
     */
    public void stop() {
        stopTopBelt();
        stopGandalf();
    }

    /**
     * Gets the CurrentBall object from the color sensor
     * @return CurrentBall detected by the color sensor
     */
    private CurrentBall getCurrentBall() {
        return colorSensor.getCurrentBall();

    }
    
    /**
     * Finds if the upper IR Sensor has been tripped e.g., there is a ball in the top conveyor
     * @return if the upper IR Sensor has been tripped
     */
    public boolean getTopConveyorHasBall() {
        return topIRSensor.get();
    }

    public boolean getGandalfHasBall() {
        return gandalfIRSensor.get();
    }
    
    @Override
    public void periodic() {
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Conveyor/Top Motor Speed", topMotor.get());
            SmartDashboard.putNumber("Conveyor/Gandalf Motor Speed", gandalfMotor.get());
            SmartDashboard.putBoolean("Conveyor/Top Conveyor Has Ball", getTopConveyorHasBall());
            SmartDashboard.putBoolean("Conveyor/Gandalf Has Ball", getGandalfHasBall());
        }  
    }
}
