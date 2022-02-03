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
 * @author Edmund Chin (edmundc421@gmail.com)
 */

public class Conveyor extends SubsystemBase {
    private final CANSparkMax topMotor;
    private final CANSparkMax gandalfMotor;

    private final ColorSensor colorSensor;
    private final DigitalInput gandalfIRSensor;
    private final DigitalInput topIRSensor;

    private boolean shooting;

    /** Creates a Conveyor subsystem */
    public Conveyor() {
        topMotor = new CANSparkMax(Ports.Conveyor.TOP_CONVEYOR_MOTOR, MotorType.kBrushless);
        gandalfMotor = new CANSparkMax(Ports.Conveyor.GANDALF_MOTOR, MotorType.kBrushless);

        colorSensor = new ColorSensor();
        gandalfIRSensor = new DigitalInput(Ports.Conveyor.GANDALF_IR_SENSOR);
        topIRSensor = new DigitalInput(Ports.Conveyor.TOP_CONVEYOR_IR_SENSOR);
        shooting = false;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShoot(boolean shooting) {
        this.shooting = shooting;
    }

    /** Spins the Top Conveyor Belt, moving the ball up to the shooter */
    private void spinTopBelt() {
        topMotor.set(ConveyorSettings.TOP_MOTOR_SPEED.get());
    }

    /**
     * Accept ball - spin the gandalf motor upwards to the top conveyor To be used when the ball is
     * team alliance color
     */
    private void acceptBall() {
        gandalfMotor.set(ConveyorSettings.ACCEPT_SPEED.get());
    }

    /**
     * Eject ball - spin the gandalf motor outwards To be used when the ball is opposing alliance
     * color
     */
    private void rejectBall() {
        // If the ball is not of our alliance color, reject ball
        gandalfMotor.set(ConveyorSettings.REJECT_SPEED.get());
    }

    /** Stops the Top Conveyor Belt */
    private void stopTopBelt() {
        topMotor.stopMotor();
    }

    /** Stops the Gandalf Motor */
    private void stopGandalf() {
        gandalfMotor.stopMotor();
    }

    /** Finds if the upper IR Sensor has been tripped e.g., there is a ball in the top conveyor */
    private boolean getTopConveyorUpperHasBall() {
        return topIRSensor.get();
    }

    /** Finds if the lower IR Sensor has been tripped */
    private boolean getTopConveyorLowerHasBall() {
        return gandalfIRSensor.get();
    }

    private boolean getEjecting() {
        return colorSensor.hasOpponentBall();
    }

    private boolean getRunning() {
        if (isShooting()) { // same as 3rd?
            return true;
        } else if (getTopConveyorUpperHasBall()) { // top IR
            return false; // all motors are stopped according to logic tabl
        } else if (colorSensor.hasAllianceBall()) { // good gap
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void periodic() {
        // boolean bottomIRHasBall = getTopConveyorLowerHasBall();
        // boolean topIRHasBall = getTopConveyorUpperHasBall();

        boolean ejecting = getEjecting();
        boolean running = getRunning();

        // Gandalf
        if (ejecting) {
            rejectBall();
        } else if (running) {
            acceptBall();
        } else {
            stopGandalf();
        }

        // Top Conveyor Motor
        if (running) {
            spinTopBelt();
        } else {
            stopTopBelt();
        }

        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Conveyor/Top Motor Speed", topMotor.get());
            SmartDashboard.putNumber("Conveyor/Gandalf Motor Speed", gandalfMotor.get());
            SmartDashboard.putBoolean(
                    "Conveyor/Top Conveyor Upper IR Has Ball", getTopConveyorUpperHasBall());
            SmartDashboard.putBoolean(
                    "Conveyor/Top Conveyor Lower IR Ball", getTopConveyorLowerHasBall());
        }
    }
}
