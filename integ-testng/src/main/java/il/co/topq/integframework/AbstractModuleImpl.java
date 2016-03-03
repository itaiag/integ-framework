package il.co.topq.integframework;

public class AbstractModuleImpl extends AbstractModule implements Namefull {

	@Override
	public void init() throws Exception {
	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
