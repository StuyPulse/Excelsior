/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.TrajectoryLoader;

/**
 * Command for autonomously manuevering the drivetrain in a line
 *
 * <p>Useful for simpler autonomous routines
 *
 * @author Myles Pasetsky
 */
public class DrivetrainDriveDistance extends DrivetrainRamsete {

    public DrivetrainDriveDistance(Drivetrain drivetrain, double distance) {
        super(drivetrain, TrajectoryLoader.getLine(distance));
    }

    @Override
    public void initialize() {
        super.initialize();
        trajectory = trajectory.relativeTo(drivetrain.getPose());
    }
}
