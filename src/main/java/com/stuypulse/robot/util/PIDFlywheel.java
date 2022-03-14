package com.stuypulse.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.math.SLMath;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

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
public class PIDFlywheel /* extends SubsystemBase */ {
    
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    private final SmartPIDController pidController;
    private final SimpleMotorFeedforward feedforward;

    public PIDFlywheel(String id, CANSparkMax motor, SimpleMotorFeedforward feedforward) {
        this.motor = motor;
        this.encoder = motor.getEncoder();

        // configure base controllers
        pidController = new SmartPIDController(id);
            // .configure(controller -> {
            //     controller.setOutputFilter(x -> SLMath.clamp(x, 0, 1));
            // });

        // configure PID controller
        PIDController controller = pidController.getController();
        controller.setIntegratorFilter(new IntegratorFilter(
            controller, 
            Settings.Shooter.INTEGRAL_MAX_RPM_ERROR, 
            Settings.Shooter.INTEGRAL_MAX_ADJUST
        ));

        this.feedforward = feedforward;
    }

    public PIDFlywheel addFollower(CANSparkMax follower, boolean inverted) {
        this.motor.follow(follower, inverted);
        return this;
    }

    public double getVelocity() {
        return encoder.getVelocity(); // TODO: make sure this reads positive values?
    }

    private double getOutput(double setpoint) {
        double ff = feedforward.calculate(setpoint, 0);
        double fb = pidController.update(setpoint, getVelocity());

        return ff + fb;
    }

    public void periodic(double setpoint) {
        double output = getOutput(setpoint);
        motor.setVoltage(output);
    }

    public void stop() {
        motor.setVoltage(0.0);
    }

}
