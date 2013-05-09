package classes;

public class Screw {
	private int headDiameter;
	private int length;
	private String name;
	private int nominalDiameter;
	private int thread;

	public Screw(String name) {
		this.name = name;
	}

	public final int getHeadDiameter() {
		return this.headDiameter;
	}

	public final int getLength() {
		return this.length;
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

	public final void setHeadDiameter(int headDiameter) {
		this.headDiameter = headDiameter;
	}

	public final void setLength(int length) {
		this.length = length;
	}

	public final void setNominalDiameter(int nominalDiameter) {
		this.nominalDiameter = nominalDiameter;
	}

	public final void setThread(int thread) {
		this.thread = thread;
	}
}
