package com.techsoft.partselector.model;

/** Represents coordinate of the part in the assembly file.
 * 
 * @author Andrii Dzhyrma */
public class Reference {
	/** @uml.property name="x" */
	private double x;
	/** @uml.property name="y" */
	private double y;
	/** @uml.property name="z" */
	private double z;

	/** Creates the reference point.
	 * 
	 * @param x - x coordinate.
	 * @param y - y coordinate.
	 * @param z - z coordinate. */
	public Reference(Double x, Double y, Double z) {
		this.x = x == null ? 0 : x.doubleValue();
		this.y = y == null ? 0 : y.doubleValue();
		this.z = z == null ? 0 : z.doubleValue();
	}

	/** @return
	 * @uml.property name="x" */
	public final double getX() {
		return this.x;
	}

	/** @param x
	 * @uml.property name="x" */
	public final void setX(double x) {
		this.x = x;
	}

	/** @return
	 * @uml.property name="y" */
	public final double getY() {
		return this.y;
	}

	/** @param y
	 * @uml.property name="y" */
	public final void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	/** @return
	 * @uml.property name="z" */
	public final double getZ() {
		return this.z;
	}

	/** @param z
	 * @uml.property name="z" */
	public final void setZ(double z) {
		this.z = z;
	}

}
