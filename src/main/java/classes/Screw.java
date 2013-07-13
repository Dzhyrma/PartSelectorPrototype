package classes;

import com.techsoft.partselector.model.Part;

public class Screw extends Part {
	private int headDiameter;
	private int length;
	private int nominalDiameter;
	private int thread;

	public Screw(String name) {
		super(name);
	}

	public final int getHeadDiameter() {
		return this.headDiameter;
	}

	public final int getLength() {
		return this.length;
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
