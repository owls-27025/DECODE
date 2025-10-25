package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;

@TeleOp(name = "TeleOp", group = "OpModes")
public class V1 extends OpMode {
    public static enum State { AUTO, MANUALCOLOR, MANUALSHOOTER, MANUAL}
    private static V1.State state = V1.State.AUTO;
    int currentBall = 0;
    String[] colors;
    boolean hasColors = false;
    String currentColor;

    @Override
    public void init() {
        Drivetrain.init(hardwareMap);
        Spindexer.init(hardwareMap);
    }
    public void loop() {

    }

    public void drivetrain() {
        
    }

    public void shooter() {
        switch (state) {
            case AUTO: {
                if(gamepad1.a) {
                    if(!hasColors) {
                        colors = Spindexer.shootMotifBall(currentBall);
                        hasColors = true;
                        currentBall = (currentBall + 1) % 3;
                    } else {
                        Spindexer.shootMotifBall(currentBall, colors);
                        currentBall = (currentBall + 1) % 3;
                    }
                } else if(gamepad1.b) {
                    if(!hasColors) {
                        colors = Spindexer.shootMotif(currentBall);
                        hasColors = true;
                    } else {
                        Spindexer.shootMotif(currentBall, colors);
                    }
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
