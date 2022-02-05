package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveForeverCommand;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class MobilityAuton extends SequentialCommandGroup{
    public MobilityAuton(RobotContainer robot){
        addCommands(
            new DrivetrainDriveForeverCommand(robot.drivetrain, -1.0);
        );
    }

    
}
