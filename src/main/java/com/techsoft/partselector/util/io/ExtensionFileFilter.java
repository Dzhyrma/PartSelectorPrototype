package com.techsoft.partselector.util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {
	
	private String extension, description;
	
	public ExtensionFileFilter(String extension, String description) {
		this.extension = extension;
		this.description = extension;
	}

	private static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String extension = getExtension(f);
		if (extension != null && extension.equals(this.extension))
			return true;

		return false;
	}

}
