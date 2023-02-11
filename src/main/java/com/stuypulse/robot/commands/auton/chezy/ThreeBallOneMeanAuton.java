// package com.stuypulse.robot.commands.auton.chezy;

// import com.stuypulse.robot.RobotContainer;
// import com.stuypulse.robot.commands.conveyor.ConveyorForceEject;
// import com.stuypulse.robot.commands.conveyor.ConveyorShoot;
// import com.stuypulse.robot.commands.drivetrain.DrivetrainAlign;
// import com.stuypulse.robot.commands.drivetrain.DrivetrainRamsete;
// import com.stuypulse.robot.commands.intake.IntakeAcquireForever;
// import com.stuypulse.robot.commands.intake.IntakeDeacquireForever;
// import com.stuypulse.robot.commands.intake.IntakeDisableSafety;
// import com.stuypulse.robot.commands.intake.IntakeEnableSafety;
// import com.stuypulse.robot.commands.intake.IntakeExtend;
// import com.stuypulse.robot.commands.leds.LEDSet;
// import com.stuypulse.robot.commands.shooter.ShooterRingShot;
// import com.stuypulse.robot.util.LEDColor;

// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;

// public class ThreeBallOneMeanAuton extends SequentialCommandGroup {

//     private static final double ROBOT_STARTUP = 1;
//     private static final double DRIVETRAIN_ALIGN_TWO = 1;
//     private static final double SHOOT_TWO = 5;
//     private static final double INTAKE_DEACQUIRE_TIME = 3;

//     private static final String GET_FIRST_PATH = "TwoBallOneMean/output/GetFirstBall.wpilib.json";
//     private static final String GET_OPPONENT_PATH = "TwoBallOneMean/output/GetOpponent.wpilib.json";
//     private static final String DISCARD_OPPONENT_PATH = "TwoBallOneMean/output/DiscardOpponent.wpilib.json";
//     private static final String SETUP_TELEOP_PATH = "TwoBallOneMean/output/SetupTeleop.wpilib.json";

//     public ThreeBallOneMeanAuton(RobotContainer robot) {
        
//          // Starting up subsystems
//          addCommands(
//             new LEDSet(robot.leds, LEDColor.YELLOW),
//             new IntakeDisableSafety(robot.intake),
//             new IntakeExtend(robot.intake),
//             new IntakeAcquireForever(robot.intake),
//             new ShooterRingShot(robot.shooter),
//             new WaitCommand(ROBOT_STARTUP)
//         );

//         // Shoot Two Balls
//         addCommands(
//                 new LEDSet(robot.leds, LEDColor.GREEN),
//                 new DrivetrainRamsete(robot.drivetrain, GET_FIRST_PATH)
//                         .robotRelative(),
//                 new IntakeEnableSafety(robot.intake),
//                 new DrivetrainAlign(robot.drivetrain, robot.camera).withTimeout(DRIVETRAIN_ALIGN_TWO),
//                 new ConveyorShoot(robot.conveyor).withTimeout(SHOOT_TWO)
//         );

//         // get & discard opponent balls 
//         addCommands(
//             new LEDSet(robot.leds, LEDColor.RED),
//             new DrivetrainRamsete(robot.drivetrain, GET_OPPONENT_PATH).fieldRelative(),
//             new DrivetrainRamsete(robot.drivetrain, DISCARD_OPPONENT_PATH).fieldRelative(),
            
//             new IntakeDeacquireForever(robot.intake)
//                         .alongWith(new ConveyorForceEject(robot.conveyor))
//                         .withTimeout(INTAKE_DEACQUIRE_TIME)
//         );

//         // move for the start of teleop
//         addCommands(
//             new LEDSet(robot.leds, LEDColor.WHITE)
//             // new DrivetrainRamsete(robot.drivetrain, SETUP_TELEOP_PATH).fieldRelative()
//          );
//     }

// }
