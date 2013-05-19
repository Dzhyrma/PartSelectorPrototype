package classes;

import com.techsoft.partselector.model.Part;

public class ScrewNut extends Part {
	private int height;
	private int nominalDiameter;
	private int thread;

	public ScrewNut(String name) {
		super(name);
	}

	public final int getHeight() {
		return this.height;
	}

	public final int getNominalDiameter() {
		return this.nominalDiameter;
	}

	public final int getThread() {
		return this.thread;
	}

	public final void setHeight(int height) {
		this.height = height;
	}

	public final void setNominalDiameter(int nominalDiameter) {
		this.nominalDiameter = nominalDiameter;
	}

	public final void setThread(int thread) {
		this.thread = thread;
	}
}
