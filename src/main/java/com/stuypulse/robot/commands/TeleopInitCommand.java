/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.subsystems.Climber.Tilt;
import com.stuypulse.robot.subsystems.Conveyor.Direction;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class TeleopInitCommand extends InstantCommand {

    private final RobotContainer robot;

    public TeleopInitCommand(RobotContainer robot) {
        this.robot = robot;

        addRequirements(robot.colorSensor);
        addRequirements(robot.intake);
        addRequirements(robot.climber);
        addRequirements(robot.conveyor);
    }

    @Override
    public void initialize() {
        robot.colorSensor.getUpdateFromDriverStation();

        robot.intake.stop();

        robot.climber.setTilt(Tilt.NO_TILT);

        robot.conveyor.setMode(ConveyorMode.DEFAULT);
        robot.conveyor.setTopBelt(Direction.STOPPED);
        robot.conveyor.setGandalf(Direction.STOPPED);
    }
}
