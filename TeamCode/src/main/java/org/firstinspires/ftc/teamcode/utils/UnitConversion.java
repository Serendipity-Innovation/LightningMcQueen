package org.firstinspires.ftc.teamcode.utils;

import static org.firstinspires.ftc.teamcode.utils.Const.ticksPerRotation;
import static org.firstinspires.ftc.teamcode.utils.Const.wheelCircumference;

public class UnitConversion {

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
