package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public final class ParametersProvider {

    private ArrayList<Properties> propertiesList = new ArrayList<>();

    private static ArrayList<String> getConfigFileNames() {
        ArrayList<String> configFileNames = new ArrayList<>();
        for (String key: System.getProperties().stringPropertyNames()) {
            if (key.startsWith("config.location")) {
                String[] fileNames = System.getProperties().getProperty(key)
                        .split(";");
                for (String fileName: fileNames) {
                    configFileNames.add(fileName);
                }
            }
        }
        return configFileNames;
    }

    private ParametersProvider() throws IOException {
        ArrayList<String> configFileNames = getConfigFileNames();
        for (String fileName: configFileNames) {
            Properties properties = new Properties();
            properties.loadFromXML(new FileInputStream(fileName));
            propertiesList.add(properties);
        }
    }

    private static ParametersProvider instance;

    private static ParametersProvider getInstance() throws IOException {
        if (instance == null) {
            instance = new ParametersProvider();
        }
        return instance;
    }

    public static String getProperty(final String key) throws IOException {
        for (Properties properties: getInstance().propertiesList) {
            String result = properties.getProperty(key, null);
            if (result != null) {
                return result;
            }
        }
        return "";
    }
}
