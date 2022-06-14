package com.stuypulse.robot.commands.conveyor;

import com.stuypulse.robot.commands.conveyor.modes.ConveyorMode;
import com.stuypulse.robot.commands.conveyor.modes.ConveyorSetMode;
import com.stuypulse.robot.subsystems.Conveyor;

public class ConveyorShootTop extends ConveyorSetMode {
    public ConveyorShootTop(Conveyor conveyor) {
        super(conveyor, ConveyorMode.SHOOT_TOP);
    }

    @Override
    public boolean isFinished() {
        return !conveyor.hasTopBeltBall();
    }
}
