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

        robot.isHumanIntake = true;
        shooter.shoot(-600);
        return true;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        robot.isHumanIntake = false;
    }
}
