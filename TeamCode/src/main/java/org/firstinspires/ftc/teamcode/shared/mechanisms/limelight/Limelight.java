package org.firstinspires.ftc.teamcode.shared.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.Robot;

import java.util.List;

public class Limelight {
    private final Limelight3A limelight;
    private final Robot robot;

    public Limelight(Robot robot) {
        this.robot = robot;
        limelight = robot.registerItem(Limelight3A.class, robot.config.limelight);
        if (limelight != null) {
            limelight.setPollRateHz(50);
            limelight.start();
        }
    }

    /** Updates robot.motif based on fiducial IDs 21/22/23. Returns true if detected. */
    public boolean getMotif() {
        if (limelight == null) return false;

        LLResult result = limelight.getLatestResult();
        if (result == null) return false;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) return false;

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            if (id == 21) { robot.motif = Robot.Colors.GPP; return true; }
            if (id == 22) { robot.motif = Robot.Colors.PGP; return true; }
            if (id == 23) { robot.motif = Robot.Colors.PPG; return true; }
        }
        return false;
    }

    public LLResult getLatestResult() {
        return limelight == null ? null : limelight.getLatestResult();
    }
}
