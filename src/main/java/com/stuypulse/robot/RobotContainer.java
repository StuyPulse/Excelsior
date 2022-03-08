/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.*;

import com.stuypulse.robot.commands.auton.*;
import com.stuypulse.robot.commands.climber.*;
import com.stuypulse.robot.commands.conveyor.*;
import com.stuypulse.robot.commands.drivetrain.*;
import com.stuypulse.robot.commands.intake.*;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.*;
import com.stuypulse.robot.constants.*;
import com.stuypulse.robot.subsystems.*;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;

public class RobotContainer {

    // Subsystems
    public final Climber climber = new Climber();
    public final ColorSensor colorSensor = new ColorSensor();
    public final Conveyor conveyor = new Conveyor(colorSensor);
    public final Drivetrain drivetrain = new Drivetrain();
    public final Intake intake = new Intake(conveyor);
    public final LEDController leds = new LEDController(this);
    public final Pump pump = new Pump();
    public final Shooter shooter = new Shooter();

    // Gamepads
    public final Gamepad driver = new AutoGamepad(Ports.Gamepad.DRIVER);
    public final Gamepad operator = new AutoGamepad(Ports.Gamepad.OPERATOR);

    // Autons
    private static SendableChooser<Command> autonChooser = new SendableChooser<>();

    public RobotContainer() {
        // Disable telemetry to reduce lag
        LiveWindow.disableAllTelemetry();
        DriverStation.silenceJoystickConnectionWarning(true);

        // Configure the button bindings
        configureDefaultCommands();
        configureButtonBindings();
        configureAutons();
    }

    /****************/
    /*** DEFAULTS ***/
    /****************/

    private void configureDefaultCommands() {
        drivetrain.setDefaultCommand(new DrivetrainDriveCommand(drivetrain, driver));
        conveyor.setDefaultCommand(new ConveyorIndexCommand(conveyor));
    }

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {
        /*** Climber ***/
        new Button(() -> -operator.getRightY() >= +0.75)
                .whileHeld(new ClimberMoveUpCommand(climber));
        new Button(() -> -operator.getRightY() <= -0.75)
                .whenPressed(new IntakeRetractCommand(intake))
                .whileHeld(new ClimberMoveDownCommand(climber));

        new Button(() -> operator.getLeftX() >= +0.75)
                .whenPressed(new ClimberMaxTiltCommand(climber));
        new Button(() -> operator.getLeftX() <= -0.75)
                .whenPressed(new ClimberNoTiltCommand(climber));

        operator.getSelectButton().whileHeld(new ClimberForceLowerCommand(climber));

        /*** Conveyor ***/
        operator.getTopButton().whileHeld(new ConveyorStopCommand(conveyor));
        operator.getLeftButton().whileHeld(new ConveyorForceEjectCommand(conveyor));

        /*** Drivetrain ***/
        driver.getBottomButton()
                .whileHeld(
                        new DrivetrainAlignCommand(
                                        drivetrain, Settings.Limelight.RING_SHOT_DISTANCE)
                                .perpetually())
                .whenReleased(new LEDSetCommand(leds, LEDColor.GREEN));

        driver.getDPadLeft()
                .whileHeld(
                        new DrivetrainTuneCommand.Angle(
                                drivetrain, Settings.Limelight.RING_SHOT_DISTANCE));
        driver.getDPadRight()
                .whileHeld(
                        new DrivetrainTuneCommand.Speed(
                                drivetrain, Settings.Limelight.RING_SHOT_DISTANCE));

        /*** Intake ***/
        operator.getRightTriggerButton()
                .whenPressed(new IntakeExtendCommand(intake))
                .whileHeld(new IntakeAcquireCommand(intake));

        operator.getRightBumper()
                .whenPressed(new IntakeExtendCommand(intake))
                .whileHeld(new IntakeAcquireCommand(intake))
                .whileHeld(new ConveyorForceIntakeCommand(conveyor));

        operator.getLeftTriggerButton().whileHeld(new IntakeDeacquireCommand(intake));

        operator.getDPadUp().whenPressed(new IntakeRetractCommand(intake));

        new Button(intake::getShouldRetract).whenPressed(new IntakeRetractCommand(intake));

        /*** Shooter ***/
        operator.getDPadLeft().whenPressed(new ShooterFenderShotCommand(shooter));
        operator.getDPadRight().whenPressed(new ShooterRingShotCommand(shooter));

        operator.getRightButton().whileHeld(new ConveyorShootCommand(conveyor).perpetually());

        operator.getLeftBumper().whenPressed(new ShooterStopCommand(shooter));
    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        // autonChooser.addOption("Do Nothing", new DoNothingAuton());

        autonChooser.addOption("0 Ball [TIMED]", new MobilityAuton.NoEncoders(this));
        // autonChooser.addOption("0 Ball [ENCODER]", new MobilityAuton.WithEncoders(this));
        // autonChooser.addOption("1 Ball", new OneBallAuton(this));
        autonChooser.addOption("2 Ball", new TwoBallAuton(this));
        autonChooser.addOption("4 Ball", new FourBallAuton(this));
        autonChooser.setDefaultOption("5 Ball", new FiveBallAuton(this));

        SmartDashboard.putData("Autonomous", autonChooser);
    }

    public Command getAutonomousCommand() {
        return autonChooser.getSelected();
    }
}
