package com.stuypulse.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.streams.IFuser;

import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.Target;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;

public class DrivetrainAlignAngleCommand extends CommandBase {

    protected final Drivetrain drivetrain;

    private final Debouncer finished;

    private final IFuser angleError;
    private final Controller angleController;

    public DrivetrainAlignAngleCommand(Drivetrain drivetrain, double debounceTime) {
        this.drivetrain = drivetrain;

        // find angle errors
        angleError =
                new IFuser(
                        Alignment.FUSION_FILTER,
                        () -> Target.getXAngle().toDegrees(),
                        () -> drivetrain.getRawGyroAngle());

        // handle angle errors
        this.angleController = Alignment.Angle.getController();

        // finish optimally
        finished = new Debouncer(debounceTime, DebounceType.kRising);

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Target.enable();
        drivetrain.setLowGear();
     
        angleError.initialize();
    }

    protected final double getAngleError() {
        return angleError.get();
    }

    protected final double getTurn() {
        return angleController.update(getAngleError());
    }

    /** this method can be overridens to change how alignment runs */
    @Override
    public void execute() {
        drivetrain.arcadeDrive(0, getTurn());
    }

    @Override
    public void end(boolean interrupted) {
        Target.disable();
    }

    /** this method allows new considerations to be made for alignment */
    protected boolean isAlignmentDone() {
        return Target.hasTarget() && angleController.isDone(Limelight.MAX_ANGLE_ERROR.get());
    }

    @Override
    public boolean isFinished() {
        return finished.calculate(isAlignmentDone());
    }
}
 