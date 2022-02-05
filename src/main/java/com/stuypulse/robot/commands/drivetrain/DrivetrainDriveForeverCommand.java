package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainDriveForeverCommand extends CommandBase {
    private final Drivetrain drivetrain;
    private final double speed;
    public DrivetrainDriveForeverCommand(Drivetrain drivetrain, double speed){
        this.drivetrain = drivetrain;
        this.speed = speed;

        addRequirements(drivetrain);
    }
    @Override
    public void execute(){
        drivetrain.curvatureDrive(speed, 0);
    }
}