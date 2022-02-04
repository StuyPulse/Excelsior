package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DrivetrainLowGearCommand extends InstantCommand {

    private Drivetrain drivetrain;

    public DrivetrainLowGearCommand(Drivetrain drivetrain){
        this.drivetrain=drivetrain;
    }
    
    @Override
    public void initialize() {
        drivetrain.setLowGear();
    }

}
