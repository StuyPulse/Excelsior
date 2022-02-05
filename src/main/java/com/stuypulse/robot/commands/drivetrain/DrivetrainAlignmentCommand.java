package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.Target;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainAlignmentCommand extends CommandBase {

    private final Drivetrain drivetrain;
    private final double targetDistance;

    public DrivetrainAlignmentCommand(Drivetrain drivetrain, double targetDistance) {
        this.drivetrain = drivetrain;
        this.targetDistance = targetDistance;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Target.enable();
    }

    private double getAngleError() {
        return Target.getXAngle().toDegrees();
    }
    
    private double getDistanceError() {
        return targetDistance - Target.getDistance();
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        Target.disable();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}
