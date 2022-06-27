/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.stuylib.math.interpolation.Interpolator;
import com.stuypulse.stuylib.math.interpolation.NearestInterpolator;

import edu.wpi.first.math.util.Units;

import com.stuypulse.robot.constants.Settings.Limelight;



/** Contains interpolation tables for shooting and alignment */
public interface ShotMap {
        
        double minDistance = 0;

        public interface Distances{
                double RING = Limelight.RING_DISTANCE.get();
                double PointA = Units.inchesToMeters(167);
                double PointB = Units.inchesToMeters(184);
                double LAUNCHPAD = Limelight.PAD_DISTANCE.get();
        }       

        // Converts a distance measurement to an RPM to shoot at
        Interpolator DISTANCE_TO_RPM =
                new NearestInterpolator(
                        new Vector2D(Distances.RING, Settings.Shooter.RING_RPM.get()),
                        new Vector2D(Distances.PointA, Units.inchesToMeters(3050)),
                        new Vector2D(Distances.PointB, Units.inchesToMeters(3200)),    
                        new Vector2D(Distances.LAUNCHPAD, Settings.Shooter.PAD_RPM.get()));

        // Converts a distance measurement to an angle offset to align to
        Interpolator DISTANCE_TO_YAW =
                new NearestInterpolator(
                        new Vector2D(Distances.RING, Limelight.RING_YAW.get()),
                        new Vector2D(Distances.PointA, Units.inchesToMeters(6.25)),
                        new Vector2D(Distances.PointB, Units.inchesToMeters(5.7)),
                        new Vector2D(Distances.LAUNCHPAD, Limelight.PAD_YAW.get()));
}