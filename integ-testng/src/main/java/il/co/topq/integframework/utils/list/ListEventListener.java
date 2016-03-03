package il.co.topq.integframework.utils.list;

import java.util.Collection;
/**
 * This interface is event driven list listener.
 * When used with {@link ChangeEventListDecorator#listeners},  
 * @author Aharon
 *
 * @param <E>
 */
interface ListEventListener<E> {

	abstract void onAddAll(Collection<? extends E> collection);

	abstract void onRemove(Object o);

	abstract void onRemoveAll(Collection<?> collection);

	abstract void onAdd(E e);

	abstract void onRetainAll(Collection<?> collection);
	
	abstract void onClear();
}
