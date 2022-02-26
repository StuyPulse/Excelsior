/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;

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
        FORWARD_SLOW,
        STOPPED,
        REVERSE
    }

    private ConveyorMode mode;

    private final CANSparkMax topBeltMotor;
    private final CANSparkMax gandalfMotor;

    private final ColorSensor colorSensor;
    private final DigitalInput topIRSensor;

    private Direction topBeltDirection;
    private Direction gandalfDirection;

    /** Creates a Conveyor subsystem */
    public Conveyor(ColorSensor colorSensor) {
        this.topBeltMotor = new CANSparkMax(Ports.Conveyor.TOP_BELT_MOTOR, MotorType.kBrushless);
        Motors.Conveyor.TOP_BELT.configure(topBeltMotor);

        this.gandalfMotor = new CANSparkMax(Ports.Conveyor.GANDALF_MOTOR, MotorType.kBrushless);
        Motors.Conveyor.GANDALF.configure(gandalfMotor);

        this.colorSensor = colorSensor;
        this.topIRSensor = new DigitalInput(Ports.Conveyor.TOP_BELT_IR_SENSOR);

        setTopBelt(Direction.STOPPED);
        setGandalf(Direction.STOPPED);
        setMode(ConveyorMode.DEFAULT);
    }

    /*** MODE CONTROL ***/

    public void setMode(ConveyorMode mode) {
        this.mode = mode;
    }

    /*** MOTOR CONTROL ***/

    /** Spins the Top Conveyor Belt, moving the ball up to the shooter. If false, */
    public void setTopBelt(Direction direction) {
        switch (topBeltDirection = direction) {
            case FORWARD:
                topBeltMotor.set(+Settings.Conveyor.TOP_BELT_SPEED.get());
                break;
            case FORWARD_SLOW:
                gandalfMotor.set(Settings.Conveyor.ACCEPT_SPEED.get() * Settings.Conveyor.SLOW_MUL.get());
                break;
            case STOPPED:
                topBeltMotor.stopMotor();
                break;
            case REVERSE:
                topBeltMotor.set(-Settings.Conveyor.TOP_BELT_SPEED.get());
                break;
        }
    }

    public void setGandalf(Direction direction) {
        switch (gandalfDirection = direction) {
            case FORWARD:
                gandalfMotor.set(Settings.Conveyor.ACCEPT_SPEED.get());
                break;
            case FORWARD_SLOW:
                gandalfMotor.set(Settings.Conveyor.ACCEPT_SPEED.get() * Settings.Conveyor.SLOW_MUL.get());
                break;
            case STOPPED:
                gandalfMotor.stopMotor();
                break;
            case REVERSE:
                gandalfMotor.set(Settings.Conveyor.REJECT_SPEED.get());
                break;
        }
    }

    public Direction getTopBeltDirection() {
        return topBeltDirection;
    }

    public Direction getGandalfDirection() {
        return gandalfDirection;
    }

    /*** SENSOR INFORMATION ***/

    public boolean getColorSensorConnected() {
        return colorSensor.isConnected();
    }

    /** Finds if the upper IR Sensor has been tripped e.g., there is a ball in the top conveyor */
    public boolean getTopBeltHasBall() {
        return Settings.Conveyor.DISABLE_IR_SENSOR.get() || !topIRSensor.get();
    }

    public boolean hasOpponentBall() {
        return colorSensor.hasOpponentBall();
    }

    public boolean hasAllianceBall() {
        return colorSensor.hasAllianceBall();
    }

    /*** AUTOMATIC RETRACTION ***/

    public boolean isFull() {
        return colorSensor.isConnected() && getTopBeltHasBall() && hasAllianceBall();
    }

    public boolean shouldRetractIntake() {
        return Settings.Conveyor.AUTO_RETRACT.get() && DriverStation.isTeleopEnabled() && isFull();
    }

    /*** PERIODIC COMMAND ***/

    @Override
    public void periodic() {
        mode.run(this);

        if (Settings.DEBUG_MODE.get()) {
            SmartDashboard.putNumber("Debug/Conveyor/Top Belt", topBeltMotor.get());
            SmartDashboard.putNumber("Debug/Conveyor/Gandalf Motor", gandalfMotor.get());
            SmartDashboard.putBoolean("Debug/Conveyor/Top IR", getTopBeltHasBall());
        }
    }
}
