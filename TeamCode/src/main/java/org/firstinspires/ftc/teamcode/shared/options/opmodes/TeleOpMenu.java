package org.firstinspires.ftc.teamcode.shared.options.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

public class TeleOpMenu extends MenuLib.Menu {

    public TeleOpMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "TELEOP");

        addOption(new MenuLib.DoubleOption(
                "Drive Speed: ",
                robot.driveSpeed,
                0.05,
                0.0,
                1.0,
                2,
                value -> robot.driveSpeed = value
        ));

        addOption(new MenuLib.DoubleOption(
                "Slow Drive Speed: ",
                robot.slowDriveSpeed,
                0.05,
                0.0,
                1.0,
                2,
                value -> robot.slowDriveSpeed = value
        ));

        addOption(new MenuLib.IntOption(
                "Forced Artifacts: ",
                robot.forcedArtifacts,
                1,
                1,
                3,
                value -> robot.forcedArtifacts = value
        ));

        addOption(new MenuLib.Option(() ->
                "Right Stick Driving: " + robot.isRightStick,
                robot::flipStick));

        addOption(new MenuLib.Option(() ->
                "Field Centric: " + robot.isFieldCentric,
                robot::flipFieldCentric));

        addOption(new MenuLib.IntOption(
                "Shooter Tolerance: ",
                robot.shooterTolerance,
                1,
                0,
                200,
                value -> robot.shooterTolerance = value
        ));

        addOption(new MenuLib.IntOption(
                "Spindexer Shoot Wait: ",
                robot.spindexerShootTimeTicks,
                1,
                0,
                10000,
                value -> robot.spindexerShootTimeTicks = value
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
