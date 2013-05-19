package com.techsoft.partselector.model;

import java.util.Collection;
import java.util.Vector;

public class Assembly {
	private String name;
	private Vector<AssemblyNode> nodes;
	private Vector<Assembly> forks;

	public Assembly(String name) {
		this.name = name;
		this.nodes = new Vector<AssemblyNode>();
		this.forks = new Vector<Assembly>();
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public void addNode(AssemblyNode node) {
		this.nodes.add(node);
	}

	public void addAssembly(Assembly assembly) {
		this.forks.add(assembly);
	}

	public Collection<Assembly> getAssemblies() {
		return new Vector<Assembly>(this.forks);
	}

	public Collection<AssemblyNode> getNodes() {
		return new Vector<AssemblyNode>(this.nodes);
	}

	private String toString(StringBuilder offset) {
		StringBuilder result = new StringBuilder();
		result.append(offset).append("Assembly [name=").append(this.name).append("]\n");
		offset.append("|    ").toString();
		for (int i = 0; i < this.nodes.size(); i++)
			result.append(offset).append(this.nodes.get(i).toString()).append("\n");
		for (Assembly node : this.forks)
			result.append(node.toString(new StringBuilder(offset)));
		return result.toString();
	}

	@Override
	public String toString() {
		return this.toString(new StringBuilder());
	}
}
