public class Washer {
	public Double innerDiameter;
	public String name;
	public int outerDiameter;
	public int thickness;
	
	public final Double getInnerDiameter() {
		return this.innerDiameter;
	}

	public final String getName() {
		return this.name;
	}

	public final int getOuterDiameter() {
		return this.outerDiameter;
	}

	public final int getThickness() {
		return this.thickness;
	}

	public final void setInnerDiameter(Double innerDiameter) {
		this.innerDiameter = innerDiameter;
	}

	public final void setOuterDiameter(int outerDiameter) {
		this.outerDiameter = outerDiameter;
	}

	public final void setThickness(int thickness) {
		this.thickness = thickness;
	}
}
