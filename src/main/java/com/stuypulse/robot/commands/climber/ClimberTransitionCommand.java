/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberTransitionCommand extends CommandBase {
    
    private final Climber climber;

    private boolean leftHookFlipped;
    private boolean rightHookFlipped;

    public ClimberTransitionCommand(Climber climber) {
        this.climber = climber;

        leftHookFlipped = false;
        rightHookFlipped = false;

        addRequirements(climber);
    }

    @Override
    public void execute() {
        if (!leftHookFlipped || !rightHookFlipped) {
            climber.setMotor(-Settings.Climber.DEFAULT_SPEED.get());

            leftHookFlipped |= climber.getLeftHookFlipped();
            rightHookFlipped |= climber.getRightHookFlipped();
        } else {
            climber.setMotor(Settings.Climber.DEFAULT_SPEED.get());
        }
    }

    @Override
    public boolean isFinished() {
        return leftHookFlipped && rightHookFlipped && climber.getHooksSecure();
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
    }
}
