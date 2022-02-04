package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakeAcquireCommand extends CommandBase {
	
	private final Intake intake;

	public IntakeAcquireCommand(Intake intake) {
		this.intake = intake;
		addRequirements(intake);
	}

	@Override
	public void execute() {
		intake.acquire();
	}

	@Override
	public void end(boolean interrupted) {
		intake.stop();
	}
}
