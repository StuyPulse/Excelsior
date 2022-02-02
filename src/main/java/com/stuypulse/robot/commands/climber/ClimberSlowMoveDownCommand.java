package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.subsystems.Climber;

public class ClimberSlowMoveDownCommand extends CommandBase{
    private Climber climber;
    
    ClimberSlowMoveDownCommand(Climber climber){
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        if (climber.getBottomReached()) {
            climber.setClimberLocked();
        }
        else {
            climber.setClimberUnlocked();
            climber.setMotor(-Constants.ClimberSettings.CLIMBER_SLOW_SPEED.get());

        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
        climber.setClimberLocked();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
