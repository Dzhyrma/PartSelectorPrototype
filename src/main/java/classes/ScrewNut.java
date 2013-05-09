package classes;

public class ScrewNut {
	private int height;
	private String name;
	private int nominalDiameter;
	private int thread;

	public ScrewNut(String name) {
		super();
		this.name = name;
	}

	public final int getHeight() {
		return this.height;
	}

	public final String getName() {
		return this.name;
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
