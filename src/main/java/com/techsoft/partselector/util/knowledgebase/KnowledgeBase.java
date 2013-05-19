package com.techsoft.partselector.util.knowledgebase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.model.HashVector;
import com.techsoft.partselector.util.SimpleLogger;
import com.techsoft.partselector.util.io.ReflectObjectInputStream;

public class KnowledgeBase implements Serializable {

	private static final long serialVersionUID = -1595392856230418960L;
	private static final Logger LOGGER = SimpleLogger.getLogger(KnowledgeBase.class);

	private static volatile KnowledgeBase instance;

	private Map<HashVector, Integer> frequency;

	private KnowledgeBase() {
		this.frequency = new HashMap<HashVector, Integer>();
	}

	private static void saveLibrary() {
		try {
			FileOutputStream fileStream = new FileOutputStream(Paths.KB_FILE_PATH);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(instance);
			objectStream.close();
			fileStream.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.toString());
		}
	}

	public synchronized static KnowledgeBase getInstance() {
		if (instance == null) {
			try {
				FileInputStream fileStream = new FileInputStream(Paths.KB_FILE_PATH);
				ObjectInputStream objectStream = new ReflectObjectInputStream(fileStream);
				instance = (KnowledgeBase) objectStream.readObject();
				objectStream.close();
				fileStream.close();
			} catch (IOException | ClassNotFoundException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
				LOGGER.log(Level.INFO, "New knowledge base was created!");
				instance = new KnowledgeBase();
				saveLibrary();
			}
		}
		return instance;
	}

	public void update(Map<HashVector, Integer> newData) {
		if (newData == null)
			return;
		for (HashVector item : newData.keySet()) {
			Integer value = newData.get(item);
			value = (value == null) ? 0 : value;
			this.frequency.put(item, this.frequency.containsKey(item) ? this.frequency.get(item) + value : value);
		}
		saveLibrary();
	}
}
