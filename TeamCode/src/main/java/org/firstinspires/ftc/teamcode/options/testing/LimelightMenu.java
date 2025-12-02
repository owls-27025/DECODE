package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;

import java.util.List;

public class LimelightMenu extends MenuLib.Menu {

    public LimelightMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "LIMELIGHT");

        Limelight limelight = robot.getLimelight();
        Drivetrain drivetrain = robot.getDrivetrain();

        LLResult result = limelight.getResult();

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (!fiducials.isEmpty()) {
            int i = 1;
            for (LLResultTypes.FiducialResult fiducial : fiducials) {
                int id = fiducial.getFiducialId(); // The ID number of the fiducial
                int finalI = i;
                addOption(new MenuLib.InfoOption(() -> "Fiducial " + finalI + ": " + id));
                i++;
            }
        }

        YawPitchRollAngles angles = drivetrain.imu.getRobotYawPitchRollAngles();
        double robotYaw = angles.getYaw(AngleUnit.DEGREES);

        limelight.updateRobotOrientation(robotYaw);

        //noinspection ConstantValue
        if (result != null && result.isValid()) {
            Pose3D botpose_mt2 = result.getBotpose_MT2();
            if (botpose_mt2 != null) {
                double x = botpose_mt2.getPosition().x;
                double y = botpose_mt2.getPosition().y;
                addOption(new MenuLib.InfoOption(() -> "MT2 Location:" + "(" + x + ", " + y + ")"));
            }
        }

        //noinspection ConstantValue
        if (result != null && !result.isValid()) {
            addOption(new MenuLib.InfoOption(() -> "No AprilTags found"));
        }

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
