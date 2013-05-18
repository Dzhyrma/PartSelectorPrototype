package com.techsoft.partselector.util.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ReflectObjectInputStream extends ObjectInputStream {

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		return ClassReader.getInstance().loadClass(desc.getName());
	}

	protected ReflectObjectInputStream() throws IOException, SecurityException {
		super();
	}

	public ReflectObjectInputStream(FileInputStream fileStream) throws IOException {
		super(fileStream);
	}

}