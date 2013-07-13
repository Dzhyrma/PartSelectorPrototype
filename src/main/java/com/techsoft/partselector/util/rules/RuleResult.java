package com.techsoft.partselector.util.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.techsoft.partselector.model.HashVector;

/** Class that used to gather results obtained by the rules.
 * 
 * @author Andrii Dzhyrma */
public class RuleResult {
	/** @uml.property name="instance"
	 * @uml.associationEnd */
	private static volatile RuleResult instance;

	/** @uml.property name="results"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     inverse="ruleResult:com.techsoft.partselector.model.HashVector" */
	private List<HashVector> results;

	private RuleResult() {
		this.results = new Vector<HashVector>();
	}

	/** @return current instance of the singleton class.
	 * @uml.property name="instance" */
	public static RuleResult getInstance() {
		if (instance == null) {
			synchronized (RuleResult.class) {
				if (instance == null)
					instance = new RuleResult();
			}
		}
		return instance;
	}

	/** Clears results. */
	public void clearResults() {
		this.results.clear();
	}

	/** Adds new result to the current instance.
	 * 
	 * @param result - combination to be added. */
	public synchronized void addResult(HashVector result) {
		this.results.add(result);
	}

	/** Gets the results obtained during the rule firing process.
	 * 
	 * @return - list of the results. */
	public List<HashVector> getResults() {
		return new ArrayList<HashVector>(this.results);
	}
}
