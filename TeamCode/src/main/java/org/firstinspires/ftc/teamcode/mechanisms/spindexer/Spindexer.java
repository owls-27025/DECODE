package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter.ShooterHelper;

public class Spindexer {
    public static void shootMotifBall(int index) {
        String[] motif = new String[3];
        motif[0] = "Green";
        motif[1] = "Purple";
        motif[2] = "Purple"; // placeholder motif because no limelight yet

        String[] colors = new String[3];
        colors[0] = "Purple";
        colors[1] = "Green";
        colors[2] = "Purple"; // placeholder for positions of each ball because not implemented yet

        ShooterHelper.shoot();

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
}
