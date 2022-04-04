/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Climber;
import com.stuypulse.robot.subsystems.LEDController;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * pulls (active hook) climber up until passive hooks are 
 * above rung so that active hook can extend up to next rung
 */
public class ClimberPullUp extends CommandBase {
    
    private final Climber climber;
    private final LEDController ledController;

    private boolean leftHookFlipped;
    private boolean rightHookFlipped;

    public ClimberPullUp(Climber climber, LEDController ledController) {
        this.climber = climber;
        this.ledController = ledController;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        leftHookFlipped = false;
        rightHookFlipped = false;
    }

    @Override
    public void execute() {
        climber.setMotor(-Settings.Climber.DEFAULT_SPEED.get());

        leftHookFlipped |= climber.getLeftHookFlipped();
        rightHookFlipped |= climber.getRightHookFlipped();
    }

    @Override
    public boolean isFinished() {
        return leftHookFlipped && rightHookFlipped;
    }

    @Override
    public void end(boolean interrupted) {
        climber.setMotorStop();
        ledController.setColor(LEDColor.SINELON);
    }
}
