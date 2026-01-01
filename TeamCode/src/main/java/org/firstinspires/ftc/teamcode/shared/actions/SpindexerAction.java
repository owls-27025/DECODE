package org.firstinspires.ftc.teamcode.shared.actions;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SpindexerAction extends BaseAction {

    private enum State {
        INTAKE_POS,
        SHOOT_POS
    }

    private State state = State.INTAKE_POS;

    private final boolean[] filled = new boolean[] { false, false, false };

    private final ElapsedTime flapTimer = new ElapsedTime();
    private final ElapsedTime ballCooldown = new ElapsedTime();
    private final ElapsedTime seekTimer = new ElapsedTime();

    private boolean lastBall = false;
    private boolean flapActive = false;

    private static final long INDEX_COOLDOWN_MS = 350;
    private static final long FLAP_UP_MS = 400;
    private static final long SEEK_COOLDOWN_MS = 120;

    public SpindexerAction(Robot robot) {
        super(robot);
        spindexer.intakePosition();
        flapTimer.reset();
        ballCooldown.reset();
        seekTimer.reset();
        syncCountFromSlots();
    }

    private void enter(State next) {
        if (state == next) return;

        state = next;
        flapActive = false;
        robot.spindexerReady = false;
        flapTimer.reset();

        switch (state) {
            case INTAKE_POS:
                spindexer.intakePosition();
                break;
            case SHOOT_POS:
                spindexer.shootPosition();
                break;
        }
    }

    private int pos() {
        return spindexer.findPosition();
    }

    private boolean atIntakePos(int p) { return (p % 2) == 1; }
    private boolean atShootPos(int p)  { return (p % 2) == 0; }

    private int intakeSlotIndex(int p) { return (p - 1) / 2; }
    private int shootSlotIndex(int p)  { return p / 2; }

    private int countFilled() {
        int c = 0;
        for (int i = 0; i < 3; i++) if (filled[i]) c++;
        return c;
    }

    private boolean anyFilled() {
        for (int i = 0; i < 3; i++) if (filled[i]) return true;
        return false;
    }

    private void syncCountFromSlots() {
        robot.artifactCount = countFilled();
    }

    private void clearAllSlots() {
        for (int i = 0; i < 3; i++) filled[i] = false;
        syncCountFromSlots();
    }

    private void markLoadedAtCurrentIntake() {
        int p = pos();
        if (!atIntakePos(p)) return;

        int slot = intakeSlotIndex(p);
        if (!filled[slot]) {
            filled[slot] = true;
            syncCountFromSlots();
        }
    }

    private void markShotAtCurrentShooter() {
        int p = pos();
        if (!atShootPos(p)) return;

        int slot = shootSlotIndex(p);
        filled[slot] = false;
        syncCountFromSlots();
    }

    @Override
    public boolean run(@NotNull TelemetryPacket packet) {
        if (robot.artifactCount <= 0 && anyFilled()) {
            clearAllSlots();
        }

        boolean ball = distance.isBall();
        boolean risingEdge = ball && !lastBall;
        lastBall = ball;

        boolean wantsShoot = robot.startShoot || robot.manualShoot;

        if (state == State.INTAKE_POS) {

            if (atIntakePos(pos())
                    && risingEdge
                    && ballCooldown.time(TimeUnit.MILLISECONDS) >= INDEX_COOLDOWN_MS) {

                if (countFilled() < 3) {
                    markLoadedAtCurrentIntake();
                    spindexer.moveToNextPosition();
                    ballCooldown.reset();
                }
            }

            if (robot.isHumanIntake && robot.humanLoadConfirm) {
                robot.humanLoadConfirm = false;

                if (atIntakePos(pos()) && countFilled() < 3) {
                    markLoadedAtCurrentIntake();
                    spindexer.moveToNextPosition();
                    ballCooldown.reset();
                }
            }

            if (wantsShoot && anyFilled()) {
                enter(State.SHOOT_POS);
            }

            if (countFilled() >= 3) {
                enter(State.SHOOT_POS);
            }
        }

        if (state == State.SHOOT_POS) {

            if (atShootPos(pos())) {
                robot.spindexerReady = Math.abs(spindexer.getCurrent() - spindexer.getTarget()) <= 10;
            } else {
                robot.spindexerReady = false;
            }

            if (wantsShoot && anyFilled() && !spindexer.isBusy()
                    && seekTimer.time(TimeUnit.MILLISECONDS) >= SEEK_COOLDOWN_MS
                    && atShootPos(pos())) {

                int slot = shootSlotIndex(pos());
                if (!filled[slot]) {
                    spindexer.moveToNextPosition();
                    seekTimer.reset();
                }
            }

            boolean canFire = robot.shooterReady && robot.spindexerReady;
            boolean hasAmmo = anyFilled();

            if (canFire && wantsShoot && hasAmmo && atShootPos(pos())) {
                int slot = shootSlotIndex(pos());
                if (filled[slot]) {
                    if (!flapActive) {
                        flapActive = true;
                        flapTimer.reset();
                        spindexer.flapUp();
                    }

                    if (flapTimer.time(TimeUnit.MILLISECONDS) >= FLAP_UP_MS) {
                        spindexer.flapDown();
                        markShotAtCurrentShooter();
                        spindexer.moveToNextPosition();

                        if (robot.manualShoot) {
                            robot.manualShoot = false;
                            robot.startShoot = false;
                        }

                        flapActive = false;
                        robot.spindexerReady = false;
                        ballCooldown.reset();
                    }
                }
            }

            if (!wantsShoot && countFilled() < 3) {
                enter(State.INTAKE_POS);
            }

            if (!anyFilled() && !robot.manualShoot) {
                robot.startShoot = false;
            }
        }

        dbg("Spindexer State", state);
        dbg("Slots", (filled[0] ? "1" : "0") + (filled[1] ? "1" : "0") + (filled[2] ? "1" : "0"));
        dbg("Artifacts", robot.artifactCount);

        return true;
    }
}
