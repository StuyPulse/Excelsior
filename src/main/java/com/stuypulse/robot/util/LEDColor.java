/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.Timer;

/**
 * Class that stores all of the different PWM values for the LED Controller.
 *
 * @author Sam Belliveau
 * @author Andrew Liu
 */
public class LEDColor {
    private final double color;
    private final boolean pulse;

    private LEDColor(double color, boolean pulse) {
        this.color = color;
        this.pulse = pulse;
    }

    private LEDColor(double color) {
        this(color, false);
    }

    public LEDColor pulse() {
        return new LEDColor(this.color, true);
    }

    public double get() {
        // Variables for detecting if we should be blinking or not
        double cT = Timer.getFPGATimestamp() % (Settings.LED.BLINK_TIME);
        double oT = (0.5 * Settings.LED.BLINK_TIME);

        // Detect if the color should be on or off
        if (pulse && cT >= oT) {
            return LEDColor.OFF.color;
        } else {
            return this.color;
        }
    }

    /***********************/
    /*** COLOR CONSTANTS ***/
    /***********************/

    public static final LEDColor BEAT = new LEDColor(-0.67);

    public static final LEDColor BREATH = new LEDColor(-0.17);
    public static final LEDColor CONFETTI = new LEDColor(-0.87);
    public static final LEDColor FIRE = new LEDColor(0.57);
    public static final LEDColor HEARTBEAT = new LEDColor(-0.25);
    public static final LEDColor SINELON = new LEDColor(-0.77);
    public static final LEDColor TWINKLES = new LEDColor(-0.53);
    public static final LEDColor WAVE = new LEDColor(-0.43);
    public static final LEDColor RAINBOW = new LEDColor(-0.99);

    public static final LEDColor AQUA = new LEDColor(0.81);
    public static final LEDColor BLACK = new LEDColor(0.99);
    public static final LEDColor BLUE = new LEDColor(0.87);
    public static final LEDColor BLUE_GREEN = new LEDColor(0.79);
    public static final LEDColor BLUE_VIOLET = new LEDColor(0.89);
    public static final LEDColor DARK_BLUE = new LEDColor(0.85);
    public static final LEDColor DARK_GRAY = new LEDColor(0.97);
    public static final LEDColor DARK_GREEN = new LEDColor(0.75);
    public static final LEDColor DARK_RED = new LEDColor(0.59);
    public static final LEDColor GOLD = new LEDColor(0.67);
    public static final LEDColor GRAY = new LEDColor(0.95);
    public static final LEDColor GREEN = new LEDColor(0.77);
    public static final LEDColor HOT_PINK = new LEDColor(0.57);
    public static final LEDColor LAWN_GREEN = new LEDColor(0.71);
    public static final LEDColor LIME = new LEDColor(0.73);
    public static final LEDColor ORANGE = new LEDColor(0.65);
    public static final LEDColor PINK = new LEDColor(0.57);
    public static final LEDColor PURPLE = new LEDColor(0.91);
    public static final LEDColor RED = new LEDColor(0.61);
    public static final LEDColor RED_ORANGE = new LEDColor(0.63);
    public static final LEDColor SKY_BLUE = new LEDColor(0.83);
    public static final LEDColor VIOLET = new LEDColor(0.91);
    public static final LEDColor WHITE = new LEDColor(0.93);
    public static final LEDColor YELLOW = new LEDColor(0.69);

    public static final LEDColor OFF = new LEDColor(0.99);
}
