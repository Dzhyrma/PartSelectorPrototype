package com.techsoft.partselector.util.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.techsoft.partselector.model.HashVector;

/** Comparator for combinations. Used to sort them by frequency.
 * 
 * @param <T>
 * 
 * @author Andrii Dzhyrma */
public class RankComparator<T extends HashVector> implements Comparator<T> {

	private Map<T, Integer> frequency;

	/** Initializes the comparator with the given frequency map for each
	 * combination.
	 * 
	 * @param frequency */
	public RankComparator(Map<T, Integer> frequency) {
		this.frequency = new HashMap<T, Integer>(frequency);
	}

	@Override
	public int compare(T o1, T o2) {
		Integer r1 = this.frequency.get(o1);
		Integer r2 = this.frequency.get(o2);
		r1 = r1 == null ? Integer.valueOf(0) : r1;
		r2 = r2 == null ? Integer.valueOf(0) : r2;
		return r2 - r1;
	}

}
