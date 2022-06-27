/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.stuylib.math.interpolation.Interpolator;
import com.stuypulse.stuylib.math.interpolation.NearestInterpolator;

import com.stuypulse.robot.constants.Settings.Limelight;

/** Contains interpolation tables for shooting and alignment */
public interface ShotMap {

    double MIN_DISTANCE = 0;

    // Converts a distance measurement to an RPM to shoot at
    Interpolator DISTANCE_TO_RPM =
            new NearestInterpolator(
                    new Vector2D(Limelight.RING_DISTANCE.get(), Settings.Shooter.RING_RPM.get()),
                    new Vector2D(167, 3050),
                    new Vector2D(184, 3200),    
                    new Vector2D(Limelight.PAD_DISTANCE.get(), Settings.Shooter.PAD_RPM.get()));

    // Converts a distance measurement to an angle offset to align to
    Interpolator DISTANCE_TO_YAW =
            new NearestInterpolator(
                    new Vector2D(Limelight.RING_DISTANCE.get(), Limelight.RING_YAW.get()),
                    new Vector2D(167, 6.25),
                    new Vector2D(184, 5.7),
                    new Vector2D(Limelight.PAD_DISTANCE.get(), Limelight.PAD_YAW.get()));
}
