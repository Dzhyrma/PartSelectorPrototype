package classes;

public class EyeScrew extends Screw {
	public EyeScrew(String name) {
		super(name);
	}

	private int eyeRadius;

	public final int getEyeRadius() {
		return this.eyeRadius;
	}

	public final void setEyeRadius(int eyeRadius) {
		this.eyeRadius = eyeRadius;
	}
}
