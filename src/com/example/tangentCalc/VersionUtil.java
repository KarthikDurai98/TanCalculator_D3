package com.example.tangentCalc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to retrieve the version of the application.
 */
public class VersionUtil {
    private static final String VERSION_FILE = "/resources/version.properties";
    private static String version = "";

    static {
        try (InputStream input = VersionUtil.class.getResourceAsStream(VERSION_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + VERSION_FILE);
            } else {
                Properties prop = new Properties();
                prop.load(input);
                version = prop.getProperty("version");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getVersion() {
        return version;
    }
}
