/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.stuylib.math.interpolation.Interpolator;
import com.stuypulse.stuylib.math.interpolation.NearestInterpolator;

/** Class containing the measurements of every item on the field */
public interface RPMMap {

    static Vector2D[] POINTS = {
        new Vector2D(Limelight.RING_DISTANCE.get(), Settings.Shooter.RING_RPM.get()),
        new Vector2D(Limelight.PAD_DISTANCE.get(), Settings.Shooter.PAD_RPM.get())
        
    };
    NearestInterpolator NearestInterpolator = new NearestInterpolator(POINTS);
    
}
   