package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveForeverCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class MobilityAuton extends SequentialCommandGroup{
    public MobilityAuton(RobotContainer robot){
        addCommands(
            new DrivetrainDriveForeverCommand(robot.drivetrain, -1.0).withTimeout(5)
        );
    }    
}