package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.subsystems.Climber;
import com.stuypulse.stuylib.control.BangBangController;
import com.stuypulse.stuylib.control.Controller;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberMoveToPositionCommand extends CommandBase {
    
    protected final Climber climber;
    protected double speed;

    private double position;

    private Controller controller;

    public ClimberMoveToPositionCommand(Climber climber, double speed, double position) {
        this.climber = climber;
        this.speed = speed;
        this.position = position;

        controller = new BangBangController(speed);

        addRequirements(climber);
    }
    
    private double getError() {
        return position - climber.getPosition();
    }
    
    @Override
    public void initialize() {}

    @Override
    public void execute() {
        climber.setMotor(controller.update(position, climber.getPosition()));
    }

    @Override
    public boolean isFinished() {
        return controller.isDone(getError());
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
    }
}
