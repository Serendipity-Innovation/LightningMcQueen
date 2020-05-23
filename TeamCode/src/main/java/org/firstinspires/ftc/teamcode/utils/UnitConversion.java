package org.firstinspires.ftc.teamcode.utils;

public class UnitConversion {

	static final double wheelCircumference = 0.104 * 12;//inches
	static final double ticksPerRotation = 1120;

    public static double getFeetToTicks(double feet){
        double rotations = feet / wheelCircumference;
        return rotations * ticksPerRotation;
    }

    public static double getAngleToFeet(double desiredAngle){
        double radianAngle = Math.toRadians(desiredAngle);
        double wheelToCenterDistance = 0.5; // note that this is radius measured in feet
        double desiredFeet = radianAngle * wheelToCenterDistance; // same as r * theta
        return desiredFeet;
    }

    public static double getTicksToInches(double ticks){
    	double rotations = ticks / ticksPerRotation;
    	return rotations * wheelCircumference;
    }
}
