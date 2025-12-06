package org.firstinspires.ftc.teamcode.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.helpers.Globals;

import java.util.List;

public class Limelight {
    public static Limelight3A limelight;

    public static void init(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(50);
        limelight.start();
    }

    public static boolean getMotif() {
        LLResult result = limelight.getLatestResult();

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (!fiducials.isEmpty()) {
            for (LLResultTypes.FiducialResult fiducial : fiducials) {
                int id = fiducial.getFiducialId();
                if (id == 21) {
                    Globals.motif = Globals.Colors.GPP;
                    return true;
                } else if (id == 22) {
                    Globals.motif = Globals.Colors.PGP;
                    return true;
                } else if (id == 23) {
                    Globals.motif = Globals.Colors.PPG;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
