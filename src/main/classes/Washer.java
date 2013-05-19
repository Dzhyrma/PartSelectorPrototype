public class Washer extends com.techsoft.partselector.model.Part {

	private static final long serialVersionUID = -1782161938L;

	public double outerDiameter;
	public double thickness;
	public double innerDiameter;

	public Washer(String name) {
		super(name);
	}

	public final double getOuterDiameter() {
		return this.outerDiameter;
	}

	public final double getThickness() {
		return this.thickness;
	}

	public final double getInnerDiameter() {
		return this.innerDiameter;
	}

	public final void setOuterDiameter(double outerDiameter) {
		this.outerDiameter = outerDiameter;
	}

	public final void setThickness(double thickness) {
		this.thickness = thickness;
	}

	public final void setInnerDiameter(double innerDiameter) {
		this.innerDiameter = innerDiameter;
	}

}