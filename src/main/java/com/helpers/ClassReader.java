package com.helpers;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import com.consts.Paths;

public class ClassReader {
	private static volatile ClassReader instance;
	private static URLClassLoader classLoader;
	static {
		File dir = new File(Paths.CLASS_PATH);
		try {
			URL[] urls = new URL[] { dir.toURI().toURL() };
			classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
		} catch (MalformedURLException e) {
			System.err.println("Wrong class path");
			System.exit(0);
		}
	}

	private ClassReader() {
	}

	public static ClassReader getInstance() {
		if (classLoader == null)
			return null;
		ClassReader localInstance = instance;
		if (localInstance == null) {
			synchronized (ClassReader.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new ClassReader();
				}
			}
		}
		return localInstance;
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return classLoader.loadClass(className);
	}

	public String[] getClassNames() {
		File dir = new File(Paths.CLASS_PATH);
		String[] classNames = dir.list(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.endsWith(".class");
			}
		});
		for (int i = 0; i < classNames.length; i++)
			classNames[i] = classNames[i].substring(0, classNames[i].length() - 6);
		return classNames;
	}
}
