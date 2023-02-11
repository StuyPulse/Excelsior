// /************************ PROJECT DORCAS ************************/
// /* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
// /* This work is licensed under the terms of the MIT license.    */
// /****************************************************************/

// package com.stuypulse.robot.commands.auton;

// import com.stuypulse.robot.RobotContainer;
// import com.stuypulse.robot.commands.conveyor.ConveyorShoot;
// import com.stuypulse.robot.commands.drivetrain.DrivetrainAlign;
// import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistance;
// import com.stuypulse.robot.commands.drivetrain.DrivetrainRamsete;
// import com.stuypulse.robot.commands.intake.IntakeAcquireForever;
// import com.stuypulse.robot.commands.intake.IntakeExtend;
// import com.stuypulse.robot.commands.intake.IntakeRetract;
// import com.stuypulse.robot.commands.leds.LEDSet;
// import com.stuypulse.robot.commands.shooter.ShooterRingShot;
// import com.stuypulse.robot.util.LEDColor;

// import edu.wpi.first.math.util.Units;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;

// /*-
//  * @author Vincent Wang (vinowang921@gmail.com)
//  * @author Ivan Wei (ivanw8288@gmail.com)
//  * @author Ivan Chen (ivanchen07@gmail.com)
//  * @author Eric Lin (ericlin071906@gmail.com)
//  * @author Marc Jiang (mjiang05@gmail.com)
//  * @author Ian Jiang (ijiang05@gmail.com)
//  * @author Carmin Vuong (carminvuong@gmail.com)
//  * @author Samuel Chen(samchen1738@gmail.com)
//  */
 
// public class FiveBallAuton extends SequentialCommandGroup {
//     // Time it takes for the shooter to reach the target speed
//     private static final double SHOOTER_INITIALIZE_DELAY = 0.3;
//     // Time it takes for the conveyor to give the shooter the ball
//     private static final double CONVEYOR_TO_SHOOTER = 3.0;
//     // Time we want to give the drivetrain to align
//     private static final double DRIVETRAIN_ALIGN_TIME = 3.0;

//     private static final String FIVE_BALL_TO_SECOND_BALL = "FiveBallAuton/output/FiveBallAcquireSecondBall.wpilib.json";
//     private static final String FIVE_BALL_TO_TERMINAL = "FiveBallAuton/output/FiveBallGetTerminalBalls.wpilib.json";
//     private static final String FIVE_BALL_TERMINAL_TO_SHOOT = "FiveBallAuton/output/FiveBallShootTerminalBalls.wpilib.json";
//     private static final String FIVE_BALL_TO_WALL_BALL = "FiveBallAuton/output/FiveBallGetWallBall.wpilib.json";

//     /** Creates a new FiveBallAuton. */
//     public FiveBallAuton(RobotContainer robot) {

//         // Starting up subsystems
//         addCommands(
//             new LEDSet(robot.leds, LEDColor.YELLOW),
//             new IntakeExtend(robot.intake),
//             new IntakeAcquireForever(robot.intake),
//             new ShooterRingShot(robot.shooter),
//             new WaitCommand(SHOOTER_INITIALIZE_DELAY)
//         );

//         // Tarmac to first ball
//         addCommands(
//             new LEDSet(robot.leds, LEDColor.GREEN),
//             new DrivetrainRamsete(robot.drivetrain, FIVE_BALL_TO_SECOND_BALL)
//                     .robotRelative());
//         addCommands(
//         );
//         addCommands(
//             new LEDSet(robot.leds, LEDColor.GREEN.pulse()),
//             new DrivetrainAlign(robot.drivetrain, robot.camera)
//                     .withTimeout(DRIVETRAIN_ALIGN_TIME)
//         );

//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.RAINBOW),
//                 new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

//         // First ball to terminal to RingShot
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.BLUE),
//                 new DrivetrainRamsete(robot.drivetrain, FIVE_BALL_TO_TERMINAL)
//                         .fieldRelative());

//         addCommands(
//                 new IntakeRetract(robot.intake),
//                 new WaitCommand(0.1694),
//                 new IntakeExtend(robot.intake), 

//                 new LEDSet(robot.leds, LEDColor.WHITE.pulse()));

//         // Return to Ring to shoot
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.PURPLE),
//                 new DrivetrainRamsete(robot.drivetrain, FIVE_BALL_TERMINAL_TO_SHOOT)
//                         .fieldRelative());
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.PURPLE.pulse()),
//                 new DrivetrainAlign(robot.drivetrain, robot.camera)
//                         .withTimeout(DRIVETRAIN_ALIGN_TIME)
//         );

//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.RAINBOW),
//                 new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

//         // Pick up and shoot fifth ball
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.PINK),
//                 new DrivetrainRamsete(robot.drivetrain, FIVE_BALL_TO_WALL_BALL)
//                         .fieldRelative());
//         addCommands(  
//                 new LEDSet(robot.leds, LEDColor.PINK.pulse()),
//                 new DrivetrainAlign(robot.drivetrain, robot.camera)
//                         .withTimeout(DRIVETRAIN_ALIGN_TIME)
//         );

//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.RAINBOW),
//                 new ConveyorShoot(robot.conveyor).withTimeout(CONVEYOR_TO_SHOOTER));

//         addCommands(new LEDSet(robot.leds, LEDColor.WHITE.pulse()));
//     }
// }
