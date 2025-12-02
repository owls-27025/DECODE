package org.firstinspires.ftc.teamcode.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.mechanisms.distanceSensor.DistanceSensor;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

public class SpindexerMenu extends MenuLib.Menu {
    private Spindexer spindexer;
    private DistanceSensor distanceSensor;

    public SpindexerMenu(MenuLib.MenuHost host, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, Robot robot) {
        super(host, gamepad1, gamepad2, telemetry, "SPINDEXER");

        spindexer = robot.getSpindexer();
        distanceSensor = robot.getDistanceSensor();

        addOption(new MenuLib.Option(
                "Move Spindexer Forwards",
                () -> spindexer.moveHalfPosition(true)
        ));

        addOption(new MenuLib.Option(
                "Move Spindexer Backwards",
                () -> spindexer.moveHalfPosition(false)
        ));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.InfoOption(() ->
                "Position: " + spindexer.spindexerMotor.getCurrentPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "Target Position: " + spindexer.spindexerMotor.getTargetPosition()));
        addOption(new MenuLib.InfoOption(() ->
                "Relative Position: " + spindexer.spindexerMotor.getCurrentPosition() % spindexer.halfSlot));
        addOption(new MenuLib.InfoOption(() ->
                "Relative Target Position: " + spindexer.spindexerMotor.getTargetPosition() % spindexer.halfSlot));
        addOption(new MenuLib.InfoOption(() ->
                        "Distance: " + distanceSensor.getDistance()));

        addOption(new MenuLib.InfoOption(() -> ""));

        addOption(new MenuLib.Option(
                "Back",
                host::goBack
        ));
    }
}
