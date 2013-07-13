package com.techsoft.partselector.util.reflect;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.techsoft.partselector.consts.Paths;

import static java.nio.file.StandardCopyOption.*;

/** Class for compiling all the runtime classes created for parts.
 * 
 * @author Andrii Dzhyrma */
public class ClassCompiler {
	public static boolean compileAllClasses() {
		File dir = new File(Paths.JAVA_PATH);
		File[] javaFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return name.endsWith(".java");
			}
		});
		if (javaFiles == null)
			return false;
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		if (javaCompiler == null) {
			System.err.println("JavaCompiler loading failed.");
			return false;
		}
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(diagnostics, Locale.ENGLISH, Charset.forName("UTF-8"));
		if (fileManager == null) {
			System.err.println("StandardJavaFileManager loading failed.");
			return false;
		}
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(javaFiles));
		javaCompiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits).call();

		for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
			System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(), diagnostic.getSource().toString());
		}
		try {
			fileManager.close();
		} catch (IOException e) {
			System.err.println("Filemanager closing failed.");
		}

		File[] classFiles = dir.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.endsWith(".class");
			}
		});

		for (File file : new File(Paths.CLASS_PATH).listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.endsWith(".class");
			}
		}))
			file.delete();

		for (File file : classFiles) {
			File newFile = new File(Paths.CLASS_PATH + file.getName());
			newFile.mkdirs();
			try {
				Files.move(file.toPath(), newFile.toPath(), REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("Class " + file.toPath() + " moving failed.");
			}
		}

		return true;
	}
}
