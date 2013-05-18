package com.techsoft.partselector.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PartFileFilter extends FileFilter {

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
		return "Files with a part information. *.par";
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String extension = getExtension(f);
		if (extension != null && extension.equals("par"))
			return true;

		return false;
	}

}
