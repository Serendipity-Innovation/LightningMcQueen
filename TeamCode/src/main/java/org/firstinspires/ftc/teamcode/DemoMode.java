package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "DemoMode", group = "BasicOpMode")
public class DemoMode extends LinearOpMode {
    private static final double coverLightPosition = -1.0;
    private static final double unCoverLightPosition = 1.0;
    private DcMotor leftWheel;
    private DcMotor rightWheel;
    private DcMotor linearActuator;
    private Servo uvToggle;

    @Override
    public void runOpMode() throws InterruptedException {
        // hardwareMap initialization
        leftWheel = hardwareMap.dcMotor.get("leftWheel");
        rightWheel =  hardwareMap.dcMotor.get("rightWheel");
        linearActuator = hardwareMap.dcMotor.get("linearActuator");
        uvToggle = hardwareMap.servo.get("uvToggle");

        // Use linear actuator encoders
        linearActuator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearActuator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        int currentPosition;
        int moveAmount;

        waitForStart();
        while (opModeIsActive()){
            // Drivetrain
            leftWheel.setPower(-gamepad2.left_stick_y);
            rightWheel.setPower(-gamepad2.right_stick_y);

            // Linear Actuator movement
            currentPosition = linearActuator.getCurrentPosition();
            if (gamepad2.dpad_down){
                moveAmount = currentPosition - 30;
                linearActuator.setTargetPosition(moveAmount);
            }
            if (gamepad2.dpad_up){
                moveAmount = currentPosition + 30;
                linearActuator.setTargetPosition(moveAmount);
            }

            // UV Toggle
            if (gamepad2.a){ // a uncovers the light
                uvToggle.setPosition(unCoverLightPosition);
                telemetry.addData("UncoverLightPosition", "Uncovered, good job Nav!");
            }

            if (gamepad2.b){ // b covers the light
                uvToggle.setPosition(coverLightPosition);
                telemetry.addData("CoverLightPosition", "Covered, good job Nav!");
            }
            telemetry.update();
            idle();
        }
    }
}
