package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShooterLaunchpadShotCommand extends SequentialCommandGroup {
    public ShooterLaunchpadShotCommand(Shooter shooter) {
        addCommands(new ShooterRetractHoodCommand(shooter));
        addCommands(new ShooterSetRPMCommand(shooter, Settings.Shooter.LAUNCHPAD_RPM));
    }
}
