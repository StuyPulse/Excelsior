/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import com.stuypulse.stuylib.control.PIDCalculator;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

public class SmartPIDController2 extends PIDController {
    private static String join(String... strings) {
        String out = strings[0];
        for (int i = 1; i < strings.length; ++i) {
            boolean trailing = out.endsWith("/");
            boolean leading = strings[i].startsWith("/");

            if (trailing && leading) {
                out = out.substring(0, out.length() - 1);
            } else if (!(trailing && leading)) {
                out += "/";
            }

            out += strings[i];
        }
        return out;
    }

    private final SmartBoolean tuning;
    private final PIDCalculator calculator;

    public SmartPIDController2(String id) {
        super(
                new SmartNumber(join(id, "P"), 0.0),
                new SmartNumber(join(id, "I"), 0.0),
                new SmartNumber(join(id, "D"), 0.0));

        tuning = new SmartBoolean(join(id, "Auto Tuning"), false);
        calculator = new PIDCalculator(new SmartNumber(join(id, "Tuning/Control Speed"), 0.1));
    }

    @Override
    protected double calculate(double error) {
        if (tuning.get()) {
            return 0.0; // can't call pidcalculator.calculate
        } else {
            return super.calculate(error);
        }
    }
}
