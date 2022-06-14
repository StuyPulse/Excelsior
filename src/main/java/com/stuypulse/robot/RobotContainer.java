/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.*;
import com.stuypulse.robot.commands.ShootAnywhere;
import com.stuypulse.robot.commands.auton.*;
import com.stuypulse.robot.commands.climber.*;
import com.stuypulse.robot.commands.conveyor.*;
import com.stuypulse.robot.commands.drivetrain.*;
import com.stuypulse.robot.commands.intake.*;
import com.stuypulse.robot.commands.shooter.*;
import com.stuypulse.robot.constants.*;
import com.stuypulse.robot.subsystems.*;

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

    public final Camera camera = new Camera(shooter);

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
        drivetrain.setDefaultCommand(new DrivetrainDrive(drivetrain, driver));
        conveyor.setDefaultCommand(new ConveyorIndex(conveyor));
    }

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {
        /*** Climber ***/
        operator.getRightStickDown()
                .whenPressed(new IntakeRetract(intake))
                .whenPressed(new ShooterStop(shooter))
                .whenPressed(new ShooterRetractHood(shooter))
                .whileHeld(new ConveyorStop(conveyor))
                .whileHeld(new IntakeStop(intake))
                .whileHeld(new ClimberMoveUp(climber));

        operator.getRightStickUp()
            .whenPressed(new IntakeRetract(intake))
            .whenPressed(new ShooterStop(shooter))
            .whenPressed(new ShooterRetractHood(shooter))
            .whileHeld(new ConveyorStop(conveyor))
            .whileHeld(new IntakeStop(intake))
            .whileHeld(new DrivetrainStop(drivetrain))
            .whileHeld(new ClimberMoveDown(climber));

        operator.getLeftStickRight().whenPressed(new ClimberMaxTilt(climber));
        operator.getLeftStickLeft().whenPressed(new ClimberNoTilt(climber));

        operator.getSelectButton().whileHeld(new ClimberForceLower(climber));

        /*** Conveyor ***/
        operator.getTopButton().whileHeld(new ConveyorStop(conveyor));
        operator.getLeftButton().whileHeld(new ConveyorForceEject(conveyor));

        /*** Drivetrain ***/
        driver.getLeftButton()
                .whileHeld(new ShooterFenderShot(shooter))
                .whileHeld(new ConveyorShootSemi(conveyor).perpetually()); 

        driver.getBottomButton()
                .whileHeld(new ShooterRingShot(shooter))
                .whileHeld(new DrivetrainAlign(drivetrain, camera).thenShoot(conveyor));

        driver.getLeftBumper()
                .whileHeld(new ShooterPadShot(shooter))
                .whileHeld(new DrivetrainPadAlign(drivetrain, camera).thenShoot(conveyor));

        //driver.getTopButton().whileHeld(new DrivetrainAlign(drivetrain, camera).perpetually());
        driver.getTopButton().whileHeld(new ShootAnywhere(this).perpetually());

        driver.getRightBumper()
            .whileHeld(new ShooterPadShot(shooter))
            .whileHeld(new DrivetrainPadAlignV2(drivetrain, camera).thenShoot(conveyor));

        /*** Intake ***/
        operator.getRightTriggerButton()
                .whenPressed(new IntakeExtend(intake))
                .whileHeld(new IntakeAcquire(intake))
                .whenReleased(new IntakeRetract(intake));

        operator.getRightBumper()
                .whenPressed(new IntakeExtend(intake))
                .whileHeld(new IntakeAcquire(intake))
                .whileHeld(new ConveyorForceIntake(conveyor))
                .whenReleased(new IntakeRetract(intake));

        operator.getLeftTriggerButton().whileHeld(new IntakeDeacquire(intake));

        operator.getDPadUp().whenPressed(new IntakeRetract(intake));

        /*** Shooter ***/
        operator.getDPadLeft().whenPressed(new ShooterFenderShot(shooter));
        operator.getDPadRight().whenPressed(new ShooterRingShot(shooter));
        operator.getDPadDown().whenPressed(new ShooterPadShot(shooter));

        operator.getRightButton().whileHeld(new ConveyorShoot(conveyor).perpetually());

        operator.getLeftBumper().whenPressed(new ShooterStop(shooter));
    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        // autonChooser.addOption("Do Nothing", new DoNothingAuton());

        autonChooser.addOption("0 Ball", new MobilityAuton.NoEncoders(this));
        // autonChooser.addOption("0 Ball [ENCODER]", new MobilityAuton.WithEncoders(this));
        // autonChooser.addOption("1 Ball", new OneBallAuton(this));
        autonChooser.addOption("2 Ball", new TwoBallAuton(this));
        // autonChooser.addOption("2 Ball Mean", new TwoBallMeanAuton(this));
        autonChooser.addOption("2 Ball Sam Mean", new TwoBallMeanerAuton(this));

        // autonChooser.addOption("4 Ball", new FourBallAuton(this));
        autonChooser.setDefaultOption("5 Ball [DEFAULT]", new FiveBallAuton(this));
        autonChooser.addOption("Partner Ball", new PartnerBallAuton(this));
        autonChooser.addOption("Two Ball One Mean", new TwoBallOneMeanAuton(this));
        autonChooser.addOption("Four Ball", new FourBallAuton(this));
        autonChooser.addOption("Blue Balls", new BlueFiveBallAuton(this));
        autonChooser.addOption("Mystery Ball", new ThreeBallMysteryAuton(this));


        SmartDashboard.putData("Autonomous", autonChooser);
    }

    public Command getAutonomousCommand() {
        // return autonChooser.getSelected();
        // return new FiveBallAuton(this);
        // return new TwoBallMeanerAuton(this);
        // return new PartnerBallAuton(this);
        return autonChooser.getSelected();
    }
}
