package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.stuypulse.robot.subsystems.Climber;

public class ClimberFullTiltCommand extends CommandBase {

    private final Climber climber;

    public ClimberFullTiltCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }
    
    @Override
    public void initialize() {}

    @Override
    public void execute() {
        climber.setTilt(Climber.Tilt.MAX_TILT);
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
