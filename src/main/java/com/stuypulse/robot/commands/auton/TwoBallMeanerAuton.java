// /************************ PROJECT DORCAS ************************/
// /* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
// /* This work is licensed under the terms of the MIT license.    */
// /****************************************************************/

// package com.stuypulse.robot.commands.auton;

// import com.stuypulse.robot.RobotContainer;
// import com.stuypulse.robot.commands.conveyor.ConveyorForceEject;
// import com.stuypulse.robot.commands.conveyor.ConveyorShoot;
// import com.stuypulse.robot.commands.drivetrain.DrivetrainAlign;
// import com.stuypulse.robot.commands.drivetrain.DrivetrainRamsete;
// import com.stuypulse.robot.commands.intake.IntakeAcquireForever;
// import com.stuypulse.robot.commands.intake.IntakeDeacquireForever;
// import com.stuypulse.robot.commands.intake.IntakeExtend;
// import com.stuypulse.robot.commands.leds.LEDSet;
// import com.stuypulse.robot.commands.shooter.ShooterRingShot;
// import com.stuypulse.robot.util.LEDColor;

// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;

// /*-
//  * @author Vincent Wang (vinowang921@gmail.com)
//  * @author Eric Lin (ericlin071906@gmail.com)
//  * @author Shaurya Sen (shauryasen12@gmail.com)
//  */

// public class TwoBallMeanerAuton extends SequentialCommandGroup {
//     // Time it takes for the shooter to reach the target speed
//     private static final double SHOOTER_INITIALIZE_DELAY = 0.7;
//     // Time it takes for the conveyor to give the shooter the ball
//     private static final double CONVEYOR_TO_SHOOTER = 1.0;
//     // Time we want to give the drivetrain to align
//     private static final double DRIVETRAIN_ALIGN_TIME = 3.0;
//     // Time it takes for the robot to deacquire two balls
//     private static final double INTAKE_DEACQUIRE_TIME = 0.75;

//     private static final String GET_SECOND_BALL = "TwoBallMeanerAuton/output/GetSecondBall.wpilib.json";
//     private static final String FIRST_OPPONENT_BALL = "TwoBallMeanerAuton/output/FirstOpponentBall.wpilib.json";
//     private static final String SETUP_FOR_LAST_BALL = "TwoBallMeanerAuton/output/SetupForLastBall.wpilib.json";
//     private static final String SECOND_OPPONENT_BALL = "TwoBallMeanerAuton/output/LastOpponentBall.wpilib.json";
//     private static final String FINAL_MOVEMENT = "TwoBallMeanerAuton/output/FinalMovement.wpilib.json";

//     public  TwoBallMeanerAuton(RobotContainer robot) {

//         // Starting up subsystems
//         addCommands(
//             new LEDSet(robot.leds, LEDColor.YELLOW),
//             new IntakeExtend(robot.intake),
//             new IntakeAcquireForever(robot.intake),
//             new ShooterRingShot(robot.shooter),
//             new WaitCommand(SHOOTER_INITIALIZE_DELAY)
//         );

//         // Shoot Two Balls
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.GREEN),
//                 new DrivetrainRamsete(robot.drivetrain, GET_SECOND_BALL)
//                         .robotRelative(),

//                 new LEDSet(robot.leds, LEDColor.GREEN.pulse()),
//                 new DrivetrainAlign(robot.drivetrain, robot.camera)
//                         .withTimeout(DRIVETRAIN_ALIGN_TIME),

//                 new LEDSet(robot.leds, LEDColor.RAINBOW),
//                 new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER)
//         );

//         // Get Wall Blue Ball
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.BLUE),
//                 new DrivetrainRamsete(robot.drivetrain, FIRST_OPPONENT_BALL)
//                         .fieldRelative());
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.PURPLE),
//                 new DrivetrainRamsete(robot.drivetrain, SETUP_FOR_LAST_BALL)
//                         .fieldRelative());
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.PINK),
//                 new DrivetrainRamsete(robot.drivetrain, SECOND_OPPONENT_BALL)
//                         .fieldRelative(),

//                 new IntakeDeacquireForever(robot.intake)
//                         .alongWith(new ConveyorForceEject(robot.conveyor))
//                         .withTimeout(INTAKE_DEACQUIRE_TIME)
//         );
                
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.AQUA),
//                 new DrivetrainRamsete(robot.drivetrain, FINAL_MOVEMENT)
//                         .fieldRelative());

//         addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
//     }
// }