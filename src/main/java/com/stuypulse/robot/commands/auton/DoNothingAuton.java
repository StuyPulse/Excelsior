/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/*-
 * This auton does nothing... it is used as a placeholder
 *
 * @author Sam Belliveau
 */
public class DoNothingAuton extends SequentialCommandGroup {

    public DoNothingAuton() {
        addCommands(
                /** Do a whole lot of nothing */
                );
    }
}
