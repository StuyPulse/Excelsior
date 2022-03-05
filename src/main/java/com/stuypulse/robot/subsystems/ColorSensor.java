/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.ColorSensor.BallColor;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
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

    private static class Sensor {
        private final ColorSensorV3 colorSensor;
        public boolean connected;
        public Color color;

        public Sensor() {
            colorSensor = new ColorSensorV3(Ports.ColorSensor.COLOR_SENSOR);
        }

        public void update() {
            this.connected = Settings.ColorSensor.ENABLED.get();
            this.connected &= !DriverStation.isAutonomous();
            this.connected &= colorSensor.isConnected();

            this.color = colorSensor.getColor();
        }
    }

    public enum CurrentBall {
        RED_BALL,
        BLUE_BALL,
        NO_BALL;
    }

    private CurrentBall target;
    private final Sensor sensor;
    private final DigitalInput ballIR;

    public ColorSensor() {
        sensor = new Sensor();
        ballIR = new DigitalInput(Ports.ColorSensor.BALL_IR_SENSOR);
        getTargetBallUpdate();
    }

    /*** IS CONNECTED ***/

    public boolean isConnected() {
        return sensor.connected;
    }

    /*** PROXIMITY DETERMINATION ***/

    public boolean hasBall() {
        return !ballIR.get();
    }

    /*** TARGET BALL DETERMINATION ***/

    public CurrentBall getTargetBallUpdate() {
        switch (DriverStation.getAlliance()) {
            case Blue:
                return target = CurrentBall.BLUE_BALL;
            case Red:
                return target = CurrentBall.RED_BALL;
            default:
                Settings.reportWarning("DriverStation.getAlliance() returned invalid!");
                return target = CurrentBall.NO_BALL;
        }
    }

    public CurrentBall getTargetBall() {
        return target;
    }

    /*** COLOR DETERMINATION ***/

    private static double getColorDistance(Color a, Color b) {
        double dr = a.red - b.red;
        double dg = a.green - b.green;
        double db = a.blue - b.blue;
        return dr * dr + dg * dg + db * db;
    }

    private Color getRawColor() {
        return sensor.color;
    }

    public CurrentBall getCurrentBall() {
        double redError = getColorDistance(getRawColor(), BallColor.RED);
        double blueError = getColorDistance(getRawColor(), BallColor.BLUE);

        if (redError < blueError) {
            return CurrentBall.RED_BALL;
        } else {
            return CurrentBall.BLUE_BALL;
        }
    }

    /*** PUBLIC BALL DETERMINATION ***/

    public boolean hasAllianceBall() {
        if (!isConnected()) {
            return hasBall();
        }

        return hasBall() && getCurrentBall() == getTargetBall();
    }

    public boolean hasOpponentBall() {
        if (!isConnected()) {
            return false;
        }

        return hasBall() && !hasAllianceBall();
    }

    /*** DEBUG INFORMATION ***/

    @Override
    public void periodic() {
        sensor.update();

        if (Settings.DEBUG_MODE.get()) {
            SmartDashboard.putBoolean("Debug/Color Sensor/Is Connected", isConnected());

            SmartDashboard.putNumber("Debug/Color Sensor/Color R", getRawColor().red);
            SmartDashboard.putNumber("Debug/Color Sensor/Color G", getRawColor().green);
            SmartDashboard.putNumber("Debug/Color Sensor/Color B", getRawColor().blue);

            SmartDashboard.putBoolean("Debug/Color Sensor/Has Any Ball", hasBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Alliance Ball", hasAllianceBall());
            SmartDashboard.putBoolean("Debug/Color Sensor/Has Opponent Ball", hasOpponentBall());
        }
    }
}
