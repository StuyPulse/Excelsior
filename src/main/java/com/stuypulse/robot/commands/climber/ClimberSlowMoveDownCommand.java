package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.subsystems.Climber;

public class ClimberSlowMoveDownCommand extends ClimberMoveCommand {
    
    public ClimberSlowMoveDownCommand(Climber climber) {
        super(climber, Constants.ClimberSettings.CLIMBER_SLOW_SPEED, false);
    }
    
}
