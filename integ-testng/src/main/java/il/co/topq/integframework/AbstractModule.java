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
	public Object getActual() {
		return actual;
	}

	@SuppressWarnings("unchecked")
	public <T> T getActual(Class<T> clazz) {
		if (clazz.isInstance(actual)) {
			return (T) getActual();
		}
		throw new ClassCastException(actual.toString() + " is not castable to "	+ clazz.getName());
	}

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.Module#setActual(java.lang.Object)
	 */
	@Override
	public void setActual(Object actual) {
		this.actual = actual;
	}

	@PostConstruct
	public void initializeObject() throws Exception{
		init();
	}
	
	@PreDestroy
	public void closeObject() throws Exception{
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
