package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.artifactCount;
import static org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.Subsystems;
import org.firstinspires.ftc.teamcode.mechanisms.light.Light;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.subsystems.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.helpers.Globals;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "TeleOp", group = "OpModes")
public class V1 extends OpMode {
    // drivetrain
    public static double currentSpeed = 1;
    public static boolean isFieldCentric = false;
    boolean firing = false;
    public static boolean inhibitButtons = false;

    @Override
    public void init() {
        // initialize subsystems
        Subsystems.init(hardwareMap, telemetry);
        firing = false;
    }

    public void loop() {
        // when start is held with a or b buttons
        if ((gamepad1.start && (gamepad1.a || gamepad1.b)) || (gamepad2.start && (gamepad2.a || gamepad2.b))) {
            inhibitButtons = true;
        }

        // release inhibit once buttons are lifted
        if ((!gamepad1.a && !gamepad1.b) || (!gamepad2.a && !gamepad2.b)) {
            inhibitButtons = false;
        }

        // change shooter velocity
        if (gamepad2.dpadUpWasPressed()) {
            Globals.ShooterVelocity += 50;
        } else if (gamepad2.dpadDownWasPressed()) {
            Globals.ShooterVelocity -= 50;
        }

        // move spindexer CW or CCW
        if (gamepad2.leftBumperWasPressed()) {
            if (Subsystems.isHumanIntake) {
                SpindexerHelper.moveToNextPosition();
            } else {
                SpindexerHelper.moveHalfPosition(false);
            }
        } else if (gamepad2.rightBumperWasPressed()) {
            if (Subsystems.isHumanIntake) {
                SpindexerHelper.moveToNextPosition();
            } else {
                SpindexerHelper.moveHalfPosition(true);
            }
        }

        // move to different spindexer positions
        if (gamepad2.left_trigger != 0) {
            SpindexerHelper.intakePosition();
        } else if (gamepad2.right_trigger != 0) {
            SpindexerHelper.shootPosition();
        }

        // rgb light control
//        if (artifactCount == 1) {
//            Light.red();
//        } else if (artifactCount == 2) {
//            Light.yellow();
//        } else if (artifactCount == 3) {
//            Light.green();
//        }

        // intake
        Subsystems.intake(gamepad1);

        // shoot
        Subsystems.shoot(gamepad2);

        // human player intake
        Subsystems.intakeHuman(gamepad2);

        // cancel functions
        if ((gamepad2.bWasPressed() || gamepad1.bWasPressed()) && !inhibitButtons) {
            Subsystems.currentShootState = Subsystems.ShootState.COMPLETED;
            Subsystems.currentState = Subsystems.IntakeState.COMPLETED;
        }

        if (gamepad1.backWasPressed()) {
            if (IntakeHelper.intake.getPower() >= 0) {
                IntakeHelper.intake.setPower(-1);
            } else if(IntakeHelper.intake.getPower() < 0) {
                IntakeHelper.intake.setPower(0);
            }
        }

        // drive
        drivetrain(gamepad1);
        telemetry();
    }

    // telemetry
    private void telemetry() {
        // shooter telemetry
        telemetry.addData("Current Velocity", Globals.ShooterVelocity);

        // color sensor telemetry
        telemetry.addData("Distance", ColorSensorHelper.colorSensor.getDistance(DistanceUnit.MM));
        telemetry.addData("Detects Ball", ColorSensorHelper.isBall());

        // intake telemetry
        telemetry.addData("Artifacts", Subsystems.artifactCount);

        // spindexer telemetry
        telemetry.addData("Spindexer Position", SpindexerHelper.findPosition());

        // drivetrain telemetry
        telemetry.addData("Field Centric", isFieldCentric);

        // state machine telemetry
        telemetry.addData("Shooter State", Subsystems.currentShootState);
        if (Subsystems.currentShootState == Subsystems.ShootState.FIRING) {
            firing = true;
        }

        telemetry.addData("Has fired", firing);

        telemetry.addData("Intake State", Subsystems.currentState);
        telemetry.addData("Delay", Subsystems.delayTimer.time(TimeUnit.MILLISECONDS));
        telemetry.addData("Delay started", Subsystems.delayStarted);

        telemetry.addData("spindexer motor position", SpindexerHelper.SpindexerMotor.getCurrentPosition());

        telemetry.addData("spindexer speed", Globals.SpindexerSpeed);

        telemetry.update();
    }
}