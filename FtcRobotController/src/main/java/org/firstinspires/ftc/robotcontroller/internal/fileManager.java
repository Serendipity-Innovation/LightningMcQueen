package org.firstinspires.ftc.teamcode.utils;

import android.app.Activity;
import android.os.Environment;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class fileManager {
	public static boolean readWriteAvailable = true;

	static final String parentFolder = "OdometryLog";
	static File externalFile;

	public static void setUp (Activity activity) {
		if(!isExternalStorageAvailable() || isExternalStorageReadOnly()){
			readWriteAvailable = false;
		}else{
			externalFile = activity.getExternalFilesDir(parentFolder);
		}
	}

	private static boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	private static boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	public static int numFiles(){
		File[] files = externalFile.listFiles();
		return files.length;
	}

	public static void write(String toWrite, String filename){
		try {
			File ExternalFile = new File(externalFile, filename);
			externalFile.mkdir();
			FileOutputStream fos = new FileOutputStream(ExternalFile);
			fos.write(toWrite.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//not necessary; for reference
	static String read(String filename){
		StringBuilder data = new StringBuilder();

		try {
			File ExternalFile = new File(externalFile, filename);
			FileInputStream fis = new FileInputStream(ExternalFile);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			while ((strLine = br.readLine()) != null) {
				data.append(strLine);
			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data.toString();
	}
}
