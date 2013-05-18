package com.techsoft.partselector;

import com.techsoft.partselector.ui.MainFrame;
import com.techsoft.partselector.util.reflect.ClassCompiler;

public class Main {

	public static void main(String[] args) {
		ClassCompiler.compileAllClasses();
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

}
