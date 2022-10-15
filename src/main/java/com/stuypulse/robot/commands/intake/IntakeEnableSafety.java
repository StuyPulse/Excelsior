package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeEnableSafety extends InstantCommand {

    public IntakeEnableSafety(Intake intake) {
        super(() -> intake.setIgnoreConveyor(false), intake);
    }
    
}
