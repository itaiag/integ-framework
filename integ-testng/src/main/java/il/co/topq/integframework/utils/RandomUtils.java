package il.co.topq.integframework.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Different random utilities, most require a Random object to allow Random with
 * seed
 * 
 * @see http://java.sun.com/j2se/1.4.2/docs/api/java/util/Random.html
 * 
 * @author Nizan Freedman
 * 
 */
public class RandomUtils {
	static final List<Character> ALPHABET_LETTERS_ALL;

	static {
		List<Character> letters = new ArrayList<>();
		for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
			letters.add((char) i);
		}

		for (int i = (int) 'a'; i <= (int) 'z'; i++) {
			letters.add((char) i);
		}

		ALPHABET_LETTERS_ALL = letters;
	}

	/**
	 * 
	 * 
	 * @param differentThen
	 *            the char to find replacement for
	 * @param caseSensitive
	 *            if False will return a char which is also different from the
	 *            upper and lower case representation of the given char
	 * @param random
	 *            the Random object to randomize with
	 * @return a random English alphabetical char
	 */
	public static char getRandomAlphabet(char differentThen, boolean caseSensitive, Random random) {
		List<Character> temp = new ArrayList<>(ALPHABET_LETTERS_ALL);
		if (differentThen != ' ') {
			if (caseSensitive) {
				temp.remove(new Character(differentThen));
			} else {
				temp.remove(new Character((char) ((differentThen + "").toUpperCase().charAt(0))));
				temp.remove(new Character((char) ((differentThen + "").toLowerCase().charAt(0))));
			}
		}

		int index = getRandomInt(0, temp.size() - 1, random);

		return temp.get(index);

	}

	/**
	 * Get an arrayList of chars in given ranges
	 * 
	 * @param ranges
	 *            the different Ascii ranges for the char - for example:
	 *            {{67,95},{97,115}}
	 * @return an ArrayList of all chars in ranges
	 */
	public static List<Character> getAllCharsInRange(int[][] ranges) {
		List<Character> chars = new ArrayList<>();
		for (int[] range : ranges) {
			for (int i = range[0]; i <= range[1]; i++) {
				chars.add((char) i);
			}
		}

		return chars;
	}

	/**
	 * Get a random char in given ranges, different than given char
	 * 
	 * @param ranges
	 *            the different Ascii ranges for the char - for example:
	 *            {{67,95},{97,115}}
	 * @param differentThen
	 *            the char to find replacement for
	 * @param caseSensitive
	 *            if False will return a char which is also different from the
	 *            upper and lower case representation of the given char
	 * @param ranbdom
	 *            the Random object to randomize with
	 * @return a random chars in given conditions
	 */
	public static char getRandomChar(int[][] ranges, char differentThen, boolean caseSensitive, Random random) {
		List<Character> chars = getAllCharsInRange(ranges);
		if (differentThen != ' ') {
			if (caseSensitive) {
				chars.remove(new Character(differentThen));
			} else {
				chars.remove(new Character((char) ((differentThen + "").toUpperCase().charAt(0))));
				chars.remove(new Character((char) ((differentThen + "").toLowerCase().charAt(0))));
			}
		}

		int index = getRandomInt(0, chars.size() - 1, random);

		return chars.get(index);
	}

	/**
	 * Get a random digit char different then given char
	 * 
	 * @param differentThen
	 *            the char to get a replacement for
	 * @param random
	 *            the Random object to randomize with
	 * @return a random Digit, other then the given one
	 */
	public static char getRandomDigit(char differentThen, Random random) {
		return getRandomChar(new int[][] { { (int) '0', (int) '9' } }, differentThen, true, random);
	}

	/**
	 * Get a random int value in given range
	 * 
	 * @param min
	 *            range start
	 * @param max
	 *            range end
	 * @param random
	 *            the Random object to random with
	 * @return an int value in the given range
	 */
	public static int getRandomInt(int min, int max, Random random) {
		if (max <= min)
			throw new IllegalArgumentException(max + " is smaller then " + min);
		int diff = max - min;

		if (diff < 0) {
			int a = getRandomInt(0, (max == 0) ? 1 : max, random);
			if (max == 0 && a == 0)
				a--;
			int b;
			if (min == Integer.MIN_VALUE) {
				b = -getRandomInt(0, Integer.MAX_VALUE - 2, random);
			} else {
				b = getRandomInt(min, 0, random);
			}
			return random.nextBoolean() ? a : b;
		}
		int randomInt = random.nextInt(diff);
		return randomInt + min;
	}

	/**
	 * Get a random long value in given range
	 * 
	 * @param min
	 *            range start (inclusive)
	 * @param max
	 *            range end (exclusive)
	 * @param random
	 *            the Random object to random with
	 * @return an int value in the given range
	 */
	public static long getRandomLong(long min, long max, Random random) {
		if (max <= min)
			throw new IllegalArgumentException(max + " is smaller then " + min);
		long val, bits, diff = max - min;
		if (diff < 0) {
			long a = getRandomLong(0, (max == 0) ? 1 : max, random);
			if (max == 0 && a == 0)
				a--;
			long b;
			if (min == Long.MIN_VALUE) {
				b = -getRandomLong(0, Long.MAX_VALUE - 2, random);
			} else {
				b = getRandomLong(min, 0, random);
			}
			return random.nextBoolean() ? a : b;
		}
		do {
			bits = (random.nextLong() << 1) >>> 1;
			val = bits % diff + min;
		} while (val < min || max <= val);
		return val;

	}

	public static float getRandomFloat(float min, float max, Random random) {
		float diff = max - min;
		float randomFloat = random.nextFloat();
		randomFloat *= diff;
		return randomFloat + min;
	}

	/**
	 * Get a random group of requested size constructed of numbers in the given
	 * ranges
	 * 
	 * @param ranges
	 *            Two-dimensional array of int ranges, for example {{1,5},{4,9}}
	 * @param amount
	 *            the group size to return
	 * @param random
	 *            the Random object to randomize with
	 * @return a randomized group with values from the selected ranges<br>
	 *         <b>NOTE: NO REPEATED VALUES</b>
	 */
	public static int[] getSeveralRandomInts(int[][] ranges, int amount, Random random) {
		List<Integer> group = new ArrayList<>();

		for (int[] ints : ranges) {
			for (int i = ints[0]; i <= ints[1]; i++) {
				group.add(i);
			}
		}

		int size = group.size();

		int[] numbers = new int[Math.min(amount, size)];

		for (int i = 0; i < amount && i < size; i++) {
			int num = getRandomInt(0, group.size() - 1, random);
			Integer value = group.get(num);
			numbers[i] = value;
			group.remove(value);
		}

		return numbers;
	}

	/**
	 * Get a randomized group of int's in the given range and size
	 * 
	 * @param min
	 *            the first group value
	 * @param max
	 *            the last group value
	 * @param amount
	 *            the size of the group to return
	 * @param random
	 *            the Random object to randomize with
	 * @return a randomize array of the given range and size
	 */
	public static int[] getSeveralRandomInts(int min, int max, int amount, Random random) {
		return getSeveralRandomInts(new int[][] { { min, max } }, amount, random);
	}

	/**
	 * Get a randomized group of int's in the given range
	 * 
	 * @param min
	 *            the first group value
	 * @param max
	 *            the last group value
	 * @param random
	 *            the Random object to randomize with
	 * @return a randomize array of the given range
	 */
	public static int[] getRandomizedIntGroup(int min, int max, Random random) {
		return getSeveralRandomInts(new int[][] { { min, max } }, max - min + 1, random);
	}

	/**
	 * Randomize a given group values.<br>
	 * the indexes are randomized and then the content is relocated<br>
	 * <b>Note: the return type matches the original type (through clone)</b>
	 * 
	 * @param originalGroup
	 *            the original group array
	 * @param random
	 *            a Random object to randomize with
	 * @return the randomized group
	 */
	public static Object[] randomizeGroup(Object[] originalGroup, Random random) {
		int[] indexes = getRandomizedIntGroup(0, originalGroup.length - 1, random);
		Object[] toReturn = originalGroup.clone(); // in order to save the type

		for (int i = 0; i < indexes.length; i++) {
			toReturn[i] = originalGroup[indexes[i]];
		}

		return toReturn;
	}

	public static <E> E getRandomItemFrom(Collection<E> collection) {
		return getRandomItemFrom(collection, collection.size());
	}

	public static <E> E getRandomItemFrom(Collection<E> collection, int limit) {
		return new ArrayList<>(collection).get(getRandomInt(0, limit, new Random()));
	}

}
