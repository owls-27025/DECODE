package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;

public class SpindexerMenu extends MenuLib.Menu {

    public SpindexerMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "SPINDEXER");

        addOption(new MenuLib.Option(
                "Move Spindexer Forwards",
                () -> robot.spindexer.moveHalfPosition(true)
        ));

        addOption(new MenuLib.Option(
                "Move Spindexer Backwards",
                () -> robot.spindexer.moveHalfPosition(false)
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.InfoOption(() ->
                "Position: " + robot.spindexer.spindexerMotor.getCurrentPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "Target Position: " + robot.spindexer.spindexerMotor.getTargetPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "Relative Position: " + robot.spindexer.spindexerMotor.getCurrentPosition() % robot.spindexer.halfSlot));
        addOption(new MenuLib.InfoOption(() ->
                "Relative Target Position: " + robot.spindexer.spindexerMotor.getTargetPosition() % robot.spindexer.halfSlot));
        addOption(new MenuLib.InfoOption(() ->
                        "Distance: " + robot.distanceSensor.distanceSensor.getDistance(DistanceUnit.MM)));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
