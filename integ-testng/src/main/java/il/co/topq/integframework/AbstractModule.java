package il.co.topq.integframework;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class AbstractModule implements Module {
	protected Object actual;
	protected boolean isClosed;
	protected String name;

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.Module#getActual()
	 */
	@Override
	public final Object getActual() {
		return actual;
	}

	/**
	 * Casts the actual object to the class or interface represented by the
	 * given {@code Class} object.
	 * 
	 * @return the object after casting, or null if obj is null
	 * 
	 * @throws ClassCastException
	 *             if the object is not null and is not assignable to the type
	 *             T.
	 */
	public final <T> T getActual(Class<T> clazz) {
		return clazz.cast(getActual());

	}

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.Module#setActual(java.lang.Object)
	 */
	@Override
	public final void setActual(Object actual) {
		this.actual = actual;
	}

	@PostConstruct
	public final void initializeObject() throws Exception {
		init();
	}
	
	@PreDestroy
	public final void closeObject() throws Exception {
		close();
	}
	
	/* (non-Javadoc)
	 * @see il.co.topq.integframework.Module#init()
	 */
	@Override
	public abstract void init() throws Exception;
	/* (non-Javadoc)
	 * @see il.co.topq.integframework.Module#close()
	 */
	@Override
	public abstract void close() throws Exception;

}
