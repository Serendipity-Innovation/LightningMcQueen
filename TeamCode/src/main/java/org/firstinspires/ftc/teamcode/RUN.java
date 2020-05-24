/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.odometryWriteManager;
import org.firstinspires.ftc.teamcode.ImageProc.Detector;
import org.firstinspires.ftc.teamcode.module.Robot;
import org.firstinspires.ftc.robotcontroller.internal.pose;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Run", group="Linear Opmode")

public class RUN extends LinearOpMode {
	Robot      robot;
	Detector   ultraviolet;
	List<pose> poses = new ArrayList<pose>();

	@Override
	public void runOpMode() {
		//INITIALIZE
		telemetry.addData("Status", "Initialized");
		telemetry.update();
		telemetry.setAutoClear(false);

		waitForStart();

		//BEGIN
		robot = new Robot(this);
		ultraviolet = new Detector(this);
		ultraviolet.start();

		//LOOP
		while (opModeIsActive()) {
			// Move at 1rot/sec and turn left
			robot.tankDrivetrain.setVelocity(360,-0.7);
			delay(1000L);

			robot.tankDrivetrain.setVelocity(0,0);
			delay(200L);

			spotDetection(poses);
			delay(500L);

			telemetry.addData("Heading", robot.tankOdometry.position.rot);
			telemetry.update();
		}

		ultraviolet.close();
		odometryWriteManager.record(poses.toArray(new pose[0]));
	}

	void spotDetection(List<pose> poses){
		if(ultraviolet.contours.size() > 0)
			poses.add(robot.tankOdometry.position.setHasDirt());
		else poses.add(robot.tankOdometry.position);


		//set light to regular
		robot.lightShield.UVoff();
		delay(100L);
		ultraviolet.setInputType(Detector.inputType.noUV);

		//set light to UV
		robot.lightShield.UVon();
		delay(100L);
		ultraviolet.setInputType(Detector.inputType.UV);

		//update to tele
		telemetry.addData("Spots Found",ultraviolet.contours.size());

	}

	void delay(Long delay){
		Long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < delay) {/*wait*/}
	}
}
