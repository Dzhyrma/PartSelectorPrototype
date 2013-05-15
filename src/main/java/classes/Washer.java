package classes;

public class Washer extends Part {
	private int innerDiameter;
	private int outerDiameter;
	private int thickness;

	public Washer(String name) {
		super(name);
	}

	public final int getInnerDiameter() {
		return this.innerDiameter;
	}

	public final int getOuterDiameter() {
		return this.outerDiameter;
	}

	public final int getThickness() {
		return this.thickness;
	}

	public final void setInnerDiameter(int innerDiameter) {
		this.innerDiameter = innerDiameter;
	}

	public final void setOuterDiameter(int outerDiameter) {
		this.outerDiameter = outerDiameter;
	}

	public final void setThickness(int thickness) {
		this.thickness = thickness;
	}
}
