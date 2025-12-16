package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;

public class Shoot extends BaseAction {
    private State state;
    private final ElapsedTime timer = new ElapsedTime();

    private enum State {
        SPIN_UP,
        READY,
        HUMAN_INTAKE
    }

    public Shoot(Robot robot) {
        super(robot);
    }

    private void enter(State next) {
        state = next;
        timer.reset();
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (isCancelled()) return false;

        if (robot.isHumanIntake) {
            state = State.HUMAN_INTAKE;
        }

        switch (state) {
            case SPIN_UP:
                shooter.shoot(Robot.Globals.shooterVelocity);
                if (Math.abs(shooter.getVelocity() - Robot.Globals.shooterVelocity) <= Robot.Globals.shooterTolerance) {
                    enter(State.READY);
                }
                break;

            case READY:
                robot.shooterReady = true;
                break;
            case HUMAN_INTAKE:
                shooter.shoot(-600);
                break;
        }

        telemetry.addData("Shoot State", state);
        return true;
    }

}