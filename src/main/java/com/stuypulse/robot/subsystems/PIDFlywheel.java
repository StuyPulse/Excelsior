/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Simulation;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.math.SLMath;
import com.stuypulse.stuylib.util.StopWatch;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.ArrayList;
import java.util.List;

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

    /** SIMULATION **/
    // private FlywheelSim sim;
    private final List<FlywheelSim> sims;
	private final StopWatch timerSim;

    /** REAL **/
    private final StopWatch timerRPM;
    private double targetRPM;
    // private double lastTargetRPM;

    private final List<CANSparkMax> motors;
    private final List<RelativeEncoder> encoders;

    private final SimpleMotorFeedforward feedforward;
    private final Controller feedback;


    public PIDFlywheel(CANSparkMax motor, SimpleMotorFeedforward feedforward, Controller feedback) {
        this.feedforward = feedforward;
        this.feedback = feedback;
        
        this.sims = new ArrayList<>();
        timerSim  = new StopWatch();
        
        this.motors = new ArrayList<>();
        this.encoders = new ArrayList<>();
        addFollower(motor);

        timerRPM = new StopWatch();
        this.targetRPM = 0.0;
        // this.lastTargetRPM = 0.0;
    }

    public PIDFlywheel addFollower(CANSparkMax follower) {
        this.motors.add(follower);
        this.encoders.add(follower.getEncoder());
        
        /* if (RobotBase.isSimulation()) */
        sims.add(new FlywheelSim(
			LinearSystemId.identifyVelocitySystem(feedforward.kv, feedforward.ka),
            DCMotor.getNeo550(1),
            Simulation.Flywheel.GEARING
		));

        // sim = new FlywheelSim(
		// 	DCMotor.getNeo550(motors.size()),
		// 	Simulation.Flywheel.GEARING, 
		// 	Simulation.Flywheel.MOMENT_OF_INTERTIA
		// );
        
        return this;
    }

    public void stop() {
        setVelocity(0);
    }

    public void setVelocity(double targetRPM) {
        this.targetRPM = targetRPM;
    }

    private double getVelocity(RelativeEncoder encoder) {
        // simulation cannot override velocity, so RPM stored in encoder position
        // NOTE: this is not the only way to store simulated rpm, but USUALLY the encoder
        // can be overridden ( thanks rev :( )
        return RobotBase.isReal() ? encoder.getVelocity() : encoder.getPosition();
    }

    public double getVelocity() {
        double velocity = 0.0;

        for (RelativeEncoder encoder : this.encoders) {
            velocity += getVelocity(encoder);
        }

        return velocity / this.encoders.size();
    }

    private static double clamp(double voltage) {
        return SLMath.clamp(voltage, 0, 16);
    }

    @Override
    public void periodic() {
        // double ff = feedforward.calculate(this.lastTargetRPM, this.targetRPM, timerRPM.reset());
        // double fb = feedback.update(this.targetRPM, getVelocity());

        // for (CANSparkMax motor : this.motors) {
        //     motor.setVoltage(clamp(ff + fb));
        // }

        // this.lastTargetRPM = this.targetRPM;
    }

	@Override
	public void simulationPeriodic() {
        double ff = feedforward.calculate(this.targetRPM, 0);
        double fb = 0; // feedback.update(this.targetRPM, getVelocity());

        final double dt = timerSim.reset();
        for (int i = 0; i < motors.size(); ++i) {
            double volts = clamp(ff + fb);
            motors.get(i).setVoltage(volts);

            System.out.println("(" + i +") " + targetRPM + "-> " + volts);
			sims.get(i).setInputVoltage(volts);

			// advance simulation by dt
			sims.get(i).update(dt);

			// update sensors & battery
			double rpm = sims.get(i).getAngularVelocityRPM();

			// encoders velocity can't be set, use position to store velocity
			System.out.println("argh pee em:" + rpm);
            // this.encoders.get(i).setPosition(rpm);

			// update battery voltage
			RoboRioSim.setVInVoltage(
				BatterySim.calculateDefaultBatteryLoadedVoltage(sims.get(i).getCurrentDrawAmps()));
        }

        // this.lastTargetRPM = this.targetRPM;

		// for (int i = 0; i < sims.size(); i++) {
			// give simulation voltage from motor (set in periodic control loop)
			// double inputVoltage = motors.get(i).get() * RobotController.getBatteryVoltage();
            // System.out.println(i + " -> (" + motors.get(i).get()  + " * " + RobotController.getBatteryVoltage() + ")");
			// sims.get(i).setInputVoltage(inputVoltage);

			// // advance simulation by dt
			// sims.get(i).update(dt);

			// // update sensors & battery
			// double rpm = sims.get(i).getAngularVelocityRPM();

			// // encoders velocity can't be set, use position to store velocity
			// this.encoders.get(i).setPosition(rpm);

			// // update battery voltage
			// RoboRioSim.setVInVoltage(
			// 	BatterySim.calculateDefaultBatteryLoadedVoltage(sims.get(i).getCurrentDrawAmps()));
		// }


        // double inputVoltage = motors.get(0).getAppliedOutput();
        // sim.setInput(inputVoltage);

        // sim.update(0.020);

        // for (RelativeEncoder encoder : encoders) {
        //     encoder
        // }
    }
}
