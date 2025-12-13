package org.firstinspires.ftc.teamcode.shared.options.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;

public class TestMenu extends MenuLib.Menu {

    public TestMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, GoBildaPinpointDriver pinpoint) {
        super(host, gamepad1, gamepad2, telemetry, "TESTING");

        addOption(new MenuLib.SubMenu(
                "Spindexer",
                host,
                () -> new SpindexerMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(new MenuLib.SubMenu(
                "Shooter",
                host,
                () -> new ShooterMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(new MenuLib.SubMenu(
                "Intake",
                host,
                () -> new IntakeMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(new MenuLib.SubMenu(
                "Drivetrain",
                host,
                () -> new DrivetrainMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(new MenuLib.SubMenu(
                "Limelight",
                host,
                () -> new LimelightMenu(host, robot, gamepad1, gamepad2, telemetry)
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
