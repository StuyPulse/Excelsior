package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.constants.Settings.Alignment;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.control.PIDCalculator;
import com.stuypulse.stuylib.control.PIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivetrainTuneCommand {
    
    public static class Angle extends DrivetrainAlignCommand {
        public Angle(Drivetrain drivetrain, Number targetDistance) {
            super(drivetrain, targetDistance, new PIDCalculator(Alignment.Angle.BANG_BANG), Alignment.Speed.getController());
        }

        @Override
        public void execute() {
            super.execute();
            PIDCalculator angleCalculator = (PIDCalculator)angleController;
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
            super(drivetrain, targetDistance, Alignment.Angle.getController(), new PIDCalculator(Alignment.Speed.BANG_BANG));
        }

        @Override
        public void execute() {
            super.execute();
            PIDCalculator distanceCalculator = (PIDCalculator)distanceController;
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
