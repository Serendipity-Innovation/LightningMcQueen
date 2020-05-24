package org.firstinspires.ftc.robotcontroller.internal;

public class odometryWriteManager extends fileManager {
	public static void record(pose[] poses){
		StringBuilder data = new StringBuilder();
		for(pose pose: poses){
			data.append(pose.toString());
			data.append("\n");
		}
		String toWrite = data.toString();
		String filename = String.valueOf(numFiles()) + ".txt";

		write(toWrite, filename);
	}

}
