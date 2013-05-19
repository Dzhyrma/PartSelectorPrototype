public class Screw extends com.techsoft.partselector.model.Part {

	private static final long serialVersionUID = 18728420044L;

	public java.lang.String thread;
	public double length;
	public double headDiameter;
	public double nominalDiameter;

	public Screw(String name) {
		super(name);
	}

	public final java.lang.String getThread() {
		return this.thread;
	}

	public final double getLength() {
		return this.length;
	}

	public final double getHeadDiameter() {
		return this.headDiameter;
	}

	public final double getNominalDiameter() {
		return this.nominalDiameter;
	}

	public final void setThread(java.lang.String thread) {
		this.thread = thread;
	}

	public final void setLength(double length) {
		this.length = length;
	}

	public final void setHeadDiameter(double headDiameter) {
		this.headDiameter = headDiameter;
	}

	public final void setNominalDiameter(double nominalDiameter) {
		this.nominalDiameter = nominalDiameter;
	}

}