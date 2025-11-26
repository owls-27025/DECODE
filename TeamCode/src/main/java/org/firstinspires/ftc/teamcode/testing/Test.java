package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp
public class Test extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        AtomicBoolean isFinished = new AtomicBoolean(false);

        MenuObject[] menuObjects = {
                new MenuObject("Move Spindexer Forwards", () -> {
                    SpindexerHelper.moveHalfPosition(true);
                }),

                new MenuObject("Move Spindexer Backwards", () -> {
                    SpindexerHelper.moveHalfPosition(false);
                }),

                new MenuObject("Exit", () -> {
                    isFinished.set(true);
                })
        };

        int pointer = 0;

        Subsystems.init(hardwareMap, telemetry);
        Drivetrain.init(hardwareMap);

        waitForStart();

        while (opModeIsActive() && !isFinished.get()) {

            if (gamepad1.dpadDownWasPressed()) {
                pointer = (pointer + 1) % menuObjects.length;
                sleep(200);
            }
            if (gamepad1.dpadUpWasPressed()) {
                pointer = (pointer - 1 + menuObjects.length) % menuObjects.length;
                sleep(200);
            }

            if (gamepad1.aWasPressed()) {
                menuObjects[pointer].action.run();
            }

            telemetry.clear();
            telemetry.addLine("====== TEST ======");
            for (int i = 0; i < menuObjects.length; i++) {
                if (i == pointer) {
                    telemetry.addLine("> " + menuObjects[i].title);
                } else {
                    telemetry.addLine("   " + menuObjects[i].title);
                }
            }
            telemetry.update();
        }
        telemetry.addLine("testing completed :)");
        telemetry.update();
        sleep(1500);
    }

    public class MenuObject {
        String title;
        Runnable action;

        public MenuObject(String name, Runnable action) {
            this.title = name;
            this.action = action;
        }
    }
}
