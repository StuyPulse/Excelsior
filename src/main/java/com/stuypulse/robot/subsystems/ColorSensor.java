/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.ColorSensor.BallColor;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.ColorMatch;
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

    /*** IS CONNECTED ***/

    public boolean isConnected() {
        return Settings.ColorSensor.ENABLED.get() && colorSensor.isConnected();
    }

    /*** COLOR DETERMINATION ***/

    private Color getRawColor() {
        return colorSensor.getColor();
    }

    private Color getMatchedColor() {
        return colorMatcher.matchClosestColor(getRawColor()).color;
    }

    /*** PROXIMITY DETERMINATION ***/

    // Returns value from 0 - 2047 [higher == closer]
    private int getProximity() {
        return colorSensor.getProximity();
    }

    private boolean hasBall() {
        return getProximity() > Settings.ColorSensor.PROXIMITY_THRESHOLD.get();
    }

    /*** BALL DETERMINATION ***/

    private CurrentBall getCurrentBall() {
        Color matched = getMatchedColor();

        if (!hasBall()) {
            return CurrentBall.NO_BALL;
        } else if (matched.equals(BallColor.RED)) {
            return CurrentBall.RED_BALL;
        } else if (matched.equals(BallColor.BLUE)) {
            return CurrentBall.BLUE_BALL;
        }

        DriverStation.reportWarning("ColorSensorMatching returned unexpected color!", true);
        return getTargetBall();
    }

    private CurrentBall getTargetBall() {
        switch (DriverStation.getAlliance()) {
            case Blue:
                return CurrentBall.BLUE_BALL;
            case Red:
                return CurrentBall.RED_BALL;
            default:
                DriverStation.reportWarning("DriverStation.getAlliance() returned invalid!", true);
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

    /*** DEBUG INFORMATION ***/

    private static String colorToString(Color color) {
        StringBuilder output = new StringBuilder(36);
        output.append("[r: ").append(Math.round(1000.0 * color.red) / 1000.0).append(",");
        output.append(" g: ").append(Math.round(1000.0 * color.green) / 1000.0).append(",");
        output.append(" b: ").append(Math.round(1000.0 * color.blue) / 1000.0).append("]");
        return output.toString();
    }

    @Override
    public void periodic() {
        if (Settings.DEBUG_MODE.get()) {
            SmartDashboard.putBoolean("Debug/Color Sensor/Is Connected", isConnected());

            SmartDashboard.putString("Debug/Color Sensor/Raw Color", colorToString(getRawColor()));
            SmartDashboard.putString(
                    "Debug/Color Sensor/Matched Color", colorToString(getMatchedColor()));

            SmartDashboard.putBoolean("Debug/Color Sensor/Has Any Ball", hasBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Alliance Ball", hasAllianceBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Opponent Ball", hasOpponentBall());

            SmartDashboard.putString("Debug/Color Sensor/Current Ball", getCurrentBall().name());
            SmartDashboard.putString("Debug/Color Sensor/Target Ball", getTargetBall().name());

            SmartDashboard.putNumber("Debug/Color Sensor/Proximity", getProximity());
        }
    }
}
