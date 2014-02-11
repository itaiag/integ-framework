package il.co.topq.integframework.rest;

import java.net.URISyntaxException;

import com.sun.jersey.api.client.ClientResponse;

public interface ApplicationContainer {

	public abstract ClientResponse reload() throws URISyntaxException;

}
