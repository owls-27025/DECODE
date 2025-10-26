package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.spindexer.distanceSensor.DistanceSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter.ShooterHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper;

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
        motif[2] = "Purple"; // placeholder motif because no limelight yet

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
        for (int i = 0; i < 3; i++) {
            SpindexerHelper.moveToPosition(i);
            long arriveDeadline = System.currentTimeMillis() + 2000;
            while (SpindexerHelper.SpindexerMotor.isBusy() && System.currentTimeMillis() < arriveDeadline) {
                try { Thread.sleep(10); } catch (InterruptedException ignored) {}
            }
            long goneDeadline = System.currentTimeMillis() + 250;
            while (DistanceSensorHelper.isBall() && System.currentTimeMillis() < goneDeadline) {
                try { Thread.sleep(10); } catch (InterruptedException ignored) {}
            }
            long presentDeadline = System.currentTimeMillis() + 3000;
            while (!DistanceSensorHelper.isBall() && System.currentTimeMillis() < presentDeadline) {
                try { Thread.sleep(10); } catch (InterruptedException ignored) {}
            }
            if (!DistanceSensorHelper.isBall()) {
                colors[i] = "Neither";
                continue;
            }
            try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            String color = ColorSensorHelper.getColor();
            long confirmDeadline = System.currentTimeMillis() + 500;
            while (System.currentTimeMillis() < confirmDeadline) {
                String newColor = ColorSensorHelper.getColor();
                if (newColor.equals(color)) break;
                color = newColor;
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            }
            colors[i] = color;
            long clearDeadline = System.currentTimeMillis() + 800;
            while (DistanceSensorHelper.isBall() && System.currentTimeMillis() < clearDeadline) {
                try { Thread.sleep(10); } catch (InterruptedException ignored) {}
            }
        }

//        IntakeHelper.stop();
    }

}