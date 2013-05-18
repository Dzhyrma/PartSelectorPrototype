package com.techsoft.partselector.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import classes.InputParameters;
import classes.Screw;
import classes.ScrewNut;
import classes.Washer;

import com.techsoft.partselector.model.*;

public class KnowledgeBaseExtractor {
	private Assembly assembly;
	private KnowledgeBase knowledgeBase;

	private boolean prepareKnowledgeBase() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("ExtractingRule.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			return false;
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		this.knowledgeBase = kbase;
		return true;
	}

	private void traverseAssembly(Assembly assembly) {
		if (assembly == null)
			return;
		Collection<AssemblyNode> nodes = assembly.getNodes();
		if (nodes != null) {
			StatefulKnowledgeSession ksession = this.knowledgeBase.newStatefulKnowledgeSession();
			InputParameters inputParameters = new InputParameters();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("Screw", Screw.class);
			parameters.put("ScrewNut", ScrewNut.class);
			parameters.put("Washer", Washer.class);
			inputParameters.setParameters(parameters);
			ksession.insert(inputParameters);
			for (AssemblyNode node : nodes)
				ksession.insert(node);
			ksession.fireAllRules();
		}
		Collection<Assembly> assemblies = assembly.getAssemblies();
		if (assemblies == null)
			return;
		for (Assembly item : assemblies)
			traverseAssembly(item);
	}

	public HashMap<HashVector, Integer> extractKnowledge() {
		if (this.assembly == null)
			return null;
		RuleResult.getInstance().clearResults();
		if (!prepareKnowledgeBase())
			return null;
		traverseAssembly(this.assembly);
		HashMap<HashVector, Integer> result = new HashMap<>();
		for (HashVector item : RuleResult.getInstance().getResults())
			result.put(item, result.containsKey(item) ? result.get(item) + 1 : 1);
		return result;
	}

	public void setAssembly(Assembly assembly) {
		this.assembly = assembly;
	}
}
