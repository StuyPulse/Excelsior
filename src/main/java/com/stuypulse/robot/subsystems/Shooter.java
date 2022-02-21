/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Settings.Shooter.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*-
 * Shoots balls out of the robot
 *
 * Contains:
 *      - Two shooter motors
 *          - One is a follower motor
 *      - One feeder motor
 *
 *      @author Vincent Lin
 *      @author Aharanish Dev
 *      @author Kevin Lio
 *      @author John Jay Wang
 *      @author Adeeb Khan
 *      @author Vicente Xia
 *      @author Julia Xue
 *      @author William Vongphanith
 *      @author Yuchen Pan
 *      @author Shaurya Sen
 *      @author Nicky Lin
 */
public class Shooter extends SubsystemBase {

    // private final SmartNumber targetRPM;

    // // Motors
    // private final CANSparkMax shooterMotor;
    // private final CANSparkMax shooterFollower;
    // private final CANSparkMax feederMotor;

    // // Encoders
    // private final RelativeEncoder shooterEncoder;
    // private final RelativeEncoder feederEncoder;

    // // PID
    // private final SparkMaxPIDController shooterPIDController;
    // private final SparkMaxPIDController feederPIDController;

    // // Hood Solenoid
    // private final Solenoid hoodSolenoid;

    public Shooter() {
        // // Network Table RPM Value
        // targetRPM = new SmartNumber("Shooter/Target", 0.0);

        // // Setup Motors
        // shooterMotor = new CANSparkMax(Ports.Shooter.LEFT_SHOOTER, MotorType.kBrushless);
        // shooterFollower = new CANSparkMax(Ports.Shooter.RIGHT_SHOOTER, MotorType.kBrushless);
        // feederMotor = new CANSparkMax(Ports.Shooter.FEEDER, MotorType.kBrushless);

        // shooterFollower.follow(shooterMotor, true);

        // shooterEncoder = shooterMotor.getEncoder();
        // feederEncoder = feederMotor.getEncoder();

        // shooterPIDController = shooterMotor.getPIDController();
        // feederPIDController = feederMotor.getPIDController();
        // configure();

        // Motors.Shooter.LEFT.configure(shooterMotor);
        // Motors.Shooter.RIGHT.configure(shooterFollower);
        // Motors.Shooter.FEEDER.configure(feederMotor);

        // // Setup Solenoid
        // hoodSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Shooter.HOOD_SOLENOID);
    }

    private void configure() {
        // shooterPIDController.setP(ShooterPID.kP);
        // shooterPIDController.setI(ShooterPID.kI);
        // shooterPIDController.setD(ShooterPID.kD);
        // shooterPIDController.setFF(ShooterPID.kF);

        // feederPIDController.setP(FeederPID.kP);
        // feederPIDController.setI(FeederPID.kI);
        // feederPIDController.setD(FeederPID.kD);
        // feederPIDController.setFF(FeederPID.kF);
    }

    /*** Speed Control ***/
    public void setShooterRPM(Number speed) {
        // targetRPM.set(speed);
    }

    /*** RPM Information ***/
    public double getShooterRPM() {
        // return Math.abs(shooterEncoder.getVelocity());
        return 0;
    }

    public double getFeederRPM() {
        // return Math.abs(feederEncoder.getVelocity());
        return 0;
    }

    /*** Hood Control ***/
    public void extendHood() {
        // hoodSolenoid.set(true);
    }

    public void retractHood() {
        // hoodSolenoid.set(false);
    }

    /*** Debug Information ***/
    @Override
    public void periodic() {
        // double rpm = targetRPM.get();

        // if (rpm < Settings.Shooter.MIN_RPM) {
        //     shooterPIDController.setReference(0, kDutyCycle);
        //     feederPIDController.setReference(0, kDutyCycle);
        // } else {
        //     double feederMultipler = Settings.Shooter.FEEDER_MULTIPLER.get();
        //     shooterPIDController.setReference(rpm, kVelocity);
        //     feederPIDController.setReference(rpm * feederMultipler, kVelocity);
        // }

        // if (Settings.DEBUG_MODE.get()) {
        //     SmartDashboard.putNumber("Debug/Shooter/Shooter RPM", getShooterRPM());
        //     SmartDashboard.putNumber("Debug/Shooter/Feeder RPM", getFeederRPM());
        // }
    }
}
