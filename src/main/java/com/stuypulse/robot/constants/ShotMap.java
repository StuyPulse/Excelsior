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
        
        double MIN_DISTANCE = 0;

        public interface Distances{
                double RING = Limelight.RING_DISTANCE.get();
                double POINT_A = Units.inchesToMeters(167);
                double POINT_B = Units.inchesToMeters(184);
                double POINT_D = Units.inchesToMeters(193);
                double POINT_E = Units.inchesToMeters(201);
                double LAUNCHPAD = Limelight.PAD_DISTANCE.get();
                double FAR = Units.inchesToMeters(234);
        }       

        // Converts a distance measurement to an RPM to shoot at
        Interpolator DISTANCE_TO_RPM =
                new NearestInterpolator(
                        new Vector2D(Distances.RING, Settings.Shooter.RING_RPM.get()),
                        new Vector2D(Distances.POINT_A, 3050),
                        new Vector2D(Distances.POINT_B, 3200),
                        new Vector2D(Distances.POINT_D, 3300),
                        new Vector2D(Distances.POINT_E, 3400),
                        new Vector2D(Distances.LAUNCHPAD, Settings.Shooter.PAD_RPM.get()));

        // Converts a distance measurement to an angle offset to align to
        Interpolator DISTANCE_TO_YAW =
                new NearestInterpolator(
                        new Vector2D(Distances.RING, Limelight.RING_YAW.get()),
                        new Vector2D(Distances.POINT_A, 6.25),
                        new Vector2D(Distances.POINT_B, 5.70),
                        new Vector2D(Distances.POINT_D, 5.50),
                        new Vector2D(Distances.POINT_E, 5.25),
                        new Vector2D(Distances.LAUNCHPAD, Limelight.PAD_YAW.get()));
}