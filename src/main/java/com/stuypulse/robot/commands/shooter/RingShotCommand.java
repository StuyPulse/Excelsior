package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.Constants.ShooterSettings;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RingShotCommand extends ParallelCommandGroup {

    public RingShotCommand(Shooter shooter) {
        addCommands(new RetractHoodCommand(shooter));
        addCommands(new SetVelocityCommand(shooter, ShooterSettings.RING_RPM));
    }
   
}
