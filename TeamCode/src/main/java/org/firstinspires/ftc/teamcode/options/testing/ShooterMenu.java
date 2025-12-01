package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;

public class ShooterMenu extends MenuLib.Menu {
    public ShooterMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "SHOOTER");

        addOption(new MenuLib.Option(
                "Start Shooter Motor",
                () -> {
                    robot.shooter.shooterMotor.setVelocity(robot.shooterVelocity);
                }
        ));

        addOption(new MenuLib.IntOption(
                "Shooter Velocity: ",
                1100,
                50,
                0,
                1600,
                value -> robot.shooterVelocity = value
        ));

        addOption(new MenuLib.DoubleOption(
                "Servo Position: ",
                0.0,
                0.05,
                0.0,
                1.0,
                2,
                robot.spindexer::moveServo
        ));

        addOption(new MenuLib.Option(
                "Light Color: " + robot.light.color,
                robot.light::cycle
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
