package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistanceCommand;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.subsystems.LEDController.LEDColor;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OneBallAuton extends SequentialCommandGroup {

    // smart number so we can tweak it
    private final SmartNumber STARTUP_DELAY = new SmartNumber("Shooter/Startup Delay", 1.0);

    public OneBallAuton(RobotContainer robot, double distance) {
        
        // start shooter
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.WHITE_SOLID),

            // turn on shooter 
            new ShooterRingShotCommand(robot.shooter),
            
            // wait tad little for shooter to spin up
            new WaitCommand(STARTUP_DELAY.get())
        );

        // drive back and align
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.BLUE_SOLID),

            // drive backwards a tad little
            new DrivetrainDriveDistanceCommand(robot.drivetrain, distance)

            // align the robot to the target
            //** new DrivetrainAlignCommand(robot.drivetrain, Math.pow(23423.4243 * 10, Math.PI)), (stand in number)**
        );

        // shoot out the ball
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_SOLID),

            // turn on conveyor (turns off at the end of auton)
            new ConveyorShootCommand(robot.conveyor)
        );
    }
}
