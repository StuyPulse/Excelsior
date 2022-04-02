/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Conveyor;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * A command to shoot while another command "is finished." This command can start up again and
 * continue running (shooting stops) so that it can be "finished" again.
 *
 * <p>The entire command is over when the robot has shot all its cargo while the command was
 * "finished"
 *
 * @author Sam Belliveau (sam.belliveau@gmail.com)
 * @author Myles Pasetsky (@selym3)
 */
public class ThenShoot extends CommandBase {

    private final Command aligner;
    private final Conveyor conveyor;

    private final Debouncer finished;

    public ThenShoot(Command aligner, Conveyor conveyor) {
        this.aligner = aligner;
        this.conveyor = conveyor;

        this.finished = new Debouncer(Settings.Conveyor.DEBOUNCE_TIME, DebounceType.kRising);

        m_requirements.addAll(this.aligner.getRequirements());
    }

    @Override
    public void initialize() {
        aligner.initialize();

        finished.calculate(false);
    }

    @Override
    public void execute() {
        aligner.execute();

        if (aligner.isFinished()) {
            conveyor.setMode(ConveyorMode.SHOOT);
        } else {
            conveyor.setMode(ConveyorMode.DEFAULT);
        }
    }

    @Override
    public boolean isFinished() {
        return aligner.isFinished() && finished.calculate(conveyor.isEmpty());
    }

    @Override
    public void end(boolean interrupted) {
        aligner.end(interrupted);
        conveyor.setMode(ConveyorMode.DEFAULT);
    }
}
