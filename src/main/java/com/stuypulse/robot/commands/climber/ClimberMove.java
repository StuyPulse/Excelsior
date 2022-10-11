/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;


import com.stuypulse.robot.subsystems.IClimber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberMove extends CommandBase {

    private final IClimber climber;
    private final Number number;
    private final boolean movingUp;

    public ClimberMove(IClimber climber, Number speed, boolean movingUp) {
        this.climber = climber;
        this.number = speed;
        this.movingUp = movingUp;

        addRequirements(climber);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        if (movingUp) {
            climber.setMotor(+this.number.doubleValue());
        } else {
            climber.setMotor(-this.number.doubleValue());
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
    }

    @Override
    public boolean isFinished() {
        return (movingUp)
                ? climber.getTopHeightLimitReached()
                : climber.getBottomHeightLimitReached();
    }
}
