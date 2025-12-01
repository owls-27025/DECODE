package org.firstinspires.ftc.teamcode.helpers;

public class Configuration {
    // robot config
    public ItemConfig distance = new ItemConfig("distance", true);

    public ItemConfig FR = new ItemConfig("FR", true);
    public ItemConfig FL = new ItemConfig("FL", true);
    public ItemConfig BR = new ItemConfig("BR", true);
    public ItemConfig BL = new ItemConfig("BL", true);

    public ItemConfig intake = new ItemConfig("intake", true);
    public ItemConfig spindexerMotor = new ItemConfig("spindexer", true);
    public ItemConfig spindexerServo = new ItemConfig("flap", true);
    public ItemConfig shooter = new ItemConfig("shooter", true);

    public ItemConfig odometry = new ItemConfig("odometry", true);

    public ItemConfig light = new ItemConfig("light", false);
    public ItemConfig limelight = new ItemConfig("limelight", true);

    public static class ItemConfig {
        public String itemName;
        public boolean itemActive;

        public ItemConfig(String key, boolean isActive) {
            itemName = key;
            itemActive = isActive;
        }
    }
}