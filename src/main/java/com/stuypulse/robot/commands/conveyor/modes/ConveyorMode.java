/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.conveyor.modes;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Conveyor.Direction;

import edu.wpi.first.wpilibj.DriverStation;

import java.util.function.Consumer;

public enum ConveyorMode {
    INDEX(
            (Conveyor conveyor) -> {
                /*** Gandalf logic ***/

                // Eject if you have wrong ball
                if (!ejectionless() && conveyor.hasOpponentBall()) {
                    conveyor.setGandalf(Direction.REVERSE);
                }

                // Stop if you already have ball
                else if (conveyor.getTopBeltHasBall()) {
                    if (conveyor.getColorSensorConnected()) {
                        conveyor.setGandalf(Direction.STOPPED);
                    } else {
                        conveyor.setGandalf(Direction.FORWARD);
                    }
                }

                // Accept Alliance Ball if no ball on top
                else if (ejectionless() || conveyor.hasAllianceBall()) {
                    conveyor.setGandalf(Direction.FORWARD);
                }

                // If you were ejecting and there is no longer a ball, stop
                else if (conveyor.getGandalfDirection() == Direction.REVERSE) {
                    conveyor.setGandalf(Direction.STOPPED);
                }

                /*** Top belt logic ***/

                // Stop if you already have ball
                if (conveyor.getTopBeltHasBall()) {
                    conveyor.setTopBelt(Direction.STOPPED);
                } else if (conveyor.hasAllianceBall()) {
                    conveyor.setTopBelt(Direction.FORWARD);
                }
            }),

    SHOOT(
            (Conveyor conveyor) -> {
                conveyor.setTopBelt(Direction.FORWARD);
                conveyor.setGandalf(
                        !ejectionless() && conveyor.hasOpponentBall()
                                ? Direction.REVERSE
                                : Direction.FORWARD);
            }),

    EJECT(
            (Conveyor conveyor) -> {
                conveyor.setGandalf(Direction.REVERSE);
                conveyor.setTopBelt(Direction.REVERSE);
            }),

    STOPPED(
            (Conveyor conveyor) -> {
                conveyor.setGandalf(Direction.STOPPED);
                conveyor.setTopBelt(Direction.STOPPED);
            }),

    DEFAULT(INDEX.method);

    private static boolean ejectionless() {
        return DriverStation.isAutonomous() || Settings.Conveyor.EJECTIONLESS.get();
    }

    private final Consumer<Conveyor> method;

    public void run(Conveyor conveyor) {
        method.accept(conveyor);
    }

    private ConveyorMode(Consumer<Conveyor> method) {
        this.method = method;
    }
}
