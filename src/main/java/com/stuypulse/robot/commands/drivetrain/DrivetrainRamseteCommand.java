/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.Constants.DrivetrainSettings.Motion;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.TrajectoryLoader;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

public class DrivetrainRamseteCommand extends RamseteCommand {

    private boolean resetPosition;
    private Trajectory trajectory;
    private Drivetrain drivetrain;

    public DrivetrainRamseteCommand(Drivetrain drivetrain, Trajectory trajectory) {
        super(
                trajectory,
                drivetrain::getPose,
                new RamseteController(),
                Motion.MOTOR_FEED_FORWARD,
                Motion.KINEMATICS,
                drivetrain::getWheelSpeeds,
                new PIDController(Motion.PID.kP, Motion.PID.kI, Motion.PID.kD),
                new PIDController(Motion.PID.kP, Motion.PID.kI, Motion.PID.kD),
                drivetrain::tankDriveVolts,
                drivetrain);

        this.resetPosition = true;
        this.trajectory = trajectory;
        this.drivetrain = drivetrain;
    }

    public DrivetrainRamseteCommand(Drivetrain drivetrain, String path) {
        this(drivetrain, TrajectoryLoader.getTrajectory(path));
    }

    public DrivetrainRamseteCommand(Drivetrain drivetrain, String... paths) {
        this(drivetrain, TrajectoryLoader.getTrajectory(paths));
    }

    // [DEFAULT] Resets the drivetrain to the begining of the trajectory
    public DrivetrainRamseteCommand robotRelative() {
        this.resetPosition = true;
        return this;
    }

    // Make the trajectory relative to the field
    public DrivetrainRamseteCommand fieldRelative() {
        this.resetPosition = false;
        return this;
    }

    @Override
    public void initialize() {
        super.initialize();

        drivetrain.setHighGear();

        if (resetPosition) {
            drivetrain.reset(trajectory.getInitialPose());
        }
    }
}
