/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.ColorSensorSettings;
import com.stuypulse.robot.Constants.ColorSensorSettings.BallColor;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

/*-
 * Detects what color ball is in the Conveyor.
 *
 * Contains:
 *      - getCurrentBall()
 *         - Gets current ball seen by subsystem.
 *         - Returns CurrentBall enum
 *      - isConnected()
 *         - Checks if the Color Sensor is connected
 *         - Returns false if not, true if yes.
 *      - hasAllianceBall()
 *         - Checks if there is a ball and the ball is the correct alliance color
 *         - Returns false if no ball or wrong Alliance color
 *      - hasOpponentBall()
 *         - Checks if there is a ball present and the ball is an opponent ball
 *         - Returns false if no ball or is alliance ball
 *      - hasBall()
 *         - Checks if there is a ball present
 *
 * @author Vincent Wang
 */
public class ColorSensor extends SubsystemBase {
    public enum CurrentBall {
        RED_BALL,
        BLUE_BALL,
        NO_BALL;
    }

    private final ColorMatch colorMatcher;

    private final ColorSensorV3 colorSensor;

    public ColorSensor() {
        colorMatcher = new ColorMatch();
        colorSensor = new ColorSensorV3(Ports.COLOR_SENSOR);
        colorMatcher.addColorMatch(BallColor.BLUE);
        colorMatcher.addColorMatch(BallColor.RED);
    }

    private Color getRawColor() {
        return colorSensor.getColor();
    }

    private ColorMatchResult getMatchedColor() {
        return colorMatcher.matchClosestColor(getRawColor());
    }

    private int getProximity() {
        return colorSensor.getProximity();
    }

    private boolean hasBall() {
        return getProximity() > ColorSensorSettings.MAX_PROXIMITY.get();
    }

    private boolean isConnected() {
        return colorSensor.isConnected();
    }

    private CurrentBall getCurrentBall() {
        ColorMatchResult matched = getMatchedColor();
        if (!hasBall()) {
            return CurrentBall.NO_BALL;
        }
        if (matched.color.equals(BallColor.RED)) {
            return CurrentBall.RED_BALL;
        }
        return CurrentBall.BLUE_BALL;
    }

    private CurrentBall getTargetBall() {
        switch (DriverStation.getAlliance()) {
            case Blue:
                return CurrentBall.BLUE_BALL;
            case Red:
                return CurrentBall.RED_BALL;
            default:
                return CurrentBall.NO_BALL;
        }
    }

    public boolean hasAllianceBall() {
        if (!isConnected()) {
            DriverStation.reportWarning("Color Sensor is disconnected!", true);
            return true;
        }
        return hasBall() && getCurrentBall() == getTargetBall();
    }

    public boolean hasOpponentBall() {
        if (!isConnected()) {
            DriverStation.reportWarning("Color Sensor is disconnected!", true);
            return false;
        }
        return hasBall() && !hasAllianceBall();
    }

    @Override
    public void periodic() {
        if (Constants.DEBUG_MODE.get()) {
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Alliance Ball", hasAllianceBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Any Ball", hasBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Is Connected", isConnected());

            SmartDashboard.putNumber("Debug/Color Sensor/Proximity", getProximity());
        }
    }
}
