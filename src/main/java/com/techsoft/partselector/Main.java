package com.techsoft.partselector;

import java.util.Map;

import com.techsoft.partselector.ui.frame.MainFrame;
import com.techsoft.partselector.util.reflect.ClassCompiler;
import com.techsoft.partselector.util.reflect.ClassGenerator;
import com.techsoft.partselector.util.reflect.ClassReader;
import com.techsoft.partselector.util.rules.RuleLibrary;
import com.techsoft.partselector.util.rules.RuleModel;
//import com.techsoft.partselector.util.rules.RuleModel;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {
		/*RuleModel rule = new RuleModel("ScrewExample.drl");
		rule.setParameter("Dbore", double.class);
		rule.setParameter("MinGap", double.class);
		rule.setParameter("Lclamp", double.class);
		rule.setParameter("MinOverlap", double.class);
		rule.setParameter("MaxOverlap", double.class);
		rule.setParameter("WasherMinGap", double.class);
		rule.setParameter("WasherMinOverlap", double.class);
		rule.addClass("Washer");
		rule.addClass("Screw");
		rule.addClass("ScrewNut");
		RuleLibrary.getInstance().addRule(rule);*/

		RuleLibrary.getInstance();
		ClassCompiler.compileAllClasses();
		Map<String, Class<?>> classesMap = ClassReader.getInstance().getClassMap();
		for (String string : classesMap.keySet())
			new ClassGenerator(classesMap.get(string)).saveClass();
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}
}
