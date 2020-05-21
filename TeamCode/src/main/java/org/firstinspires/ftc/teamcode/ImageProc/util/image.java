package org.firstinspires.ftc.teamcode.ImageProc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

public class image{
	static Scalar black = new Scalar(0,0,0);
	static Scalar white = new Scalar(255,255,255);
	
	static Point tl = new Point(0,0);
	Point br;
	
	//in favor of separating the ~100 Mat methods from 
	//our own image methods, we've elected to not make
	//image extend Mat
	public Mat me;
	String name = "";
	
	public image(String uri, String name) {
		me = Imgcodecs.imread(uri);
		br = new Point(me.height(),me.width());
		this.name = name;
	}
	
	public image (int width, int height, String name) {
		//256 value, three color channels
		me =  RecyclingPlant.request(name,width,height,CvType.CV_8UC3);
		
		//fill with black
		Imgproc.rectangle(me, tl, br, black);
		this.name = name;
	}
	
	public image (Mat inp, String name) {
		me = inp;
		br = new Point(me.height(),me.width());
		this.name = name;
	}

	/*
	public static void features(image objectImage, image sceneImage) {

		image objecti = objectImage.clone();
		image scenei = sceneImage.clone();

		objecti.removeSpecks(5);
		scenei.removeSpecks(5);

		Mat object = objecti.me;
		Mat scene = scenei.me;

		//HighGui.imshow("obj", object);
		//HighGui.imshow("scene", scene);
		//HighGui.waitKey();

		@SuppressWarnings("deprecation")
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.AKAZE);
		@SuppressWarnings("deprecation")
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.AKAZE);
		
		MatOfKeyPoint 	keypointsObject 	= new MatOfKeyPoint();
		Mat 			descriptorsObject	= new Mat();
		MatOfKeyPoint 	keypointsScene		= new MatOfKeyPoint();
		Mat 			descriptorsScene 	= new Mat();

		//find Feature Points/Descriptors
		detector.detect(object, keypointsObject);
		extractor.compute(object, keypointsObject, descriptorsObject);
		detector.detect(scene, keypointsScene);
		extractor.compute(scene, keypointsScene, descriptorsScene);
		
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		List<MatOfDMatch> matches = new ArrayList<>();
		
		//find Matches
		matcher.knnMatch(descriptorsObject, descriptorsScene, matches, 2);
		
		//filter Matches
		List<DMatch> listOfGoodMatches = new ArrayList<>();
		
		//min
		double min=1000000;
		for(MatOfDMatch match: matches) {
			double distance = match.toArray()[0].distance;
			min = Math.min(distance, min);
		}
		//override
		//min = 10000;

		//good matches
		for(MatOfDMatch match: matches) {
			if(match.rows()<=1) continue;
			
			DMatch[] Dmatch = match.toArray();
			
			if (Dmatch[0].distance < 0.88 * Dmatch[1].distance) {
                listOfGoodMatches.add(Dmatch[0]);
            }
		}
		MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(listOfGoodMatches);
		
        //Draw matches
        Mat imgMatches = new Mat();
        Features2d.drawMatches(object, keypointsObject, scene, keypointsScene, goodMatches, imgMatches, Scalar.all(-1),
                Scalar.all(-1), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
        
        //Show detected matches
        HighGui.imshow("Good Matches", imgMatches);
        
        /*
        //put keypoints mats into lists
        List<KeyPoint> keypoints1_List = keypointsObject.toList();
        List<KeyPoint> keypoints2_List = keypointsScene.toList();

        //put keypoints into point2f mats so calib3d can use them to find homography
        LinkedList<Point> objList = new LinkedList<Point>();
        LinkedList<Point> sceneList = new LinkedList<Point>();
        for(int i=0;i<listOfGoodMatches.size();i++)
        {
            objList.addLast(keypoints2_List.get(i).pt);
            sceneList.addLast(keypoints1_List.get(i).pt);
        }
        MatOfPoint2f obj = new MatOfPoint2f();
        MatOfPoint2f sce = new MatOfPoint2f();
        obj.fromList(objList);
        sce.fromList(sceneList);

        //run homography on object and sce points
        Mat H = Calib3d.findHomography(obj, sce,Calib3d.RANSAC, 5);
        Mat tmp_corners = new Mat(4,1,CvType.CV_32FC2);
        Mat sce_corners = new Mat(4,1,CvType.CV_32FC2);

        //get corners from object
        tmp_corners.put(0, 0, new double[] {0,0});
        tmp_corners.put(1, 0, new double[] {scene.cols(),0});
        tmp_corners.put(2, 0, new double[] {scene.cols(),scene.rows()});
        tmp_corners.put(3, 0, new double[] {0,scene.rows()});

        Core.perspectiveTransform(tmp_corners,sce_corners, H);


        Imgproc.line(imgMatches, new Point(sce_corners.get(0,0)), new Point(sce_corners.get(1,0)), new Scalar(0, 255, 0),4);
        Imgproc.line(imgMatches, new Point(sce_corners.get(1,0)), new Point(sce_corners.get(2,0)), new Scalar(0, 255, 0),4);
        Imgproc.line(imgMatches, new Point(sce_corners.get(2,0)), new Point(sce_corners.get(3,0)), new Scalar(0, 255, 0),4);
        Imgproc.line(imgMatches, new Point(sce_corners.get(3,0)), new Point(sce_corners.get(0,0)), new Scalar(0, 255, 0),4);

	}*/

	public void findEdges() {
		// Remove noise
        blur(3);
        toGray();
        
        Mat edgeX = RecyclingPlant.request("edgeX");
		Mat edgeY = RecyclingPlant.request("edgeY");
		Mat edgeXabs = RecyclingPlant.request("edgeXabs");
		Mat edgeYabs = RecyclingPlant.request("edgeYabs");
        
        //note: for ksize=3 use scharr instead of Sobel 
        //Output mat as 16bit signed one channel gray (to prevent overflow) 
       
        /*Imgproc.Sobel( me, edgeX, CvType.CV_16S, 0, 1,3);//ksize, scale, delta
        Imgproc.Sobel( me, edgeY, CvType.CV_16S, 1, 0,3);//ksize, scale 1, delta 0
        */
        
        Imgproc.Scharr( me, edgeX, CvType.CV_16S, 0, 1);//ksize, scale, delta
        Imgproc.Scharr( me, edgeY, CvType.CV_16S, 1, 0);//ksize, scale 1, delta 0
        
        //converting back to 8 bit unsigned
        Core.convertScaleAbs(edgeX,edgeXabs);
        Core.convertScaleAbs(edgeY, edgeYabs);
        
        //add together
        Core.addWeighted(edgeXabs,0.5,edgeYabs,0.5,0,me);   
	}
	
	public List<MatOfPoint> contours(boolean externalOnly) {
		List<MatOfPoint> contours = new ArrayList<>();
		int mode = externalOnly?Imgproc.RETR_EXTERNAL:Imgproc.RETR_LIST;
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		
		Imgproc.findContours(me, contours, new Mat(), mode, method);
		
		return contours;
	}

	public void drawContours(List<MatOfPoint> contours) {
		Scalar color = new Scalar(0, 255, 0); // Green
		
		for (int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(me, contours, i, color, -1);
		}
	}
	
	public static void sortContours(List<MatOfPoint> contours) {
		for(int i=0;i<contours.size();i++) {
			double area = Imgproc.contourArea(contours.get(i));
			double perimeter = Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true);
			if(!(area > 50 && (perimeter*perimeter)/area < 100)) {//big enough, short enough
				contours.remove(i);
				i--;
			}
		}
	}
	
	public void translate(int x, int y) {
		Mat dst = new Mat();
		
		Point[] meTri = new Point[3];
	        meTri[0] = new Point( 0, 0 );
	        meTri[1] = new Point( me.cols() - x, 0 );
	        meTri[2] = new Point( me.cols() - x, me.rows() + y);
        Point[] dstTri = new Point[3];
	        dstTri[0] = new Point( x, -y );
	        dstTri[1] = new Point( me.cols() - 1, -y );
	        dstTri[2] = new Point( me.cols() - 1, me.rows() - 1);
	        
	    
	    Mat warpMat = Imgproc.getAffineTransform(new MatOfPoint2f(meTri), new MatOfPoint2f(dstTri));
	    Imgproc.warpAffine(me, me, warpMat, me.size() );
	        
	}
	
	public void resize(double ratio) {
		Imgproc.resize(me, me, new Size(me.width()*ratio,me.height()*ratio));
	}
	
	public void subtract(image in) {
		Core.subtract(me, in.me, me);
	}
	
	public void add(image in) {
		Core.add(me, in.me, me);
	}
	
	public void invert() {
		Core.bitwise_not(me, me);
	}
	
	public void binarize(int power) {//140
		
		Imgproc.adaptiveThreshold(me, me, 255, 
				Imgproc.ADAPTIVE_THRESH_MEAN_C, 
				Imgproc.THRESH_BINARY, 
				801, //ksize
				-power); //fuzz factor (repress static noise)
	}
	
	public void removeSpecks(int power) {//3
		Mat kernel = Mat.ones(power, power, CvType.CV_32F);
		
		Imgproc.morphologyEx(me, me, Imgproc.MORPH_OPEN, kernel);
	}
	
	public void dilate(int power) {
		Mat kernel = Mat.ones(power, power, CvType.CV_32F);
		
		Imgproc.morphologyEx(me, me, Imgproc.MORPH_DILATE, kernel);
	}
	
	public void equalize() {
		Imgproc.equalizeHist(me, me);
	}
	
	public void blur(int size) {
		//cannot be even
		if(size%2==0)size+=1;
		
		//We left sigma[x,y] at 0 so algo can auto calculate
		Imgproc.GaussianBlur(me, me, new Size(size,size), 0, 0, Core.BORDER_DEFAULT );
	}
	
	public void toGray() {
		Imgproc.cvtColor(me, me, Imgproc.COLOR_RGB2GRAY);
	}public void toColor() {
		Imgproc.cvtColor(me, me, Imgproc.COLOR_GRAY2RGB);
	}public void binaryToColor() {
		Core.merge(new ArrayList<>(Arrays.asList(me,me,me)), me);
	}
	
	
	public void show() {
		Display.show(this);
	}
	
	public image clone() {
		return new image(me.clone(), name);
	}

}
