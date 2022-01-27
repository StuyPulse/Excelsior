/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import com.stuypulse.robot.Constants.ColorSensorSettings;
import com.stuypulse.robot.Constants.ColorSensorSettings.BallColor;
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
        RED_BALL(Alliance.Red),
        BLUE_BALL(Alliance.Blue),
        NO_BALL(Alliance.Invalid);

        public final Alliance allianceColor;

        private CurrentBall(Alliance allianceColor) {
            this.allianceColor = allianceColor;
        }
    }

    private final ColorMatch colorMatcher;

    private final ColorSensorV3 colorSensor;

    public ColorSensor() {
        colorMatcher = new ColorMatch();
        colorSensor = new ColorSensorV3(Ports.COLOR_SENSOR);
        colorMatcher.addColorMatch(BallColor.RED);
        colorMatcher.addColorMatch(BallColor.BLUE);
    }

    public CurrentBall getCurrentBall() {
        ColorMatchResult matched = getMatchedColor();
        if (isBallPresent()) {
            if (matched.color.equals(BallColor.RED)) {
                return CurrentBall.RED_BALL;
            }
            if (matched.color.equals(BallColor.BLUE)) {
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

    public boolean isBallPresent() {
        return getMatchedColor().confidence > ColorSensorSettings.MIN_CONFIDENCE.get();
    }

    public boolean gapHasAllianceBall() {
        if (!isBallPresent()) return false;
        return getCurrentBall().allianceColor.equals(DriverStation.getAlliance());
    }

    @Override
    public void periodic() {
        colorMatcher.setConfidenceThreshold(ColorSensorSettings.MIN_CONFIDENCE.get());
        SmartDashboard.putBoolean("ColorSensor/Color Sensor Has Alliance Ball", gapHasAllianceBall());
        SmartDashboard.putBoolean("ColorSensor/Color Sensor Has Any Ball", isBallPresent());

    }
}
