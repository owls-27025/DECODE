package org.firstinspires.ftc.teamcode.shared.actions;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.shared.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.shared.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.shared.mechanisms.distance.Distance;
import org.firstinspires.ftc.teamcode.shared.mechanisms.intake.Intake;
import org.firstinspires.ftc.teamcode.shared.mechanisms.shooter.Shooter;
import org.firstinspires.ftc.teamcode.shared.mechanisms.spindexer.Spindexer;

public abstract class BaseAction implements com.acmerobotics.roadrunner.Action {
    protected final Robot robot;
    protected final Shooter shooter;
    protected final Spindexer spindexer;
    protected final Intake intake;
    protected final Light light;
    protected final Limelight limelight;
    protected final Drivetrain drivetrain;
    protected final Distance distance;

    private boolean cancelled = false;

    protected BaseAction(Robot robot) {
        this.robot = robot;
        this.shooter = robot.shooter;
        this.spindexer = robot.spindexer;
        this.intake = robot.intake;
        this.light = robot.light;
        this.limelight = robot.limelight;
        this.drivetrain = robot.drivetrain;
        this.distance = robot.distance;
    }

    /** Request cancellation. Next run() should return false. */
    public final void cancel() {
        if (!cancelled) {
            cancelled = true;
            onCancel();
        }
    }

    /** Cleanup hook (stop motors, reset servos). Override as needed. */
    protected void onCancel() {}

    protected final boolean isCancelled() {
        return cancelled;
    }

    /** Optional helper to call at top of run(). */
    protected final boolean stopIfCancelled(TelemetryPacket packet) {
        return !cancelled;
    }
}
