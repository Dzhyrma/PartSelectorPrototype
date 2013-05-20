public class CylindricScrew extends Screw {

	private static final long serialVersionUID = 10381417367L;

	public double headWidth;

	public CylindricScrew(String name) {
		super(name);
	}

	public final double getHeadWidth() {
		return this.headWidth;
	}

	public final void setHeadWidth(double headWidth) {
		this.headWidth = headWidth;
	}

}