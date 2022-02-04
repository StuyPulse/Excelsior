package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.Constants.ShooterSettings;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class ShooterFenderShotCommand extends ParallelCommandGroup {

    public ShooterFenderShotCommand(Shooter shooter) {
        addCommands(new ShooterExtendHoodCommand(shooter));
        addCommands(new ShooterSetRPMCommand(shooter, ShooterSettings.FENDER_RPM));
    }
    
}