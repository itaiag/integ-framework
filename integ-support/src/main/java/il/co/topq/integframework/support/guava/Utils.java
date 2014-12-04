package il.co.topq.integframework.support.guava;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Suppliers.compose;
import static com.google.common.base.Verify.verifyNotNull;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class Utils {
	@SuppressWarnings("unused")
	private final int _;

	private Utils() {
		_ = 0;
	}

	public static <K, V> Supplier<Map<K, V>> newHashMap() {
		return new Supplier<Map<K, V>>() {
			@Override
			public Map<K, V> get() {
				return Maps.<K, V> newHashMap();
			}
		};
	}

	public static <K, V> Supplier<HashBiMap<K, V>> newBiHashMap() {
		return new Supplier<HashBiMap<K, V>>() {
			@Override
			public HashBiMap<K, V> get() {
				return HashBiMap.<K, V> create();
			}
		};
	}

	public static <E> Supplier<List<E>> newArrayList() {
		return new Supplier<List<E>>() {
			@Override
			public List<E> get() {
				return Lists.newArrayList();
			}
		};
	}

	public static <T> Supplier<T> forConstructor(final Constructor<T> constructor) {
		return new Supplier<T>() {
			@Override
			public T get() {
				try {
					return constructor.newInstance();
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	/**
	 * Get an item from a map, for the given key.<br>
	 * If the item does not exist, putting the result of {@link Supplier#get()}
	 * to the map for this key.
	 * 
	 * @param map
	 *            the map to get the item from
	 * @param key
	 *            the key to search in the map using {@link Map#get}
	 * @param supplier
	 *            a {@link Supplier} that provides a default value if the item
	 *            does not exist
	 * @return the value in the map.
	 */
	public static <K, V> V getOrAdd(Map<K, V> map, K key, Supplier<? extends V> supplier) {
		return fromNullable(verifyNotNull(map).get(key)).or(fromNullable(compose(addToMap(map, key), supplier)).orNull());
	}

	/**
	 * 
	 * @param map
	 * @param key
	 * @return a function that puts an item to the map to the given key
	 */
	public static <K, V> Function<V, V> addToMap(final Map<K, V> map, final K key) {
		return new Function<V, V>() {
			@Override
			public V apply(V input) {
				map.put(key, input);
				return map.get(key);
			}
		};
	};

	public static <T> Function<T, String> getSimpleClassName() {
		return new Function<T, String>() {
			@Override
			public String apply(T t) {
				return t.getClass().getSimpleName();
			}
		};
	}
}
