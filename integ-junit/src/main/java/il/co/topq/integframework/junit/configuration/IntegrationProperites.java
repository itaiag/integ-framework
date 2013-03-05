package il.co.topq.integframework.junit.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class IntegrationProperites {

	private static final String PROPERTIES_FILE = "integration.properties";

	private Properties properties;

	private static IntegrationProperites instance;

	private IntegrationProperites() {
		loadProperties();
	}

	private void loadProperties() {
		properties = new Properties();
		if (new File(PROPERTIES_FILE).exists()) {
			loadPropertiesFromFile();

		} else {
			setDefaultProperties();
			writePropertiesToFile();
		}

	}

	private void writePropertiesToFile() {
		try {
			properties.store(new FileOutputStream(PROPERTIES_FILE), "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setDefaultProperties() {
		properties.setProperty(FrameworkOptions.REPORTER_CLASSES.getKey(), FrameworkOptions.REPORTER_CLASSES.getDefaultValue());
	}

	private void loadPropertiesFromFile() {
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getOptionValue(FrameworkOptions option) {
		if (null == properties) {
			return null;
		}
		return properties.getProperty(option.getKey());
	}

	public static synchronized IntegrationProperites getInstance() {
		if (null == instance) {
			instance = new IntegrationProperites();
		}
		return instance;
	}
}
