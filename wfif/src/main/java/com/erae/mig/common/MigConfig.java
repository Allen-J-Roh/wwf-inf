package com.erae.mig.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MigConfig {

	public String getPropValues(String key) throws IOException {
		String result = "";
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			String propFileName = "./application.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			result = prop.getProperty(key);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
		return result;
	}
}
