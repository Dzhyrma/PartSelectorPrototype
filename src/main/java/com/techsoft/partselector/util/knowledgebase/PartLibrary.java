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

/** Represents the library of parts.
 * 
 * @author Andrii Dzhyrma */
public class PartLibrary implements Serializable {

	private static final long serialVersionUID = -6131079720244059106L;
	private static final Logger LOGGER = SimpleLogger.getLogger(PartLibrary.class);
	/** @uml.property name="instance"
	 * @uml.associationEnd */
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

	/** @return the instance of current singleton class.
	 * @uml.property name="instance" */
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

	/** Adds part to the library.
	 * 
	 * @param clazz - class of the part to be added.
	 * @param part - part to be added. */
	private void addPart(Class<?> clazz, Part part) {
		if (clazz == null || part == null)
			return;
		Class<?> oldClass = this.partNames.get(part.getName());
		if (oldClass != null && this.parts.containsKey(oldClass)) {
			List<Part> list = this.parts.get(oldClass);
			int index = Collections.binarySearch(list, part, new PartComparator<Part>());
			if (index >= 0)
				list.remove(index);
		}
		if (this.parts.containsKey(clazz)) {
			List<Part> list = this.parts.get(clazz);
			int index = Collections.binarySearch(list, part, new PartComparator<Part>());
			if (index < 0)
				list.add(-index - 1, part);
			else
				list.set(index, part);
		} else {
			List<Part> list = new ArrayList<Part>();
			list.add(part);
			this.parts.put(clazz, list);
		}
		this.partNames.put(part.getName(), clazz);
	}

	/** Adds parts to the library.
	 * 
	 * @param clazz - class of the parts to be added.
	 * @param parts - list of the parts to be added. */
	public void addParts(Class<?> clazz, List<Part> parts) {
		if (parts == null)
			return;
		for (Part part : parts)
			this.addPart(clazz, part);
		saveLibrary();
	}

	/** Removes parts from the library.
	 * 
	 * @param clazz - class of the parts to be removed.
	 * @param parts - parts to be removed. */
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

	/** Gets parts for specific class.
	 * 
	 * @param clazz - class of the parts to be returned.
	 * @return array of the parts. */
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

	/** Removes the bunch of parts of the specific type.
	 * 
	 * @param clazz - type of the parts to remove. */
	public void removeClass(Class<?> clazz) {
		List<Part> removedList = this.parts.remove(clazz);
		if (removedList != null)
			for (Part part : removedList)
				if (part != null)
					this.partNames.remove(part.getName());
		saveLibrary();
	}

	/** Gets parts using the name of it.
	 * 
	 * @param partName - name of the part to be returned.
	 * @return instance of the part with the given name. Null, if part was not
	 *         found. */
	public Part getPart(String partName) {
		List<Part> parts = this.parts.get(this.partNames.get(partName));
		if (parts == null)
			return null;
		int index = Collections.binarySearch(parts, new Part(partName), new PartComparator<Part>());
		return (index < 0) ? null : parts.get(index);
	}
}
