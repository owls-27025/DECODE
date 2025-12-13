package org.firstinspires.ftc.teamcode.shared.options.testing;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

import java.util.List;

public class LimelightMenu extends MenuLib.Menu {

    public LimelightMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "LIMELIGHT");

        LLResult result = robot.limelight.getLatestResult();
        if (result == null) {
            addOption(new MenuLib.InfoOption(() -> "No result yet"));
        } else if (!result.isValid()) {
            addOption(new MenuLib.InfoOption(() -> "Result invalid (no tags?)"));
        } else {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            if (fiducials != null && !fiducials.isEmpty()) {
                int i = 1;
                for (LLResultTypes.FiducialResult fiducial : fiducials) {
                    int id = fiducial.getFiducialId();
                    int idx = i;
                    addOption(new MenuLib.InfoOption(() -> "Fiducial " + idx + ": " + id));
                    i++;
                }
            } else {
                addOption(new MenuLib.InfoOption(() -> "No fiducials"));
            }

            Pose3D botpose = result.getBotpose_MT2();
            if (botpose != null) {
                double x = botpose.getPosition().x;
                double y = botpose.getPosition().y;
                addOption(new MenuLib.InfoOption(() -> "MT2 Pose: (" + x + ", " + y + ")"));
            }
        }

        addOption(new MenuLib.InfoOption(() -> ""));
        addOption(new MenuLib.Option("Back", host::goBack));
    }
}
