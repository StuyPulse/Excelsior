package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Shooter.*;
import com.stuypulse.robot.util.FlywheelController;
import com.stuypulse.stuylib.network.SmartNumber;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;
import com.stuypulse.stuylib.streams.filters.TimedRateLimit;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SimpleShooter extends SubsystemBase {
    
    private final CANSparkMax left;
    private final CANSparkMax right;
    private final CANSparkMax feeder;

    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;
    private final RelativeEncoder feederEncoder;

    private final FlywheelController shooterController;
    private final FlywheelController feederController;

    private final Solenoid hood;

    private final SmartNumber targetRPM;
    private final IFilter targetFilter;

    public SimpleShooter() {
        left = new CANSparkMax(Ports.Shooter.LEFT, MotorType.kBrushless);
        right = new CANSparkMax(Ports.Shooter.RIGHT, MotorType.kBrushless);
        feeder = new CANSparkMax(Ports.Shooter.FEEDER, MotorType.kBrushless);

        leftEncoder = left.getEncoder();
        rightEncoder = right.getEncoder();
        feederEncoder = feeder.getEncoder();

        shooterController = new FlywheelController(ShooterPID.kP, ShooterPID.kI, ShooterPID.kD, ShooterFF.kS, ShooterFF.kV, ShooterFF.kA);
        feederController = new FlywheelController(FeederPID.kP, FeederPID.kI, FeederPID.kD, FeederFF.kS, FeederFF.kV, FeederFF.kA);

        hood = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Shooter.HOOD_SOLENOID);

        targetRPM = new SmartNumber("Shooter/Target RPM", 0.0);
        targetFilter = new TimedRateLimit(Settings.Shooter.MAX_TARGET_RPM_CHANGE)
                        .then(new LowPassFilter(Settings.Shooter.CHANGE_RC));

        Motors.Shooter.LEFT.configure(left);
        Motors.Shooter.RIGHT.configure(right);
        Motors.Shooter.FEEDER.configure(feeder);
    }

    // Shooter Control

    public void setShooterRPM(Number speed) {
        targetRPM.set(speed);
    }

    public void extendHood() {
        hood.set(true);
    }

    public void retractHood() {
        hood.set(false);
    }

    // Encoder Readings

    public double getShooterRPM() {
        return (leftEncoder.getVelocity() + rightEncoder.getVelocity()) / 2;
    }

    public double getFeederRPM() {
        return feederEncoder.getVelocity();
    }

    public boolean isFenderMode() {
        return hood.get();
    }

    // Target RPM Reading

    public double getRawTargetRPM() {
        return targetRPM.get();
    }

    public double getTargetRPM() {
        return targetFilter.get(getRawTargetRPM());
    }

    public boolean isReady() {
        return Math.abs(getShooterRPM() - getRawTargetRPM()) < Settings.Shooter.MAX_RPM_ERROR;
    }

    @Override
    public void periodic() {
        double setpoint = getTargetRPM();

        if (setpoint < Settings.Shooter.MIN_RPM) {
            left.stopMotor();
            right.stopMotor();
            feeder.stopMotor();
        } else {
            left.setVoltage(shooterController.update(setpoint, getShooterRPM()));
            right.setVoltage(shooterController.getOutput());
            feeder.setVoltage(feederController.update(setpoint, getFeederRPM()) * Settings.Shooter.FEEDER_MULTIPLER.get());
        }

        SmartDashboard.putNumber("Debug/Shooter/Shooter RPM", getShooterRPM());
        SmartDashboard.putNumber("Debug/Shooter/Feeder RPM", getFeederRPM());
        SmartDashboard.putNumber("Debug/Shooter/Shooter Controller", shooterController.getOutput());
        SmartDashboard.putNumber("Debug/Shooter/Feeder Controller", feederController.getOutput());
    }


}
