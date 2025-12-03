package org.firstinspires.ftc.teamcode;

public class Configuration {
    // robot config
    public static configItem distance = new configItem("distance", true);

    public static configItem FR = new configItem("FR", true);
    public static configItem FL = new configItem("FL", true);
    public static configItem BR = new configItem("BR", true);
    public static configItem BL = new configItem("BL", true);

    public static configItem intake = new configItem("intake", true);
    public static configItem spindexerMotor = new configItem("spindexer", true);
    public static configItem spindexerServo = new configItem("flap", true);
    public static configItem shooter = new configItem("shooter", true);

    public static configItem odometry = new configItem("odometry", true);

    public static configItem light  = new configItem("light", false);

    public static class configItem {
        public String itemName;
        public boolean itemActive;

        public configItem(String key, boolean isActive) {
            itemName = key;
            itemActive = isActive;
        }
    }
}