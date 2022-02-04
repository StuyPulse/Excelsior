package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import com.stuypulse.robot.subsystems.Climber;

public class ClimberNoTiltCommand extends InstantCommand {

    private final Climber climber;

    public ClimberNoTiltCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }
    
    @Override
    public void initialize() {
        climber.setTilt(Climber.Tilt.NO_TILT);
    }
}
