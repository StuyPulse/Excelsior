/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.conveyor.modes;

import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Conveyor.Direction;

import java.util.function.Consumer;

public enum ConveyorMode {
    INDEX(
            (Conveyor conveyor) -> {
                /*** Gandalf logic ***/

                // Eject if you have wrong ball
                if (conveyor.hasOpponentBall()) {
                    conveyor.setGandalf(Direction.REVERSE);
                }

                // Stop if you already have ball
                else if (conveyor.hasTopBeltBall()) {
                    conveyor.setGandalf(Direction.STOPPED);
                }

                // Accept Alliance Ball if no ball on top
                else if (conveyor.hasAllianceBall()) {
                    conveyor.setGandalf(Direction.FORWARD);
                }

                // If you were ejecting and there is no longer a ball, stop
                else if (conveyor.getGandalfDirection() == Direction.REVERSE) {
                    conveyor.setGandalf(Direction.STOPPED);
                }

                /*** Top belt logic ***/

                // Stop if you already have ball
                if (conveyor.hasTopBeltBall()) {
                    conveyor.setTopBelt(Direction.STOPPED);
                }

                // Run upwards if you have an alliance ball
                else if (conveyor.hasAllianceBall()) {
                    conveyor.setTopBelt(Direction.FORWARD);
                }

                // Stop Ejecting Once Done
                else if (conveyor.getTopBeltDirection() == Direction.REVERSE) {
                    conveyor.setTopBelt(Direction.STOPPED);
                }
            }),

    FORCE_INTAKE(
            (Conveyor conveyor) -> {
                conveyor.setTopBelt(
                        conveyor.hasTopBeltBall() ? Direction.STOPPED : Direction.FORWARD);
                conveyor.setGandalf(Direction.FORWARD);
            }),

    SHOOT(
            (Conveyor conveyor) -> {
                conveyor.setTopBelt(
                        conveyor.hasTopBeltBall() ? Direction.FORWARD_SLOW : Direction.FORWARD);
                conveyor.setGandalf(
                        conveyor.hasOpponentBall() ? Direction.REVERSE : Direction.FORWARD);
            }),

    SHOOT_SLOW(
            (Conveyor conveyor) -> {
                conveyor.setTopBelt(Direction.FORWARD_SLOW);
                conveyor.setGandalf(
                        conveyor.hasOpponentBall() ? Direction.REVERSE : Direction.FORWARD);
            }),
    
    SHOOT_TOP(
            (Conveyor conveyor) -> {
                conveyor.setTopBelt(Direction.FORWARD);
                conveyor.setGandalf(Direction.STOPPED);
            }),
            
    SEMI_AUTO(
            (Conveyor conveyor) -> {
                boolean shouldStop = conveyor.hasNewBall();
                conveyor.setTopBelt(shouldStop ? Direction.STOPPED : Direction.FORWARD_SLOW);
                conveyor.setGandalf(shouldStop ? Direction.STOPPED : Direction.FORWARD);
            }
        ),
    
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

    private final Consumer<Conveyor> method;

    public void run(Conveyor conveyor) {
        method.accept(conveyor);
    }

    private ConveyorMode(Consumer<Conveyor> method) {
        this.method = method;
    }
}
