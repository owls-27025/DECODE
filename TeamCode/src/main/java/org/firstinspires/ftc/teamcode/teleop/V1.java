package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.Spindexer;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.intake.IntakeHelper;

import java.util.Arrays;

@TeleOp(name = "TeleOp", group = "OpModes")
public class V1 extends OpMode {
    public enum State { AUTO, MANUAL}
    private static State state = V1.State.AUTO;
    int currentBall = 0;
    String[] colors;
    boolean hasColors = false;
    String currentColor;
    double currentSpeed = 1;
    boolean isFieldCentric = false;
    int currentIntakeBall = 0;

    @Override
    public void init() {
//        Drivetrain.init(hardwareMap);
        Spindexer.init(hardwareMap, telemetry);
    }
    public void loop() {
//        drivetrain();
//        shooter();
        intake();

        if(gamepad2.right_bumper) {
            state = State.MANUAL;
        } else if(gamepad2.left_bumper) {
            state = State.AUTO;
        }

        telemetry.addData("Spindexer Position", SpindexerHelper.findPosition());
        telemetry.addData("Colors", Arrays.toString(Spindexer.colors));
        telemetry.addData("Current Color", ColorSensorHelper.getColor());
        telemetry.addData("Current Position", SpindexerHelper.SpindexerMotor.getCurrentPosition());
        telemetry.addData("Current Offset", SpindexerHelper.getStateOffset());
        telemetry.addData("Current State", Spindexer.state);
        telemetry.addData("current state again", Spindexer.state);
        telemetry.addData("Target Position", SpindexerHelper.SpindexerMotor.getTargetPosition());
        telemetry.addData("Power", SpindexerHelper.SpindexerMotor.getPower());
        telemetry.update();
    }

    public void drivetrain() {
        if(gamepad1.left_bumper) {
            currentSpeed = 0.35;
        } else {
            currentSpeed = 1;
        }

        if(gamepad1.guide) {
            if(isFieldCentric) {
                isFieldCentric = true;
                return;
            } else {
                isFieldCentric = false;
                return;
            }
        }

        if(gamepad1.start) {
            Drivetrain.resetIMU();
        }

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if(isFieldCentric) {
            y = Drivetrain.fieldCentricDrive(x, y)[0];
            x = Drivetrain.fieldCentricDrive(x, y)[1];
        }

        Drivetrain.FL.setPower((y + x + rx) * currentSpeed);
        Drivetrain.BL.setPower((y - x + rx) * currentSpeed);
        Drivetrain.FR.setPower((y - x - rx) * currentSpeed);
        Drivetrain.BR.setPower((y + x - rx) * currentSpeed);
    }

    public void shooter() {
        switch (state) {
            case AUTO: {
                if(gamepad2.a) {
                    if(!hasColors) {
//                        colors = Spindexer.shootMotifBall(currentBall);
                        hasColors = true;
                        currentBall = (currentBall + 1) % 3;
                    } else {
                        Spindexer.shootMotifBall(currentBall, colors);
                        currentBall = (currentBall + 1) % 3;
                    }
                } else if(gamepad2.b) {
                    if(!hasColors) {
//                        colors = Spindexer.shootMotif(currentBall);
                        hasColors = true;
                    } else {
                        Spindexer.shootMotif(currentBall, colors);
                    }
                }
                break;
            }
            case MANUAL: {
                if(gamepad2.dpad_left || gamepad2.dpad_up) {
                    currentColor = "Green";
                } else if(gamepad2.dpad_right || gamepad2.dpad_down) {
                    currentColor = "Purple";
                }
                if(gamepad2.a) {
                    if(!hasColors) {
//                        colors = Spindexer.shootMotifBall(currentColor);
                        hasColors = true;
                        currentBall = (currentBall + 1) % 3;
                    } else {
                        Spindexer.shootMotifBall(currentColor, colors);
                        currentBall = (currentBall + 1) % 3;
                    }
                }
                break;
            }
        }
    }

    public void intake() {
        if(gamepad1.aWasPressed()) {
            if (IntakeHelper.intake.getPower() != 0) {
                IntakeHelper.stop();
            } else {
                IntakeHelper.start();
            }
        } else if(gamepad1.bWasPressed()) {
            telemetry.addLine("before thingy");
            telemetry.update();
            Spindexer.intake(currentIntakeBall);
            telemetry.addLine("after thingy");
            telemetry.update();
            currentIntakeBall = (currentIntakeBall + 1) % 3;
        }
    }
}