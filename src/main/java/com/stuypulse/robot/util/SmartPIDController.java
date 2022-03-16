/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import com.stuypulse.stuylib.control.PIDCalculator;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

public class SmartPIDController extends PIDController {
    private static String join(String... strings) {
        return String.join("/", strings);
    }

    private final SmartBoolean tuningPID;
    private final SmartBoolean tuningPD;
    private final PIDCalculator calculator;

    public SmartPIDController(String id) {
        super(
                new SmartNumber(join(id, "P"), 0.0),
                new SmartNumber(join(id, "I"), 0.0),
                new SmartNumber(join(id, "D"), 0.0));

        tuningPID = new SmartBoolean(join(id, "Tuning PID"), false);
        tuningPD = new SmartBoolean(join(id, "Tuning PD"), false);
        calculator = new PIDCalculator(new SmartNumber(join(id, "Tuning Speed"), 1.0));
    }

    public SmartPIDController setControlSpeed(Number speed) {
        calculator.setControlSpeed(speed);
        return this;
    }

    @Override
    protected double calculate(double error) {
        if (tuningPID.get()) {
            tuningPD.set(false);
            PIDController result = calculator.getPIDController();
            setP(result.getP());
            setI(result.getI());
            setD(result.getD());
            return calculator.update(error);
        } else if (tuningPD.get()) {
            tuningPID.set(false);
            PIDController result = calculator.getPDController();
            setP(result.getP());
            setI(result.getI());
            setD(result.getD());
            return calculator.update(error);
        } else {
            return super.calculate(error);
        }
    }
}
