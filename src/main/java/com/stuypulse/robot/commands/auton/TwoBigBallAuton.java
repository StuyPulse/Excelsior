package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.intake.IntakeAcquireCommand;
import com.stuypulse.robot.commands.intake.IntakeAcquireForeverCommand;
import com.stuypulse.robot.commands.intake.IntakeExtendCommand;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.constants.Settings.LED;
import com.stuypulse.robot.util.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class TwoBigBallAuton extends SequentialCommandGroup {
    public static final String AQUIRE_SECOND_BALL  = "TwoBigBallAquireSecondBall.wpilib.json";
    public static final double START_DELAY = 1.0; //starting delay for the auton
    public static final double SHOOTER_INITIALIZE_DELAY = 1.0; // time we allow the shooter to get up to speed
    public static final double CONVEYOR_TO_SHOOTER_DELAY = 1.0; // time it takes for the ball to go up the conveyor
    public static final double DRIVETRAIN_ALIGN_TIME = 3.0; // time we allow for the robot align to the goal

    public TwoBigBallAuton(RobotContainer robot){
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.AQUA), new WaitCommand(START_DELAY)
        );

        //start up the subsystems
        addCommands(
            new IntakeExtendCommand(robot.intake),
            new IntakeAcquireForeverCommand(robot.intake),
            new ShooterRingShotCommand(robot.shooter),
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
            
        );

        // run the path, and shoot it 
        addCommands(
            new LEDset
        );
    }


}
