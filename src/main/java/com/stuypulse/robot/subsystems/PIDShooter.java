package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.util.PIDFlywheel;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PIDShooter extends SubsystemBase {

    private final SmartNumber targetRPM;

    private final PIDFlywheel shooter;
    private final PIDFlywheel feeder;

    public PIDShooter() {
        targetRPM = new SmartNumber("Shooter/Target RPM", 0.0);
        
        // Setup shooter flywheel 
        CANSparkMax shooterMotor = new CANSparkMax(Ports.Shooter.LEFT_SHOOTER, MotorType.kBrushless);
        CANSparkMax shooterFollower = new CANSparkMax(Ports.Shooter.RIGHT_SHOOTER, MotorType.kBrushless);

        shooter = new PIDFlywheel("Debug/Shooter/Shooter Flywheel", shooterMotor, null)
            .addFollower(shooterFollower, true);

        // Setup feeder flywheel
        CANSparkMax feederMotor = new CANSparkMax(Ports.Shooter.FEEDER, MotorType.kBrushless);
        feeder = new PIDFlywheel("Debug/Shooter/Feeder Flywheel", feederMotor, null);
    }

    @Override
    public void periodic() {
        double setpoint = targetRPM.get();

        if (setpoint < Settings.Shooter.MIN_RPM) {
            shooter.stop();
            feeder.stop();
        } else {
            shooter.periodic(setpoint);
            feeder.periodic(setpoint * Settings.Shooter.FEEDER_MULTIPLER.get());
        }
    }
    
}
