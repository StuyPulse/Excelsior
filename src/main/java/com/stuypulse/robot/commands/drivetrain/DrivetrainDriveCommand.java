package com.stuypulse.robot.commands.drivetrain;

import com.stuypulse.robot.Constants.DrivetrainSettings;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.math.SLMath;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.IFilterGroup;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainDriveCommand extends CommandBase {
    
    private Drivetrain drivetrain;
    private Gamepad driver;

    private IFilter speedFilter = new IFilterGroup(
        x -> SLMath.deadband(x, DrivetrainSettings.SPEED_DEADBAND.get()),
        x -> SLMath.spow(x, DrivetrainSettings.SPEED_POWER.get()),
        new LowPassFilter(DrivetrainSettings.SPEED_FILTER)    
    );
    
    private IFilter angleFilter = new IFilterGroup(
        x -> SLMath.deadband(x, DrivetrainSettings.ANGLE_DEADBAND.get()),
        x -> SLMath.spow(x, DrivetrainSettings.ANGLE_POWER.get()),
        new LowPassFilter(DrivetrainSettings.ANGLE_FILTER)    
    );

    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad driver) {
        this.drivetrain = drivetrain;
        this.driver = driver;
        
        addRequirements(drivetrain);
    }

    public void execute() {
        if (driver.getRawRightButton()) {
            drivetrain.setLowGear();
        } else {
            drivetrain.setHighGear();
        }

        double speed = driver.getRightTrigger() - driver.getLeftTrigger();
        double angle = driver.getLeftX();

        drivetrain.curvatureDrive(
            speedFilter.get(speed), 
            angleFilter.get(angle)
        );
    }

    public boolean isFinished() {
        return false;
    }

}
