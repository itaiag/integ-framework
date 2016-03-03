package il.co.topq.integframework.rest;

import static com.sun.jersey.api.client.Client.create;

import javax.ws.rs.core.MediaType;

import il.co.topq.integframework.AbstractModuleImpl;
import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;
import il.co.topq.integframework.reporting.Reporter.Style;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.ClientResponse;

public class RESTClientModule extends AbstractModuleImpl {
	String uri;

	public ClientResponse post(JSONObject input, String action) throws Exception {
		Reporter.log("Request", action + "\nBody:" + input.toString(3), Style.PLAINTEXT);
		StringBuilder builder = new StringBuilder(uri);
		if (!uri.endsWith("/")) {
			builder.append("/");
		}
		builder.append(action);
		ClientResponse clientResponse = create().resource(builder.toString()).type("application/json")
				.accept("application/json").post(ClientResponse.class, input);

		setActual(clientResponse.getEntity(String.class));
		try {
			JSONObject response = new JSONObject(getActual(String.class));
			Reporter.log("Response", response.toString(3), Style.PLAINTEXT);
		} catch (JSONException e) {
			Reporter.log("Response", getActual(String.class), Style.PLAINTEXT);// should
																				// never
																				// happen!!
		}

		return clientResponse;
	}
	
	public ClientResponse post(String postData,MediaType postDataMediaType, String action) throws Exception {
		Reporter.log("Request", action + "\nBody:" + postData, Style.PLAINTEXT);
		StringBuilder builder = new StringBuilder(uri);
		if (!uri.endsWith("/")) {
			builder.append("/");
		}
		builder.append(action);
		ClientResponse clientResponse = create().resource(builder.toString()).type(postDataMediaType)
				.accept("application/json").post(ClientResponse.class, postData);

		setActual(clientResponse.getEntity(String.class));
		try {
			JSONObject response = new JSONObject(getActual(String.class));
			Reporter.log("Response", response.toString(3), Style.PLAINTEXT);
		} catch (JSONException e) {
			Reporter.log("Response", getActual(String.class), Style.PLAINTEXT);
			// should never happen!!
		}

		return clientResponse;
	}
	

	public ClientResponse get(String action) throws Exception {
		Reporter.log("Request", action, Style.PLAINTEXT);
		StringBuilder builder = new StringBuilder(uri);
		if (!uri.endsWith("/")) {
			builder.append("/");
		}
		builder.append(action);
		ClientResponse clientResponse = create().resource(builder.toString()).accept("application/json")
				.get(ClientResponse.class);

		setActual(clientResponse.getEntity(String.class));
		try {
			JSONObject response = new JSONObject(getActual(String.class));
			Reporter.log("Response", response.toString(3), Style.PLAINTEXT);
		} catch (JSONException e) {
			Reporter.log("Response", getActual(String.class), Style.PLAINTEXT);// should
																				// never
																				// happen!!
		}

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
		Reporter.log(input.toString(), Style.PLAINTEXT);
		StringBuilder builder = new StringBuilder(uri);
		if (!uri.endsWith("/")) {
			builder.append("/");
		}
		builder.append(action);
		ClientResponse clientResponse = create().resource(builder.toString()).type("application/json").accept("application/json")
				.put(ClientResponse.class, input);

		setActual(clientResponse.getEntity(String.class));
		Reporter.log("Response", Color.BLUE);
		Reporter.log(getActual(String.class), Style.PLAINTEXT);
		return clientResponse;
	}

}
