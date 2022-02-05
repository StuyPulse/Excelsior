package com.stuypulse.robot.util;

import com.stuypulse.robot.Constants.LimelightSettings;
import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.network.limelight.Limelight;

public class Target {
    
    private static final Limelight kLimelight = Limelight.getInstance();

    public static boolean hasTarget() {
        return kLimelight.getValidTarget();
    }

    public static Angle getYAngle() {
        return Angle.fromDegrees(kLimelight.getTargetYAngle()); 
    }

    public static double getDistance() {
        double yAngle = kLimelight.getTargetYAngle();
        double opposite = LimelightSettings.HUB_HEIGHT - LimelightSettings.LIMELIGHT_HEIGHT;


        return opposite / Angle.fromDegrees(yAngle).tan();
    }

}
