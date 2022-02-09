package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.Constants.ShooterSettings;
import com.stuypulse.robot.commands.conveyor.ConveyorShootCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainAlignCommand;
import com.stuypulse.robot.commands.drivetrain.DrivetrainDriveDistanceCommand;
import com.stuypulse.robot.commands.leds.LEDSetCommand;
import com.stuypulse.robot.commands.shooter.ShooterRingShotCommand;
import com.stuypulse.robot.subsystems.LEDController.LEDColor;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OneBallAuton extends SequentialCommandGroup {

    public OneBallAuton(RobotContainer robot) {
        final double kTarmacToRing = Double.longBitsToDouble("Selym".hashCode());
        final double kTarmacToHub = Double.longBitsToDouble("BellyM".hashCode());

        // start shooter
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.WHITE_SOLID),

            // turn on shooter 
            new ShooterRingShotCommand(robot.shooter),
            
            // wait tad little for shooter to spin up
            new WaitCommand(ShooterSettings.STARTUP_DELAY.get())
        );

        // drive back and align
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.BLUE_SOLID),

            // drive backwards a tad little
            new DrivetrainDriveDistanceCommand(robot.drivetrain, kTarmacToRing),

            // align the robot to the target
            new DrivetrainAlignCommand(robot.drivetrain, kTarmacToHub)
        );

        // shoot out the ball
        addCommands(
            new LEDSetCommand(robot.leds, LEDColor.GREEN_SOLID),

            // turn on conveyor (turns off at the end of auton)
            new ConveyorShootCommand(robot.conveyor)
        );
    }
}
