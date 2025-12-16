package org.firstinspires.ftc.teamcode.shared.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.Robot;

import java.util.List;

public class Limelight {
    private final Limelight3A limelight;

    public Limelight(Robot.Config config) {
        limelight = config.registerItem(Limelight3A.class, Robot.Config.limelight);
        if (limelight != null) {
            limelight.setPollRateHz(50);
            limelight.start();
        }
    }
    public boolean getMotif() {
        if (limelight == null) return false;

        LLResult result = limelight.getLatestResult();
        if (result == null) return false;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) return false;

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            if (id == 21) { Robot.Globals.motif = Robot.Globals.Colors.GPP; return true; }
            if (id == 22) { Robot.Globals.motif = Robot.Globals.Colors.PGP; return true; }
            if (id == 23) { Robot.Globals.motif = Robot.Globals.Colors.PPG; return true; }
        }
        return false;
    }

    public LLResult getLatestResult() {
        return limelight == null ? null : limelight.getLatestResult();
    }
}
