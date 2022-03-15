/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;

/** @author Myles Pasetsky (@selym3) */
public class DrivetrainAlignToShootCommand extends DrivetrainAlignCommand {
    private final Conveyor conveyor;
    private final Debouncer emptied;

    public DrivetrainAlignToShootCommand(
            Drivetrain drivetrain, Conveyor conveyor, Number targetDistance) {
        super(drivetrain, targetDistance);

        this.conveyor = conveyor;
        this.emptied = new Debouncer(0.2, DebounceType.kRising);
        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
        super.initialize();
        emptied.calculate(false);
    }

    @Override
    public void execute() {
        if (shouldShoot()) {
            // mode is shoot until command ends
            conveyor.setMode(ConveyorMode.SHOOT);
        } else {
            super.execute(); // <-- align
        }
    }

    private boolean shouldShoot() {
        // the align command will finish when we are aligned
        return super.isFinished();
    }

    @Override
    public boolean isFinished() {
        return emptied.calculate(conveyor.isEmpty());
    }

    @Override
    public void end(boolean i) {
        super.end(i);
        conveyor.setMode(ConveyorMode.DEFAULT);
    }
}
