package org.firstinspires.ftc.teamcode.module;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LightShield {
	DcMotor linearActuator = null;
	Servo   UVToggle       = null;

	final double positionOpen = 0;
	final double positionClose = 0.5;

	public LightShield(HardwareMap hardwareMap){
		//set up motors
		linearActuator  = hardwareMap.get(DcMotor.class, "linearActuator");
		UVToggle = hardwareMap.get(Servo.class, "UVToggle");

		// Set wheels to run with encoders
		linearActuator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		linearActuator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}

	public void UVon() {
		UVToggle.setPosition(positionOpen);
	}

	public void UVoff(){
		UVToggle.setPosition(positionClose);
	}

	public void changeHeight(int ticks)  {
		linearActuator.setTargetPosition(linearActuator.getCurrentPosition() + ticks);
	}
}


