package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeExtendCommand extends InstantCommand {
	
	private final Intake intake;

	public IntakeExtendCommand(Intake intake) {
		this.intake = intake;
		addRequirements(intake);
	}

	@Override
	public void execute() {
		intake.extend();
	}
}