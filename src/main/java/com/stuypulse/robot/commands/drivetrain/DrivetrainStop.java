/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.IDrivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * command will prevent the drivetrain from moving,
 *
 * <p>mostly used just in case when climbing (prevent current being drawn from drivetrain)
 *
 * <p>will return to drivetrain drive command after
 */
public class DrivetrainStop extends CommandBase {
    private final IDrivetrain drivetrain;

    public DrivetrainStop(IDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
