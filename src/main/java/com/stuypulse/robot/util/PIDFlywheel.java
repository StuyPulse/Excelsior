package com.stuypulse.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;

public class PIDFlywheel /* extends SubsystemBase */ {
    
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    private final SmartPIDController pidController;
    private final SimpleMotorFeedforward feedforward;

    // private final SmartNumber setpoint; // rpm

    public PIDFlywheel(String id, CANSparkMax motor) {
        this.motor = motor;
        this.encoder = motor.getEncoder();

        pidController = new SmartPIDController("todo");
        feedforward = new SimpleMotorFeedforward(0, 0, 0);
    }

    public void periodic(double setpoint) {
        double ff = feedforward.calculate(setpoint, 0);
        double fb = pidController.update(setpoint, encoder.getVelocity());

        double output = fb + ff;

        motor.setVoltage(output);   
    }

}
