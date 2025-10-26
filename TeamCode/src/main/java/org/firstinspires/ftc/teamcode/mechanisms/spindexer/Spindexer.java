package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
    public static State state = State.SHOOTING;

    public static void init(HardwareMap hardwareMap) {
        SpindexerHelper.init(hardwareMap);
        ColorSensorHelper.init(hardwareMap);
//        ShooterHelper.init(hardwareMap);
//        IntakeHelper.init(hardwareMap);
        DistanceSensorHelper.init(hardwareMap);
    }
    public static String[] shootMotifBall(int index) {
        motif[0] = "Green";
        motif[1] = "Purple";
        motif[2] = "Purple"; // placeholder motif because there is no limelight code yet

        colors = SpindexerHelper.getColors();

        // ShooterHelper.shoot();

        int nextPosition = -1;
        String target = motif[index];

        for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(target)) {
                nextPosition = i;
                break;
            }
        }
        SpindexerHelper.moveToPosition(nextPosition);
        // move spindexer servo to shoot ball

        return colors;
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

    public static String[] shootMotifBall(String color) {
        int index = -1;

        String[] colors = SpindexerHelper.getColors();

        for (int i = 0; i < 3; i++) {
            if (colors[i].equals(color)) {
                index = i;
                break;
            }
        }

        // ShooterHelper.shoot();
        SpindexerHelper.moveToPosition(index);
        // move spindexer servo to shoot ball

        return colors;
    }

    public static String[] shootMotif(int currentBall) {
        String[] colors;
        colors = shootMotifBall(currentBall);
        shootMotifBall(1 + currentBall, colors);
        shootMotifBall(2 + currentBall, colors);

        return colors;
    }

    public static void shootMotif(int currentBall, String[] colors) {
        shootMotifBall(currentBall, colors);
        shootMotifBall(1 + currentBall, colors);
        shootMotifBall(2 + currentBall, colors);
    }

    public static void intake() {
//        IntakeHelper.start();

        state = State.INTAKE;
        SpindexerHelper.moveToPosition(0);
        while(!DistanceSensorHelper.isBall()) {
            // a a a a a  a a
        }
        state = State.COLORSENSOR;
        SpindexerHelper.moveToPosition(0);
        while(Objects.equals(ColorSensorHelper.getColor(), "Neither")) {
            // do nothing
        }
        String color = ColorSensorHelper.getColor();
        colors[0] = color;
        state = State.INTAKE;
        SpindexerHelper.moveToPosition(2);

//        IntakeHelper.stop();
    }

}