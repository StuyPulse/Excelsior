/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;

/** @author Myles Pasetsky (@selym3) */
public class DrivetrainAlignToShootCommand extends DrivetrainAlignCommand {

    private final Conveyor conveyor;
    private final Debouncer emptied;

    public DrivetrainAlignToShootCommand(
            Drivetrain drivetrain, Conveyor conveyor, Number distance) {
        super(drivetrain, distance);

        this.conveyor = conveyor;
        this.emptied = new Debouncer(Settings.Conveyor.MANUAL_DEBOUNCE_TIME, DebounceType.kRising);

        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
        super.initialize();
        emptied.calculate(false);
    }

    @Override
    public void execute() {
        super.execute();

        if (super.isFinished()) {
            conveyor.setMode(ConveyorMode.SHOOT);
        } else {
            conveyor.setMode(ConveyorMode.DEFAULT);
        }
    }

    @Override
    public boolean isFinished() {
        return emptied.calculate(conveyor.isEmpty());
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        conveyor.setMode(ConveyorMode.DEFAULT);
    }
}
