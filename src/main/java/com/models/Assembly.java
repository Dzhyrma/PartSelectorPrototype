package com.models;

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
}
