package org.firstinspires.ftc.teamcode.opmodes.auto;

public class AutoParams {
    public enum Alliances {
        RED,
        BLUE
    }

    public enum Strategies {
        FRONT,
        BACK
    }

    public Alliances alliance;
    public Strategies strategy;
    public boolean gate;
    public int spikes;
    public boolean humanPlayer;
    public int delay;
}