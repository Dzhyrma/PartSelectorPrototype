package com.techsoft.partselector.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.model.Part;
import com.techsoft.partselector.util.io.ClassReader;
import com.techsoft.partselector.util.io.ReflectObjectInputStream;
import com.techsoft.partselector.util.comparator.PartComparator;

public class PartLibrary implements Serializable {

	private static final long serialVersionUID = -6131079720244059106L;
	private static PartLibrary instance;

	private Map<Class<?>, List<Part>> parts;

	private PartLibrary() {
		this.parts = new HashMap<Class<?>, List<Part>>();
	}

	private static void saveLibrary() {
		try {
			FileOutputStream fileStream = new FileOutputStream(Paths.PART_LIB_PATH);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(instance);
			objectStream.close();
			fileStream.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public synchronized static PartLibrary getInstance() {
		if (instance == null) {
			try {
				ClassReader.getInstance().loadClass("Washer");
				FileInputStream fileStream = new FileInputStream(Paths.PART_LIB_PATH);
				ObjectInputStream objectStream = new ReflectObjectInputStream(fileStream);
				instance = (PartLibrary) objectStream.readObject();
				objectStream.close();
				fileStream.close();
			} catch (IOException | ClassNotFoundException e) {
				System.err.println(e);
				instance = new PartLibrary();
				saveLibrary();
			}
		}
		return instance;
	}

	public void addParts(Class<?> clazz, List<Part> parts) {
		if (clazz == null)
			return;
		if (this.parts == null)
			this.parts = new HashMap<Class<?>, List<Part>>();
		if (!this.parts.containsKey(clazz))
			this.parts.put(clazz, new ArrayList<Part>(parts));
		else
			this.parts.get(clazz).addAll(parts);
		Collections.sort(this.parts.get(clazz), new PartComparator<Part>());
		saveLibrary();
	}

	public void deleteParts(Class<?> clazz, List<Part> parts) {
		if (clazz == null || parts == null)
			return;
		List<Part> classParts = this.parts.get(clazz);
		for (Part part : parts) {
			int index;
			do {
				index = Collections.binarySearch(classParts, part, new PartComparator<Part>());
				if (index >= 0)
					classParts.remove(index);
			} while (index >= 0);
		}
		saveLibrary();
	}

	public Part[] getParts(Class<?> clazz) {
		if (this.parts != null && this.parts.containsKey(clazz)) {
			List<Part> resultList = this.parts.get(clazz);
			if (resultList == null)
				return new Part[0];
			Part[] result = new Part[resultList.size()];
			int index = 0;
			for (Part part : resultList)
				result[index++] = part;
			return result;
		} else
			return new Part[0];
	}
}
