package il.co.topq.integframework.assertion;

import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;
import il.co.topq.integframework.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.xml.XmlSuite;

public class CollectionAssertion<E> extends AbstractAssertionLogic<List<E>> {

	private String expectedTitle = "", actualTitle = "";
	final protected List<E> expected;
	private List<E> singlesInActual, singlesInExpected;
	private long maxMismatchesToReport = -1, maxNotFoundToReport = -1, maxUnexpectedToReport = -1;
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

	public CollectionAssertion(Collection<E> expected) {
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

	public CollectionAssertion<E> maxMismatchesToReport(long max) {
		this.maxMismatchesToReport = max;
		return this;
	}

	public CollectionAssertion<E> maxNotFoundToReport(long max) {
		this.maxNotFoundToReport = max;
		return this;
	}

	public CollectionAssertion<E> maxUnexpectedToReport(long max) {
		this.maxUnexpectedToReport = max;
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

	public CollectionAssertion<E> andKeepMatchesForFurtherAnalysis() {
		keepMatches = true;
		return this;
	}

	@Override
	public String getTitle() {
		StringBuilder titleBuilder = new StringBuilder("all items");
		if (!StringUtils.isEmpty(expectedTitle)) {
			titleBuilder.append(" from ").append(expectedTitle);
		}
		titleBuilder.append(" should exists");
		if (!StringUtils.isEmpty(actualTitle)) {
			titleBuilder.append(" in ").append(actualTitle);
		}
		if (allItems) {
			titleBuilder.append(" and nothing more");
		}
		return titleBuilder.toString();
	}

	public String getMessage() {
		return status ? "passed" : "failed";
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
			message = "";
			singlesInExpected = new ArrayList<E>(expected.size());
			singlesInActual = new ArrayList<E>(actual.size());

			if (!allItems) {
				if (expected.size() > actual.size()) {
					status = false;
					Reporter.logToFile(new AssertionError(new StringBuilder("Size of actual items is ").append(actual.size())
							.append(", should be at least ").append(expected.size()).append("\n")));
					if (exitIfSizeDoesNotMatch) {
						return;
					}
				}
				singlesInActual.addAll(actual);
			} else {
				if (expected.size() != actual.size()) {
					Reporter.logToFile(new AssertionError(new StringBuilder("Size of actual items is ").append(actual.size())
							.append("instead of ").append(expected.size()).append("\n")));
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
				if (iactual >= sortedActual.size()) {
					// when atLeast flag is true, all expected items which had
					// no actual - must be added to the singles list.
					singlesInExpected.addAll(sortedExpected.subList(iexpected, sortedExpected.size()));
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
			long singlesCounter = 0;
			for (E e : singlesInExpected) {
				Reporter.logToFile("Item not found",
						new AssertionError(new StringBuilder(e.toString()).append(" was expected but not found")));
				if (++singlesCounter > maxNotFoundToReport) {
					break;
				}
			}

			singlesCounter = 0;
			for (E e : singlesInActual) {
				StringBuilder itemFound = new StringBuilder(e.toString()).append(" was found");
				if (!allItems) {
					if (++singlesCounter > maxUnexpectedToReport) {
						break;
					}
					itemFound.append(" unexpectedly");
					Reporter.logToFile("Unexpected item found", new AssertionError(itemFound));
				} else {
					Reporter.log(itemFound.toString(), XmlSuite.DEFAULT_VERBOSE + 1);
				}

			}

			status = singlesInExpected.isEmpty() && (singlesInActual.isEmpty() || !allItems);
			if (!status) {
				message = "Total items not found: " + singlesInExpected.size() + "\n";
				if (allItems) {
					message = message + "Total unexpected items found: " + singlesInActual.size() + "\n";
				}
			}
		} else if (matches != null) {
			status = true;
			Reporter.log("Validating matches data using " + comparator.toString(), XmlSuite.DEFAULT_VERBOSE + 1);
			long mismatchCounter = 0;
			String itemMismatchTitle = "Item mismatch when comparing " + comparator.toString();
			for (PairOfMatches<E> match : matches) {
				if (comparator.compare(match.actual, match.expected) != 0) {
					status = false;
					StringBuilder itemMismatch = new StringBuilder("The item:\n[").append(match.actual)
							.append("]\nwhich was acually found, did not match the expected item\n[")
							.append(match.expected.toString()).append("]");
					if (++mismatchCounter <= maxMismatchesToReport) {
						Reporter.logToFile(itemMismatchTitle, itemMismatch.toString(), Color.RED);
					}
				}
			}
			message = "Total reproccessed items:" + matches.size() + "\n";
			if (mismatchCounter > 0) {
				message = message + "Total mismatch data items found:" + mismatchCounter + "\n when comparing "
						+ comparator.toString();
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

	@SafeVarargs
	public final CollectionAssertion<E> compareWith(Comparator<E>... comparators) {
		return compareWith(Arrays.asList(comparators));
	}

	public CollectionAssertion<E> compareWith(List<Comparator<E>> comparators) {
		for (Comparator<E> comparator : comparators) {
			withComparator(comparator);
		}
		return this;
	}

	/**
	 * reset {@link Comparator}s list to the new given comparators
	 * 
	 * @param comparators
	 * @return
	 */
	@SafeVarargs
	public final CollectionAssertion<E> andNowCompareWith(Comparator<E>... comparators) {
		this.comparators = null;
		return andNowCompareWith(Arrays.asList(comparators));
	}

	/**
	 * reset {@link Comparator}s list to the new given comparators
	 * 
	 * @param comparators
	 * @return
	 */
	public CollectionAssertion<E> andNowCompareWith(List<Comparator<E>> comparators) {
		this.comparators = null;
		for (Comparator<E> comparator : comparators) {
			withComparator(comparator);
		}
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
