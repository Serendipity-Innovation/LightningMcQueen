package org.firstinspires.ftc.teamcode.ImageProc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ImageProc.util.UVDetectionCore;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.List;

@TeleOp
public class Detector {
	public enum inputType{
		UV, noUV
	}
	//state of the real camera
	inputType camInput = inputType.noUV;
	//0 is await regular
	//1 is await UV
	int stage = 0;

	//stored frames for comparison
	Mat UV;
	Mat noUV;

	//found spots from most recent cycle
	public List<MatOfPoint> contours;
	//spots visualized
	Mat display;

    OpenCvCamera phoneCam;

    public Detector(OpMode opmode) {
	    int cameraMonitorViewId = opmode.hardwareMap.appContext.getResources()
			    .getIdentifier("cameraMonitorViewId", "id", opmode.hardwareMap.appContext.getPackageName());
	    phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

	    //Do Not Activate the Camera Monitor View
	    //phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK);

	    phoneCam.openCameraDevice();

	    phoneCam.setPipeline(new OpenCvPipeline() {
	    	public Mat processFrame(Mat input) {
	    		return pipeline(input);
		    }
	    });
    }

    public void start() {
	    phoneCam.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_RIGHT);
    }

    public void close() {
	    phoneCam.stopStreaming();
    }

    public void setInputType(inputType ty){
    	camInput = ty;
    }

    private Mat pipeline(Mat input){

		if(stage == 0 && camInput == inputType.noUV) {
			noUV = input;
			stage = 1;
		}
		if(stage == 1 && camInput == inputType.UV) {
			UV = input;
			stage = 0;
		}

		//inputs NOT loaded, stage is NOT correct
		if(UV == null || noUV == null || stage == 0)
			return display;

	    UVDetectionCore.process(UV, noUV,0,0);
	    display = UVDetectionCore.display;
	    contours = UVDetectionCore.contours;
	    return display;
    }
}