/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.ConveyorSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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

    public enum Direction {
        FORWARD,
        STOPPED,
        REVERSE
    }

    // private final CANSparkMax topBeltMotor;
    // private final CANSparkMax gandalfMotor;

    // private final ColorSensor colorSensor;
    // private final DigitalInput topIRSensor;

    private Direction topBeltDirection;
    private Direction gandalfDirection;

    /** Creates a Conveyor subsystem */
    public Conveyor() {
        // topBeltMotor = new CANSparkMax(Ports.Conveyor.TOP_BELT_MOTOR, MotorType.kBrushless);
        // gandalfMotor = new CANSparkMax(Ports.Conveyor.GANDALF_MOTOR, MotorType.kBrushless);

        // colorSensor = new ColorSensor();
        // topIRSensor = new DigitalInput(Ports.Conveyor.TOP_BELT_IR_SENSOR);

        setTopBelt(Direction.STOPPED);
        setGandalf(Direction.STOPPED);
    }

    /** Spins the Top Conveyor Belt, moving the ball up to the shooter. If false, */
    public void setTopBelt(Direction direction) {
        topBeltDirection = direction;
        // switch (direction) {
        //     case FORWARD:
        //         topBeltMotor.set(ConveyorSettings.TOP_BELT_SPEED.get());
        //         break;
        //     case STOPPED:
        //         topBeltMotor.stopMotor();
        //         break;
        //     case REVERSE:
        //         topBeltMotor.set(-ConveyorSettings.TOP_BELT_SPEED.get());
        //         break;
        // }
    }

    public void setGandalf(Direction direction) {
        gandalfDirection = direction;
        // switch (direction) {
        //     case FORWARD:
        //         gandalfMotor.set(ConveyorSettings.ACCEPT_SPEED.get());
        //         break;
        //     case STOPPED:
        //         gandalfMotor.stopMotor();
        //         break;
        //     case REVERSE:
        //         gandalfMotor.set(ConveyorSettings.REJECT_SPEED.get());
        //         break;
        // }
    }

    public Direction getTopBeltDirection() {
        return topBeltDirection;
    }

    public Direction getGandalfDirection() {
        return gandalfDirection;
    }

    /** Finds if the upper IR Sensor has been tripped e.g., there is a ball in the top conveyor */
    public boolean getTopBeltHasBall() {
        return true; // ConveyorSettings.DISABLE_IR_SENSOR.get() || topIRSensor.get();
    }

    public boolean hasOpponentBall() {
        return false; // colorSensor.hasOpponentBall();
    }

    public boolean hasAllianceBall() {
        return true; // colorSensor.hasAllianceBall();
    }

    public boolean isFull() {
        return true; // colorSensor.isConnected() && getTopBeltHasBall() && hasAllianceBall();
    }

    public boolean shouldRetractIntake() {
        return false; // ConveyorSettings.AUTO_RETRACT.get() && DriverStation.isTeleopEnabled() && isFull();
    }

    @Override
    public void periodic() {
        // if (Constants.DEBUG_MODE.get()) {
        //     SmartDashboard.putNumber("Debug/Conveyor/Top Belt Speed", topBeltMotor.get());
        //     SmartDashboard.putNumber("Debug/Conveyor/Gandalf Motor Speed", gandalfMotor.get());
        //     SmartDashboard.putBoolean(
        //             "Debug/Conveyor/Top Belt Upper IR Has Ball", getTopBeltHasBall());
        // }
    }
}
