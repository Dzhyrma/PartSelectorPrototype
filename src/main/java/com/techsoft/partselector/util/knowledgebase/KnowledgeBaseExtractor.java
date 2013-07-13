package com.techsoft.partselector.util.knowledgebase;

import java.util.Collection;
import java.util.HashMap;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.model.*;
import com.techsoft.partselector.util.reflect.ClassReader;
import com.techsoft.partselector.util.rules.InputParameters;
import com.techsoft.partselector.util.rules.RuleResult;

/** Class that extracts useful combinations from the assembly files using rules.
 * 
 * @author Andrii Dzhyrma */
public class KnowledgeBaseExtractor {
	private KnowledgeBase knowledgeBase;

	private boolean prepareKnowledgeBase(String path) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource(Paths.EXTRACTION_RULES_PATH + path), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors)
				System.err.println(error);
			return false;
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		this.knowledgeBase = kbase;
		return true;
	}

	private void traverseAssembly(Assembly assembly, InputParameters inputParameters) {
		if (assembly == null)
			return;
		Collection<AssemblyNode> nodes = assembly.getNodes();
		if (nodes != null) {
			StatefulKnowledgeSession ksession = this.knowledgeBase.newStatefulKnowledgeSession();
			ksession.insert(inputParameters);
			for (AssemblyNode node : nodes)
				ksession.insert(node);
			ksession.fireAllRules();
		}
		Collection<Assembly> assemblies = assembly.getAssemblies();
		if (assemblies == null)
			return;
		for (Assembly item : assemblies)
			traverseAssembly(item, inputParameters);
	}

	/** Returns the frequency map of the extracted combinations.
	 * 
	 * @param assembly - assembly to read information from.
	 * @param ruleName - name of the rule to be applied.
	 * @return the frequency map. Null, if something went wrong. */
	public HashMap<HashVector, Integer> extractKnowledge(Assembly assembly, String ruleName) {
		if (assembly == null || ruleName == null)
			return null;
		RuleResult.getInstance().clearResults();
		if (!prepareKnowledgeBase(ruleName))
			return null;
		InputParameters inputParameters = new InputParameters();
		inputParameters.setParameters(new HashMap<String, Object>(ClassReader.getInstance().getClassMap()));

		traverseAssembly(assembly, inputParameters);
		HashMap<HashVector, Integer> result = new HashMap<HashVector, Integer>();
		for (HashVector item : RuleResult.getInstance().getResults())
			result.put(item, result.containsKey(item) ? result.get(item) + 1 : 1);
		return result;
	}
}
