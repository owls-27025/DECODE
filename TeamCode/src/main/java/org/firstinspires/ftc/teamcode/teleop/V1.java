package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;

@TeleOp(name = "TeleOp", group = "OpModes")
public class V1 extends OpMode {
    // drivetrain
    public static double currentSpeed = 1;
    public static boolean isFieldCentric = false;

    // state machine
    public static enum State { INIT, DEFAULT, INTAKE, SHOOT }
    public static State state;
    public static State lastState;
    public static boolean isIntaking;
    public static boolean isShooting;

    @Override
    public void init() {
        // initialize subsystems
        Subsystems.init(hardwareMap, telemetry);

        // set up state machine
        state = State.DEFAULT;
        lastState = State.INIT;
        isIntaking = false;
        isShooting = false;
    }

    public void loop() {
        // change shooter velocity
        if (gamepad2.dpadUpWasPressed()) {
            Subsystems.SHOOTER_VELOCITY += 50;
        } else if (gamepad2.dpadDownWasPressed()) {
            Subsystems.SHOOTER_VELOCITY -= 50;
        }

        // change shooter subtraction
        if (gamepad2.dpadLeftWasPressed()) {
            Subsystems.SUBTRACTION_VELOCITY -= 10;
        } else if (gamepad2.dpadRightWasPressed()) {
            Subsystems.SUBTRACTION_VELOCITY += 10;
        }

        if (gamepad2.aWasPressed()) {
            state = State.INTAKE;
        } else if (gamepad2.xWasPressed()) {
            state = State.SHOOT;
        } else if (gamepad2.bWasPressed()) {
            state = State.DEFAULT;
        }

        switch (state) {
            case DEFAULT:
                Subsystems.drivetrain(gamepad1);
                telemetry();
                break;
            case INTAKE:
                try {
                    Subsystems.intake();
                    isIntaking = true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Subsystems.drivetrain(gamepad1);
                telemetry();
                break;
            case SHOOT:
                try {
                    Subsystems.shoot();
                    isShooting = true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Subsystems.drivetrain(gamepad1);
                telemetry();
                break;
        }
    }

    private void telemetry() {
        // shooter telemetry
        telemetry.addData("Shooting Velocity", Subsystems.SHOOTER_VELOCITY);
        telemetry.addData("Shooting Subtraction", Subsystems.SUBTRACTION_VELOCITY);

        // color sensor telemetry
        telemetry.addData("Distance", ColorSensorHelper.colorSensor.getDistance(DistanceUnit.MM));

        // intake telemetry
        telemetry.addData("Artifacts", Subsystems.artifactCount);

        // spindexer telemetry
        telemetry.addData("Spindexer Position", SpindexerHelper.findPosition());

        // drivetrain telemetry
        telemetry.addData("Field Centric", isFieldCentric);

        // state machine telemetry
        telemetry.addData("Current State", state);
        telemetry.addData("Last State", lastState);

        telemetry.update();
    }
}