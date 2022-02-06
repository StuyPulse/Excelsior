package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.TrajectoryLoader;

/**
 * Command for autonomously manuevering the drivetrain in a line
 * 
 * Useful for simpler autonomous routines
 * 
 * @author Myles Pasetsky
 */
public class DrivetrainDriveDistanceCommand extends DrivetrainRamseteCommand {

    public DrivetrainDriveDistanceCommand(Drivetrain drivetrain, double distance) {
        super(drivetrain, TrajectoryLoader.getLine(distance));
    }

}