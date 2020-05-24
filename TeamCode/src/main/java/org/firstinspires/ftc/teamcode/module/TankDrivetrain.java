package org.firstinspires.ftc.teamcode.module;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class TankDrivetrain {

	DcMotorEx left, right;
	public TankDrivetrain(HardwareMap hardwareMap){

		left  = (DcMotorEx) hardwareMap.get(DcMotor.class, "leftWheel");
		right = (DcMotorEx) hardwareMap.get(DcMotor.class, "rightWheel");

		// Wheels are mounted opposite so one must be reversed
		left.setDirection(DcMotor.Direction.FORWARD);
		right.setDirection(DcMotor.Direction.REVERSE);

		//enforce braking
		left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		// Set wheels to run with encoders
		left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		//reset encoders to 0
		left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}

	/**
	 * Degrees per second
	 * @param turn -1 for full left, 1 for full right
	 */
	public void setVelocity(double speed,double turn){
		left.setVelocity(speed * scaleTo(-turn), AngleUnit.DEGREES);
		right.setVelocity(speed * scaleTo(turn), AngleUnit.DEGREES);
	}

	/**
	 * Scale -1-1 TO 0-1
	 * @param num
	 * @return
	 */
	double scaleTo(double num){
		return 0.5 * (num + 1);
	}
}
