package org.firstinspires.ftc.teamcode.options;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Globals;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.options.MenuLib;
import org.firstinspires.ftc.teamcode.options.TestMenu;

@TeleOp
public class Settings extends LinearOpMode implements MenuLib.MenuHost {
    private MenuLib.Menu mainMenu;
    private MenuLib.Menu currentMenu;

    @Override
    public void runOpMode() {
        Subsystems.init(hardwareMap, telemetry);
        Drivetrain.init(hardwareMap);

        mainMenu = new MenuLib.Menu(this, gamepad1, telemetry, "SETTINGS") {
            {
                addOption(new MenuLib.Option(
                        () -> "Side: " + Globals.side,
                        Globals::flipSide
                ));

                addOption(new MenuLib.Option(
                        () -> "Alliance: " + Globals.alliance,
                        Globals::flipAlliance
                ));

                addOption(new MenuLib.DoubleOption(
                        "Spindexer Speed: ",
                        0.1,
                        0.05,
                        0.0,
                        1.0,
                        2,
                        value -> Globals.SpindexerSpeed = value
                ));

                addOption(new MenuLib.SubMenu(
                        "Test",
                        Settings.this,
                        () -> new TestMenu(Settings.this, gamepad1, telemetry)
                ));
            }
        };

        mainMenu.onSelected();
        currentMenu = mainMenu;

        waitForStart();

        while (opModeIsActive()) {
            if (currentMenu != null) {
                currentMenu.loop();
            }
        }
    }


    @Override
    public void setCurrentMenu(MenuLib.Menu menu) {
        currentMenu = menu;
    }

    @Override
    public void goToMainMenu() {
        mainMenu.onSelected();
        currentMenu = mainMenu;
    }
}
