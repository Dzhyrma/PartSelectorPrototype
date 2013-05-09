package com.sample;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javassist.compiler.Javac;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.helpers.ClassCompiler;
import com.helpers.ClassGenerator;
import com.helpers.ClassReader;
import com.helpers.PartReader;

import classes.EyeScrew;
import classes.InputParameters;
import classes.Screw;
import classes.ScrewNut;
import classes.Washer;

/** This is a sample class to launch a rule. */
public class DroolsTest {

	private static InputParameters inputParameters;

	private static int NUMBER_OF_INSTANCES = 200;

	public static final void main(String[] args) throws IOException {
		PartReader p = new PartReader();
		p.loadInstances("./din128b.par");

		if (!ClassCompiler.compileAllClasses()) {
			System.err.println("Can not compile all classes");
			return;
		}

		ClassReader classReader = ClassReader.getInstance();
		System.out.println(Arrays.toString(classReader.getClassNames()));
		Map<String, String> attributeMap = new HashMap<String, String>();
		attributeMap.put("name", "NAME");
		attributeMap.put("innerDiameter", "DI");
		try {
			p.convertToObjects(classReader.loadClass("Washer"), attributeMap);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
		    ClassGenerator c = new ClassGenerator(classReader.loadClass("EyeScrew2"));
		    c.saveClass();
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}*/

		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = null;
			//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
			// go !
			for (int j = 100; j <= 100; j++) {
				NUMBER_OF_INSTANCES = j;
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < 1; i++) {
					ksession = kbase.newStatefulKnowledgeSession();
					initFacts(ksession);
					ksession.fireAllRules();
					ksession.dispose();
				}
				long stopTime = System.currentTimeMillis();
				long elapsedTime = stopTime - startTime;
				System.out.println(elapsedTime);
			}
			//logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static void initFacts(StatefulKnowledgeSession ksession) {
		inputParameters = new InputParameters();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("Dbore", 17);
		parameters.put("Lclamp", 29);
		parameters.put("MinGap", 1);
		parameters.put("MinOverlap", 3);
		parameters.put("MaxOverlap", 10);
		parameters.put("WasherMinGap", 1);
		parameters.put("WasherMinOverlap", 0);
		inputParameters.setParameters(parameters);

		ksession.insert(inputParameters);

		for (int dn = 14; dn <= 18; dn += 2)
			for (int l = 50; l < 60; l += 5)
				for (int thread = 11; thread <= 15; thread += 2) {
					EyeScrew screw = new EyeScrew("Schraube_M" + thread + "_" + dn + "x" + l + "_DIN_933");
					screw.setHeadDiameter((dn == 18) ? 27 : (dn + 8));
					screw.setLength(l);
					screw.setNominalDiameter(dn);
					screw.setThread(thread);
					ksession.insert((Object)screw);
				}

		int rand = new Random().nextInt(NUMBER_OF_INSTANCES - 50);

		for (int dn = 14; dn <= 14 + rand; dn += 2)
			for (int thread = 11; thread <= 15; thread += 2) {
				ScrewNut screwNut = new ScrewNut("Mutter_M" + thread + "_" + dn + "_DIN_934");
				screwNut.setNominalDiameter(dn);
				screwNut.setHeight(dn - 3);
				screwNut.setThread(thread);
				ksession.insert((Object)screwNut);
			}

		for (int i = 0; i <= NUMBER_OF_INSTANCES - 50 - rand; i += 2) {
			Washer washer = new Washer("Scheibe_B_" + (15 + i) + "_DIN_125");
			washer.setInnerDiameter(15 + i);
			washer.setOuterDiameter(28 + ((i == 4) ? (i + 2) : i));
			washer.setThickness(3);
			ksession.insert((Object)washer);
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

}