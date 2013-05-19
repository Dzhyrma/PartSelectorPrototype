package com.techsoft.partselector.util.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import com.techsoft.partselector.util.reflect.ClassReader;

public class ReflectObjectInputStream extends ObjectInputStream {

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		Class<?> result;
		try {
			result = ClassReader.getInstance().loadClass(desc.getName());
		} catch (ClassNotFoundException e) {
			return super.resolveClass(desc);
		}
		ObjectStreamClass resultDesc = ObjectStreamClass.lookupAny(result);
		if (resultDesc == null)
			return super.resolveClass(desc);
		if (resultDesc.getSerialVersionUID() != desc.getSerialVersionUID())
			throw new ClassNotFoundException("Serial Versions of UID in classes '" + desc.getName() + "' are different!");
		return result;
	}

	protected ReflectObjectInputStream() throws IOException, SecurityException {
		super();
	}

	public ReflectObjectInputStream(FileInputStream fileStream) throws IOException {
		super(fileStream);
	}

}