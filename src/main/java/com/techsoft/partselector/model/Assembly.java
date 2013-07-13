package com.techsoft.partselector.model;

import java.util.ArrayList;
import java.util.Collection;

/** Represents an assembly object that can contain other assemblies inside and
 * assembly nodes.
 * 
 * @author Andrii Dzhyrma */
public class Assembly {
	/** Name of the current assembly.
	 * 
	 * @uml.property name="name" */
	private String name;
	/** @uml.property name="nodes"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     inverse="assembly:com.techsoft.partselector.model.AssemblyNode" */
	private ArrayList<AssemblyNode> nodes;
	/** @uml.property name="forks"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     inverse="assembly1:com.techsoft.partselector.model.Assembly" */
	private ArrayList<Assembly> forks;

	/** Creates an assembly that can be contained in others.
	 * 
	 * @param name - name of the current assembly. */
	public Assembly(String name) {
		this.name = name;
		this.nodes = new ArrayList<AssemblyNode>();
		this.forks = new ArrayList<Assembly>();
	}

	/** @return name of the current assembly.
	 * @uml.property name="name" */
	public final String getName() {
		return this.name;
	}

	/** @param name - new name for the current assembly.
	 * @uml.property name="name" */
	public final void setName(String name) {
		this.name = name;
	}

	/** Adds a new assembly node to the current assembly.
	 * 
	 * @param node - node to be added. */
	public void addNode(AssemblyNode node) {
		this.nodes.add(node);
	}

	/** Adds a new assembly as a fork to the current.
	 * 
	 * @param assembly - assembly to be added. */
	public void addAssembly(Assembly assembly) {
		this.forks.add(assembly);
	}

	/** Gets all forks of the current assembly.
	 * 
	 * @return collection of assemblies. Returns empty collection, if the were
	 *         no forks. */
	public Collection<Assembly> getAssemblies() {
		return new ArrayList<Assembly>(this.forks);
	}

	/** Gets all assembly nodes of the current assembly.
	 * 
	 * @return collection of assembly nodes. Returns empty collection, if the
	 *         were no assembly nodes. */
	public Collection<AssemblyNode> getNodes() {
		return new ArrayList<AssemblyNode>(this.nodes);
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
