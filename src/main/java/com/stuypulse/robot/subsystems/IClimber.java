package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IClimber extends SubsystemBase{
    public static enum Tilt {
        MAX_TILT, NO_TILT
    }
    public final void stop() {
        setVoltage(0);

    }
    
    public abstract void setTilt(Tilt tilt);
    public abstract void setVoltage(double voltage);
}
