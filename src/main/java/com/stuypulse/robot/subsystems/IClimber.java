package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IClimber extends SubsystemBase{
    public static enum Tilt {
        MAX_TILT, NO_TILT
    }
 
    public abstract void setMotor();
    public abstract void setMotorStop();

    public abstract boolean getBottomHeightLimitReached();
    public abstract boolean getTopHeightLimitReached();

    public abstract void forceLowerClimber();
    
    public abstract void setTilt(Tilt tilt);
    public abstract void setVoltage(double voltage);
}
