package il.co.topq.integframework.rest;

import static com.sun.jersey.api.client.Client.create;
import static il.co.topq.integframework.utils.StringUtils.isEmpty;
import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;
import il.co.topq.integframework.utils.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.uri.UriBuilderImpl;

public class TomcatRESTApplicationContainer extends RESTClientModule implements ApplicationContainer {

	String tomcatURI = ""; // http://localhost:8080/manager/text/reload?path=/examples
	String application = "";
	String userInfo = "";
	String user = "", password = "";

	@Override
	public void init() throws Exception {

		super.init();
		URI superClientURI = new URI(super.uri);
		if (StringUtils.isEmpty(tomcatURI)) {
			UriBuilderImpl uriBuilder = new UriBuilderImpl();
			uriBuilder.uri(superClientURI).replacePath("/manager/text/");
			if (!StringUtils.isEmpty(userInfo)) {
				uriBuilder.userInfo(userInfo);
				String[] userInfoSplit = userInfo.split(Pattern.quote(":"));
				user = userInfoSplit[0];
				password = userInfoSplit[1];
			}

			this.tomcatURI = uriBuilder.build().toString();
		}
		if (StringUtils.isEmpty(application)) {
			application = superClientURI.getPath();
		}
	}

	@Override
	public ClientResponse reload() throws URISyntaxException {
		Reporter.log("Reloading " + application, Color.BLUE);
		UriBuilderImpl uriBuilder = new UriBuilderImpl();
		try {
			uriBuilder.uri(new URI(tomcatURI)).path("reload");
		} catch (IllegalArgumentException e) {
			// happens when path is null. well i did put something!
		}

		if (application.endsWith("/")) {
			application = StringUtils.getPrefix(application, "/");
		}
		uriBuilder.queryParam("path", application);

		URI uri = uriBuilder.build();

		Client client = create();
		if (!isEmpty(user)) {
			client.addFilter(new HTTPBasicAuthFilter(user, password));
		}

		ClientResponse clientResponse = client.resource(uri).accept("text/plain").get(ClientResponse.class);

		setActual(clientResponse.getEntity(String.class));
		Reporter.log("Response", Color.BLUE);
		Reporter.log("<pre>\n" + getActual(String.class) + "</pre>\n");
		return clientResponse;
	}

	public String getTomcatURI() {
		return tomcatURI;
	}

	public void setTomcatURI(String tomcatURI) {
		this.tomcatURI = tomcatURI;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
