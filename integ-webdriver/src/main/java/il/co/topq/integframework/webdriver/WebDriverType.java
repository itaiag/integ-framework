package il.co.topq.integframework.webdriver;

import java.util.EnumSet;

/**
 * 
 * @author Liel Ran ,Create Date - 22.12.11
 * 
 */
public enum WebDriverType {
	FIREFOX_DRIVER("firefox"), INTERNET_EXPLORER_DRIVER("iexplore"), HTML_UNIT_DRIVER("htmlunit"), CHROME_DRIVER(
			"chrome"), OPERA_DRIVER("opera"), IPHONE_DRIVER("iphone"), ANDROID_DRIVER("android"), SAFARI_DRIVER(
			"safari"), PHANTOMJS_DRIVER("phantomJs");

	private String webDriverType;

	WebDriverType(String type) {
		webDriverType = type;
	}

	public String getBorwserType() {
		return webDriverType;
	}

	public static WebDriverType getBrowserTypeFromString(String type) {
		if (type != null) {
			for (WebDriverType webDriverTypeItem : WebDriverType.values()) {
				if (webDriverTypeItem.webDriverType.toLowerCase().equals(type.toLowerCase())) {
					return webDriverTypeItem;
				}
			}
		}

		return null;
	}

	public static String getAllSupportedWebDriverTypesAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("The Supported WebDriverType:");
		sb.append("\n");
		int index = 1;
		for (WebDriverType type : EnumSet.allOf(WebDriverType.class)) {
			sb.append(index + ". " + type.name() + "\n");
			index++;
		}
		return sb.toString();
	}
}
