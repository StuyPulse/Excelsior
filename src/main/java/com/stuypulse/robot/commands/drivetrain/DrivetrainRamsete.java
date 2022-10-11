/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.constants.Settings.Drivetrain.Motion;
import com.stuypulse.robot.subsystems.IDrivetrain;
import com.stuypulse.robot.subsystems.IDrivetrain.Gear;
import com.stuypulse.robot.util.TrajectoryLoader;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

public class DrivetrainRamsete extends RamseteCommand {

    protected boolean resetPosition;
    protected Trajectory trajectory;
    protected IDrivetrain drivetrain;

    public DrivetrainRamsete(IDrivetrain drivetrain, Trajectory trajectory) {
        super(
                trajectory,
                drivetrain::getPose,
                new RamseteController(),
                Motion.KINEMATICS,
                drivetrain::tankDrive,
                drivetrain);

        this.resetPosition = true;
        this.trajectory = trajectory;
        this.drivetrain = drivetrain;
    }

    public DrivetrainRamsete(IDrivetrain drivetrain, String path) {
        this(drivetrain, TrajectoryLoader.getTrajectory(path));
    }

    public DrivetrainRamsete(IDrivetrain drivetrain, String... paths) {
        this(drivetrain, TrajectoryLoader.getTrajectory(paths));
    }

    // [DEFAULT] Resets the drivetrain to the begining of the trajectory
    public DrivetrainRamsete robotRelative() {
        this.resetPosition = true;
        return this;
    }

    // Make the trajectory relative to the field
    public DrivetrainRamsete fieldRelative() {
        this.resetPosition = false;
        return this;
    }

    @Override
    public void initialize() {
        super.initialize();

        drivetrain.setGear(Gear.HIGH);

        if (resetPosition) {
            drivetrain.reset(trajectory.getInitialPose());
        }
    }
}
