package com.techsoft.partselector.util;

import java.util.List;
import java.util.Vector;

import com.techsoft.partselector.model.HashVector;

public class RuleResult {
	private static volatile RuleResult instance;

	private List<HashVector> results;

	private RuleResult() {
		this.results = new Vector<HashVector>();
	}

	public static RuleResult getInstance() {
		if (instance == null) {
			synchronized (RuleResult.class) {
				if (instance == null)
					instance = new RuleResult();
			}
		}
		return instance;
	}

	public void clearResults() {
		this.results.clear();
	}

	public void addResult(HashVector result) {
		this.results.add(result);
	}

	public Vector<HashVector> getResults() {
		return new Vector<HashVector>(this.results);
	}
}
