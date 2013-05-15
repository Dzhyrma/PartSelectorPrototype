package com.helpers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.models.HashVector;

public class RankComparator<HashVector> implements Comparator<HashVector> {

	private Map<HashVector, Integer> frequency;
	
	public RankComparator (Map<HashVector, Integer> frequency) {
		this.frequency = new HashMap<HashVector, Integer>(frequency); 
	}

	@Override
	public int compare(HashVector o1, HashVector o2) {
		Integer r1 = this.frequency.containsKey(o1) ? this.frequency.get(o1) : 0;
		Integer r2 = this.frequency.containsKey(o2) ? this.frequency.get(o2) : 0;
		r1 = r1 == null ? Integer.valueOf(0) : r1;
		r2 = r2 == null ? Integer.valueOf(0) : r2;
		return r2 - r1;
	}

}
