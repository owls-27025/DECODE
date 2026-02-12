package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.OwlsOpMode;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;

public class ShooterSpinupStopwatch extends OwlsOpMode {
    private static final double TARGET_VELOCITY = 1050;

    private DcMotorEx shooter;
    private DcMotorEx shooter1;
    private final ElapsedTime stopwatch = new ElapsedTime();

    private boolean timingActive;
    private double lastSpinupTimeMs = -1;

    @Override
    protected void onStart() {
        shooter = hardwareMap.get(DcMotorEx.class, Robot.Configuration.shooter.itemName);
        shooter1 = hardwareMap.get(DcMotorEx.class, Robot.Configuration.shooter1.itemName);

        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter.setVelocity(0);
        shooter1.setVelocity(0);

        timingActive = false;
        lastSpinupTimeMs = -1;
    }

    @Override
    protected void runLoop() {
        if (!timingActive && p1.pressed(OwlsGamepad.Button.X)) {
            shooter.setVelocity(TARGET_VELOCITY);
            shooter1.setVelocity(TARGET_VELOCITY);
            stopwatch.reset();
            timingActive = true;
        }

        if (timingActive) {
            double shooterVelocity = Math.abs(shooter.getVelocity());
            double shooter1Velocity = Math.abs(shooter1.getVelocity());

            if (shooterVelocity >= TARGET_VELOCITY && shooter1Velocity >= TARGET_VELOCITY) {
                lastSpinupTimeMs = stopwatch.milliseconds();
                shooter.setVelocity(0);
                shooter1.setVelocity(0);
                timingActive = false;
            }
        }
    }

    @Override
    protected void telemetry() {
        telemetry.addLine("Press X on gamepad 1 to run a spin-up test.");
        telemetry.addData("Target velocity", TARGET_VELOCITY);
        telemetry.addData("Shooter velocity", Math.abs(shooter.getVelocity()));
        telemetry.addData("Shooter1 velocity", Math.abs(shooter1.getVelocity()));

        if (timingActive) {
            telemetry.addData("Stopwatch (ms)", stopwatch.milliseconds());
            telemetry.addLine("Timing in progress...");
        } else {
            telemetry.addLine("Waiting for X press...");
        }

        if (lastSpinupTimeMs >= 0) {
            telemetry.addData("Last time to target (ms)", lastSpinupTimeMs);
        }
    }

    @Override
    protected void onStop() {
        if (shooter != null) shooter.setVelocity(0);
        if (shooter1 != null) shooter1.setVelocity(0);
    }
}
