package com.techsoft.partselector.model;

public class Reference {
	private double x;
	private double y;
	private double z;

	public Reference(Double x, Double y, Double z) {
		this.x = x == null ? 0 : x.doubleValue();
		this.y = y == null ? 0 : y.doubleValue();
		this.z = z == null ? 0 : z.doubleValue();
	}

	public final double getX() {
		return this.x;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final double getY() {
		return this.y;
	}

	public final void setY(double y) {
		this.y = y;
	}

	@Override
    public String toString() {
	    return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

	public final double getZ() {
		return this.z;
	}

	public final void setZ(double z) {
		this.z = z;
	}

}
