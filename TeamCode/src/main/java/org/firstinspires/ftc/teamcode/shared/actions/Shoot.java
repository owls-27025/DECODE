package org.firstinspires.ftc.teamcode.shared.actions;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;

public class Shoot extends BaseAction {
    private final int velocity;
    private State state;
    private final ElapsedTime timer = new ElapsedTime();

    private boolean advancedThisCycle;

    private enum State {
        SPIN_UP,
        READY
    }

    public Shoot(Robot robot, int numArtifacts, int velocity) {
        super(robot);
        this.velocity = velocity;
    }

    private void enter(State next) {
        state = next;
        timer.reset();
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (isCancelled()) return false;

        switch (state) {
            case SPIN_UP:
                shooter.shoot(velocity);
                if (Math.abs(shooter.getVelocity() - velocity) <= Robot.Globals.shooterTolerance) {
                    enter(State.READY);
                }
                break;

            case READY:
                robot.shooterReady = true;
                break;


        }

        telemetry.addData("Shoot State", state);
        return true;
    }

}