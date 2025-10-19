package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import org.firstinspires.ftc.teamcode.mechanisms.spindexer.shooter.ShooterHelper;

public class Spindexer2 {
    public static String[] shootMotifBall(int index) {
        String[] motifVar = new String[3];
        motifVar[0] = "Green";
        motifVar[1] = "Purple";
        motifVar[2] = "Purple"; // placeholder motif because no limelight yet

        String[] spindexerVar = SpindexerHelper.getColors();


        int y = 0;
        for (int i = 0; i < 3; i++) {
            if (spindexerVar[0] == motifVar[i] && y != 1) {
                y = 1;
                SpindexerHelper.moveToNextPosition();
                //SpindexerHelper.moveServo();
                //ShooterHelper.shoot();
                SpindexerHelper.moveToPosition(0);
            }
            else if (spindexerVar[1] == motifVar[i] && y != 2) {
                y = 2;
                SpindexerHelper.moveToPosition(2);
                //SpindexerHelper.moveServo();
                //ShooterHelper.shoot();
                SpindexerHelper.moveToNextPosition();
            }
            else if (spindexerVar[2] == motifVar[i] && y != 3) {
                y = 3;

                //SpindexerHelper.moveServo();
                //ShooterHelper.shoot();
            }
        }

        return spindexerVar;
    }

    public static void shootMotif() {
        shootMotifBall(0);
        shootMotifBall(1);
        shootMotifBall(2);
    }
}
