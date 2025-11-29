package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.options.MenuLib;
import org.firstinspires.ftc.teamcode.options.Settings;

public class TestMenu extends MenuLib.Menu {

    public TestMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "TEST MENU");

        addOption(new MenuLib.SubMenu(
                "Spindexer",
                host,
                () -> new SpindexerMenu(host, gamepad1, gamepad2, telemetry, pinpoint)
        ));

        addOption(new MenuLib.SubMenu(
                "Shooter",
                host,
                () -> new ShooterMenu(host, gamepad1, gamepad2, telemetry, pinpoint)
        ));

        addOption(new MenuLib.SubMenu(
                "Intake",
                host,
                () -> new IntakeMenu(host, gamepad1, gamepad2, telemetry, pinpoint)
        ));

        addOption(new MenuLib.SubMenu(
                "Drivetrain",
                host,
                () -> new DrivetrainMenu(host, gamepad1, gamepad2, telemetry, pinpoint)
        ));

        addOption(new MenuLib.InfoOption(() ->
                ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goToMainMenu
        ));
    }
}
