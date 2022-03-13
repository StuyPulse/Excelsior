/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.stuylib.control.PIDCalculator;
import com.stuypulse.stuylib.control.PIDController;

import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivetrainTuneCommand {

    public static class Angle extends DrivetrainAlignCommand {
        public Angle(Drivetrain drivetrain, Number targetDistance) {
            super(
                    drivetrain,
                    targetDistance,
                    Alignment.Angle.getTuner(),
                    new PIDController()); // do not move during auto angle tuning
        }

        @Override
        public void execute() {
            super.execute();
            PIDCalculator angleCalculator = (PIDCalculator) angleController;
            PIDController angleOutput = angleCalculator.getPDController();

            SmartDashboard.putNumber("Drivetrain/Alignment/Angle/Tuned/P", angleOutput.getP());
            SmartDashboard.putNumber("Drivetrain/Alignment/Angle/Tuned/I", angleOutput.getI());
            SmartDashboard.putNumber("Drivetrain/Alignment/Angle/Tuned/D", angleOutput.getD());
        }

        public boolean isFinished() {
            return false;
        }
    }

    public static class Speed extends DrivetrainAlignCommand {
        public Speed(Drivetrain drivetrain, Number targetDistance) {
            super(
                    drivetrain,
                    targetDistance,
                    Alignment.Angle.getController(),
                    Alignment.Speed.getTuner());
        }

        @Override
        public void execute() {
            super.execute();
            PIDCalculator distanceCalculator = (PIDCalculator) distanceController;
            PIDController distanceOutput = distanceCalculator.getPDController();

            SmartDashboard.putNumber("Drivetrain/Alignment/Speed/Tuned/P", distanceOutput.getP());
            SmartDashboard.putNumber("Drivetrain/Alignment/Speed/Tuned/I", distanceOutput.getI());
            SmartDashboard.putNumber("Drivetrain/Alignment/Speed/Tuned/D", distanceOutput.getD());
        }

        public boolean isFinished() {
            return false;
        }
    }
}
