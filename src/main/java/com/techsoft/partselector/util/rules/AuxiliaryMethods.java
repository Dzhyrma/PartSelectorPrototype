package com.techsoft.partselector.util.rules;

import java.lang.reflect.InvocationTargetException;

import com.techsoft.partselector.model.Reference;

/** Class with auxiliary methods for rules.
 * 
 * @author Andrii Dzhyrma */
public class AuxiliaryMethods {
	/** Returns the numerical value of the field.
	 * 
	 * @param name - name of the field.
	 * @param object - object to retrieve value from.
	 * @return numerical value. */
	public synchronized static Number getField(String name, Object object) {
		try {
			Object field = object.getClass().getMethod(name).invoke(object);
			if (field instanceof Number)
				return (Number) field;
			else
				return 0;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return 0;
		}
	}

	/** Returns the string value of the field.
	 * 
	 * @param name - name of the field.
	 * @param object - object to retrieve value from.
	 * @return string value. */
	public static String getStringField(String name, Object object) {
		try {
			Object field = object.getClass().getMethod(name).invoke(object);
			return field.toString();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

	/** Returns the area of triangle represented by three points. Used to check,
	 * whether the three point are on the same line.
	 * 
	 * @param ref1 - coordinates for first point.
	 * @param ref2 - coordinates for second point.
	 * @param ref3 - coordinates for third point.
	 * @return area of the triangle. */
	public static double getTriangleArea(Reference ref1, Reference ref2, Reference ref3) {
		double z1z2 = ref1.getZ() - ref2.getZ();
		double z3z1 = ref3.getZ() - ref1.getZ();
		double z3z2 = ref3.getZ() - ref2.getZ();
		return Math.abs(ref1.getX() * (z3z2 + ref2.getY() - ref3.getY()) + ref2.getX() * (ref3.getY() - ref1.getY() - z3z1) + ref3.getX()
		                * (z1z2 + ref2.getY() - ref1.getY()) - ref1.getY() * z3z2 + ref2.getY() * z3z1 + ref3.getY() * z1z2);
	}

	/** Returns the distance between two points.
	 * 
	 * @param ref1 - coordinates for first point.
	 * @param ref2 - coordinates for second point.
	 * @return distance between points. */
	public static double getDistance(Reference ref1, Reference ref2) {
		return Math.sqrt(Math.pow(ref1.getX() - ref2.getX(), 2) + Math.pow(ref1.getY() - ref2.getY(), 2) + Math.pow(ref1.getZ() - ref2.getZ(), 2));
	}
}
