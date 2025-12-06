package org.firstinspires.ftc.teamcode.options;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.helpers.Globals;
import org.firstinspires.ftc.teamcode.helpers.MenuLib;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.limelight.Limelight;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.options.opmodes.AutoMenu;
import org.firstinspires.ftc.teamcode.options.opmodes.TeleOpMenu;
import org.firstinspires.ftc.teamcode.options.testing.TestMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp
public class Settings extends LinearOpMode implements MenuLib.MenuHost {

    private MenuLib.Menu mainMenu;
    private MenuLib.Menu currentMenu;

    private final List<MenuLib.Menu> menuStack = new ArrayList<>();

    private AtomicBoolean isFinished;

    @Override
    public void runOpMode() {
        Subsystems.init(hardwareMap, telemetry);
        Drivetrain.init(hardwareMap);
        Limelight.init(hardwareMap);

        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Configuration.odometry.itemName);

        isFinished = new AtomicBoolean(false);

        mainMenu = new MenuLib.Menu(this, gamepad1, gamepad2, telemetry, "SETTINGS") {
            {

                // change spindexer speed
                addOption(new MenuLib.DoubleOption(
                        "Spindexer Speed: ",
                        Globals.SpindexerSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.SpindexerSpeed = value
                ));

                addOption(new MenuLib.DoubleOption(
                        "Intake Speed: ",
                        Globals.intakeSpeed,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.intakeSpeed = value
                ));

                // blank line
                addOption(new MenuLib.InfoOption(() ->
                        ""));

                addOption(new MenuLib.SubMenu(
                        "TeleOp",
                        Settings.this,
                        () -> new TeleOpMenu(Settings.this, gamepad1, gamepad2, telemetry, pinpoint)
                ));

                addOption(new MenuLib.SubMenu(
                        "Auto",
                        Settings.this,
                        () -> new AutoMenu(Settings.this, gamepad1, gamepad2, telemetry, pinpoint)
                ));

                // go to testing submenu
                addOption(new MenuLib.SubMenu(
                        "Testing",
                        Settings.this,
                        () -> new TestMenu(Settings.this, gamepad1, gamepad2, telemetry, pinpoint)
                ));

                addOption(new MenuLib.InfoOption(
                        () -> ""
                ));

                // exit opmode
                addOption(new MenuLib.Option(
                        "Exit",
                        () -> isFinished.set(true)
                ));
            }
        };

        // run menu code
        mainMenu.onSelected();
        currentMenu = mainMenu;
        menuStack.clear();
        menuStack.add(mainMenu);

        waitForStart();

        while (opModeIsActive() && !isFinished.get()) {
            pinpoint.update();
            if (currentMenu != null) {
                currentMenu.loop();
            }
        }

        // run when exiting
        telemetry.addLine("exiting...");
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
