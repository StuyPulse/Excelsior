/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.conveyor;

import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.robot.subsystems.Conveyor.Direction;

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

    public static class Ejectionless extends ConveyorIndexCommand {
        public Ejectionless(Conveyor conveyor) {
            super(conveyor, true);
        }
    }

    private final Conveyor conveyor;
    private final boolean ejectionless;

    /** Creates a new ConveyorDefaultCommand. */
    private ConveyorIndexCommand(Conveyor conveyor, boolean ejectionless) {
        this.conveyor = conveyor;
        this.ejectionless = ejectionless;

        addRequirements(conveyor);
    }

    @Override
    public void execute() {
        /*** Gandalf logic ***/

        // Eject if you have wrong ball
        if (!ejectionless && conveyor.hasOpponentBall()) { // Start eject
            conveyor.setGandalf(Direction.REVERSE);
        } 
        
        // Stop if you already have ball
        else if (conveyor.getTopBeltHasBall()) {
            conveyor.setGandalf(Direction.STOPPED);
        } 
        
        // Accept Alliance Ball if no ball on top
        else if (conveyor.hasAllianceBall()) {
            conveyor.setGandalf(Direction.FORWARD);
        } 
        
        // If you were ejecting and there is no longer a ball, stop
        else if (conveyor.getGandalfDirection() == Direction.REVERSE) { // End eject (we know the opponent ball is no longer there, and nothing else important is happening)
            conveyor.setGandalf(Direction.STOPPED);
        }


        /*** Top belt logic ***/

        // Stop if you already have ball
        if (conveyor.getTopBeltHasBall()) {
            conveyor.setTopBelt(Direction.STOPPED);
        } 
        
        // Accept Alliance Ball if no ball on top
        else if (conveyor.hasAllianceBall()) {
            conveyor.setTopBelt(Direction.FORWARD);
        }
    }
}
