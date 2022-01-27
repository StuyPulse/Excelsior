/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import com.stuypulse.robot.Constants.ColorSensorSettings;
import com.stuypulse.robot.Constants.Ports;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * Detects what color ball is in the Conveyor.
 *
 * Contains:
 *      - getCurrentBall()
 *         - Gets current ball seen by subsystem.
 *         - Returns CurrentBall enum
 *      - gapHasAllianceBall()
 *         - Checks if there is a ball and the ball is the correct alliance color
 *         - Returns false if no ball or wrong Alliance color
 *      - isBallPresent()
 *         - Checks if there is a ball present
 *
 * @author Vincent Wang
 */
public class ColorSensor extends SubsystemBase {
    public enum CurrentBall {
        RED_BALL(Alliance.Red, new Color(0.5432, 0.3401, 0.1169)),
        BLUE_BALL(Alliance.Blue, new Color(0.1826, 0.42505, 0.3982)),
        NO_BALL(null, null);

        public final Alliance allianceColor;
        public final Color BallColor;

        private CurrentBall(Alliance allianceColor, Color BallColor) {
            this.allianceColor = allianceColor;
            this.BallColor = BallColor;
        }
    }

    private final ColorMatch colorMatcher;

    private final ColorSensorV3 colorSensor;

    public ColorSensor() {
        colorMatcher = new ColorMatch();
        colorSensor = new ColorSensorV3(Ports.COLOR_SENSOR);
        colorMatcher.addColorMatch(CurrentBall.RED_BALL.BallColor);
        colorMatcher.addColorMatch(CurrentBall.BLUE_BALL.BallColor);
    }

    public CurrentBall getCurrentBall() {
        ColorMatchResult matched = getMatchedColor();
        if (hasBall()) {
            if (matched.color.equals(CurrentBall.RED_BALL.BallColor)) {
                return CurrentBall.RED_BALL;
            }
            if (matched.color.equals(CurrentBall.BLUE_BALL.BallColor)) {
                return CurrentBall.BLUE_BALL;
            }
        }
        return CurrentBall.NO_BALL;
    }

    private Color getColor() {
        return colorSensor.getColor();
    }

    private ColorMatchResult getMatchedColor() {
        return colorMatcher.matchClosestColor(getColor());
    }

    public boolean hasBall() {
        return getMatchedColor().confidence > ColorSensorSettings.MIN_CONFIDENCE.get();
    }

    public boolean hasAllianceBall() {
        // Checks if the driver station the ball belongs to is the same as your driver station
        // If no ball present, driver station is null
        return DriverStation.getAlliance() == getCurrentBall().allianceColor;
    }

    @Override
    public void periodic() {
        colorMatcher.setConfidenceThreshold(ColorSensorSettings.MIN_CONFIDENCE.get());
        SmartDashboard.putBoolean("ColorSensor/Color Sensor Has Alliance Ball", hasAllianceBall());
        SmartDashboard.putBoolean("ColorSensor/Color Sensor Has Any Ball", hasBall());

    }
}
