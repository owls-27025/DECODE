package org.firstinspires.ftc.teamcode.shared.mechanisms.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoParams;

import java.util.Formatter;
import java.util.List;
import java.util.Objects;

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

    public boolean getMotif(AutoParams params) {
        if (params.strategy == AutoParams.Strategies.BACK) {
            if (limelight.getStatus().getPipelineIndex() != 3) {
                limelight.pipelineSwitch(3);
                return false;
            }
        } else if (params.alliance == AutoParams.Alliances.RED) {
            if (limelight.getStatus().getPipelineIndex() != 1) {
                limelight.pipelineSwitch(1);
                return false;
            }
        } else {
            if (limelight.getStatus().getPipelineIndex() != 2) {
                limelight.pipelineSwitch(2);
                return false;
            }
        }

        if (params.strategy != AutoParams.Strategies.BACK) {
            if (params.alliance == AutoParams.Alliances.RED) {
                setPitchPos(0.5);
                setYawPos(0.13);
            } else if (params.alliance == AutoParams.Alliances.BLUE) {
                setPitchPos(0.5);
                setYawPos(0.94);
            }
        } else {
            setPitchPos(0.5);
            setYawPos(0.5);
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
            if (id == 21) {
                Robot.Globals.motif = Robot.Globals.Colors.GPP;
                return true;
            }
            if (id == 22) {
                Robot.Globals.motif = Robot.Globals.Colors.PGP;
                return true;
            }
            if (id == 23) {
                Robot.Globals.motif = Robot.Globals.Colors.PPG;
                return true;
            }
        }

        Robot.Globals.motif = Robot.Globals.Colors.PGP;
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

    public double getDistanceToGoal() {
        LLResult result = limelight.getLatestResult();

        return Math.sqrt(Math.pow(-58.9 - result.getBotpose_MT2().getPosition().y, 2) + Math.pow(53.9 - result.getBotpose_MT2().getPosition().x, 2));
    }

    public double getTy() {
        List<LLResultTypes.FiducialResult> fiducials = limelight.getLatestResult().getFiducialResults();

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            return fiducial.getTargetYDegrees();
        }

        return 0;
    }

    public boolean doesExist() {
        return limelight != null && limelight.isRunning();
    }

    public int getID() {
        List<LLResultTypes.FiducialResult> fiducials = limelight.getLatestResult().getFiducialResults();

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            return id;
        }

        return 0;
    }


    public void setPipeline(int pipeline) {
        if (limelight != null) {
            limelight.pipelineSwitch(pipeline);
        }
    }

    public int getPipeline() {
        return limelight == null ? -1 : limelight.getStatus().getPipelineIndex();
    }
}