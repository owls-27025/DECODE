package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.limelightvision.LLResultTypes;

import java.util.Arrays;

public class LimelightTest extends OwlsOpMode {
    @Override
    public void runLoop() {
        for (LLResultTypes.FiducialResult i : limelight.getLatestResult().getFiducialResults()) {
            telemetry.addData("Fiducial: ", i.getFiducialId());
        }
    }
}