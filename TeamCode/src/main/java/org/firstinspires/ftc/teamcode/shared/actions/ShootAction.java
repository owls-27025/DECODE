package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;

public class ShootAction extends BaseAction {
    private State state;
    private final ElapsedTime timer = new ElapsedTime();

    private enum State {
        SPIN_UP,
        TARGETING,
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
            if (state != State.HUMAN_INTAKE) enter(State.HUMAN_INTAKE);
        } else {
            if (state == State.HUMAN_INTAKE) enter(State.SPIN_UP);
        }

        switch (state) {
            case SPIN_UP:
                robot.shooterReady = false;
                shooter.shoot(Robot.Globals.shooterVelocity);
                if (Math.abs(shooter.getVelocity() - Robot.Globals.shooterVelocity) <= Robot.Globals.shooterTolerance) {
                    enter(State.READY);
                }
                break;
            case TARGETING:
                robot.shooterReady = false;

            case READY:
                robot.shooterReady = true;
                shooter.shoot(Robot.Globals.shooterVelocity);
                if (Math.abs(shooter.getVelocity() - Robot.Globals.shooterVelocity) > Robot.Globals.shooterTolerance) {
                    enter(State.SPIN_UP);
                }
                break;

            case HUMAN_INTAKE:
                robot.shooterReady = false;
                shooter.shoot(-600);
                if (!robot.isHumanIntake) {
                    enter(State.SPIN_UP);
                }
                break;
        }

//        dbg("Shooter State", state);
//        dbg("Shooter Velocity", shooter.getVelocity());

        return true;
    }
}