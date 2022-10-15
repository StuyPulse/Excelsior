package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeDisableSafety extends InstantCommand {
 
    public IntakeDisableSafety(Intake intake) {
        super(() -> intake.setIgnoreConveyor(true), intake);
    }

}
