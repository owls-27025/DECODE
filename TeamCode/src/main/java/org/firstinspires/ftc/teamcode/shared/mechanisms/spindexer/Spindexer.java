package org.firstinspires.ftc.teamcode.shared.mechanisms.spindexer;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;

import java.util.Arrays;

public class Spindexer {
    public final double[] intakePositions = new double[]{1.0, 3.0, 5.0};
    public final double[] shootPositions  = new double[]{0.0, 2.0, 4.0};

    private final Robot robot;
    private final DcMotor motor;
    private final Servo flap;

    private static final int SLOTS = 3;
    private static final int POSITIONS = 6;

    private final int tpr;
    private final int single;

    private final String[] colors = new String[SLOTS];
    private final boolean[] isIntakePos = new boolean[POSITIONS];
    private final boolean[] isShootPos  = new boolean[POSITIONS];

    public Spindexer(Robot robot) {
        this.robot = robot;
        this.tpr = robot.tpr;
        this.single = tpr * 2;

        motor = robot.registerItem(DcMotor.class, robot.config.spindexerMotor);
        flap  = robot.registerItem(Servo.class, robot.config.spindexerServo);

        Arrays.fill(isIntakePos, false);
        Arrays.fill(isShootPos, false);
        for (double p : intakePositions) isIntakePos[((int) p) % POSITIONS] = true;
        for (double p : shootPositions)  isShootPos[((int) p) % POSITIONS]  = true;

        if (motor != null) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(0);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setDirection(DcMotor.Direction.FORWARD);

            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(robot.spindexerSpeed);
        }

        if (flap != null) {
            flap.setPosition(robot.servoDownPos);
        }

        Arrays.fill(colors, "-");
    }

    private void goToTicks(int targetTicks) {
        if (motor == null) return;
        motor.setTargetPosition(targetTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(robot.spindexerSpeed);
    }

    private void goRelativeTicks(int deltaTicks) {
        if (motor == null) return;
        goToTicks(motor.getTargetPosition() + deltaTicks);
    }

    public void moveToNextPosition()     { goRelativeTicks(single); }
    public void moveToPreviousPosition() { goRelativeTicks(-single); }
    public void moveHalfPosition(boolean forward) { goRelativeTicks(forward ? tpr : -tpr); }

    public void flapUp() {
        if (flap != null) flap.setPosition(1.0);
    }

    public void flapDown() {
        if (flap != null) flap.setPosition(robot.servoDownPos);
    }

    /** 0..5 half-slot index */
    public int findPosition() {
        if (motor == null) return 0;
        int ticks = motor.getCurrentPosition();
        return floorMod(ticks / tpr, POSITIONS);
    }

    public void intakePosition() { snapToAllowed(isIntakePos); }
    public void shootPosition()  { snapToAllowed(isShootPos); }

    private void snapToAllowed(boolean[] allowed) {
        if (motor == null) return;

        int currTicks = motor.getCurrentPosition();

        boolean onHalfSlot = (currTicks % tpr) == 0;
        int currPos = floorMod(currTicks / tpr, POSITIONS);
        if (onHalfSlot && allowed[currPos]) {
            goToTicks(currTicks);
            return;
        }

        int nextHalfSlotTicks = currTicks + (tpr - floorMod(currTicks, tpr));
        int nextPos = floorMod(nextHalfSlotTicks / tpr, POSITIONS);

        int target = allowed[nextPos] ? nextHalfSlotTicks : nextHalfSlotTicks + tpr;
        goToTicks(target);
    }

    private static int floorMod(int a, int b) {
        int r = a % b;
        return (r < 0) ? r + b : r;
    }

    public boolean isBusy() {
        return motor != null && motor.isBusy();
    }

    public void setDirection(DcMotor.Direction direction) {
        if (motor != null) motor.setDirection(direction);
    }

    public int getCurrent() {
        return motor == null ? 0 : motor.getCurrentPosition();
    }

    public int getTarget() {
        return motor == null ? 0 : motor.getTargetPosition();
    }


    public void setFlapPosition(double pos) {
        if (flap != null) flap.setPosition(pos);
    }
    public void reset() {
        if (motor == null) return;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(robot.spindexerSpeed);
    }
}
