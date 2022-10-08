package com.stuypulse.robot.subsystems;

public abstract class IShooter extends SubsystemBase{
    public abstract void setShooterRPM(Number speed);
    public abstract void extendHood();
    public abstract void retractHood();
    public abstract double getShooterRPM();
    public abstract double getFeederRPM();
    public abstract boolean isFenderMode();
}
