package org.firstinspires.ftc.teamcode.options.opmodes;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.Robot;

public class TeleOpMenu extends MenuLib.Menu {

    public TeleOpMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "TELEOP");

        // change driving speed
        addOption(new MenuLib.DoubleOption(
                "Drive Speed: ",
                robot.getDriveSpeed(),
                0.05,
                0.0,
                1.0,
                2,
                robot::setDriveSpeed
        ));

        // change slow driving speed
        addOption(new MenuLib.DoubleOption(
                "Slow Drive Speed: ",
                robot.getSlowDriveSpeed(),
                0.05,
                0.0,
                1.0,
                2,
                robot::setSlowDriveSpeed
        ));

        // change number of forced artifacts for y button
        addOption(new MenuLib.IntOption(
                "Forced Artifacts: ",
                robot.getForcedArtifacts(),
                1,
                1,
                3,
                robot::setForcedArtifacts
        ));

        // toggle right stick driving
        addOption(new MenuLib.Option(() ->
                "Right Stick Driving: " + robot.getRightStick(),
                () -> robot.setRightStick(!robot.getRightStick())
        ));

        // toggle field centric
        addOption(new MenuLib.Option(
                () -> "Field Centric: " + robot.getFieldCentric(),
                () -> robot.setFieldCentric(!robot.getFieldCentric())
        ));


        // shooter tolerance setting
        addOption(new MenuLib.IntOption(
                "Shooter Tolerance: ",
                robot.getShooterTolerance(),
                1,
                0,
                15,
                robot::setShooterTolerance
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
