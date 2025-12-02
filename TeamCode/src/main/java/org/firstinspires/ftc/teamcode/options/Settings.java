package org.firstinspires.ftc.teamcode.options;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.helpers.BaseOpMode;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.roadrunner.tuning.TuningOpModes;
import org.firstinspires.ftc.teamcode.options.opmodes.AutoMenu;
import org.firstinspires.ftc.teamcode.options.opmodes.TeleOpMenu;
import org.firstinspires.ftc.teamcode.options.testing.TestMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp(name = "Settings", group = "Settings")
public class Settings extends BaseOpMode implements MenuLib.MenuHost {
    private MenuLib.Menu mainMenu;
    private MenuLib.Menu currentMenu;

    private final List<MenuLib.Menu> menuStack = new ArrayList<>();

    private AtomicBoolean isFinished;
    private GoBildaPinpointDriver pinpoint;

    @Override
    protected void onInit() {
        if (config != null && config.odometry != null && config.odometry.itemActive) {
            pinpoint = hardwareMap.get(
                    GoBildaPinpointDriver.class,
                    config.odometry.itemName
            );
        }

        isFinished = new AtomicBoolean(false);

        // Build the main menu
        mainMenu = new MenuLib.Menu(this, gamepad1, gamepad2, telemetry, "SETTINGS") {
            {
                // change spindexer speed
                addOption(new MenuLib.DoubleOption(
                        "Spindexer Speed: ",
                        robot.getSpindexerSpeed(),
                        0.05,
                        0.0,
                        1.0,
                        2,
                        robot::setSpindexerSpeed
                ));

                addOption(new MenuLib.Option(
                        "Tuning OpModes (Requires Restart): " + !TuningOpModes.DISABLED,
                        () -> TuningOpModes.DISABLED = (!TuningOpModes.DISABLED)
                ));

                // blank line
                addOption(new MenuLib.InfoOption(() -> ""));

                // teleop options submenu
                addOption(new MenuLib.SubMenu(
                        "TeleOp",
                        Settings.this,
                        () -> new TeleOpMenu(Settings.this, gamepad1, gamepad2, telemetry, robot)
                ));

                // auto options submenu
                addOption(new MenuLib.SubMenu(
                        "Auto",
                        Settings.this,
                        () -> new AutoMenu(Settings.this, gamepad1, gamepad2, telemetry, robot)
                ));

                // testing submenu
                addOption(new MenuLib.SubMenu(
                        "Testing",
                        Settings.this,
                        () -> new TestMenu(Settings.this, gamepad1, gamepad2, telemetry, pinpoint, robot)
                ));

                // blank line
                addOption(new MenuLib.InfoOption(() -> ""));

                // exit opmode
                addOption(new MenuLib.Option(
                        "Exit",
                        () -> isFinished.set(true)
                ));
            }
        };

        mainMenu.onSelected();
        currentMenu = mainMenu;
        menuStack.clear();
        menuStack.add(mainMenu);
    }

    @Override
    protected void onLoop() {

        if (isFinished.get()) {
            requestOpModeStop();
            return;
        }

        if (pinpoint != null) {
            pinpoint.update();
        }

        if (currentMenu != null) {
            currentMenu.loop();
        }
    }

    @Override
    protected void onStop() {
        telemetry.addLine("Exiting...");
        telemetry.update();
        sleep(1500);
    }


    @Override
    public void setCurrentMenu(MenuLib.Menu menu) {
        if (menu != null) {
            currentMenu = menu;
            menuStack.add(menu);
        }
    }

    @Override
    public void goToMainMenu() {
        mainMenu.onSelected();
        currentMenu = mainMenu;
        menuStack.clear();
        menuStack.add(mainMenu);
    }

    @Override
    public void goBack() {
        if (menuStack.size() > 1) {
            menuStack.remove(menuStack.size() - 1);
            currentMenu = menuStack.get(menuStack.size() - 1);
            currentMenu.onSelected();
        } else {
            isFinished.set(true);
        }
    }
}