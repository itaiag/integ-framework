package il.co.topq.integframework;

import com.google.common.base.Supplier;

public interface PostDataGenerator {

	public void finish();

	public boolean isDone();

	public void post(String data);

	public String getPostData() throws InterruptedException;

	public int getCapacity();

}
