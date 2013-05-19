package com.techsoft.partselector;

import java.util.Map;

import com.techsoft.partselector.ui.frame.MainFrame;
import com.techsoft.partselector.util.reflect.ClassCompiler;
import com.techsoft.partselector.util.reflect.ClassGenerator;
import com.techsoft.partselector.util.reflect.ClassReader;

public class Main {

	public static void main(String[] args) {
		ClassCompiler.compileAllClasses();
		Map<String, Class<?>> classesMap = ClassReader.getInstance().getClassMap();
		for (String string : classesMap.keySet()) 
			new ClassGenerator(classesMap.get(string)).saveClass();
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

}
