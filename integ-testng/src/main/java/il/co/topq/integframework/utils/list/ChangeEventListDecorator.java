package il.co.topq.integframework.utils.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ChangeEventListDecorator<E> implements List<E> {
	final private List<E> l;
	final protected List<ListEventListener<E>> listeners = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public ChangeEventListDecorator(List<E> l) {
		this.l = l;
		if (this instanceof ListEventListener<?>) {
			listeners.add((ListEventListener<E>) this);
		}
		onAddAll_(l);
	}

	@Override
	public int size() {
		return l.size();
	}

	@Override
	public boolean isEmpty() {
		return l.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return l.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		final Iterator<E> iterator = l.iterator();
		return new Iterator<E>() {
			E lastItem;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public E next() {
				return lastItem = iterator.next();
			}

			@Override
			public void remove() {
				onRemove_(lastItem);
				iterator.remove();
			}
		};

	}

	@Override
	public Object[] toArray() {
		return l.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return l.toArray(a);
	}

	@Override
	public boolean add(E e) {
		onAdd_(e);
		return l.add(e);
	}

	@Override
	public boolean remove(Object o) {
		onRemove_(o);
		return l.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return l.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		onAddAll_(c);
		return l.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		onAddAll_(c);
		return l.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		onRemoveAll_(c);
		return l.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		onRetainAll_(c);
		return l.retainAll(c);
	}

	@Override
	public void clear() {
		l.clear();
		onClear_();
	}

	@Override
	public boolean equals(Object o) {
		return l.equals(o);
	}

	@Override
	public int hashCode() {
		return l.hashCode();
	}

	@Override
	public E get(int index) {
		return l.get(index);
	}

	@Override
	public E set(int index, E element) {
		return l.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		l.add(index, element);
	}

	@Override
	public E remove(int index) {
		return l.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return l.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return l.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return l.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		final ListIterator<E> listIterator = l.listIterator(index);
		return new ListIterator<E>() {
			E lastItem;

			@Override
			public boolean hasNext() {
				return listIterator.hasNext();
			}

			@Override
			public E next() {
				return lastItem = listIterator.next();
			}

			@Override
			public boolean hasPrevious() {
				return listIterator.hasPrevious();
			}

			@Override
			public E previous() {
				return lastItem = listIterator.previous();
			}

			@Override
			public int nextIndex() {
				return listIterator.nextIndex();
			}

			@Override
			public int previousIndex() {
				return listIterator.previousIndex();
			}

			@Override
			public void remove() {
				onRemove_(lastItem);
				listIterator.remove();

			}

			@Override
			public void set(E e) {
				onRemove_(lastItem);
				listIterator.set(e);
			}

			@Override
			public void add(E e) {
				onAdd_(e);
				listIterator.add(e);
			}
		};
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		ChangeEventListDecorator<E> list = new ChangeEventListDecorator<>(l.subList(fromIndex, toIndex));
		list.listeners.addAll(listeners);
		return list;
	}

	private final void onAdd_(E e) {
		for (ListEventListener<E> listener : listeners) {
			listener.onAdd(e);
		}
	}

	private final void onRemove_(Object o) {
		for (ListEventListener<E> listener : listeners) {
			listener.onRemove(o);
		}
	}

	private final void onAddAll_(Collection<? extends E> collection) {
		for (ListEventListener<E> listener : listeners) {
			listener.onAddAll(collection);
		}
	}

	private final void onRemoveAll_(Collection<?> collection) {
		for (ListEventListener<E> listener : listeners) {
			listener.onRemoveAll(collection);
		}
	}

	private final void onRetainAll_(Collection<?> collection) {
		for (ListEventListener<E> listener : listeners) {
			listener.onRetainAll(collection);
		}
	}

	private final void onClear_() {
		for (ListEventListener<E> listener : listeners) {
			listener.onClear();
		}
	}

	@Override
	public String toString() {
		return l.toString();
	}
}
