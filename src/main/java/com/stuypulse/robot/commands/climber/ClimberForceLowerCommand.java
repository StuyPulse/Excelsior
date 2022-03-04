/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.stuylib.util.StopWatch;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberForceLowerCommand extends CommandBase {

    private final Climber climber;
    private final StopWatch timer;

    public ClimberForceLowerCommand(Climber climber) {
        this.climber = climber;
        this.timer = new StopWatch();
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        if (climber.getLocked()) {
            timer.reset();
        }
    }

    @Override
    public void execute() {
        if (timer.getTime() < Settings.Climber.BRAKE_DELAY.get()) {
            climber.setUnlocked();
            climber.setMotorStop();
        } else {
            climber.forceLowerClimber();
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
        climber.setLocked();
    }
}
