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

    public boolean getMotif() {
        if (Robot.Globals.alliance == Robot.Globals.Alliances.RED) {
            setPitchPos(0.5);
            setYawPos(0.17);
        } else if (Robot.Globals.alliance == Robot.Globals.Alliances.BLUE) {
            setPitchPos(0.5);
            setYawPos(0.83);
        }

        if (limelight == null) {
            Robot.Globals.motif = Robot.Globals.Colors.PGP;
            return false;
        }

        LLResult result = limelight.getLatestResult();
        if (result == null) {
            Robot.Globals.motif = Robot.Globals.Colors.PGP;
            return false;
        }

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) {
            Robot.Globals.motif = Robot.Globals.Colors.PGP;
            return false;
        }

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

    public boolean getBack() {
        return limelight.getLatestResult().getBotpose().getPosition().x > 35;
    }
}
