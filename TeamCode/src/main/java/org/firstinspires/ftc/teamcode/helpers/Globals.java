package org.firstinspires.ftc.teamcode.helpers;

public class Globals {
    public static double SpindexerSpeed = 0.5;

    public static double DriveSpeed = 1.0;

    public static double SlowDriveSpeed = 0.35;

    public static int ShooterVelocity = 1250;
    public static int ShooterTolerance = 50;

    public static int ForcedArtifacts = 1;

    public static boolean isFieldCentric = false;
    public static boolean isRightStick = false;
    public static int humanWait = 750;
    public static int spindexerShootTime = 15;

    public static void flipStick() {
        if (isRightStick) {
            isRightStick = false;
        } else if (!isRightStick) {
            isRightStick = true;
        }
    }

    public static void flipFieldCentric() {
        if (isFieldCentric) {
            isFieldCentric = false;
        } else if (!isFieldCentric) {
            isFieldCentric = true;
        }
    }

    public enum Alliances {
        RED,
        BLUE
    }

    public static Alliances alliance = Alliances.RED;

    public static void flipAlliance() {
        if (alliance == Alliances.RED) {
            alliance = Alliances.BLUE;
        } else if (alliance == Alliances.BLUE) {
            alliance = Alliances.RED;
        }
    }

    public enum Sides {
        GOAL,
        WALL
    }

    public static Sides side = Sides.GOAL;

    public static void flipSide() {
        if (side == Sides.GOAL) {
            side = Sides.WALL;
        } else if (side == Sides.WALL) {
            side = Sides.GOAL;
        }
    }

    public enum AutoStrategies {
        LEAVE,
        FOURCYCLE
    }

    public static AutoStrategies autoStrategy = AutoStrategies.FOURCYCLE;

    public static void cycleStrategy() {
        if (autoStrategy == AutoStrategies.LEAVE) {
            autoStrategy = AutoStrategies.FOURCYCLE;
        } else if (autoStrategy == AutoStrategies.FOURCYCLE) {
            autoStrategy = AutoStrategies.LEAVE;
        }
    }

    public enum Colors {
        PPG(0),
        PGP(1),
        GPP(2);

        public final int index;

        Colors(int index) {
            this.index = index;
        }
    }
    public static Colors motif;

    public static double servoDown = 0.52;

    public static double intakeSpeed = 0.6;
}
