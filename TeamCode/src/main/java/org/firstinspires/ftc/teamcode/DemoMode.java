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

        leftWheel.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()){
            // Drivetrain
            leftWheel.setPower(-gamepad2.left_stick_y);
            rightWheel.setPower(-gamepad2.right_stick_y);

            // Linear Actuator movement
            if (gamepad2.dpad_down){
                linearActuator.setPower(5);
                Thread.sleep(100);
            }
            if (gamepad2.dpad_up){
                linearActuator.setPower(-5);
                Thread.sleep(100);
            }

            // UV Toggle
            if (gamepad2.a){ // a uncovers the light
                uvToggle.setPosition(unCoverLightPosition);
                telemetry.addData("UncoverLightPosition", "Uncovered, good job Nav!");
                Thread.sleep(1000);
            }

            if (gamepad2.b){ // b covers the light
                uvToggle.setPosition(coverLightPosition);
                telemetry.addData("CoverLightPosition", "Covered, good job Nav!");
                Thread.sleep(1000);
            }
            telemetry.update();
            idle();
        }
    }
}
