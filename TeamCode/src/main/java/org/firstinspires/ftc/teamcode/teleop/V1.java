package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

@TeleOp(name = "TeleOp", group = "OpModes")
public class V1 extends OpMode {
    public static enum State { AUTO, MANUALCOLOR, MANUALSHOOTER, MANUAL}
    private static V1.State state = V1.State.AUTO;
    Drivetrain drivetrain = new Drivetrain();
    Spindexer spindexer = new Spindexer();

    @Override
    public void init() {
        drivetrain.init(hardwareMap);
        Spindexer.init(hardwareMap);
    }
    public void loop() {

    }

    public void shooter() {
        switch (state) {
            case AUTO: {
                if(gamepad1.a) {

                }
                break;
            }
            case MANUALCOLOR: {

                break;
            }
            case MANUALSHOOTER: {

                break;
            }
            case MANUAL: {
                break;
            }
        }
    }
}
