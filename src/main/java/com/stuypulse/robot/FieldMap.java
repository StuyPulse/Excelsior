/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

// origin is at center of hub
// units are meters

public interface FieldMap {

    static double imperialToMeters(int feet, double inches) {
        return Units.feetToMeters(feet) + Units.inchesToMeters(inches);
    }

    // height of tape
    // robot starting positions
    // shooting distance

    public interface Field {
        double WIDTH = imperialToMeters(27, 0);
        double LENGTH = imperialToMeters(54, 0);
        Translation2d CENTER = new Translation2d(0, 0);
    }

    public interface Balls {
        public interface Alliance {
            Translation2d UPPER = new Translation2d(-3.286667, 2.073733);
            Translation2d MIDDLE = new Translation2d(-3.173632, -2.242902);
            Translation2d LOWER = new Translation2d(-0.658126, -3.830068);
            Translation2d TERMINAL = new Translation2d(-7.164845, -2.990216);
        }

        public interface Opponent {
            Translation2d UPPER = new Translation2d(-2.242902, 3.173632);
            Translation2d MIDDLE = new Translation2d(-3.790375, -0.857674);
            Translation2d LOWER = new Translation2d(0.857674, -3.790375);
        }
    }

    public interface Hangar {
        double WIDTH = imperialToMeters(9, 8);
        double LENGTH = imperialToMeters(10, 8.75);
        double HEIGHT = imperialToMeters(6, 2);

        Translation2d BOTTOM_LEFT =
                new Translation2d(-imperialToMeters(27, 0), +imperialToMeters(3, 10));

        Translation2d BOTTOM_RIGHT =
                new Translation2d(-imperialToMeters(16, 5.75), +imperialToMeters(3, 10));

        Translation2d TOP_RIGHT =
                new Translation2d(-imperialToMeters(16, 5.75), +imperialToMeters(13, 6));
    }

    public interface Hub {
        Translation2d CENTER = new Translation2d(0, 0);

        double HEIGHT = imperialToMeters(8, 8);
        double UPPER_RADIUS = imperialToMeters(2, 0);
    }

    public interface Terminal {
        double LENGTH = imperialToMeters(7, 8.5);
    }
}
