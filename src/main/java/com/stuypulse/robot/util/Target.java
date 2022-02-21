/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.network.limelight.Limelight;
import com.stuypulse.stuylib.network.limelight.Limelight.LEDMode;

import com.stuypulse.robot.constants.Settings;

public class Target {

    private static final Limelight kLimelight = Limelight.getInstance();

    public static void enable() {
        kLimelight.setLEDMode(LEDMode.FORCE_ON);
    }

    public static void disable() {
        kLimelight.setLEDMode(LEDMode.PIPELINE);
    }

    public static boolean hasTarget() {
        return kLimelight.getValidTarget()
                && Settings.Limelight.MIN_VALID_DISTANCE < getDistance()
                && getDistance() < Settings.Limelight.MAX_VALID_DISTANCE;
    }

    public static Angle getXAngle() {
        return Angle.fromDegrees(
                kLimelight.getTargetXAngle() + Settings.Limelight.LIMELIGHT_YAW.get());
    }

    public static Angle getYAngle() {
        return Angle.fromDegrees(
                kLimelight.getTargetYAngle() + Settings.Limelight.LIMELIGHT_PITCH.get());
    }

    public static double getDistance() {
        return Settings.Limelight.HEIGHT_DIFFERENCE / getYAngle().tan();
    }
}
