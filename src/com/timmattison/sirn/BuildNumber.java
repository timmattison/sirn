package com.timmattison.sirn;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BuildNumber {
	private static final String BUILD_NUMBER_PROPERTIES_FILE = "/com/timmattison/sirn/BuildNumber.properties";
	private static String versionInformation = null;

	public static void printVersionInformation() {
		System.out.println("applicationVersion = " + getVersionInformation());
	}

	public static String getVersionInformation() {
		if (versionInformation == null) {
			try {
				InputStream inputStream = Main.class.getResourceAsStream(BUILD_NUMBER_PROPERTIES_FILE);

				if (inputStream == null) {
					// Can't find it, forget it
					throw new IOException("Couldn't find BuildNumber.properties");
				}

				Properties properties = new Properties();
				properties.load(inputStream);
				//String buildDate = properties.getProperty("buildDate");
				String buildNumber = properties.getProperty("buildNumber");
				String versionHash = properties.getProperty("versionHash");

				// Do nothing with the build date for now but use the first six characters of the version hash
				//   so we can be certain that our versions are identical even if the build number is wrong
				versionInformation = buildNumber + "-" + versionHash.substring(0, 6);
			} catch (IOException e) {
				// Couldn't read it, let them know
				versionInformation = "VERSION NOT SET";
			}
		}

		return versionInformation;
	}
}
