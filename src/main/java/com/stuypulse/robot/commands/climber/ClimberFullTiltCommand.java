package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import com.stuypulse.robot.subsystems.Climber;

public class ClimberFullTiltCommand extends InstantCommand {

    private final Climber climber;

    public ClimberFullTiltCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }  

    @Override
    public void initialize() {
        climber.setTilt(Climber.Tilt.MAX_TILT);
    }
}
