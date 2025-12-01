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
                robot.driveSpeed,
                0.05,
                0.0,
                1.0,
                2,
                value -> robot.driveSpeed = value
        ));

        // change slow driving speed
        addOption(new MenuLib.DoubleOption(
                "Slow Drive Speed: ",
                robot.slowDriveSpeed,
                0.05,
                0.0,
                1.0,
                2,
                value -> robot.slowDriveSpeed = value
        ));

        // change number of forced artifacts for y button
        addOption(new MenuLib.IntOption(
                "Forced Artifacts: ",
                robot.forcedArtifacts,
                1,
                1,
                3,
                value -> robot.forcedArtifacts = value
        ));

        // toggle right stick driving
        addOption(new MenuLib.Option(() ->
                "Right Stick Driving: " + robot.isRightStick,
                () -> robot.isRightStick = (!robot.isRightStick)));

        // toggle field centric
        addOption(new MenuLib.Option(
                () -> "Field Centric: " + robot.isFieldCentric,
                () -> robot.isFieldCentric = (!robot.isFieldCentric)
        ));


        // shooter tolerance setting
        addOption(new MenuLib.IntOption(
                "Shooter Tolerance: ",
                15,
                1,
                0,
                15,
                value -> robot.shooterTolerance = value
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
