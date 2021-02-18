package utilities;

import java.io.*;
import java.util.Properties;

public class PropertiesFileCreator {

    public void writeProperties() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(DBConnection.DB_URL, "YOUR URL"); // todo type your database URL in the second String
        properties.setProperty(DBConnection.DB_USER, "YOUR USERNAME"); // todo type your db username in the second String
        properties.setProperty(DBConnection.DB_PASSWORD, "YOUR PASSWORD"); //todo type your db password in the second String

        try (OutputStream outputStream = new FileOutputStream("project.properties")) {
            properties.store(outputStream, "Database Properties File");
        }
    }
}
