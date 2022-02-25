/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.ColorSensor.BallColor;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

    private CurrentBall targetBall;
    private final ColorSensorV3 colorSensor;

    public ColorSensor() {
        colorSensor = new ColorSensorV3(Ports.COLOR_SENSOR);
        Settings.ColorSensor.getAllianceChooser();
        updateAllianceColor();
    }

    /*** IS CONNECTED ***/

    public boolean isConnected() {
        return Settings.ColorSensor.ENABLED.get() && colorSensor.isConnected();
    }

    /*** PROXIMITY DETERMINATION ***/

    // Returns value from 0 - 2047 [higher == closer]
    private int getProximity() {
        return colorSensor.getProximity();
    }

    private boolean hasBall() {
        if (!isConnected()) {
            if (Settings.ENABLE_WARNINGS.get()) {
                DriverStation.reportWarning("Color Sensor is disconnected!", true);
            }

            return true;
        }
        return getProximity() > Settings.ColorSensor.PROXIMITY_THRESHOLD.get();
    }

    /*** TARGET BALL DETERMINATION ***/

    // Used to decide whether to use FMS or SmartDashboard provided alliance color
    public Alliance getAllianceColor() {
        Alliance network = Settings.ColorSensor.ALLIANCE_COLOR.getSelected();
        Alliance fms = DriverStation.getAlliance();

        return network == Alliance.Invalid ? fms : network;
    }

    public CurrentBall updateAllianceColor() {
        switch (getAllianceColor()) {
            case Blue:
                return targetBall = CurrentBall.BLUE_BALL;
            case Red:
                return targetBall = CurrentBall.RED_BALL;
            default:
                if (Settings.ENABLE_WARNINGS.get()) {
                    DriverStation.reportWarning(
                            "DriverStation.getAlliance() returned invalid!", true);
                }
                return targetBall = CurrentBall.NO_BALL;
        }
    }

    private CurrentBall getTargetBall() {
        return targetBall;
    }

    /*** COLOR DETERMINATION ***/

    private static double getColorDistance(Color a, Color b) {
        double dr = a.red - b.red;
        double dg = a.green - b.green;
        double db = a.blue - b.blue;
        return dr * dr + dg * dg + db * db;
    }

    private Color getRawColor() {
        return colorSensor.getColor();
    }

    private CurrentBall getCurrentBall() {
        Color color = getRawColor();

        double redError = getColorDistance(color, BallColor.RED);
        double blueError = getColorDistance(color, BallColor.BLUE);

        if (redError < blueError) {
            return CurrentBall.RED_BALL;
        } else {
            return CurrentBall.BLUE_BALL;
        }
    }

    /*** PUBLIC BALL DETERMINATION ***/

    public boolean hasAllianceBall() {
        if (!isConnected()) {
            if (Settings.ENABLE_WARNINGS.get()) {
                DriverStation.reportWarning("Color Sensor is disconnected!", true);
            }

            return true;
        }

        return hasBall() && getCurrentBall() == getTargetBall();
    }

    public boolean hasOpponentBall() {
        if (!isConnected()) {
            if (Settings.ENABLE_WARNINGS.get()) {
                DriverStation.reportWarning("Color Sensor is disconnected!", true);
            }

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

            SmartDashboard.putBoolean("Debug/Color Sensor/Has Any Ball", hasBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Alliance Ball", hasAllianceBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Opponent Ball", hasOpponentBall());

            SmartDashboard.putString("Debug/Color Sensor/Current Ball", getCurrentBall().name());
            SmartDashboard.putString("Debug/Color Sensor/Target Ball", getTargetBall().name());

            SmartDashboard.putNumber("Debug/Color Sensor/Proximity", getProximity());
        }
    }
}
