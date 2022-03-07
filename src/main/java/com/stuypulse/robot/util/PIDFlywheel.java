package com.stuypulse.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class PIDFlywheel {
    
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    private final SmartPIDController pidController;

    public PIDFlywheel(CANSparkMax leader) {
    }

}
