package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;

public class Shoot extends BaseAction {
    private final int startShots;
    private final int velocity;

    private int shotsLeft;
    private State state;
    private final ElapsedTime timer = new ElapsedTime();

    private boolean advancedThisCycle;

    private enum State {
        MOVE_TO_SHOOT_POS,
        SPIN_UP,
        FIRE_WAIT,
        ADVANCE,
        DONE
    }

    public Shoot(Robot robot, int numArtifacts, int velocity) {
        super(robot);
        this.startShots = numArtifacts;
        this.velocity = velocity;

        this.shotsLeft = numArtifacts;
        this.state = (numArtifacts <= 0) ? State.DONE : State.MOVE_TO_SHOOT_POS;
    }

    private void enter(State next) {
        state = next;
        timer.reset();

        if (next == State.ADVANCE) advancedThisCycle = false;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (isCancelled()) return false;

        if (state == State.DONE) {
            spindexer.shootPosition();
            return false;
        }

        switch (state) {
            case MOVE_TO_SHOOT_POS:
                spindexer.shootPosition();
                enter(State.SPIN_UP);
                break;

            case SPIN_UP:
                shooter.shoot(velocity);
                if (Math.abs(shooter.getVelocity() - velocity) <= robot.shooterTolerance) {
                    spindexer.flapUp();
                    enter(State.FIRE_WAIT);
                }
                break;

            case FIRE_WAIT:
                if (timer.milliseconds() >= 500) {
                    spindexer.flapDown();
                    enter(State.ADVANCE);
                }
                break;

            case ADVANCE:
                if (!advancedThisCycle) {
                    if (startShots == 2) {
                        spindexer.setDirection(DcMotor.Direction.REVERSE);
                        spindexer.moveToNextPosition();
                        spindexer.setDirection(DcMotor.Direction.FORWARD);
                    } else {
                        spindexer.moveToNextPosition();
                    }
                    advancedThisCycle = true;
                }

                int posError = Math.abs(spindexer.getCurrent() - spindexer.getTarget());
                if (posError < robot.tpr * 0.1) {
                    robot.artifactCount--;
                    shotsLeft--;

                    if (shotsLeft <= 0) enter(State.DONE);
                    else enter(State.SPIN_UP);
                }
                break;
        }

        telemetry.addData("Shoot State", state);
        return true;
    }

    @Override
    protected void onCancel() {
        spindexer.intakePosition();
        spindexer.flapDown();
    }
}