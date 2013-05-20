public class ScrewNut extends com.techsoft.partselector.model.Part {

	private static final long serialVersionUID = -24540738105L;

	public double height;
	public java.lang.String thread;
	public double nominalDiameter;

	public ScrewNut(String name) {
		super(name);
	}

	public final double getHeight() {
		return this.height;
	}

	public final java.lang.String getThread() {
		return this.thread;
	}

	public final double getNominalDiameter() {
		return this.nominalDiameter;
	}

	public final void setHeight(double height) {
		this.height = height;
	}

	public final void setThread(java.lang.String thread) {
		this.thread = thread;
	}

	public final void setNominalDiameter(double nominalDiameter) {
		this.nominalDiameter = nominalDiameter;
	}

}