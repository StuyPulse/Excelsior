/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.conveyor;

import com.stuypulse.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;

/*
 * @author Ivan Wei (ivanw8288@gmail.com)
 * @author Ivan Chen (ivanchen07@gmail.com)
 * @author Tony Chen (tchenpersonal50@gmail.com)
 * @author Gus Watkins (gus@guswatkins.net)
 * @author Kelvin Zhao (kzhao31@github)
 * @author Richie Xue (keobkeig/GlitchRich)
 * @author Rui Dong (ruidong0629@gmail.com)
 * @author Anthony Chen (achen318)
 * @author Joseph Mei (Gliese667Cc/SaggyTroy)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Carmin Vuong (carminvuong@gmail.com)
 * @author Jeff Chen (jeffc998866@gmail.com)
 * @author Sudipta Chakraborty (sudiptacc)
 * @author Andrew Che (andrewtheemerald@gmail.com)
 * @author Niki Chen (nikichen6769@gmail.com)
 * @author Vincent Wang (vinowang921@gmail.com)
 * @author Edmund Chin (edmundc421@gmail.com)
 */

public class ConveyorIndexCommand extends CommandBase {

    private final Conveyor conveyor;

    /** Creates a new ConveyorDefaultCommand. */
    public ConveyorIndexCommand(Conveyor conveyor) {
        this.conveyor = conveyor;
        addRequirements(conveyor);
    }

    @Override
    public void execute() {
        boolean ejecting = conveyor.getGandalfShouldEject();
        boolean running = conveyor.getBothShouldRun();

        // Gandalf
        if (ejecting) {
            conveyor.rejectBall();
        } else if (running) {
            conveyor.acceptBall();
        } else {
            conveyor.stopGandalf();
        }

        // Top Conveyor Motor
        if (running) {
            conveyor.spinTopBelt();
        } else {
            conveyor.stopTopBelt();
        }
    }
}
