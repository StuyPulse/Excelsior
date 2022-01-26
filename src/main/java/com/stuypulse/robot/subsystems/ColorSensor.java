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

    private Color getColor() {
        return colorSensor.getColor();
    }

    private ColorMatchResult getMatchedColor() {
        return colorMatcher.matchClosestColor(getColor());
    }

    public boolean hasBall() {
        return getMatchedColor().confidence > ColorSensorSettings.MIN_CONFIDENCE.get();
    }

    public CurrentBall getCurrentBall() {
        ColorMatchResult matched = getMatchedColor();
        if (hasBall()) {
            if (matched.color.equals(BallColor.RED)) {
                return CurrentBall.RED_BALL;
            }
            if (matched.color.equals(BallColor.BLUE)) {
                return CurrentBall.BLUE_BALL;
            }
        }
        return CurrentBall.NO_BALL;
    }
    
    public boolean gapContainsAllianceBall() {
    // Choose not to store the alliance in order to avoid FMS initially giving faulty color
    Alliance alliance = DriverStation.getAlliance();
    CurrentBall presentBall = getCurrentBall();
    if (presentBall == CurrentBall.NO_Ball) {
        return false;
    }
    return (alliance == Alliance.Blue && presentBall == CurrentBall.BLUE_BALL) ||
           (alliance == Alliance.Red && presentBall == CurrentBall.RED_BALL);
    }
    
    public boolean gapContainsBall() {
        return getCurrentBall() != CurrentBall.NO_BALL;
    }

    private CurrentBall getTargetBall() { 

        switch(DriverStation.getAlliance()) {
            case Blue:
                return CurrentBall.BLUE_BALL;
            case Red:
                return CurrentBall.RED_BALL;
            default:
                return CurrentBall.NO_BALL;
        }
        
    }

    public boolean hasAllianceBall() {
        // Choose not to store the alliance in order to avoid FMS initially giving
        // faulty color
        return hasBall() && getCurrentBall() == getTargetBall();   
    }

    @Override
    public void periodic() {
        colorMatcher.setConfidenceThreshold(ColorSensorSettings.MIN_CONFIDENCE.get());
        SmartDashboard.putBoolean("ColorSensor/Color Sensor Has Alliance Ball", hasAllianceBall());
        SmartDashboard.putBoolean("ColorSensor/Color Sensor Has Any Ball", hasBall());

    }
}
