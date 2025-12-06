package org.firstinspires.ftc.teamcode.options.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;

public class TeleOpMenu extends MenuLib.Menu {

    public TeleOpMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "TELEOP");

        // change driving speed
        addOption(new MenuLib.DoubleOption(
                "Drive Speed: ",
                Globals.DriveSpeed,
                0.05,
                0.0,
                1.0,
                2,
                value -> Globals.DriveSpeed = value
        ));

        // change slow driving speed
        addOption(new MenuLib.DoubleOption(
                "Slow Drive Speed: ",
                Globals.SlowDriveSpeed,
                0.05,
                0.0,
                1.0,
                2,
                value -> Globals.SlowDriveSpeed = value
        ));

        // change number of forced artifacts for y button
        addOption(new MenuLib.IntOption(
                "Forced Artifacts: ",
                Globals.ForcedArtifacts,
                1,
                1,
                3,
                value -> Globals.ForcedArtifacts = value
        ));

        // toggle right stick driving
        addOption(new MenuLib.Option(() ->
                "Right Stick Driving: " + Globals.isRightStick,
                Globals::flipStick));

        // toggle field centric
        addOption(new MenuLib.Option(() ->
                "Field Centric: " + Globals.isFieldCentric,
                Globals::flipFieldCentric));

        // shooter tolerance setting
        addOption(new MenuLib.IntOption(
                "Shooter Tolerance: ",
                Globals.ShooterTolerance,
                1,
                0,
                100,
                value -> Globals.ShooterTolerance = value
        ));

        addOption(new MenuLib.IntOption(
                "Spindexer Shoot Wait: ",
                Globals.spindexerShootTime,
                1,
                0,
                10000,
                value -> Globals.spindexerShootTime = value
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
