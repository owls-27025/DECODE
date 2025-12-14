package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsController;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;

public class ShooterMenu extends MenuLib.Menu {
    public ShooterMenu(MenuLib.MenuHost host, Robot robot, OwlsController gamepad1, OwlsController gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "SHOOTER");

        addOption(MenuLib.Option.action(
                () -> "Start Shooter Motor",
                () -> robot.shooter.shoot(robot.shooterVelocity)
        ));

        addOption(MenuLib.Option.value(
                () -> "Shooter Velocity: " + robot.shooter.getVelocity(),
                () -> {
                    robot.shooterVelocity += 10;
                    robot.shooter.shoot(robot.shooterVelocity);
                },
                () -> {
                    robot.shooterVelocity -= 10;
                    robot.shooter.shoot(robot.shooterVelocity);
                }
        ));

        addOption(MenuLib.Option.value(
                () -> "Flap Position: " + robot.spindexer.getFlapPosition(),
                () -> robot.spindexer.setFlapPosition(robot.spindexer.getFlapPosition() + 0.05),
                () -> robot.spindexer.setFlapPosition(robot.spindexer.getFlapPosition() - 0.05)
        ));

        addOption(MenuLib.Option.action(
                () -> "Light Color: " + robot.light.get(),
                robot.light::cycle
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(() -> "Back", host::goBack));
    }
}
