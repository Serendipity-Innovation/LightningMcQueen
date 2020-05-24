package org.firstinspires.ftc.teamcode.module;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.pose;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.utils.Const;
import org.firstinspires.ftc.teamcode.utils.UnitConversion;

import static org.firstinspires.ftc.teamcode.utils.Const.distanceBetweenWheels;

public class TankOdometry {
	LinearOpMode opMode;
	DcMotor left, right;
	public Imu imu;

	// inches, radians
	public pose position = new pose(0,0,0);

	public TankOdometry(OpMode opMode){
		if(!(opMode instanceof LinearOpMode)) opMode.telemetry.addData("Error","Linear Op Mode");
		this.opMode = (LinearOpMode) opMode;

		left = opMode.hardwareMap.get(DcMotor.class, "leftWheel");
		right = opMode.hardwareMap.get(DcMotor.class, "rightWheel");
		imu = new Imu(opMode.hardwareMap);

		new Thread(this::cycle).start();

	}

	private void cycle(){
		double leftPos = 0;
		double rightPos = 0;

		while (opMode.opModeIsActive()){

			double leftDiffTicks = left.getCurrentPosition() - leftPos;
			double rightDiffTicks = right.getCurrentPosition() - rightPos;
			leftPos = leftDiffTicks + leftPos;
			rightPos = rightDiffTicks + rightPos;

			double leftDiff = UnitConversion.getTicksToInches(leftDiffTicks);
			double rightDiff = UnitConversion.getTicksToInches(rightDiffTicks);

			pose intrinsicDiff = getDisplacement(leftDiff, rightDiff);
			pose extrinsicDiff = rotateAbout(intrinsicDiff, position);

			pose imuDiff = imu.getPositionChange();

			//increment position with weights
			position = pose.add(position,pose.addWeighted(extrinsicDiff, imuDiff,0.7,0.3));

		}
	}

	//did not use, for reference
	private void updatePosition(double dx, double dy, double heading, Position position){
		position.x += dx * Math.cos(heading+90);
		position.y += dx * Math.sin(heading+90);
		position.x += dy * Math.cos(heading);
		position.y += dy * Math.sin(heading);
	}

	/**
	 * Target's values are in terms of the coordinate system of anchor (with rotation included)
	 * Calculates target's REAL values extrinsically
	 * @param target
	 * @param anchor
	 * @return
	 */
	pose rotateAbout(pose target, pose anchor){
		pose diff = pose.subtract(target,anchor);

		double xPrime = diff.x * Math.cos(anchor.rot) - diff.y * Math.sin(anchor.rot);
		double yPrime = diff.x * Math.sin(anchor.rot) + diff.y * Math.cos(anchor.rot);
		double rotPrime = target.rot += anchor.rot % (2 * Math.PI);

		return new pose(xPrime, yPrime, rotPrime);
	}

	/**
	 * See _getDisplacement
	 * @param leftDelta
	 * @param rightDelta
	 * @return
	 */
	pose getDisplacement (double leftDelta, double rightDelta){
		if(leftDelta < rightDelta){
			return _getDisplacement(leftDelta, rightDelta);
		}
		if(rightDelta < leftDelta){
			pose displacement = _getDisplacement(leftDelta, rightDelta);
			displacement.x *= -1;
			displacement.rot *= -1;
		}

		return  new pose(0, leftDelta, 0);
	}


	/**
	 * get displacement of tank drive and angle change
	 * see document in Serendipity project files
	 * @param innerDelta inches
	 * @param outerDelta inches
	 * @return displacement of tank drive and angle change
	 */
	pose _getDisplacement (double innerDelta, double outerDelta){

		//robot rotates about a point (not necessarily between wheels)
		//this angle is the angle that the bot rotates around
		double angle = innerDelta / distanceBetweenWheels;
		//this is the radius of the circle that inner wheel makes
		double radius = (innerDelta * distanceBetweenWheels) / (outerDelta - innerDelta);

		double dx = (radius + 0.5 * distanceBetweenWheels) * Math.cos(angle);
		double dy = (radius + 0.5 * distanceBetweenWheels) * Math.sin(angle);

		return new pose(dx, dy, angle);

		//DID YOU DID YOU DID YOU KNOW?
		//IN JAVA JAVA JAVA YOU CAN CAN
		//IMPORT NOT ONLY: NOT ONLY CLASSES
		//BUT ALSO BUT ALSO ALSO FIELDS!!!!!!!!!
		//YAAAY FOR C CONVENTIONS

		//You can also <from __future__ import brackets> in python
	}

}
