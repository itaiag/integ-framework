package il.co.topq.integframework;

public class AbstractModuleImpl extends AbstractModule {

	@Override
	public void init() throws Exception {
	}
	@Override
	public void close() throws Exception {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
