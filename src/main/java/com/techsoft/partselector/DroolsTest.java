package com.techsoft.partselector;

import java.io.IOException;
import java.util.HashMap;
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

import classes.EyeScrew;
import classes.Screw;
import classes.ScrewNut;
import classes.Washer;

import com.techsoft.partselector.model.AssemblyNode;
import com.techsoft.partselector.model.Reference;
import com.techsoft.partselector.util.rules.InputParameters;
import com.techsoft.partselector.util.rules.RuleResult;

/** This is a sample class to launch a rule. Used for tests only */
public class DroolsTest {

	private static InputParameters inputParameters;

	private static int NUMBER_OF_INSTANCES = 200;

	public static final void main(String[] args) throws IOException {

		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = null;
			//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
			// go !
			//ksession = kbase.newStatefulKnowledgeSession();

			for (int j = 100; j <= 400; j++) {
				NUMBER_OF_INSTANCES = j;
				RuleResult.getInstance().clearResults();
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < 100; i++) {
					ksession = kbase.newStatefulKnowledgeSession();
					initFacts(ksession);
					RuleResult.getInstance().clearResults();
					ksession.fireAllRules();
					ksession.dispose();
				}
				long stopTime = System.currentTimeMillis();
				long elapsedTime = stopTime - startTime;
				System.out.println(elapsedTime);
				/*System.out.println(RuleResult.getInstance().getResults());
				List<HashVector> result = RuleResult.getInstance().getResults();
				KnowledgeBaseExtractor kbe = new KnowledgeBaseExtractor();
				RankComparator<HashVector> rc = new RankComparator<HashVector>(kbe.extractKnowledge(someAssembly, "ExtractingRule.drl"));
				Collections.sort(result, rc);
				System.out.println(result);*/
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
		parameters.put("Washer", Washer.class);
		parameters.put("ScrewNut", ScrewNut.class);
		parameters.put("Screw", Screw.class);
		inputParameters.setParameters(parameters);

		ksession.insert(inputParameters);

		int rand1 = new Random().nextInt(NUMBER_OF_INSTANCES);
		int rand2 = new Random().nextInt(NUMBER_OF_INSTANCES);
		if (rand1 > rand2) {
			rand1 ^= rand2;
			rand2 ^= rand1;
			rand1 ^= rand2;
		}

		for (int l = 50; l <= 50 + rand1; l += 1) {
			EyeScrew screw = new EyeScrew("Schraube_M11_16x" + l + "_DIN_933");
			screw.setHeadDiameter(24);
			screw.setLength(l);
			screw.setNominalDiameter(16);
			screw.setThread(11);
			AssemblyNode assemblyNode = new AssemblyNode(screw, new Reference(0d, 0d, (double) l));
			ksession.insert((Object) assemblyNode);
		}

		for (int dn = 14; dn <= 14 + rand2 - rand1; dn += 1) {
			ScrewNut screwNut = new ScrewNut("Mutter_M11_" + dn + "_DIN_934");
			screwNut.setNominalDiameter(dn < 20 ? 16 : dn);
			screwNut.setHeight(dn - 3);
			screwNut.setThread(11);
			AssemblyNode assemblyNode = new AssemblyNode(screwNut, new Reference(0d, 0d, (double) dn));
			ksession.insert((Object) assemblyNode);
		}

		for (int i = 0; i <= NUMBER_OF_INSTANCES - rand2; i += 1) {
			Washer washer = new Washer("Scheibe_B_" + (15 + i) + "_DIN_125");
			washer.setInnerDiameter(15 + i);
			washer.setOuterDiameter(28 + i);
			washer.setThickness(3);
			AssemblyNode assemblyNode = new AssemblyNode(washer, new Reference(0d, 0d, (double) i));
			ksession.insert((Object) assemblyNode);
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("extraction/ExtractingRule.drl"), ResourceType.DRL);
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
