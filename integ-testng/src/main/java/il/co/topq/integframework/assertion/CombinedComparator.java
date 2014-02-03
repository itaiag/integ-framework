package il.co.topq.integframework.assertion;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

class CombinedComparator<T> implements Comparator<T> {

	private final List<Comparator<T>> comparators;
	private Comparator<T> currComparator;

	public CombinedComparator(List<Comparator<T>> comparators) {
		this.comparators = comparators.subList(0, comparators.size());
	}

	@Override
	public int compare(T o1, T o2) {
		int comparisonResult = 0;
		Iterator<Comparator<T>> comparatorsIterator = this.comparators.iterator();
		while (comparisonResult == 0 && comparatorsIterator.hasNext()) {
			currComparator = comparatorsIterator.next();
			comparisonResult = currComparator.compare(o1, o2);
		}
		if (!comparatorsIterator.hasNext()){
			currComparator = null; //cleanup for next iteration
		}
		return comparisonResult;
	}

	@Override
	public String toString() {
		return currComparator == null ? "" : currComparator.toString();
	}

}