package com.techsoft.partselector.util.knowledgebase;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.model.Part;
import com.techsoft.partselector.util.SimpleLogger;
import com.techsoft.partselector.util.io.ReflectObjectInputStream;
import com.techsoft.partselector.util.comparator.PartComparator;

public class PartLibrary implements Serializable {

	private static final long serialVersionUID = -6131079720244059106L;
	private static final Logger LOGGER = SimpleLogger.getLogger(PartLibrary.class);
	private static volatile PartLibrary instance;

	private Map<Class<?>, List<Part>> parts;
	private Map<String, Class<?>> partNames;

	private PartLibrary() {
		this.parts = new HashMap<Class<?>, List<Part>>();
		this.partNames = new HashMap<String, Class<?>>();
	}

	private static void saveLibrary() {
		try {
			FileOutputStream fileStream = new FileOutputStream(Paths.PART_LIB_FILE_PATH);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(instance);
			objectStream.close();
			fileStream.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	public synchronized static PartLibrary getInstance() {
		if (instance == null) {
			try {
				FileInputStream fileStream = new FileInputStream(Paths.PART_LIB_FILE_PATH);
				ObjectInputStream objectStream = new ReflectObjectInputStream(fileStream);
				instance = (PartLibrary) objectStream.readObject();
				objectStream.close();
				fileStream.close();
			} catch (IOException | ClassNotFoundException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
				LOGGER.log(Level.INFO, "New knowledge base was created!");
				instance = new PartLibrary();
				saveLibrary();
			}
		}
		return instance;
	}

	private void addPart(Class<?> clazz, Part part) {
		if (clazz == null || part == null || (this.partNames.get(part.getName()) != null && this.partNames.get(part.getName()) != clazz))
			return;
		this.partNames.put(part.getName(), clazz);
		if (!this.parts.containsKey(clazz)) {
			List<Part> list = new ArrayList<Part>();
			list.add(part);
			this.parts.put(clazz, list);
		} else {
			List<Part> list = this.parts.get(clazz);
			int index = Collections.binarySearch(list, part, new PartComparator<Part>());
			if (index < 0)
				list.add(-index - 1, part);
			else
				list.set(index, part);
		}
	}

	public void addParts(Class<?> clazz, List<Part> parts) {
		if (parts == null)
			return;
		for (Part part : parts)
			this.addPart(clazz, part);
		saveLibrary();
	}

	public void removeParts(Class<?> clazz, List<Part> parts) {
		if (clazz == null || parts == null)
			return;
		List<Part> classParts = this.parts.get(clazz);
		for (Part part : parts) {
			if (part != null) {
				this.partNames.remove(part.getName());
				int index;
				do {
					index = Collections.binarySearch(classParts, part, new PartComparator<Part>());
					if (index >= 0)
						classParts.remove(index);
				} while (index >= 0);
			}
		}
		saveLibrary();
	}

	public Part[] getParts(Class<?> clazz) {
		if (!this.parts.containsKey(clazz))
			return new Part[0];
		List<Part> resultList = this.parts.get(clazz);
		if (resultList == null)
			return new Part[0];
		Part[] result = new Part[resultList.size()];
		int index = 0;
		for (Part part : resultList)
			result[index++] = part;
		return result;
	}

	public void removeClass(Class<?> clazz) {
		List<Part> removedList = this.parts.remove(clazz);
		if (removedList != null)
			for (Part part : removedList)
				if (part != null)
					this.partNames.remove(part.getName());
		saveLibrary();
	}

	public Part getPart(String partName) {
		List<Part> parts = this.parts.get(this.partNames.get(partName));
		if (parts == null)
			return null;
		int index = Collections.binarySearch(parts, new Part(partName), new PartComparator<Part>());
		return (index < 0) ? null : parts.get(index);
	}
}
