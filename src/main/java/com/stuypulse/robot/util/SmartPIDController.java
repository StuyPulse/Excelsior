/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.PIDCalculator;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

import java.util.function.Function;

/**
 * A PID Controller intended to go on SmartDashboard / Shuffleboard.
 *
 * <p>The PID gains exist on the network as well as an auto alignment mode.
 *
 * @author Myles Pasetsky
 */
public class SmartPIDController extends Controller {
    // private final String id;

    // PID Controller
    private final SmartNumber p;
    private final SmartNumber i;
    private final SmartNumber d;

    private final PIDController controller;

    // PID Calculator (Auto Tuner)
    private final SmartBoolean autoTuning;
    private final SmartNumber tuningSpeed;

    private final PIDCalculator calculator;
    private Function<PIDCalculator, PIDController> calculatorOutput;

    public SmartPIDController(String id) {
        /*this.*/ id = id.endsWith("/") ? id.substring(0, id.length() - 1) : id;

        // controller
        p = new SmartNumber(id + "/kP", 0.0);
        i = new SmartNumber(id + "/kI", 0.0);
        d = new SmartNumber(id + "/kD", 0.0);

        controller = new PIDController(p, i, d);

        // tuner
        autoTuning = new SmartBoolean(id + "/Auto Tuning?", false);
        tuningSpeed = new SmartNumber(id + "/Tune Speed", 0.0);

        calculator = new PIDCalculator(tuningSpeed);
        calculatorOutput = x -> x.getPIDController(); // make an enum for this?
    }

    @Override
    public String toString() {
        return autoTuning.get() ? calculator.toString() : controller.toString();
    }

    /*******************
     * CONFIGURE GAINS *
     *******************/

    public SmartPIDController setGains(Number p, Number i, Number d) {
        this.p.set(p);
        this.i.set(i);
        this.d.set(d);

        return this;
    }

    public SmartPIDController setTuneSpeed(Number speed) {
        this.tuningSpeed.set(speed);
        return this;
    }

    /**********************
     * CONFIGURE BEHAVIOR *
     **********************/

    public SmartPIDController setCalculatorOutput(Function<PIDCalculator, PIDController> method) {
        this.calculatorOutput = method;
        return this;
    }

    public PIDController getController() {
        return controller;
    }

    /********************
     * CALCULATE OUTPUT *
     ********************/

    @Override
    protected double calculate(double error) {
        if (autoTuning.get()) {
            // run bang-bang loop to find amplitude & calculate PID gains
            double output = calculator.update(error);

            // put tuned controller on smart dashboard
            PIDController tunedController = calculatorOutput.apply(calculator);
            p.set(tunedController.getP());
            i.set(tunedController.getI());
            d.set(tunedController.getD());

            return output;
        } else {
            // just use the controller
            return controller.update(error);
        }
    }
}
