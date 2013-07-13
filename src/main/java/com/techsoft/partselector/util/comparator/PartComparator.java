package com.techsoft.partselector.util.comparator;

import java.util.Comparator;

import com.techsoft.partselector.model.Part;

/** Comparator for comparing parts by their name.
 * 
 * @param <T> - type of the part.
 * 
 * @author Andrii Dzhyrma */
public class PartComparator<T extends Part> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2)
			return 0;
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}
