package org.firstinspires.ftc.teamcode.shared.options.testing;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.shared.options.MenuLib;
import org.firstinspires.ftc.teamcode.shared.mechanisms.spindexer.Spindexer;

import java.util.function.Supplier;

public class SpindexerMenu extends MenuLib.Menu {

    private final Robot robot;
    private final Spindexer spindexer;

    public SpindexerMenu(MenuLib.MenuHost host, Robot robot, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry) {
        super(host, gamepad1, gamepad2, telemetry, "SPINDEXER");
        this.robot = robot;
        this.spindexer = robot.spindexer;

        addOption(new MenuLib.Option("Move Spindexer Forwards", new Runnable() {
            @Override public void run() {
                spindexer.moveHalfPosition(true);
            }
        }));

        addOption(new MenuLib.Option("Move Spindexer Backwards", new Runnable() {
            @Override public void run() {
                spindexer.moveHalfPosition(false);
            }
        }));

        addOption(new MenuLib.InfoOption(new Supplier<String>() {
            @Override public String get() {
                // replace these with your actual getters/fields
                return "Position: " + spindexer.getCurrent();
            }
        }));

        addOption(new MenuLib.InfoOption(new Supplier<String>() {
            @Override public String get() {
                return "Target Position: " + spindexer.getTarget();
            }
        }));

        addOption(new MenuLib.InfoOption(new Supplier<String>() {
            @Override public String get() {
                return "Relative Position: " + (spindexer.getCurrent() % robot.tpr);
            }
        }));

        addOption(new MenuLib.InfoOption(new Supplier<String>() {
            @Override public String get() {
                return "Relative Target: " + (spindexer.getTarget() % robot.tpr);
            }
        }));

        addOption(new MenuLib.Option("Back", new Runnable() {
            @Override public void run() {
                host.goBack();
            }
        }));
    }
}
