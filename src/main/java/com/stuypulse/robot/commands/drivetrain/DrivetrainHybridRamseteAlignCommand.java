/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.Target;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * @author Ivan Wei (ivanw8288@gmail.com)
 * 
 * A command group that allows direct switch from path following into target alignment
 * to avoid the extra period of time of alignment after completing a path.
 */
public class DrivetrainHybridRamseteAlignCommand extends CommandBase {
    private static final double SWAP_DISTANCE = Settings.Limelight.RING_SHOT_DISTANCE + 2 * Settings.Limelight.INTAKE_TO_LIMELIGHT;
    private final DrivetrainRamseteCommand ramseteCommand;
    private final DrivetrainAlignCommand alignCommand;
    private boolean switched;
    /**
     * Creates a Hybrid Ramsete Align command which immediately swaps from path following
     * to alignment as soon as a target is acquired.
     * @param alignmentTimeout time allowed for alignment
     */
    public DrivetrainHybridRamseteAlignCommand(Drivetrain drivetrain, DrivetrainRamseteCommand ramseteCommand, DrivetrainAlignCommand alignCommand, double alignmentTimeout) {
        
        this.ramseteCommand = ramseteCommand;
        this.alignCommand = alignCommand;

        switched = false;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        ramseteCommand.initialize();
    }

    @Override
    public void execute() {
        
        if (switched) {
            alignCommand.execute();
            return;
        }

        if(!switched && Target.getDistance() < SWAP_DISTANCE) {
            switched = true;
            alignCommand.initialize();
            return;
        }

        ramseteCommand.execute();
    }

    @Override
    public void end(boolean interrupted) {
        alignCommand.end(false);
        ramseteCommand.end(false);
    }
}
