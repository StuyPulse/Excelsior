/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.ColorSensor.BallRGB;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
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
            connected = false;
            color = Color.kBlack;
        }

        public void update() {
            this.connected = Settings.ColorSensor.ENABLED.get();
            if (this.connected) this.connected &= !DriverStation.isAutonomous();
            if (this.connected) this.connected &= colorSensor.isConnected();

            if (this.connected) this.color = colorSensor.getColor();
        }
    }

    public enum BallColor {
        RED_BALL,
        BLUE_BALL
    }

    private BallColor target;
    private final Sensor sensor;
    private final DigitalInput ballIR;

    private final Debouncer alliance;
    private final Debouncer opponent;

    public ColorSensor() {
        sensor = new Sensor();
        ballIR = new DigitalInput(Ports.ColorSensor.BALL_IR_SENSOR);

        alliance = new Debouncer(Settings.ColorSensor.DEBOUNCE_TIME, DebounceType.kRising);
        opponent = new Debouncer(Settings.ColorSensor.DEBOUNCE_TIME, DebounceType.kRising);

        getTargetBallUpdate();
    }

    /*** PROXIMITY DETERMINATION ***/

    public boolean hasBall() {
        return !ballIR.get();
    }

    /*** TARGET BALL DETERMINATION ***/

    public BallColor getTargetBallUpdate() {
        switch (DriverStation.getAlliance()) {
            default:
                Settings.reportWarning("DriverStation.getAlliance() returned invalid Color!");
            case Red:
                return target = BallColor.RED_BALL;
            case Blue:
                return target = BallColor.BLUE_BALL;
        }
    }

    public BallColor getTargetBall() {
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

    public BallColor getCurrentBall() {
        // Get the error to each of the target colors
        double redError = getColorDistance(getRawColor(), BallRGB.RED);
        double blueError = getColorDistance(getRawColor(), BallRGB.BLUE);

        // Bias the error towards the alliance color
        if (getTargetBall() == BallColor.RED_BALL) {
            redError /= Settings.ColorSensor.TARGET_BIAS.get();
        }

        if (getTargetBall() == BallColor.BLUE_BALL) {
            blueError /= Settings.ColorSensor.TARGET_BIAS.get();
        }

        // Return the color that the sensor is sensing
        return redError < blueError ? BallColor.RED_BALL : BallColor.BLUE_BALL;
    }

    /*** PUBLIC BALL DETERMINATION ***/

    private boolean isConnected() {
        return sensor.connected;
    }

    public boolean hasAllianceBall() {
        if (!isConnected()) {
            return hasBall();
        }

        return alliance.calculate(hasBall() && getCurrentBall() == getTargetBall());
    }

    public boolean hasOpponentBall() {
        if (!isConnected()) {
            return false;
        }

        return opponent.calculate(hasBall() && getCurrentBall() != getTargetBall());
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
