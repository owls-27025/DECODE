package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

import java.util.List;

public class LimelightMenu extends MenuLib.Menu {

    public LimelightMenu(MenuLib.MenuHost host, Robot robot, OwlsGamepad gamepad1, OwlsGamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "LIMELIGHT");

        LLResult result = robot.limelight.getLatestResult();
        if (result == null) {
            addOption(MenuLib.Option.info(() -> "No result yet"));
        } else if (!result.isValid()) {
            addOption(MenuLib.Option.info(() -> "Result invalid (no tags?)"));
        } else {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            if (fiducials != null && !fiducials.isEmpty()) {
                int i = 1;
                for (LLResultTypes.FiducialResult fiducial : fiducials) {
                    int id = fiducial.getFiducialId();
                    int idx = i;
                    addOption(MenuLib.Option.info(() -> "Fiducial " + idx + ": " + id));
                    i++;
                }
            } else {
                addOption(MenuLib.Option.info(() -> "No fiducials"));
            }

            Pose3D botpose = result.getBotpose_MT2();
            if (botpose != null) {
                double x = botpose.getPosition().x;
                double y = botpose.getPosition().y;
                addOption(MenuLib.Option.info(() -> "MT2 Pose: (" + x + ", " + y + ")"));
            }
        }

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(() -> "Back", host::goBack));
    }
}
