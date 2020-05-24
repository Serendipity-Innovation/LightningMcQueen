package org.firstinspires.ftc.robotcontroller.internal;

public class pose {
	public double x,y,rot;
	public boolean hasDirt = false;

	public  pose(double x, double y, double rot){
		this.x =x;
		this.y=y;
		this.rot = rot;
	}

	public pose setHasDirt(){
		hasDirt = true;
		return this;
	}

	public static pose subtract(pose one, pose two){
		return new pose(one.x-two.x, one.y-two.y, one.rot-two.rot);
	}

	public static pose add(pose one, pose two){
		return new pose(one.x+two.x, one.y+two.y, one.rot+two.rot);
	}

	public static pose addWeighted(pose one, pose two, double OneWeight, double TwoWeight){
		return new pose(
				one.x * OneWeight + two.x * TwoWeight,
				one.y * OneWeight + two.y * TwoWeight,
				one.rot * OneWeight + two.rot * TwoWeight);
	}

	public pose clone(){
		return new pose(x,y,rot);
	}

	public String toString(){
		return String.valueOf(x)+" "+String.valueOf(y)+" "+String.valueOf(rot)+" "+String.valueOf(hasDirt);
	}
}
