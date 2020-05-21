package org.firstinspires.ftc.teamcode.ImageProc.util;

import org.firstinspires.ftc.teamcode.ImageProc.util.image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.List;

public class UVDetectionCore {
	public static  List<MatOfPoint> contours;
	static public Mat display;

	public static void process(Mat wUV, Mat woUV, int xTrans, int yTrans) {
		
		image UV = new image(wUV,"With UV Light");
		image noUV = new image(woUV,"Without UV Light");

		//noise Map
		UV.findEdges();
		noUV.findEdges();

			UV.show();
			noUV.show();
		
		UV.translate(xTrans, yTrans);


		noUV.dilate(5);

		//image.features(UV, noUV);
		UV.subtract(noUV);
			//noUV.show();
			//UV.show();



		//remove low frq noise
		image UVmask = UV.clone();
		UVmask.binarize(36); //bottleneck: takes 5 mili seconds
		UVmask.invert();		
		UV.subtract(UVmask);

			//UV.show();
		
		//find shapes. Perhaps another bottleneck: 11 mili seconds. That's a lot!
		contours = UV.contours(false);
		image.sortContours(contours);
		
		image preDisplay = new image(wUV, "display");
		//display.translate(xTrans,yTrans);
		preDisplay.drawContours(contours);
		display = preDisplay.me;

	}
}
