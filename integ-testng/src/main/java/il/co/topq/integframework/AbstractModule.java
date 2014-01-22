package il.co.topq.integframework;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class AbstractModule {
	protected Object actual;
	protected boolean isClosed;
	protected String name;

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
	
	public abstract void init() throws Exception;
	public abstract void close() throws Exception;

}
