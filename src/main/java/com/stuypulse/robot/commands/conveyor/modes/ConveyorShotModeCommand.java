package com.stuypulse.robot.commands.conveyor.modes;

import com.stuypulse.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ConveyorShotModeCommand extends InstantCommand {

    protected final ConveyorShotMode mode;
    protected final Conveyor conveyor;

    /** Creates a new ConveyorIndexCommand. */
    public ConveyorShotModeCommand(Conveyor conveyor, ConveyorShotMode mode) {
        this.conveyor = conveyor;
        this.mode = mode;

        addRequirements(conveyor);
    }

    @Override
    public final void initialize() {
        conveyor.setShotMode(this.mode);
    }
}
