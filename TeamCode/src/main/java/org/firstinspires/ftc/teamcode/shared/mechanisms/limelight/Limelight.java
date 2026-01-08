package org.firstinspires.ftc.teamcode.shared.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;

import java.util.List;

public class Limelight {
    private final Limelight3A limelight;
    private final Servo yaw;
    private final Servo pitch;

    public Limelight(Robot.Configuration configuration) {
        limelight = Robot.Configuration.registerItem(Limelight3A.class, Robot.Configuration.limelight);
        if (limelight != null) {
            limelight.setPollRateHz(50);
            limelight.start();
        }

        yaw = Robot.Configuration.registerItem(Servo.class, Robot.Configuration.yaw);
        pitch = Robot.Configuration.registerItem(Servo.class, Robot.Configuration.pitch);
    }

    public void getMotif() {

        setYawPos(0.17);
        if (limelight == null) {
            Robot.Globals.motif = Robot.Globals.Colors.PGP;
            return;
        }

        LLResult result = limelight.getLatestResult();
        if (result == null) return;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) return;

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            if (id == 21) { Robot.Globals.motif = Robot.Globals.Colors.GPP; return; }
            if (id == 22) { Robot.Globals.motif = Robot.Globals.Colors.PGP; return; }
            if (id == 23) { Robot.Globals.motif = Robot.Globals.Colors.PPG; return; }
        }
    }

    public LLResult getLatestResult() {
        return limelight == null ? null : limelight.getLatestResult();
    }

    public double getYawPos() {
        return yaw.getPosition();
    }

    public double getPitchPos() {
        return pitch.getPosition();
    }

    public void setYawPos(double pos) {
        yaw.setPosition(pos);
    }

    public void setPitchPos(double pos) {
        pitch.setPosition(pos);
    }
}
