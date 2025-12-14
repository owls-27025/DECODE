package org.firstinspires.ftc.teamcode.shared.actions;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;
import org.jetbrains.annotations.NotNull;

public class HumanPlayerIntake extends BaseAction {

    public HumanPlayerIntake(Robot robot) {
        super(robot);
    }

    @Override
    public boolean run(@NotNull TelemetryPacket telemetryPacket) {
        if (isCancelled()) return false;

        shooter.shoot(-600);
        return true;
    }
}
