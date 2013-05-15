package com.helpers;

import java.lang.reflect.InvocationTargetException;

import com.models.Reference;

public class AuxiliaryMethods {
	public static Number getField(String name, Object object) {
		try {
			return (Number) object.getClass().getMethod(name).invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
	public static double getTriangleArea(Reference ref1, Reference ref2, Reference ref3) {
		double z1z2 = ref1.getZ() - ref2.getZ();
		double z3z1 = ref3.getZ() - ref1.getZ();
		double z3z2 = ref3.getZ() - ref2.getZ();
		return Math.abs(ref1.getX() * (z3z2 + ref2.getY() - ref3.getY()) + ref2.getX() * (ref3.getY() - ref1.getY() - z3z1) + ref3.getX() * (z1z2 + ref2.getY() - ref1.getY()) - 
				ref1.getY() * z3z2 + ref2.getY() * z3z1 + ref3.getY() * z1z2);
	}

	public static double getDistance(Reference ref1, Reference ref2) {
		return Math.sqrt(Math.pow(ref1.getX() - ref2.getX(), 2) + Math.pow(ref1.getY() - ref2.getY(), 2) + Math.pow(ref1.getZ() - ref2.getZ(), 2));
	}
}
