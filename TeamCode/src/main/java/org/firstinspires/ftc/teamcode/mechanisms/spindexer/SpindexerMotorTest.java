package org.firstinspires.ftc.teamcode.mechanisms.spindexer;

import static org.firstinspires.ftc.teamcode.mechanisms.spindexer.SpindexerHelper.SpindexerMotor;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.colorSensor.ColorSensorHelper;
import org.firstinspires.ftc.teamcode.mechanisms.spindexer.distanceSensor.DistanceSensorHelper;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@TeleOp(name = "Spindexer Motor Test", group = "Spindexer")
public class SpindexerMotorTest extends OpMode {

    //based on spindexer motor ticks per rotation
    private final static int HALF_SLOT_TICKS = 48;
    private final static int SLOT_TICKS = 96;
    private long loopIndex = 0L;
    //There are 6 positions on the spindexer each represented as below
    //              3
    //      2       *       4
    //          *   *   *
    //              *
    //          *   *   *
    //      1       *       5
    //              0

    private final double[] intakePositions = new double[]{1.0,3.0,5.0};
    private final double[] shootPositions = new double[]{0.0,2.0,4.0};
    private String[] colors ;
    private int intakeArtifactCount;
    @Override
    public void init() {
        SpindexerHelper.init(hardwareMap, telemetry);
        DistanceSensorHelper.init(hardwareMap);
        ColorSensorHelper.init(hardwareMap);
    }
    public void loop() {
        //motorPositionTelemetry();
        //If A is pressed, move the spindexer half a slot to the next position.
        if (gamepad1.aWasPressed()){
            moveSpindexerMotor(HALF_SLOT_TICKS);
        }
        if (gamepad1.bWasPressed()){
            moveToIntakePosition();
        }
        if (gamepad1.xWasPressed()){
           intakeArtifactsAndReadColors();
        }
        loopIndex++;

    }

    private void moveSpindexerMotor(int ticks) {
        int targetTicks = SpindexerMotor.getCurrentPosition() + ticks;
        moveSpindexerToTargetPosition(targetTicks);
        telemetry.addData("current ticks", SpindexerMotor.getCurrentPosition());
        telemetry.addData("target ticks", SpindexerMotor.getTargetPosition());

    }

    private void moveSpindexerToTargetPosition(int targetPosition){
        SpindexerMotor.setTargetPosition(targetPosition);
        SpindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SpindexerMotor.setPower(0.5);
    }

    //This method moves the spindexer to the intake position no matter in which position the
    //spindexer is in
    private void moveToIntakePosition() {
        int currPositionTicks = SpindexerMotor.getCurrentPosition();
        int targetPosition;
        //Check if the current position is already Intake position
        double currPos = findExactPosition(currPositionTicks);
        telemetry.addLine("Moving to intake position....Before");
        telemetry.addData("Loop Index..", loopIndex);
        telemetry.addData("Current position Ticks:", currPositionTicks);
        telemetry.addData("Current position:", currPos);
        if (DoubleStream.of(intakePositions).anyMatch(n -> n == currPos)) {
            targetPosition = currPositionTicks;
        } else {
            int nextHalfSlotTicks = (currPositionTicks + HALF_SLOT_TICKS - currPositionTicks % HALF_SLOT_TICKS);
            double nextPos = (double)((double)nextHalfSlotTicks / HALF_SLOT_TICKS) % 6;
            telemetry.addData("Next Half slot ticks", nextHalfSlotTicks);
            telemetry.addData("Next Pos:", nextPos);
            //Check if the next position is one of the Intake positions - 1, 3, 5
            if (DoubleStream.of(intakePositions).anyMatch(n -> n == nextPos)) {
                targetPosition = nextHalfSlotTicks;
            } else {
                targetPosition = nextHalfSlotTicks + HALF_SLOT_TICKS;
            }
        }
        telemetry.addData("Target Intake position:", targetPosition);

        moveSpindexerToTargetPosition(targetPosition);
        telemetry.addData("Current position after intake motor moved:", SpindexerMotor.getTargetPosition());
        //telemetry.update();
    }

    private double findExactPosition(int ticks) {
        return (double)(ticks/HALF_SLOT_TICKS) % 6;
    }

    /**
     * This method is used to intake 3 artifacts and read the colors into the colors array
     */
    private void intakeArtifactsAndReadColors(){
        colors = new String[3];
        moveToIntakePosition();
        for (int i=0;i<3;i++) {
            intakeArtifact();
            while(Objects.equals(ColorSensorHelper.getColor(), "Neither")) {
                // nothing
            }
            colors[i] = ColorSensorHelper.getColor();
            telemetry.addData("Color: ",colors[i]);
            telemetry.update();
            moveSpindexerMotor(96);
            telemetry.addLine("thingy");
            telemetry.addData("current ticks1", SpindexerMotor.getCurrentPosition());
            telemetry.addData("target ticks1", SpindexerMotor.getTargetPosition());
            telemetry.update();
//            while(Math.abs(SpindexerMotor.getCurrentPosition() - SpindexerMotor.getTargetPosition()) > 20) {
//                telemetry.addData("distanccee", DistanceSensorHelper.distance.getDistance(DistanceUnit.MM));
//                telemetry.update();
//            }
        }
//        telemetry.update();
    }
    private void intakeArtifact(){
        telemetry.addData("Intake Artifact Count before intake:", intakeArtifactCount);
        while(!DistanceSensorHelper.isBall()){
            telemetry.addLine("Waiting for ball intake...");
            //telemetry.update();
        }
        if(DistanceSensorHelper.isBall()) {
            //Move to color sensor position
            moveSpindexerMotor(HALF_SLOT_TICKS);
            intakeArtifactCount++;
        }
        telemetry.addData("Intake Artifact Count:", intakeArtifactCount);
    }

    private void motorPositionTelemetry(){
        telemetry.addData("Loop Index ----------------------", loopIndex);
        telemetry.addData("Target Spindexer Position", SpindexerMotor.getTargetPosition());
        telemetry.addData("Slot Position: ", findExactPosition(SpindexerMotor.getCurrentPosition()));
        telemetry.addData("Current Spindexer Position: ", SpindexerMotor.getCurrentPosition());
        telemetry.update();
    }
}
