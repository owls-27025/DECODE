package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controls {
    public Gamepad driver1;
    public Gamepad driver2;

    public boolean d1AJustPressed;
    public boolean d1BJustPressed;
    public boolean d1XJustPressed;
    public boolean d1YJustPressed;
    public boolean d1StartJustPressed;
    public boolean d1BackJustPressed;

    public boolean d2AJustPressed;
    public boolean d2BJustPressed;
    public boolean d2XJustPressed;
    public boolean d2YJustPressed;
    public boolean d2StartJustPressed;
    public boolean d2BackJustPressed;

    public boolean d1A, d1B, d1X, d1Y, d1Start, d1Back;
    public boolean d1LBumper, d1RBumper;
    public boolean d2A, d2B, d2X, d2Y, d2Start, d2Back;
    public boolean d2LBumper, d2RBumper;

    public boolean inhibitButtons;
    public boolean slowMode;
    public boolean resetIMU;

    private boolean prevD1A, prevD1B, prevD1X, prevD1Y, prevD1Start, prevD1Back;
    private boolean prevD2A, prevD2B, prevD2X, prevD2Y, prevD2Start, prevD2Back;

    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        driver1 = gamepad1;
        driver2 = gamepad2;

        // ----- Driver 1 -----
        d1A = gamepad1.a;
        d1B = gamepad1.b;
        d1X = gamepad1.x;
        d1Y = gamepad1.y;
        d1Start = gamepad1.start;
        d1Back = gamepad1.back;
        d1LBumper = gamepad1.left_bumper;
        d1RBumper = gamepad1.right_bumper;

        d1AJustPressed     = d1A && !prevD1A;
        d1BJustPressed     = d1B && !prevD1B;
        d1XJustPressed     = d1X && !prevD1X;
        d1YJustPressed     = d1Y && !prevD1Y;
        d1StartJustPressed = d1Start && !prevD1Start;
        d1BackJustPressed  = d1Back && !prevD1Back;

        // ----- Driver 2 -----
        d2A = gamepad2.a;
        d2B = gamepad2.b;
        d2X = gamepad2.x;
        d2Y = gamepad2.y;
        d2Start = gamepad2.start;
        d2Back = gamepad2.back;
        d2LBumper = gamepad2.left_bumper;
        d2RBumper = gamepad2.right_bumper;

        d2AJustPressed     = d2A && !prevD2A;
        d2BJustPressed     = d2B && !prevD2B;
        d2XJustPressed     = d2X && !prevD2X;
        d2YJustPressed     = d2Y && !prevD2Y;
        d2StartJustPressed = d2Start && !prevD2Start;
        d2BackJustPressed  = d2Back && !prevD2Back;

        inhibitButtons =
                (d1Start && (d1A || d1B)) ||
                        (d2Start && (d2A || d2B));

        slowMode = d1LBumper;

        resetIMU = d1StartJustPressed;

        prevD1A = d1A;
        prevD1B = d1B;
        prevD1X = d1X;
        prevD1Y = d1Y;
        prevD1Start = d1Start;
        prevD1Back = d1Back;

        prevD2A = d2A;
        prevD2B = d2B;
        prevD2X = d2X;
        prevD2Y = d2Y;
        prevD2Start = d2Start;
        prevD2Back = d2Back;
    }
}
