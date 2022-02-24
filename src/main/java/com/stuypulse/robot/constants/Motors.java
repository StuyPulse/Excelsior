/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

/*-
 * File containing all of the configurations that different motors require.
 *
 * Such configurations include:
 *  - If it is Inverted
 *  - The Idle Mode of the Motor
 *  - The Current Limit
 */
public interface Motors {

    Config CLIMBER = new Config(false, IdleMode.kBrake, 80);

    public interface Conveyor {
        Config GANDALF = new Config(true, IdleMode.kBrake, 60);
        Config TOP_BELT = new Config(false, IdleMode.kBrake, 60);
    }

    public interface Drivetrain {
        int CURRENT_LIMIT_AMPS = 60;
        IdleMode IDLE_MODE = IdleMode.kBrake;

        Config LEFT = new Config(true, IDLE_MODE, CURRENT_LIMIT_AMPS);
        Config RIGHT = new Config(false, IDLE_MODE, CURRENT_LIMIT_AMPS);

        boolean GRAYHILL_INVERTED = true;
    }

    Config INTAKE = new Config(true, IdleMode.kBrake, 60);

    public interface Shooter {
        Config LEFT = new Config(false, IdleMode.kCoast, 80);
        Config RIGHT = new Config(true, IdleMode.kCoast, 80);
        Config FEEDER = new Config(false, IdleMode.kCoast, 80);
    }

    /** Class to store all of the values a motor needs */
    public static class Config {
        public final boolean INVERTED;
        public final IdleMode IDLE_MODE;
        public final int CURRENT_LIMIT_AMPS;

        public Config(boolean inverted, IdleMode idleMode, int currentLimitAmps) {
            this.INVERTED = inverted;
            this.IDLE_MODE = idleMode;
            this.CURRENT_LIMIT_AMPS = currentLimitAmps;
        }

        public void configure(CANSparkMax motor) {
            motor.setInverted(INVERTED);
            motor.setIdleMode(IDLE_MODE);
            motor.setSmartCurrentLimit(CURRENT_LIMIT_AMPS);
            motor.burnFlash();
        }
    }
}
