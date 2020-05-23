package org.firstinspires.ftc.teamcode.module;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
	public LightShield    lightShield;
	public TankDrivetrain tankDrivetrain;
	public TankOdometry   tankOdometry;

	/**
	 * Declare after robot starts
	 * @param opMode
	 */
	public Robot(OpMode opMode){
		HardwareMap hardwareMap = opMode.hardwareMap;

		lightShield = new LightShield(hardwareMap);
		tankDrivetrain = new TankDrivetrain(hardwareMap);
		tankOdometry = new TankOdometry(hardwareMap);

		opMode.telemetry.addData("Robot", "Initialized");
	}


}
