package org.firstinspires.ftc.teamcode.options;

public class Globals {
    public static double SpindexerSpeed = 0.1;

    public static double DriveSpeed = 1.0;

    public static double SlowDriveSpeed = 0.35;

    public static int ShooterVelocity = 1100;
    public static int ShooterTolerance = 15;

    public static int ForcedArtifacts = 1;

    public static boolean isFieldCentric = false;
    public static boolean isRightStick = false;
    public static int humanWait = 750;

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
}
