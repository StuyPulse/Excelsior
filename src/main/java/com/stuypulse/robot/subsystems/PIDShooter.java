package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Shooter;
import com.stuypulse.robot.util.PIDFlywheel;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Shooter subsystem for shooting balls out of the robot.
 * 
 * Uses feedforward to control two flywheels -- one for shooting
 * and one for feeding. This drives the wheels to a desired RPM.
 * 
 * Feedforward models (and feedback gains) are obtained through
 * system identification.  
 * 
 * Also contains an adjustable hood, which physically
 * allows for two shooting angles.
 * 
 * @author Myles Pasetsky (@selym3)
 */
public class PIDShooter extends SubsystemBase {

    private final SmartNumber targetRPM;

    private final PIDFlywheel shooter;
    private final PIDFlywheel feeder;

    private final Solenoid hood;

    public PIDShooter() {
        targetRPM = new SmartNumber("Shooter/Target RPM", 0.0);
        
        // Setup shooter flywheel 
        CANSparkMax shooterMotor = new CANSparkMax(Ports.Shooter.LEFT_SHOOTER, MotorType.kBrushless);
        CANSparkMax shooterFollower = new CANSparkMax(Ports.Shooter.RIGHT_SHOOTER, MotorType.kBrushless);

        Motors.Shooter.LEFT.configure(shooterMotor);
        Motors.Shooter.RIGHT.configure(shooterFollower);

        shooter = new PIDFlywheel(shooterMotor, Shooter.SHOOTER_FEED_FORWARD, Shooter.ShooterPID.getController());
        shooter.addFollower(shooterFollower, true);

        // Setup feeder flywheel
        CANSparkMax feederMotor = new CANSparkMax(Ports.Shooter.FEEDER, MotorType.kBrushless);
        Motors.Shooter.FEEDER.configure(feederMotor);

        feeder = new PIDFlywheel(feederMotor, Shooter.FEEDER_FEED_FORWARD, Settings.Shooter.FeederPID.getController());

        // Create hood solenoid
        hood = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Shooter.HOOD_SOLENOID);
    }

    public void setShooterRPM(Number speed) {
        targetRPM.set(speed);
    }

    public void extendHood() {
        hood.set(true);
    }

    public void retractHood() {
        hood.set(false);
    }

    public double getShooterRPM() {
        return Math.abs(shooter.getVelocity());
    }

    public double getFeederRPM() {
        return Math.abs(feeder.getVelocity());
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
