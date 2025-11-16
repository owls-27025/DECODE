package org.firstinspires.ftc.teamcode;

public class Configuration {
    // robot config
    public static configItem colorSensor = new configItem("color sensor", true);

    public static configItem FR = new configItem("frontRight", true);
    public static configItem FL = new configItem("frontLeft", true);
    public static configItem BR = new configItem("backRight", true);
    public static configItem BL = new configItem("backLeft", true);

    public static configItem intake = new configItem("intake", true);
    public static configItem spindexerMotor = new configItem("spindexer", true);
    public static configItem spindexerServo = new configItem("flap", true);
    public static configItem shooter = new configItem("shooter", true);

    public static configItem odometry = new configItem("odometry", true);



    public static class configItem {
        public String itemName;
        public boolean itemActive;

        public configItem(String key, boolean isActive) {
            itemName = key;
            itemActive = isActive;
        }
    }
}