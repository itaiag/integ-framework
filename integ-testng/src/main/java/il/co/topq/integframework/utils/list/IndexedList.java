package il.co.topq.integframework.utils.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Supplier;

/**
 * 
 * This list allows access to indexed items, if they
 * 
 * implement {@link IndexedBy} and return unique value on
 * {@link IndexedBy#getIndex()}
 * 
 * @author Aharon
 * @param <ELEMENTS>
 *            the type of contained elements
 * @param <INDEX>
 *            the type of keys
 */
public class IndexedList<INDEX, ELEMENTS extends IndexedBy<INDEX>> extends ChangeEventListDecorator<ELEMENTS> implements ListEventListener<ELEMENTS> {
	private Map<INDEX, ELEMENTS> internalMap;

	public IndexedList(List<ELEMENTS> l) {
		super(l);

	}

	private Map<INDEX, ELEMENTS> _internalMap() {
		return internalMap == null ? internalMap = new HashMap<>() : internalMap;
	}

	/**
	 * Returns the item to which the specified index key is set, or null if
	 * there is no mapping for the index
	 * 
	 * @param index
	 *            the index to search for
	 * @return null if no item was added with the given index
	 */
	public ELEMENTS getItem(INDEX index) {
		return _internalMap().get(index);
	}

	/**
	 * Returns the item to which the specified index key is set<br>
	 * If there is no mapping for the index, invoking {@link Supplier#get()} to
	 * get one.
	 * 
	 * @param index
	 *            the index to search for
	 * @param supplier
	 *            a supplier to create items if needed.
	 * @return the item with the given index, or null if there is no mapping for
	 *         the index, <b>and the supplier didn't provide an item that is
	 *         indexed by</b>
	 */
	public ELEMENTS getItem(INDEX index, Supplier<? extends ELEMENTS> supplier) {
		if (!_internalMap().containsKey(index)) {
			this.add(supplier.get());
		}
		return getItem(index);
	}

	@Override
	public void onAddAll(Collection<? extends ELEMENTS> collection) {
		for (ELEMENTS e : collection) {
			_internalMap().put(e.getIndex(), e);
		}

	}

	@Override
	public void onRemove(Object o) {
		if (_internalMap().containsValue(o)) {
			@SuppressWarnings("unchecked")
			// the internal map contain only elements of type e
			ELEMENTS e = (ELEMENTS) o;
			_internalMap().remove(e.getIndex());
		}

	}

	@Override
	public void onRemoveAll(Collection<?> collection) {
		for (Object object : collection) {
			onRemove(object);
		}
	}

	@Override
	public void onAdd(ELEMENTS e) {
		ELEMENTS old = _internalMap().put(e.getIndex(), e);
		if (contains(old)) {
			remove(old);
			_internalMap().put(e.getIndex(), e);
		}

	}

	@Override
	public void onRetainAll(Collection<?> collection) {
		Map<INDEX, ELEMENTS> newMap = new HashMap<>();
		for (ELEMENTS e : _internalMap().values()) {
			if (collection.contains(e)) {
				newMap.put(e.getIndex(), e);
			}
		}
		internalMap = newMap;
	}

	public Collection<INDEX> keys() {
		return new ArrayList<>(internalMap.keySet());
	}

	@Override
	public void onClear() {
		_internalMap().clear();
	}

}
