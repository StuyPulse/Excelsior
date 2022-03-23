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
        // kLimelight.setLEDMode(LEDMode.PIPELINE);
        kLimelight.setLEDMode(LEDMode.FORCE_ON);
    }

    public static boolean hasAnyTarget() {
        return kLimelight.getValidTarget();
    }

    public static boolean hasTarget() {
        return hasAnyTarget()
                && Settings.Limelight.MIN_VALID_DISTANCE < getDistance()
                && getDistance() < Settings.Limelight.MAX_VALID_DISTANCE;
    }

    public static Angle getXAngle() {
        if (!hasAnyTarget()) {
            Settings.reportWarning("Unable To Find Target! [getXAngle() was called]");
            return Angle.kZero;
        }

        return Angle.fromDegrees(
                kLimelight.getTargetXAngle() + Settings.Limelight.LIMELIGHT_YAW.get());
    }

    public static Angle getYAngle() {
        if (!hasAnyTarget()) {
            Settings.reportWarning("Unable To Find Target! [getYAngle() was called]");
            return Angle.kZero;
        }

        return Angle.fromDegrees(
                kLimelight.getTargetYAngle() + Settings.Limelight.LIMELIGHT_PITCH.get());
    }

    public static double getDistance() {
        if (!hasAnyTarget()) {
            Settings.reportWarning("Unable To Find Target! [getDistance() was called]");
            return Settings.Limelight.RING_SHOT_DISTANCE;
        }

        return Settings.Limelight.HEIGHT_DIFFERENCE / getYAngle().tan();
    }
}
