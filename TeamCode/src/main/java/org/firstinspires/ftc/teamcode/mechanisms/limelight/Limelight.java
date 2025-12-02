package org.firstinspires.ftc.teamcode.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Configuration.ItemConfig;
import org.firstinspires.ftc.teamcode.helpers.BaseSubsystem;

import java.util.List;

public class Limelight extends BaseSubsystem {
    private Limelight3A limelight;

    public Limelight(HardwareMap hardwareMap,
                     Telemetry telemetry,
                     ItemConfig cfg) {
        super(hardwareMap, telemetry, cfg.itemActive);

        ifActive(() -> limelight = hardwareMap.get(Limelight3A.class, cfg.itemName));
    }

    public LLResult getResult() {
        return limelight.getLatestResult();
    }

    public int getMotif() {
        if (!active || limelight == null) return 0;

        LLResult result = limelight.getLatestResult();
        if (result == null) return 0;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) return 0;

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            if (id > 20 && id < 24) {
                return id;
            }
        }

        return 0;
    }

    public void updateRobotOrientation(double robotYaw) {
        limelight.updateRobotOrientation(robotYaw);
    }
}
