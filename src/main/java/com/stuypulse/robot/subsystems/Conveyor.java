/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.stuypulse.robot.Constants.Ports;
import com.stuypulse.robot.subsystems.ColorSensor.CurrentBall;
import com.stuypulse.robot.Constants.ConveyorSettings;

/**
 * Tranports balls from the Intake to the Shooter.
 *
 * Contains:
 *     - spinTopBelt()
 *      - Spins the Top Conveyor Belt
 *     - stopTopBelt()
 *      - Stops the Top Conveyor Belt
 *     - reset()
 *      - Stops both the Top Conveyor Belt and the Ejection Motor
 *     - ejectBall()
 *      - Eject ball if the ball is not of our alliance color
 *     - acceptBall()
 *      - Moves ball up if the ball is alliance color
 *     - getCurrentBall()
 *      - Returns the color of the current ball
 *     - getMicroSwitch()
 *      - Returns true if the switch is pressed
 * @author Ivan Wei (ivanw8288@gmail.com)
 * @author Ivan Chen (ivanchen07@gmail.com)
 * @author Tony Chen (tchenpersonal50@gmail.com)
 * @author Gus Watkins
 * @author Kelvin Zhao (kzhao31@github)
 * @author Richie Xue (keobkeig/GlitchRich)
 * @author Rui Dong (ruidong0629@gmail.com)
 * @author Anthony Chen (achen318)
 * @author Joseph Mei (Gliese667Cc/SaggyTroy)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Carmin Vuong (carminvuong@gmail.com)
 * @author Jeff Chen (jeffc998866@gmail.com)
 * @author Sudipta Chakraborty (sudiptacc)
 * @author Niki Chen (nikichen6769@gmail.com)
 * @author Vincent Wang (vinowang921@gmail.com)
 */
 
public class Conveyor extends SubsystemBase {
    DigitalInput irSensor;
    ColorSensor colorSensor;
    
    CANSparkMax topMotor;
    CANSparkMax ejectMotor;    

    public Conveyor() {
        topMotor = new CANSparkMax(Ports.Conveyor.TOP_MOTOR, MotorType.kBrushless);
        ejectMotor = new CANSparkMax(Ports.Conveyor.EJECTOR_MOTOR, MotorType.kBrushless);
        colorSensor = new ColorSensor();
        irSensor = new DigitalInput(Ports.Conveyor.IR_SENSOR);
        // Initialize Motors, etc... here!
    }

    public void spinTopBelt() {
        topMotor.set(ConveyorSettings.TOP_MOTOR_SPEED);
    }
    
    public void ejectBall(){
        // If the ball is not of our alliance color, eject ball
        ejectMotor.set(-1 * ConveyorSettings.EJECT_SPEED);
    }
    
    public CurrentBall getCurrentBall() {
        return colorSensor.getCurrentBall();
    }
    
    public void acceptBall() {
        // Ball is alliance color, move ball up
        ejectMotor.set(ConveyorSettings.EJECT_SPEED);
    }
    
    public boolean getMicroSwitch() {
        return irSensor.get(); // returns true if the switch is pressed
    }
    
   public void stopTopBelt() {
        topMotor.set(0.0); // stops top motor
    }

    public void stopEject() {
        ejectMotor.set(0.0); // stops eject motor
    }

    public void reset() {
        stopTopBelt();
        stopEject();
    }

    @Override
    public void periodic() {  
    }
}

// comment for perspicuity
// David Mister Holmes