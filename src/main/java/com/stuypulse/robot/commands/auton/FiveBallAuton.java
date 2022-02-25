/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainRamseteCommand;
import com.stuypulse.robot.commands.intake.IntakeAcquireForeverCommand;
import com.stuypulse.robot.commands.intake.IntakeExtendCommand;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.constants.Settings.Limelight;
import com.stuypulse.robot.subsystems.LEDController.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*-
 * @author Vincent Wang (vinowang921@gmail.com)
 * @author Ivan Wei (ivanw8288@gmail.com)
 * @author Ivan Chen (ivanchen07@gmail.com)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Marc Jiang (mjiang05@gmail.com)
 * @author Ian Jiang (ijiang05@gmail.com)
 * @author Carmin Vuong (carminvuong@gmail.com)
 * @author Samuel Chen(samchen1738@gmail.com)
 */
 
public class FiveBallAuton extends SequentialCommandGroup {
    // Time it takes for the intake to go down
    private static final double INTAKE_FALL_DOWN = 0.1;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 0.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 0.5;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 1.5;

    private static final double HUMAN_WAIT_TIME = 0.5;

    private static final String FIVE_BALL_TO_SECOND_BALL = "FiveBallAuton/output/FiveBallAcquireSecondBall.wpilib.json";
    private static final String FIVE_BALL_TO_TERMINAL = "FiveBallAuton/output/FiveBallGetTerminalBalls.wpilib.json";
    private static final String FIVE_BALL_TERMINAL_TO_SHOOT = "FiveBallAuton/output/FiveBallShootTerminalBalls.wpilib.json";
    private static final String FIVE_BALL_TO_WALL_BALL = "FiveBallAuton/output/FiveBallGetWallBall.wpilib.json";

    /** Creates a new FiveBallAuton. */
    public FiveBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.YELLOW_SOLID),
            new ShooterRingShotCommand(robot.shooter),
            new IntakeExtendCommand(robot.intake),
            new WaitCommand(INTAKE_FALL_DOWN),
            new IntakeAcquireForeverCommand(robot.intake),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        // Tarmac to first ball
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_SOLID),
            new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TO_SECOND_BALL)
                    .robotRelative());
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_PULSE),
            new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                    .withTimeout(DRIVETRAIN_ALIGN_TIME)
        );

        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        // First ball to terminal to RingShot
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.BLUE_SOLID),
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TO_TERMINAL)
                        .fieldRelative());

        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.WHITE_PULSE),
                new WaitCommand(HUMAN_WAIT_TIME));

        // Return to Ring to shoot
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.PURPLE_SOLID),
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TERMINAL_TO_SHOOT)
                        .fieldRelative());
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.PURPLE_PULSE),
                new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME)
        );

        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        // Pick up and shoot fifth ball
        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.PINK_SOLID),
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TO_WALL_BALL)
                        .fieldRelative());
        addCommands(  
                new LEDSetCommand(robot.leds, LEDColor.PINK_PULSE),
                new DrivetrainAlignCommand(robot.drivetrain, Limelight.RING_SHOT_DISTANCE)
                        .withTimeout(DRIVETRAIN_ALIGN_TIME)
        );

        addCommands(
                new LEDSetCommand(robot.leds, LEDColor.RAINBOW),
                new ConveyorShootCommand(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

        addCommands(new LEDSetCommand(robot.leds, LEDColor.WHITE_PULSE));
    }
}
