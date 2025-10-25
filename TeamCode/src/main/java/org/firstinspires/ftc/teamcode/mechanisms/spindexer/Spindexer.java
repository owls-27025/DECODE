package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import org.firstinspires.ftc.teamcode.mechanisms.spindexer.intake.IntakeHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter.ShooterHelper;

public class Spindexer {
    static String[] motif = new String[3];
    static String[] colors = new String[3];
    public static void shootMotifBall(int index) {
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
    }

    public static void shootMotifBall(int index, String[] colors) {
        String[] motif = new String[3];
        motif[0] = "Green";
        motif[1] = "Purple";
        motif[2] = "Purple"; // placeholder motif because no limelight yet

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
    }

    public static void shootMotif() {
        shootMotifBall(0);
        shootMotifBall(1);
        shootMotifBall(2);
    }

    public static void shootMotif(String[] colors) {
        shootMotifBall(0, colors);
        shootMotifBall(1, colors);
        shootMotifBall(2, colors);
    }

    public static void intake() {
        IntakeHelper.start();

        for (int i = 0; i < 3; i++) {
            SpindexerHelper.moveToPosition(0);
            double currentTime = System.currentTimeMillis();
            while(System.currentTimeMillis() < currentTime + 1500) {
                Spindexer.colors[i] = ColorSensorHelper.getColor();
            }
        }

        IntakeHelper.stop();
    }
}
