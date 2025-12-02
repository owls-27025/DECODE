package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.mechanisms.intake.Intake;

public class IntakeMenu extends MenuLib.Menu {
    public IntakeMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "INTAKE");

        Intake intake = robot.getIntake();

        addOption(new MenuLib.DoubleOption(
                "Intake Power",
                0.0,
                0.05,
                0.0,
                1.0,
                2,
                intake::setIntakePower
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
