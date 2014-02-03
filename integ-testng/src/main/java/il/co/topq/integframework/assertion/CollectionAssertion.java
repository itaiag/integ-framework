package il.co.topq.integframework.assertion;

import il.co.topq.integframework.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionAssertion<E> extends AbstractAssertionLogic<List<E>> {

	private String expectedTitle = "", actualTitle = "";
	final protected List<E> expected;
	private List<E> singlesInActual, singlesInExpected;

	private static final class PairOfMatches<E> {
		final E actual, expected;

		public PairOfMatches(E actual, E expected) {
			this.actual = actual;
			this.expected = expected;
		}
	}

	protected boolean keepMatches = true;
	private List<PairOfMatches<E>> matches;

	protected void addMatch(E actual, E expected) {
		if (keepMatches) {
			if (matches == null) {
				matches = new ArrayList<CollectionAssertion.PairOfMatches<E>>();
			}
			matches.add(new PairOfMatches<E>(actual, expected));
		}
	}

	protected boolean allItems = false, exitIfSizeDoesNotMatch = true;

	private List<Comparator<E>> comparators;

	StringBuilder report = new StringBuilder();

	public CollectionAssertion(List<E> expected) {
		this.expected = new ArrayList<E>(expected);
	}

	public CollectionAssertion<E> containsAllItems() {
		this.allItems = true;
		return this;
	}

	public CollectionAssertion<E> dontExitIfSizeDoesNotMatch() {
		this.exitIfSizeDoesNotMatch = false;
		return this;
	}

	public CollectionAssertion<E> withActualTitled(String title) {
		actualTitle = title;
		return this;
	}

	public CollectionAssertion<E> withExpectedTitled(String title) {
		expectedTitle = title;
		return this;
	}
	public CollectionAssertion<E> andKeepMatchesForFurtherAnalysis(){
		keepMatches = true;
		return this;
	}

	@Override
	public String getTitle() {
		StringBuilder titleBuilder = new StringBuilder("Assert that all items");
		if (!StringUtils.isEmpty(expectedTitle)) {
			titleBuilder.append(" from ").append(expectedTitle);
		}
		titleBuilder.append(" exists");
		if (!StringUtils.isEmpty(actualTitle)) {
			titleBuilder.append(" in ").append(actualTitle);
		}
		if (allItems) {
			titleBuilder.append(" and nothing more");
		}
		return titleBuilder.toString();
	}

	public String getMessage() {
		message = report.toString();
		return message;
	}

	@Override
	public void doAssertion() {
		Comparator<E> comparator;
		if (this.comparators == null) {
			comparator = simpleComparator;
		} else {
			if (comparators.size() == 1) {
				comparator = comparators.get(0);
			} else {
				comparator = new CombinedComparator<E>(comparators);
			}
		}
		if (keepMatches && matches == null || !keepMatches) {
			singlesInExpected = new ArrayList<E>(expected.size());
			singlesInActual = new ArrayList<E>(actual.size());

			if (!allItems) {
				if (expected.size() > actual.size()) {
					report.append("Size of actual items is ").append(
							actual.size());
					report.append(", should be at least ")
							.append(expected.size()).append("\n");
					status = false;
					if (exitIfSizeDoesNotMatch) {
						return;
					}
				}
				singlesInActual.addAll(actual);
			} else {
				if (expected.size() != actual.size()) {
					report.append("Size of actual items is ").append(
							actual.size());
					report.append("instead of ").append(expected.size())
							.append("\n");
					status = false;
					if (exitIfSizeDoesNotMatch) {
						return;
					}
				}
			}
			exitIfSizeDoesNotMatch = true;

			List<E> sortedActual = actual.subList(0, actual.size());
			List<E> sortedExpected = expected.subList(0, expected.size());
			Collections.sort(sortedActual, comparator);
			Collections.sort(sortedExpected, comparator);
			for (int iactual = 0, iexpected = 0; (iactual < sortedActual.size() || !allItems)
					&& (iexpected < sortedExpected.size());) {
				if (iactual >= sortedActual.size()) {// when atLeast flag is true,
														// all expected items which
														// had no actual - must be
														// added to the singles list.
					singlesInExpected.addAll(sortedExpected.subList(iexpected,
							sortedExpected.size()));
					break;
				}
				E currActual = sortedActual.get(iactual);
				E currExpected = sortedExpected.get(iexpected);

				int comparison = comparator.compare(currActual, currExpected);
				if (0 == comparison) {
					// "Found " + currActual.toString());
					addMatch(currActual, currExpected);

					singlesInActual.remove(currActual);
					iexpected++;
					iactual++;
				} else if (comparison > 0) {
					singlesInExpected.add(currExpected);
					iexpected++;
				} else {
					singlesInActual.add(currActual);
					iactual++;
				}
			}
			if (report != null) {
				for (E e : singlesInExpected) {
					report.append(e.toString()).append(
							" was expected but not found");
					report.append("\n");
				}

				for (E e : singlesInActual) {
					report.append(e.toString()).append(" was found");
					if (!allItems) {
						report.append(" unexpectedly");
					}
					report.append("\n");
				}

			}

			status = singlesInExpected.isEmpty()
					&& (singlesInActual.isEmpty() || !allItems);
		}
		else if (matches!=null){
			report.append("validating matches data");
			for (PairOfMatches<E> match : matches) {
				if (comparator.compare(match.actual, match.expected)!=0){
					report.append(match.actual).append(" did not match ").append(match.expected).append(" when comparing using ").append(comparator.toString()).append("\n");  
				}
			}
		}
	}

	public CollectionAssertion<E> withComparator(Comparator<E> comparator) {
		if (this.comparators == null) {
			comparators = new ArrayList<Comparator<E>>();
		}
		comparators.add(comparator);
		return this;
	}

	protected final Comparator<E> simpleComparator = new Comparator<E>() {

		@Override
		public int compare(E o1, E o2) {
			if (o1 instanceof Comparable<?>) {
				@SuppressWarnings("unchecked")
				Comparable<E> o1a = (Comparable<E>) o1;
				return o1a.compareTo(o2);
			} else {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					return -1;
				} else if (o2 == null)
					return 1;
				return Integer.compare(o1.hashCode(), o2.hashCode());
			}
		}
	};

}
