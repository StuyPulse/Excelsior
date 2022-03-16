/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.control.Controller;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

/**
 * A utility class meant for controlling a flywheel system (shooter, feeder, etc.) by driving it to
 * a reference rotations per minute.
 *
 * <p>Stores a simple feedforward model of the shooter based on the voltage-balance equation and a
 * PID controller to correct for any error.
 *
 * @author Myles Pasetsky (@selym3)
 * @author Sam Belliveau (sam.belliveau@gmail.com)
 */
public class PIDFlywheel extends SubsystemBase {

    private double targetRPM;

    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    private final SimpleMotorFeedforward feedforward;
    private final Controller feedback;

    public PIDFlywheel(CANSparkMax motor, SimpleMotorFeedforward feedforward, Controller feedback) {
        this.motor = motor;
        this.encoder = motor.getEncoder();

        this.targetRPM = 0.0;

        this.feedforward = feedforward;
        this.feedback = feedback;
    }

    public PIDFlywheel addFollower(CANSparkMax follower, boolean inverted) {
        this.motor.follow(follower, inverted);
        return this;
    }

    public void stop() {
        setVelocity(0);
    }

    public void setVelocity(double targetRPM) {
        this.targetRPM = targetRPM;
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }

    public void periodic() {
        double ff = feedforward.calculate(this.targetRPM, 0);
        double fb = feedback.update(this.targetRPM, getVelocity());

        this.motor.setVoltage(ff + fb);
    }
}
