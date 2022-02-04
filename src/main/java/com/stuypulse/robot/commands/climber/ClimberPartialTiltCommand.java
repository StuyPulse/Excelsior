package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import com.stuypulse.robot.subsystems.Climber;

public class ClimberPartialTiltCommand extends InstantCommand {

    private final Climber climber;

    public ClimberPartialTiltCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }  

    @Override
    public void initialize() {
        climber.setTilt(Climber.Tilt.PARTIAL_TILT);
    }
}
