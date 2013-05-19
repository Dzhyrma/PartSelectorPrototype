package com.techsoft.partselector.util.rules;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.util.SimpleLogger;
import com.techsoft.partselector.util.io.ReflectObjectInputStream;

public class RuleLibrary implements Serializable {

	private static final long serialVersionUID = -4999339379220861937L;
	private static final Logger LOGGER = SimpleLogger.getLogger(RuleLibrary.class);
	private static volatile RuleLibrary instance;

	private PriorityQueue<RuleModel> rules;

	private RuleLibrary() {
		this.rules = new PriorityQueue<RuleModel>();
	}

	private static void saveLibrary() {
		try {
			FileOutputStream fileStream = new FileOutputStream(Paths.RULE_LIB_FILE_PATH);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(instance);
			objectStream.close();
			fileStream.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	public static synchronized RuleLibrary getInstance() {
		if (instance == null) {
			try {
				FileInputStream fileStream = new FileInputStream(Paths.RULE_LIB_FILE_PATH);
				ObjectInputStream objectStream = new ReflectObjectInputStream(fileStream);
				instance = (RuleLibrary) objectStream.readObject();
				objectStream.close();
				fileStream.close();
			} catch (IOException | ClassNotFoundException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
				LOGGER.log(Level.INFO, "New rule library was created!");
				instance = new RuleLibrary();
				saveLibrary();
			}
		}
		return instance;
	}

	public void addRule(RuleModel rule) {
		if (rule != null) {
			this.rules.remove(rule);
			this.rules.offer(rule);
			saveLibrary();
		}
	}

	public Vector<RuleModel> getRules() {
		return new Vector<RuleModel>(this.rules);
	}
}
