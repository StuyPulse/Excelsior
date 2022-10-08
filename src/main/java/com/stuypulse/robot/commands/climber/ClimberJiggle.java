package com.stuypulse.robot.commands.climber;

import com.stuypulse.stuylib.util.StopWatch;
import com.stuypulse.robot.constants.Settings;

import com.stuypulse.robot.subsystems.IClimber;
import com.stuypulse.robot.subsystems.IClimber.Tilt;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberJiggle extends CommandBase {
    
    private final IClimber climber;
    private final com.stuypulse.stuylib.util.StopWatch timer;

    private final Number duration;

    public ClimberJiggle(IClimber climber) {
        this.climber = climber;
        timer = new StopWatch();
        this.duration = Settings.Climber.JIGGLE_TIME;
        addRequirements(climber);
    }

    public void initialize() {
        this.climber.setTilt(Tilt.NO_TILT);
        timer.reset();
    }

    public void execute() {
        this.climber.setTilt(Tilt.MAX_TILT);
    }

    public boolean isFinished() {
        return this.timer.getTime() > duration.doubleValue();
    }

    public void end(boolean interrupted) {
        climber.setTilt(Tilt.NO_TILT);
    }

}
