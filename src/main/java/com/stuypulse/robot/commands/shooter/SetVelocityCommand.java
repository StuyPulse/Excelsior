package com.stuypulse.robot.commands.shooter;

import com.stuypulse.robot.subsystems.Shooter;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetVelocityCommand extends InstantCommand {

    private final Shooter shooter;
    private SmartNumber targetRPM;
    
    public SetVelocityCommand(Shooter shooter, SmartNumber targetRPM) {
        this.shooter = shooter;
        this.targetRPM = targetRPM;
        addRequirements(shooter);
    }

    @Override
    public void initialize(){
        shooter.setShooterRPM(targetRPM);
    }

}
