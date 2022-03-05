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
import com.stuypulse.robot.commands.shooter.*;
import com.stuypulse.robot.constants.*;
import com.stuypulse.robot.subsystems.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
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

    private void configureDefaultCommands() {
        // TODO: ADD DEFAULT SUBSYSTEM COMMANDS
        drivetrain.setDefaultCommand(new DrivetrainDriveCommand(drivetrain, driver));
        conveyor.setDefaultCommand(new ConveyorIndexCommand(conveyor));
    }

    private void configureButtonBindings() {
        /***********************/
        /*** Climber Control ***/
        /***********************/

        new Button(() -> operator.getRightY() >= +0.75)
                .whileHeld(new ClimberMoveUpCommand(climber));
        new Button(() -> operator.getRightY() <= -0.75)
                .whenPressed(new IntakeRetractCommand(intake))
                .whileHeld(new ClimberMoveDownCommand(climber));

        new Button(() -> operator.getLeftX() >= +0.75)
                .whenPressed(new ClimberMaxTiltCommand(climber));
        new Button(() -> operator.getLeftX() <= -0.75)
                .whenPressed(new ClimberNoTiltCommand(climber));

        operator.getSelectButton().whileHeld(new ClimberForceLowerCommand(climber));

        operator.getBottomButton().whenPressed(new ClimberBrakeCommand(climber));

        /*************************/
        /*** Conveyor Control ***/
        /*************************/

        operator.getTopButton().whileHeld(new ConveyorStopCommand(conveyor));
        operator.getLeftButton().whileHeld(new ConveyorForceEjectCommand(conveyor));

        /**************************/
        /*** Drivetrain Control ***/
        /**************************/

        driver.getBottomButton()
                .whileHeld(
                        new DrivetrainAlignCommand(
                                        drivetrain, Settings.Limelight.RING_SHOT_DISTANCE)
                                .perpetually());

        driver.getDPadLeft()
                .whileHeld(
                        new DrivetrainTuneCommand.Angle(
                                drivetrain, Settings.Limelight.RING_SHOT_DISTANCE));
        driver.getDPadRight()
                .whileHeld(
                        new DrivetrainTuneCommand.Speed(
                                drivetrain, Settings.Limelight.RING_SHOT_DISTANCE));

        /**********************/
        /*** Intake Control ***/
        /**********************/

        operator.getRightTriggerButton()
                .whenPressed(new IntakeExtendCommand(intake))
                .whileHeld(new IntakeAcquireCommand(intake));

        operator.getRightBumper()
                .whenPressed(new IntakeExtendCommand(intake))
                .whileHeld(new IntakeAcquireCommand(intake))
                .whileHeld(new ConveyorForceIntakeCommand(conveyor));

        operator.getLeftTriggerButton().whileHeld(new IntakeDeacquireCommand(intake));

        operator.getDPadUp().whenPressed(new IntakeRetractCommand(intake));

        // new Button(conveyor::shouldRetractIntake).whenPressed(new IntakeRetractCommand(intake));

        /***********************/
        /*** Shooter Control ***/
        /***********************/

        operator.getDPadLeft().whenPressed(new ShooterFenderShotCommand(shooter));
        operator.getDPadRight().whenPressed(new ShooterRingShotCommand(shooter));

        operator.getRightButton().whileHeld(new ConveyorShootCommand(conveyor).perpetually());

        operator.getLeftBumper().whenPressed(new ShooterStopCommand(shooter));
    }

    public void configureAutons() {
        autonChooser.addOption("Do Nothing", new DoNothingAuton());

        autonChooser.addOption("No Encoders (moby)", new MobilityAuton.NoEncoders(this));
        autonChooser.setDefaultOption("With Encoders (moby)", new MobilityAuton.WithEncoders(this));
        autonChooser.addOption("One Ball", new OneBallAuton(this));
        autonChooser.addOption("Two Ball", new TwoBallAuton(this));
        autonChooser.addOption("Four Ball", new FourBallAuton(this));
        autonChooser.addOption("Five Ball", new FiveBallAuton(this));

        SmartDashboard.putData("Autonomous", autonChooser);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autonChooser.getSelected();
    }
}
