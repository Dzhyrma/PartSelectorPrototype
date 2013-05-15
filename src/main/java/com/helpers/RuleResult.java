package com.helpers;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import com.models.HashVector;

public class RuleResult {
	private static volatile RuleResult instance;

	private Vector<HashVector> results;

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

	public ArrayList<HashVector> getResults() {
		return new ArrayList<HashVector>(this.results);
	}
}
