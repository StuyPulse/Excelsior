/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.util.Target;

import edu.wpi.first.math.trajectory.Trajectory;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * @author Ivan Wei (ivanw8288@gmail.com)
 * 
 * A command group that allows direct switch from path following into target alignment as soon as a target is acquired,
 * to avoid the extra period of time of alignment after completing a path.
 */
public class DrivetrainHybridRamseteAlignCommand extends SequentialCommandGroup {

    public DrivetrainHybridRamseteAlignCommand(Drivetrain drivetrain, Trajectory trajectory, Number targetDistance, double alignmentTimeout) {
        addCommands(
            new DrivetrainRamseteCommand(drivetrain, trajectory).withInterrupt(() -> Target.hasTarget()),
            new DrivetrainAlignCommand(drivetrain, targetDistance).withTimeout(alignmentTimeout)
        );
    }

    @Override
    public void initialize() {
        // Although AlignCommand will enable the limelight LED, we want it on before to check 
        // if a target enters range to run AlignCommand in the first place
        Target.enable();
    }

    @Override
    public void end(boolean interrupted) {
        Target.disable();
    }
}
