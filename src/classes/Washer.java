package classes;

import com.techsoft.partselector.model.Part;
public class Washer extends Part{
    private static final long serialVersionUID = -369829215951402400L;
	public double innerDiameter;
	public double outerDiameter;
	public double thickness;
	
	public Washer(String name) {
		super(name);
	}
	
	public final double getInnerDiameter() {
		return this.innerDiameter;
	}

	public final double getOuterDiameter() {
		return this.outerDiameter;
	}

	public final double getThickness() {
		return this.thickness;
	}

	public final void setInnerDiameter(double innerDiameter) {
		this.innerDiameter = innerDiameter;
	}

	public final void setOuterDiameter(double outerDiameter) {
		this.outerDiameter = outerDiameter;
	}

	public final void setThickness(double thickness) {
		this.thickness = thickness;
	}
}