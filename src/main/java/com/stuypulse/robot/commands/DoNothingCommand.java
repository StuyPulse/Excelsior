/************************ PROJECT DORCUS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

/*-
 * Most important command on robot
 *
 * @author Sam Belliveau
 * @author Myles Pasetsky
 * @author Nicky Lin
 * @author Ivan Wei
 * @author Vincent Wang
 * @author Jason Zhou
 */
public class DoNothingCommand extends CommandBase {

    /** Creates a new DoNothingCommand. */
    public DoNothingCommand() {
        // Use addRequirements() here to declare subsystem dependencies.
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
