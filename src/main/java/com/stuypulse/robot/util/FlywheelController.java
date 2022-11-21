package com.stuypulse.robot.util;

import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.math.SLMath;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;

public class FlywheelController {

    private final PIDController controller;
    private final SimpleMotorFeedforward feedforward;

    private double output;

    public FlywheelController(Number kP, Number kI, Number kD, double kS, double kV, double kA) {
        controller = new PIDController(kP, kI, kD);
        feedforward = new SimpleMotorFeedforward(kS, kV, kA);
    }

    public double update(double setpoint, double measurement) {
        return output = SLMath.clamp(feedforward.calculate(setpoint) + controller.update(setpoint, measurement), 0, 16);
    }

    public double getOutput() {
        return output;
    }
}
