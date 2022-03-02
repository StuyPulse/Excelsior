/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/*-
 * Climbs at end of match
 *
 * Contains:
 *      - Change tilt of climber
 *      - Move climber via motor
 *      - Different tilt angles
 *      - Encoder + Solenoid used for stopping
 *
 * @author independence106(Jason Zhou)
 * @author Ca7Ac1(Ayan Chowdhury)
 * @author marcjiang7(Marc Jiang)
 * @author ambers7(Amber Shen)
 * @author Souloutz(Howard Kong)
 * @author jiayuyan0501(Jiayu Yan)
 * @author ijiang05(Ian Jiang)
 * @author TraceyLin(Tracey Lin)
 * @author annazheng14(Anna Zheng)
 * @author lonelydot(Raymond Zhang)
 * @author andylin2004(Andy Lin)
 * @author hwang30git(Hui Wang)
 */
public class Climber extends SubsystemBase {

    public enum Tilt {
        MAX_TILT(Value.kReverse),
        NO_TILT(Value.kForward);

        private final Value extended;

        private Tilt(Value extended) {
            this.extended = extended;
        }
    }

    private final Encoder climberEncoder; 
    // encoder on neo sensor

    private final CANSparkMax climber;

    private final Solenoid stopper;

    private final DoubleSolenoid tilter;

    public Climber() {
        climber = new CANSparkMax(Ports.Climber.MOTOR, MotorType.kBrushless);
        Motors.CLIMBER.configure(climber);

        climberEncoder = new Encoder(Ports.Climber.ENCODER_SOURCE_A, Ports.Climber.ENCODER_SOURCE_B);
        climberEncoder.setDistancePerPulse(Settings.Climber.CLIMBER_ENCODER_RATIO);

        stopper = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Climber.STOPPER);

        if (Settings.Climber.ENABLE_TILT) {
            tilter =
                    new DoubleSolenoid(
                            PneumaticsModuleType.CTREPCM,
                            Ports.Climber.TILTER_FORWARD,
                            Ports.Climber.TILTER_REVERSE);
        } else {
            tilter = null;
        }
    }

    /*** MOTOR CONTROL ***/

    public void setMotor(double speed) {
        if (stopper.get()) {
            Settings.reportWarning("Climber attempted to run while lock was enabled!");
            setMotorStop();
        } else {
            climber.set(speed);
        }
    }

    public void setMotorStop() {
        climber.stopMotor();
    }

    /*** BRAKE CONTROL ***/

    public void setClimberLocked() {
        setMotorStop();
        stopper.set(true);
    }

    public void setClimberUnlocked() {
        stopper.set(false);
    }

    /*** TILE CONTROL ***/

    public void setTilt(Tilt tilt) {
        if (tilter != null) {
            tilter.set(tilt.extended);
        } else {
            Settings.reportWarning("Climber attempted to tilt while solenoids are disabled!");
        }
    }

    /*** ENCODER ***/

    public double getDistanceTraveled() {
        return climberEncoder.getDistance();
    }

    public void reset() {
        climberEncoder.reset();
    }

    /*** DEBUG INFORMATION ***/

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (Settings.DEBUG_MODE.get()) {
            if (tilter != null) {
                SmartDashboard.putBoolean(
                        "Debug/Climber/Max Tilt", tilter.get().equals(Value.kReverse));
            }
            SmartDashboard.putBoolean("Debug/Climber/Stopper Active", stopper.get());
            SmartDashboard.putNumber("Debug/Climber/Climber Speed", climber.get());
        }
    }
}
