package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ClimberReset extends InstantCommand {
    private final Climber climber;

    public ClimberReset(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        this.climber.reset();
    }
}
