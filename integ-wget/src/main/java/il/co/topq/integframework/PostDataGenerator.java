package il.co.topq.integframework;

public interface PostDataGenerator {
	/**
	 * notify this {@link PostDataGenerator} that no more data will be sent
	 */
	public void finish();

	/**
	 * 
	 * @return true if no more data available and the generator is finished its
	 *         work
	 */
	public boolean isDone();

	/**
	 * send data later
	 * 
	 * @param data
	 */
	public void post(String data);

	/**
	 * client should get data from this method and send it. this method may
	 * block until new data exists
	 * 
	 * @return data or null if there is no more data
	 * @throws InterruptedException
	 *             when this thread is interrupted while waiting for data
	 */
	public String getPostData() throws InterruptedException;

	/**
	 * 
	 * @return the capacity that this generator can contain.
	 */
	public int getCapacity();

}
