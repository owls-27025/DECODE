package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.helpers.MecanumDrive;

@Autonomous
public class LimelightTestOpMode extends LinearOpMode {

    private Limelight3A limelight;

    private static final double SEARCH_ANGULAR_POWER = -0.5; // motor power, negative turns clockwise
    private static final double SEARCH_TIMEOUT_SEC = 5.0; // maximum time it'll rotate before timing out
    private static final long STARTUP_SETTLE_MS = 300; // waits for a bit
    private static final long SEARCH_SLEEP_MS = 20; // waits for a tiny bit
    private static final double SEARCH_ACCURACY = 1; // degree difference to target before it'll stop

    @Override
    public void runOpMode() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);

        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        limelight.start();

        sleep(STARTUP_SETTLE_MS);

        LLResult initial = limelight.getLatestResult();
        if (initial != null && initial.isValid()) {
            turnToTxZero(drive, initial);
            return;
        }

        double t0 = getRuntime();
        boolean found = false;

        while (opModeIsActive() && (getRuntime() - t0) < SEARCH_TIMEOUT_SEC) {
            drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), SEARCH_ANGULAR_POWER));

            LLResult res = limelight.getLatestResult();
            if (res != null && res.isValid()) {
                found = true;
                break;
            }

            telemetry.addData("Searching", "%.2fs", getRuntime() - t0);
            telemetry.update();
            sleep(SEARCH_SLEEP_MS);
        }

        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), 0));

        if (!found) {
            telemetry.addLine("No targets found (timeout).");
            telemetry.update();
            return;
        }

        LLResult lock = limelight.getLatestResult();
        if (lock != null && lock.isValid()) {
            while(opModeIsActive() && limelight.getLatestResult().isValid() && Math.abs(limelight.getLatestResult().getTx()) > SEARCH_ACCURACY) {
                LLResult result = limelight.getLatestResult();
                turnToTxZero(drive, result);
            }
        } else {
            telemetry.addLine("Lost target after search stop.");
            telemetry.update();
        }
    }

    private void turnToTxZero(MecanumDrive drive, LLResult res) {
        double txDeg = res.getTx();
        double turnRad = -Math.toRadians(txDeg);

        TrajectoryActionBuilder tab = drive.actionBuilder(drive.localizer.getPose());
        Actions.runBlocking(tab.turn(turnRad).build());

        sleep(SEARCH_SLEEP_MS);

        telemetry.addData("Turned (deg)", txDeg);
        telemetry.update();
    }
}
