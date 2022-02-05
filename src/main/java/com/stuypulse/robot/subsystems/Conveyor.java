/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.ConveyorSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * The Conveyor subsystem is meant to transport team alliance balls from the intake to the shooter, while rejecting balls that are
 * of the opposing alliance's color.
 *
 * Contains:
 * - A gandalf motor above a gap that will spin a wheel either out the back of the robot or upwards to the upper conveyor
 * - An upper conveyor (with a motor) that can hold a ball or transport balls to the shooter
 * - A color sensor that detects the color of the ball held in the gap near the gandalf motor
 * - An IR Sensor that detects the presence of a ball in the upper conveyor
 *
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
 * @author Edmund Chin (edmundc421@gmail.com)
 */

public class Conveyor extends SubsystemBase {
    private final CANSparkMax topBeltMotor;
    private final CANSparkMax gandalfMotor;

    private final ColorSensor colorSensor;
    private final DigitalInput gandalfIRSensor;
    private final DigitalInput topIRSensor;

    private boolean shooting;

    /** Creates a Conveyor subsystem */
    public Conveyor() {
        topBeltMotor = new CANSparkMax(Ports.Conveyor.TOP_BELT_MOTOR, MotorType.kBrushless);
        gandalfMotor = new CANSparkMax(Ports.Conveyor.GANDALF_MOTOR, MotorType.kBrushless);

        colorSensor = new ColorSensor();
        gandalfIRSensor = new DigitalInput(Ports.Conveyor.GANDALF_IR_SENSOR);
        topIRSensor = new DigitalInput(Ports.Conveyor.TOP_BELT_IR_SENSOR);

        shooting = false;
    }

    /** Spins the Top Conveyor Belt, moving the ball up to the shooter */
    public void spinTopBelt() {
        topBeltMotor.set(ConveyorSettings.TOP_BELT_SPEED.get());
    }

    /**
     * Accept ball - spin the gandalf motor upwards to the top conveyor To be used when the ball is
     * team alliance color
     */
    public void acceptBall() {
        gandalfMotor.set(ConveyorSettings.ACCEPT_SPEED.get());
    }

    /**
     * Eject ball - spin the gandalf motor outwards To be used when the ball is opposing alliance
     * color
     */
    public void rejectBall() {
        // If the ball is not of our alliance color, reject ball
        gandalfMotor.set(ConveyorSettings.REJECT_SPEED.get());
    }

    /** Stops the Top Conveyor Belt */
    public void stopTopBelt() {
        topBeltMotor.stopMotor();
    }

    /** Stops the Gandalf Motor */
    public void stopGandalf() {
        gandalfMotor.stopMotor();
    }

    /** Finds if the upper IR Sensor has been tripped e.g., there is a ball in the top conveyor */
    public boolean getTopBeltHasBall() {
        return topIRSensor.get();
    }

    /** Finds if the lower IR Sensor has been tripped */
    public boolean getGandalfHasBall() {
        return gandalfIRSensor.get();
    }

    /** Marks the conveyor as in a shooting state, forcing both motors to run except when rejecting */
    public void setShoot(boolean shooting) {
        this.shooting = shooting;
    }

    /** Returns if the conveyor is in its shooting state */
    public boolean isShooting() {
        return shooting;
    }

    /** Returns if an opponent ball is in the ejection gap, meaning that the gandalf motor should always be spinning out */
    public boolean getGandalfShouldEject() {
        return colorSensor.hasOpponentBall();
    }

    /** Returns, based on the flow chart attached to the repo, if both motors should be running or not; overriden by getGandalfShouldEject() for gandalf motor */
    public boolean getBothShouldRun() {
        if (isShooting()) {
            return true;
        } else if (getTopBeltHasBall()) {
            return false;
        } else if (colorSensor.hasAllianceBall()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void periodic() {
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Conveyor/Top Belt Speed", topBeltMotor.get());
            SmartDashboard.putNumber("Conveyor/Gandalf Motor Speed", gandalfMotor.get());
            SmartDashboard.putBoolean(
                    "Conveyor/Top Belt Upper IR Has Ball", getTopBeltHasBall());
            SmartDashboard.putBoolean(
                    "Conveyor/Top Belt Lower IR Ball", getGandalfHasBall());
        }
    }
}
