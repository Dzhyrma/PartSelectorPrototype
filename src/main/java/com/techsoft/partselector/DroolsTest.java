package com.techsoft.partselector;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.techsoft.partselector.model.Assembly;
import com.techsoft.partselector.model.AssemblyNode;
import com.techsoft.partselector.model.HashVector;
import com.techsoft.partselector.model.Part;
import com.techsoft.partselector.model.Reference;
import com.techsoft.partselector.util.KnowledgeBaseExtractor;
import com.techsoft.partselector.util.RuleResult;
import com.techsoft.partselector.util.io.AssemblyReader;
import com.techsoft.partselector.util.io.ClassReader;
import com.techsoft.partselector.util.io.PartReader;
import com.techsoft.partselector.util.reflect.ClassCompiler;
import com.techsoft.partselector.util.comparator.RankComparator;

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
		System.out.println(AssemblyReader.readAssemblyFromXML("Assembly.xml"));
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
	        p.convertToObjects((Class<? extends Part>) classReader.loadClass("Washer"), attributeMap);
        } catch (ClassNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }

		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = null;
			//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
			// go !
			for (int j = 100; j <= 100; j++) {
				Assembly someAssembly = null;
				NUMBER_OF_INSTANCES = j;
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < 1; i++) {
					ksession = kbase.newStatefulKnowledgeSession();
					someAssembly = initFacts(ksession);
					RuleResult.getInstance().clearResults();
					ksession.fireAllRules();
					ksession.dispose();
				}
				long stopTime = System.currentTimeMillis();
				long elapsedTime = stopTime - startTime;
				System.out.println(elapsedTime);
				System.out.println(RuleResult.getInstance().getResults());
				List<HashVector> result = RuleResult.getInstance().getResults();
				KnowledgeBaseExtractor kbe = new KnowledgeBaseExtractor();
				kbe.setAssembly(someAssembly);
				RankComparator<HashVector> rc = new RankComparator<HashVector>(kbe.extractKnowledge());
				Collections.sort(result, rc);
				System.out.println(result);
			}
			//logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static Assembly initFacts(StatefulKnowledgeSession ksession) {
		inputParameters = new InputParameters();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("Dbore", 17);
		parameters.put("Lclamp", 29);
		parameters.put("MinGap", 1);
		parameters.put("MinOverlap", 3);
		parameters.put("MaxOverlap", 10);
		parameters.put("WasherMinGap", 1);
		parameters.put("WasherMinOverlap", 0);
		parameters.put("Screw", Screw.class);
		parameters.put("ScrewNut", ScrewNut.class);
		parameters.put("Washer", Washer.class);
		inputParameters.setParameters(parameters);

		ksession.insert(inputParameters);

		EyeScrew screw1 = null;
		ScrewNut screwNut1 = null;
		Washer washer1 = null;
		for (int dn = 14; dn <= 18; dn += 2)
			for (int l = 50; l < 60; l += 5)
				for (int thread = 11; thread <= 15; thread += 2) {
					EyeScrew screw = new EyeScrew("Schraube_M" + thread + "_" + dn + "x" + l + "_DIN_933");
					screw.setHeadDiameter((dn == 18) ? 27 : (dn + 8));
					screw.setLength(l);
					screw.setNominalDiameter(dn);
					screw.setThread(thread);
					ksession.insert((Object) screw);
					if (screw.getName().compareTo("Schraube_M11_14x50_DIN_933") == 0)
						screw1 = screw;
				}

		int rand = new Random().nextInt(NUMBER_OF_INSTANCES - 50);

		for (int dn = 14; dn <= 14 + rand; dn += 2)
			for (int thread = 11; thread <= 15; thread += 2) {
				ScrewNut screwNut = new ScrewNut("Mutter_M" + thread + "_" + dn + "_DIN_934");
				screwNut.setNominalDiameter(dn);
				screwNut.setHeight(dn - 3);
				screwNut.setThread(thread);
				ksession.insert((Object) screwNut);
				if (screwNut.getName().compareTo("Mutter_M11_14_DIN_934") == 0)
					screwNut1 = screwNut;
			}

		for (int i = 0; i <= NUMBER_OF_INSTANCES - 50 - rand; i += 2) {
			Washer washer = new Washer("Scheibe_B_" + (15 + i) + "_DIN_125");
			washer.setInnerDiameter(15 + i);
			washer.setOuterDiameter(28 + ((i == 4) ? (i + 2) : i));
			washer.setThickness(3);
			ksession.insert((Object) washer);
			if (washer.getName().compareTo("Scheibe_B_15_DIN_125") == 0)
				washer1 = washer;
		}
		
		Assembly assembly = new Assembly("main assemby");
		Assembly bg1 = new Assembly("BG1");
		AssemblyNode screwNode = new AssemblyNode(screw1, new Reference(Double.valueOf(0), Double.valueOf(0), Double.valueOf(0)));
		AssemblyNode screwNutNode = new AssemblyNode(screwNut1, new Reference(Double.valueOf(0), Double.valueOf(0), Double.valueOf(5)));
		AssemblyNode washerNode = new AssemblyNode(washer1, new Reference(Double.valueOf(0), Double.valueOf(0), Double.valueOf(4)));
		bg1.addNode(screwNode);
		bg1.addNode(screwNutNode);
		bg1.addNode(washerNode);
		assembly.addAssembly(bg1);
		
		return assembly;
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


