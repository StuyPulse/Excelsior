package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.Constants.ShooterSettings;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class FenderShotCommand extends ParallelCommandGroup {

    public FenderShotCommand(Shooter shooter) {
        addCommands(new ExtendHoodCommand(shooter));
        addCommands(new SetVelocityCommand(shooter, ShooterSettings.FENDER_RPM));
    }
    
}