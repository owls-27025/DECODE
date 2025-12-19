package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;

public class ShootAction extends BaseAction {
    private State state;
    private final ElapsedTime timer = new ElapsedTime();

    private enum State {
        SPIN_UP,
        READY,
        HUMAN_INTAKE
    }

    public ShootAction(Robot robot) {
        super(robot);
        enter(State.SPIN_UP);
    }

    private void enter(State next) {
        state = next;
        timer.reset();
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
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