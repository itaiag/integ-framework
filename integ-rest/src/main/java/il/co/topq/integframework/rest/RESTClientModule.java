package il.co.topq.integframework.rest;

import static com.sun.jersey.api.client.Client.create;
import il.co.topq.integframework.AbstractModuleImpl;
import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.ClientResponse;

public class RESTClientModule extends AbstractModuleImpl {
	String uri;

	public ClientResponse post(JSONObject input, String action) throws Exception {
		Reporter.log("Request", Color.BLUE);
		Reporter.log("<pre>\n" + input.toString() + "</pre>\n");
		StringBuilder builder = new StringBuilder(uri);
		if (!uri.endsWith("/")) {
			builder.append("/");
		}
		builder.append(action);
		ClientResponse clientResponse = create().resource(builder.toString()).type("application/json")
				.accept("application/json").post(ClientResponse.class, input);

		setActual(clientResponse.getEntity(String.class));
		Reporter.log("Response", Color.BLUE);
		Reporter.log("<pre>\n" + getActual(String.class) + "</pre>\n");
		return clientResponse;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ClientResponse put(JSONObject input, String action) {
		Reporter.log("Request", Color.BLUE);
		Reporter.log("<pre>\n" + input.toString() + "</pre>\n");
		StringBuilder builder = new StringBuilder(uri);
		if (!uri.endsWith("/")) {
			builder.append("/");
		}
		builder.append(action);
		ClientResponse clientResponse = create().resource(builder.toString()).type("application/json").accept("application/json")
				.put(ClientResponse.class, input);

		setActual(clientResponse.getEntity(String.class));
		Reporter.log("Response", Color.BLUE);
		Reporter.log("<pre>\n" + getActual(String.class) + "</pre>\n");
		return clientResponse;
	}

}
