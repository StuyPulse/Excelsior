/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.subsystems;

import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.network.limelight.Limelight;
import com.stuypulse.stuylib.network.limelight.Limelight.LEDMode;
import com.stuypulse.stuylib.util.StopWatch;
import com.stuypulse.robot.constants.Field;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Field.Hub;
import com.stuypulse.robot.util.ComputerVisionUtil;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.net.PortForwarder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {

    private final Shooter shooter;
    private final Drivetrain drivetrain;

    private final Limelight limelight;

    private final StopWatch timer;

    public Camera(Shooter shooter, Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.shooter = shooter;
        this.limelight = Limelight.getInstance();

        this.timer = new StopWatch();

        for (int port : Settings.Limelight.PORTS) {
            PortForwarder.add(port, "limelight.local", port);
        }

        CameraServer.startAutomaticCapture();
        // CameraServer.startAutomaticCapture("Intake Camera", 0);
        // CameraServer.startAutomaticCapture("Intake Camera", 1);
    }

    /*
     * double cameraHeightMeters,
     * double targetHeightMeters,
     * double cameraPitchRadians,
     * double targetPitchRadians,
     * Rotation2d targetYaw,
     * Rotation2d gyroAngle,
     * Pose2d fieldToTarget,
     * Transform2d cameraToRobot
     */

    public Pose2d getRobotPose() {
        return ComputerVisionUtil.estimateFieldToRobot(
                Settings.Limelight.LIMELIGHT_HEIGHT,
                Hub.HEIGHT,
                Math.toRadians(Settings.Limelight.LIMELIGHT_PITCH.get()),
                Field.Hub.PITCH.toRadians(),
                getXAngle().getRotation2d(),
                drivetrain.getAngle().getRotation2d(),
                Field.Hub.LOCATION,
                Settings.Limelight.TO_ROBOT);
    }
    // Translation2d fieldPos = new Translation2d(getDistance(), new
    // Rotation2d(getXAngle().toRadians());

    /*** Has Target ***/
    public boolean hasAnyTarget() {
        return limelight.getValidTarget();
    }

    public boolean hasTarget() {
        return hasAnyTarget()
                && shooter.isReady()
                && Settings.Limelight.MIN_VALID_DISTANCE < getDistance()
                && getDistance() < Settings.Limelight.MAX_VALID_DISTANCE;
    }

    /*** Angles ***/
    public Angle getXAngle() {
        if (!hasAnyTarget()) {
            Settings.reportWarning("Unable To Find Target! [getXAngle() was called]");
            return Angle.kZero;
        }

        return Angle.fromDegrees(
                limelight.getTargetXAngle() + Settings.Limelight.LIMELIGHT_YAW.get());
    }

    public Angle getYAngle() {
        if (!hasAnyTarget()) {
            Settings.reportWarning("Unable To Find Target! [getYAngle() was called]");
            return Angle.kZero;
        }

        return Angle.fromDegrees(
                limelight.getTargetYAngle() + Settings.Limelight.LIMELIGHT_PITCH.get());
    }

    /*** Distance ***/

    /** returns distance from intake to center of hub */
    public double getDistance() {
        if (!hasAnyTarget()) {
            Settings.reportWarning("Unable To Find Target! [getDistance() was called]");
            return Settings.Limelight.RING_DISTANCE.get();
        }

        return Settings.Limelight.CENTER_TO_HUB +
                Settings.Limelight.LIMELIGHT_TO_INTAKE +
                Settings.Limelight.HEIGHT_DIFFERENCE / getYAngle().tan(); // distance from edge of goal to limelight
    }

    /*** Periodic ***/

    @Override
    public void periodic() {
        // Disconnect warnings
        if (!limelight.isConnected()) {
            Settings.reportWarning("Limelight Disconnected!");
        }

        // Robot odometry
        if (hasAnyTarget()) {
            drivetrain.updateOdometry(getRobotPose(), timer.reset());
        }

        // LED Mode
        if (DriverStation.isDisabled()) {
            limelight.setLEDMode(LEDMode.PIPELINE);
        } else if (shooter.isFenderMode()) {
            limelight.setLEDMode(LEDMode.FORCE_OFF);
        } else {
            limelight.setLEDMode(LEDMode.FORCE_ON);
        }
    }
}