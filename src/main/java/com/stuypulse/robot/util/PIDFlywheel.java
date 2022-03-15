package com.stuypulse.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.stuypulse.stuylib.control.Controller;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;

/**
 * A utility class meant for controlling a flywheel system
 * (shooter, feeder, etc.) by driving it to a reference 
 * rotations per minute.
 * 
 * Stores a simple feedforward model of the shooter based on the
 * voltage-balance equation and a PID controller to correct for 
 * any error. 
 * 
 * @author Myles Pasetsky (@selym3)
 * @author Sam Belliveau (sam.belliveau@gmail.com)
 */
public class PIDFlywheel /* extends SubsystemBase implements Sendable */ {
    
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    private final SimpleMotorFeedforward feedforward;
    private final Controller feedback;

    public PIDFlywheel(CANSparkMax motor, SimpleMotorFeedforward feedforward, Controller feedback) {
        this.motor = motor;
        this.encoder = motor.getEncoder();

        this.feedforward = feedforward;
        this.feedback = feedback;
    }

    public PIDFlywheel addFollower(CANSparkMax follower, boolean inverted) {
        this.motor.follow(follower, inverted);
        return this;
    }

    public double getVelocity() { // getMeasurement()
        return encoder.getVelocity(); // TODO: make sure this reads positive values?
    }

    private double getOutput(double setpoint) {
        double ff = feedforward.calculate(setpoint, 0); // 0 acceleration for now
        double fb = feedback.update(setpoint, getVelocity());

        return ff + fb;
    }

    public void periodic(double setpoint) {
        motor.setVoltage(getOutput(setpoint));
    }

    public void stop() {
        motor.setVoltage(0.0);
    }

}
