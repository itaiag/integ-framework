package il.co.topq.integframework;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.*;

public class PostDataGeneratorImpl implements PostDataGenerator {

	private boolean finish = false;
	private final ArrayBlockingQueue<String> queue;
	private final int capacity;

	public PostDataGeneratorImpl(int clientCapacity) {
		queue = new ArrayBlockingQueue<>(capacity = clientCapacity);
	}

	@Override
	public void finish() {
		this.finish = true;
	}

	@Override
	public boolean isDone() {
		return this.finish && this.queue.isEmpty();
	}

	@Override
	public void post(String data) {
		queue.add(data);
	}

	@Override
	public int getCapacity() {
		return Math.max(capacity, queue.size());
	}

	@Override
	public String getPostData() throws InterruptedException {
		String res = null;
		while (!this.isDone() && res == null) {
			res = queue.poll(1, SECONDS);
		}
		return res;

	}

}
