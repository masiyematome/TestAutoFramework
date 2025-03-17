package commons;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.*;

public class FileUtil {

    public static String getProperty(String filePath, String property) {
        if(property.isBlank()){
            LogUtil.logError(FileUtil.class, "Couldn't fetch blank / null property '" + property + "'");
            throw new RuntimeException("Couldn't fetch blank / null property '" + property + "'.");
        }
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
            LogUtil.logInfo(FileUtil.class, String.format("Property '" + property + "' loaded successfully from '%s' ", filePath));

        } catch (IOException e) {
            LogUtil.logError(FileUtil.class, "Couldn't load property '" + property + "' from " + filePath + "." + e);
        }
        return properties.getProperty(property);
    }

    public static Properties getProperties(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
            LogUtil.logInfo(FileUtil.class, String.format("Properties loaded successfully from '%s' ", filePath));

        } catch (IOException e) {
            LogUtil.logError(FileUtil.class, "Couldn't read properties file " + e);
        }
        return properties;
    }

    public static List<List<String>> getTestData(String filePath) {
        List<List<String>> csvData = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                csvData.add(Arrays.asList(line));
            }
            LogUtil.logInfo(FileUtil.class, "Data from '" + filePath + "' loaded successfully");
        } catch (IOException | CsvValidationException e) {
            LogUtil.logError(FileUtil.class, "Couldn't read csv file " + e);
            throw new RuntimeException(e);
        }
        return csvData;
    }

    public static String getDataFromFile(String filePath) {
        StringBuilder builder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int data;
            while ((data = fis.read()) != -1) {
                builder.append((char) data);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing file '" + filePath + "'", e);
        }
        return builder.toString();
    }

    public static void createAFile(String source, String target) {
        try {
            FileOutputStream fos = new FileOutputStream(target);
            fos.write(source.getBytes());
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("Error creating file '" + target + "'", e);
        }
    }

    public static byte[] readFileAsBytes(String filePath) {
        File file = new File(filePath);
        byte[] dataAsBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            if (fis.read(dataAsBytes) != file.length()) {
                LogUtil.logError(FileUtil.class, "Failed to load all the data from '" + filePath + "' into byte array");
                throw new RuntimeException("Failed to load all the data from '" + filePath + "' into byte array");
            }
        } catch (IOException e) {
            LogUtil.logError(FileUtil.class, "Error while reading file '" + filePath + "' " + e);
            throw new RuntimeException("Error while reading file '" + filePath + "' ", e);
        }
        return dataAsBytes;
    }

}
