/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

/*-
 * A simple configuration class that can store:
 *  - If a motor is inverted
 *  - The Idle Mode of the motor
 *  - The Current limit
 *
 * @author Sam Belliveau
 */
public class MotorConfig {

    public final boolean INVERTED;
    public final IdleMode IDLE_MODE;
    public final int CURRENT_LIMIT_AMPS;

    public MotorConfig(boolean inverted, IdleMode idleMode, int currentLimitAmps) {
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
