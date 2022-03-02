/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.stuylib.util.StopWatch;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberMoveCommand extends CommandBase {

    private final Climber climber;
    private final StopWatch timer;
    private final Number number;
    private final boolean movingUp;

    public ClimberMoveCommand(Climber climber, Number speed, boolean movingUp) {
        this.climber = climber;
        this.timer = new StopWatch();
        this.number = speed;
        this.movingUp = movingUp;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        timer.reset();
        climber.setClimberUnlocked();
    }

    @Override
    public void execute() {
        if (timer.getTime() < Settings.Climber.CLIMBER_DELAY.get()) {
            climber.setMotorStop();
        } else if (movingUp) {
            climber.setMotor(+this.number.doubleValue());
        } else {
            climber.setMotor(-this.number.doubleValue());
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
    }
}
