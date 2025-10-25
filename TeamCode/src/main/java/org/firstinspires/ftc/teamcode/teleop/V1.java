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
        drivetrain();
        shooter();

        if(gamepad2.right_bumper) {
            state = State.MANUALCOLOR;
        } else if(gamepad2.left_bumper) {
            state = State.AUTO;
        }
    }

    public void drivetrain() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        Drivetrain.FL.setPower(y + x + rx);
        Drivetrain.BL.setPower(y - x + rx);
        Drivetrain.FR.setPower(y - x - rx);
        Drivetrain.BR.setPower(y + x - rx);
    }

    public void shooter() {
        switch (state) {
            case AUTO: {
                if(gamepad2.a) {
                    if(!hasColors) {
                        colors = Spindexer.shootMotifBall(currentBall);
                        hasColors = true;
                        currentBall = (currentBall + 1) % 3;
                    } else {
                        Spindexer.shootMotifBall(currentBall, colors);
                        currentBall = (currentBall + 1) % 3;
                    }
                } else if(gamepad2.b) {
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
                if(gamepad2.dpad_left || gamepad2.dpad_up) {
                    currentColor = "Green";
                } else if(gamepad2.dpad_right || gamepad2.dpad_down) {
                    currentColor = "Purple";
                }
                if(gamepad2.a) {
                    if(!hasColors) {
                        colors = Spindexer.shootMotifBall(currentColor);
                        hasColors = true;
                        currentBall = (currentBall + 1) % 3;
                    } else {
                        Spindexer.shootMotifBall(currentColor, colors);
                        currentBall = (currentBall + 1) % 3;
                    }
                }
                break;
            }
            case MANUALSHOOTER: {
                // no shooter yet
                break;
            }
            case MANUAL: {
                // no shooter yet but more
                break;
            }
        }
    }
}
