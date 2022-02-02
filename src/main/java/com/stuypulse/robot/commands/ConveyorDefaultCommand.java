// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Conveyor;
import com.stuypulse.stuylib.input.Gamepad;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ConveyorDefaultCommand extends CommandBase {
  /** Creates a new ConveyorDefaultCommand. */
  private final Conveyor conveyor;
  private final Gamepad gamepad;

  boolean ejecting;
  boolean running;

  public ConveyorDefaultCommand(Conveyor conveyor, Gamepad gamepad) {
    this.conveyor = conveyor;
    this.gamepad = gamepad;

    ejecting = false;
    running = false;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Eject if there is an enemy ball

    // At the beginning, we will set the booleans using the conveyor subsystems' methods
    // Next, we will use these boolean variables to determine what state we are in, and thus which motors
    // we will turn on and off
    /* The main states:
      * Ejecting the ball
        * Turn the Gandalf Motor the other way
      * Running (Shooting)
        * Run the top motor
        * Run the Gandalf Motor continuously
      * Stop
        * Turn both motors off
    */

    ejecting = 
      conveyor.getColorSensor().hasBall() && 
     !conveyor.getColorSensor().hasAllianceBall();

    // In here, DO NOT modify conveyor directly
    if (gamepad.getRawBottomButton()) { // same as 3rd?
      running = true;
    } else if (conveyor.getTopConveyorUpperHasBall()) { // top IR
      running = false; //all motors are stopped according to logic tabl
    } else if (conveyor.getColorSensor().hasAllianceBall()) { // good gap
      running = true;
    } else { // default
      running = false;
    } 
    
    // Gandalf
    if (ejecting) {
      conveyor.rejectBall();
    } else if (running) {
      conveyor.acceptBall();
    } else {
      conveyor.stopGandalf();
    }
    
    // Top Conveyor Motor
    if (running) {
      conveyor.spinTopBelt();
    } else {
      conveyor.stopTopBelt();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
