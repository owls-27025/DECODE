package org.firstinspires.ftc.teamcode;

public class Globals {
    public static double SpindexerSpeed;

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
