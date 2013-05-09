package com.models;

public class AssemblyTreeNode extends TreeNode {
	private String assemblyName;
	TreeNode childNode;

	public final String getAssemblyName() {
		return this.assemblyName;
	}

	public final void setAssemblyName(String assemblyName) {
		this.assemblyName = assemblyName;
	}
}
