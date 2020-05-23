package org.firstinspires.ftc.teamcode.module;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.pose;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class Imu {
	BNO055IMU imuDevice;

	Orientation angles;
	pose        position = new pose(0,0,0);
	/**
	 * Handles imu setup and data requests without hassle
	 * Please instantiate after waitForStart()
	 * @param hardwareMap
	 */
	public Imu(HardwareMap hardwareMap){
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
		parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
		parameters.loggingEnabled      = true;
		parameters.loggingTag          = "IMU";
		parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

		// Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
		// on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
		// and named "imu".
		imuDevice = hardwareMap.get(BNO055IMU.class, "gyroscope");
		imuDevice.initialize(parameters);

		// Start the logging of measured acceleration
		imuDevice.startAccelerationIntegration(new Position(), new Velocity(), 1000);
	}

	/**
	 * Radians
	 * @return heading in Radians
	 */
	public double getHeading() {
		angles = imuDevice.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
		return angles.thirdAngle;
	}

	/**
	 * Gives change in position since last time method was called (METERS)
	 * I think x,y is flat on ground coordinate axes
	 */
	public pose getPositionChange(){
		Position current = imuDevice.getPosition();
		pose currentPose = new pose(0,0,0);
		//TODO check axes
		currentPose.x = current.x;
		currentPose.y = current.y;
		currentPose.rot = getHeading();

		pose diff = pose.subtract(currentPose, position);

		position = currentPose.clone();

		return position;
	}
}
