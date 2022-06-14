package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** 
 * command will prevent the intake from moving,
 * 
 * mostly used just in case when climbing (prevent current being
 * drawn from intake)
 *
 * will return to button controls after
 */
public class IntakeStop extends CommandBase {
    private final Intake intake;

    public IntakeStop(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    public void execute() {
        intake.stop();
    }

    public boolean isFinished() {
        return false;
    }
}
