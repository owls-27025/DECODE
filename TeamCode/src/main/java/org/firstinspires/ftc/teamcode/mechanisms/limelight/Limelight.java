package org.firstinspires.ftc.teamcode.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class Limelight {
    public Limelight3A limelight;

    public void init(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    public int getMotif() {
        int motif = 0;

        LLResult result = limelight.getLatestResult();

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId(); // The ID number of the fiducial
            if (id > 20 && id < 24) {
                motif = id;
                break;
            }
        }

        return motif;
    }
}
