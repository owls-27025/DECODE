package org.firstinspires.ftc.teamcode.shared.helpers.options.menus.testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.helpers.OwlsGamepad;
import org.firstinspires.ftc.teamcode.shared.helpers.options.libraries.MenuLib;
import org.firstinspires.ftc.teamcode.shared.mechanisms.spindexer.Spindexer;

public class SpindexerMenu extends MenuLib.Menu {

    private final Spindexer spindexer;

    public SpindexerMenu(MenuLib.MenuHost host, Robot robot, OwlsGamepad gamepad1, OwlsGamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "SPINDEXER");
        this.spindexer = robot.spindexer;

        addOption(MenuLib.Option.value(
                () -> "Move Spindexer",
                () -> spindexer.moveHalfPosition(false),
                () -> spindexer.moveHalfPosition(true)
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.info(
                () -> "Spindexer Position: " + spindexer.getCurrent()
        ));

        addOption(MenuLib.Option.info(
                () -> "Target Position: " + spindexer.getTarget()
        ));

        addOption(MenuLib.Option.info(
                () -> "Relative Position: " + (spindexer.getCurrent() % Robot.Globals.tpr)
        ));

        addOption(MenuLib.Option.info(
                () -> "Relative Target: " + (spindexer.getTarget() % Robot.Globals.tpr)
        ));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.info(() -> "Distance: " + robot.distance.getDistanceMm()));

        addOption(MenuLib.Option.info(() -> ""));

        addOption(MenuLib.Option.action(
                () -> "Back",
                host::goBack
        ));
    }
}