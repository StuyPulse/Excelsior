package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.subsystems.Climber;

import com.stuypulse.stuylib.control.BangBangController;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj2.command.CommandBase;


public class ClimberMoveDistanceCommand extends CommandBase {
    
    private final Climber climber;

    private BangBangController controller;

    private double relativeDistance;
    private double target;  

    public ClimberMoveDistanceCommand(Climber climber, Number relativeDistance) {
        this.climber = climber;
        this.relativeDistance = relativeDistance.doubleValue();
        controller = new BangBangController(Settings.Climber.SLOW_SPEED.get());

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        target = relativeDistance + climber.getPosition();
    }
    
    @Override
    public void execute() {
        climber.setMotor(controller.update(target, climber.getPosition()));
    }

    @Override
    public boolean isFinished() {
        return controller.isDone(target);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
    }
}
