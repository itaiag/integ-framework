package il.co.topq.integframework.rest;

import static com.sun.jersey.api.client.Client.create;
import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;
import il.co.topq.integframework.utils.StringUtils;

import java.net.URI;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.uri.UriBuilderImpl;

public class TomcatRESTApplicationContainer extends RESTClientModule implements ApplicationContainer {

	String tomcatURI = ""; // http://localhost:8080/manager/text/reload?path=/examples
	String application = "";
	String userInfo = "";

	@Override
	public void init() throws Exception {

		super.init();
		URI superClientURI = new URI(super.uri);
		if (StringUtils.isEmpty(tomcatURI)) {
			UriBuilderImpl uriBuilder = new UriBuilderImpl();
			uriBuilder.uri(superClientURI).path("/manager/text/");
			if (!StringUtils.isEmpty(userInfo)) {
				uriBuilder.userInfo(userInfo);
			}
			this.tomcatURI = uriBuilder.build().toString();
		}
		if (StringUtils.isEmpty(application)) {
			application = superClientURI.getPath();
		}
	}

	@Override
	public ClientResponse reload() {
		Reporter.log("Reloading " + application, Color.BLUE);

		StringBuilder builder = new StringBuilder(tomcatURI);
		if (!tomcatURI.endsWith("/")) {
			builder.append("/");
		}
		builder.append("reload?path=").append(application);
		ClientResponse clientResponse = create().resource(builder.toString()).get(ClientResponse.class);

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

}
