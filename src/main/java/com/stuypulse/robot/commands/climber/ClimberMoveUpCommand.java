/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.stuypulse.robot.subsystems.Climber;
import com.stuypulse.robot.Constants;

public class ClimberMoveUpCommand extends CommandBase {

    private final Climber climber;

    /** Creates a new DoNothingCommand. */
    public ClimberMoveUpCommand(Climber climber) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.climber = climber;
     
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        // we won't need this because we just routinely check to see if the top has been reached and set motor voltage to 1
    }

    @Override
    public void execute() {
        if (climber.getTopReached()) {
            climber.setMotorStop();
            climber.setClimberLocked();
        } else {
            climber.setClimberUnlocked();
            climber.setMotor(Constants.ClimberSettings.CLIMBER_DEFAULT_SPEED.get());
        }
    }


    public void end(boolean interrupted) {
        climber.setMotorStop();
        climber.setClimberLocked();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}