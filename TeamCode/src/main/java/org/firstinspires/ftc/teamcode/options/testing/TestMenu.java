package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.Robot;

public class TestMenu extends MenuLib.Menu {

    public TestMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "TESTING");

        addOption(new MenuLib.SubMenu(
                "Spindexer",
                host,
                () -> new SpindexerMenu(host, gamepad1, gamepad2, telemetry, robot)
        ));

        addOption(new MenuLib.SubMenu(
                "Shooter",
                host,
                () -> new ShooterMenu(host, gamepad1, gamepad2, telemetry, robot)
        ));

        addOption(new MenuLib.SubMenu(
                "Intake",
                host,
                () -> new IntakeMenu(host, gamepad1, gamepad2, telemetry, robot)
        ));

        addOption(new MenuLib.SubMenu(
                "Drivetrain",
                host,
                () -> new DrivetrainMenu(host, gamepad1, gamepad2, telemetry, pinpoint, robot)
        ));

        addOption(new MenuLib.SubMenu(
                "Limelight",
                host,
                () -> new LimelightMenu(host, gamepad1, gamepad2, telemetry, robot)
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
