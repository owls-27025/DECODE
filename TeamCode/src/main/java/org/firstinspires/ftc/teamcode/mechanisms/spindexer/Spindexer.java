package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.distanceSensor.DistanceSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper;

import java.util.Objects;

public class Spindexer {
    static String[] motif = new String[3];
    public static String[] colors = new String[3];
    public enum State {SHOOTING, INTAKE, COLORSENSOR}
    public static State state;
    private static Telemetry codeisntworking;

    public static void init(HardwareMap hardwareMap, Telemetry telemetry) {
        SpindexerHelper.init(hardwareMap, telemetry);
        ColorSensorHelper.init(hardwareMap);
//        ShooterHelper.init(hardwareMap);
        IntakeHelper.init(hardwareMap);
        DistanceSensorHelper.init(hardwareMap);
        codeisntworking = telemetry;
        state = State.SHOOTING;
    }

    public static void shootMotifBall(int index, String[] colors) {
        String[] motif = new String[3];
        motif[0] = "Green";
        motif[1] = "Purple";
        motif[2] = "Purple"; // placeholder motif because no limelight yet

        // ShooterHelper.shoot();

        int nextPosition = -1;
        String target = motif[index];

        for (int i = 0; i < 3; i++) {
            if (colors[i].equals(target)) {
                nextPosition = i;
                break;
            }
        }
        SpindexerHelper.moveToPosition(nextPosition);
        // move spindexer servo to shoot ball
    }

    public static void shootMotifBall(String color, String[] colors) {
        int index = -1;

        for (int i = 0; i < 3; i++) {
            if (colors[i].equals(color)) {
                index = i;
                break;
            }
        }

        // ShooterHelper.shoot();
        SpindexerHelper.moveToPosition(index);
        // move spindexer servo to shoot ball
    }

    public static void shootMotif(int currentBall, String[] colors) {
        shootMotifBall(currentBall, colors);
        shootMotifBall(1 + currentBall, colors);
        shootMotifBall(2 + currentBall, colors);
    }

    public static void intake(int pos) {
        state = State.INTAKE;

        SpindexerHelper.moveToPosition(pos);

        while(!DistanceSensorHelper.isBall()) {
            // a a a a a  a a
        }
        state = State.COLORSENSOR;

        SpindexerHelper.moveToPosition(pos);
        while(Objects.equals(ColorSensorHelper.getColor(), "Neither")) {
            // do nothing
        }

        String color = ColorSensorHelper.getColor();
        colors[pos] = color;

        state = State.INTAKE;
        SpindexerHelper.moveToPosition(pos + 1);
    }
}