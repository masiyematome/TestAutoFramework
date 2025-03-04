package api.utilities;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import java.io.*;
import java.util.*;
import java.util.List;

@Getter
public class FileUtil {
    private static FileUtil instance;
    private Properties properties;
    private List<List<String>> csvData;

    private FileUtil(String properties, String csvFile){
        if(properties != null && !properties.isBlank())
            loadProperties(properties);
        if(csvFile != null && !csvFile.isBlank())
            loadTestData(csvFile);
    }

    private void loadProperties(String filePath){
        properties = new Properties();
        try(FileInputStream inputStream = new FileInputStream(filePath)){
            properties.load(inputStream);
            LogUtil.logInfo(this,String.format("Properties loaded successfully from '%s' ", filePath));

        }catch (IOException e){
            LogUtil.logError(this,"Couldn't read properties file " + e);
        }
    }

    private void loadTestData(String filePath){
        csvData = new ArrayList<>();
        try(CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                .withSkipLines(1).build()){
            String[] line;
            while((line = csvReader.readNext()) != null){
                csvData.add(Arrays.asList(line));
            }
            LogUtil.logInfo(this,"Data from '" + filePath + "' loaded successfully");
        }catch (IOException | CsvValidationException e){
            LogUtil.logError(this,"Couldn't read csv file " + e);
        }
    }

    public static void initialize(String propertiesFilePath, String csvFilePath){
        if(instance == null){
            if((propertiesFilePath == null || propertiesFilePath.isBlank()) && (csvFilePath == null || csvFilePath.isBlank())){
                throw new IllegalArgumentException("Properties and csv file paths cannot both be unset. Set at least one");
            }
            instance = new FileUtil(propertiesFilePath, csvFilePath);
        }
    }

    public static FileUtil getInstance(){
        if(instance == null){
            throw new IllegalStateException("FileUtil instance is uninitialized. Call initialize first.");
        }
        return instance;
    }

    public Properties getProperties(){
        if (properties == null) throw new RuntimeException("Cannot fetch properties. Properties file not initialized.");
        return properties;
    }

    public static String getDataFromFile(String filePath){
        StringBuilder builder = new StringBuilder();
        try(FileInputStream fis = new FileInputStream(filePath)){
            int data;
            while((data = fis.read()) !=-1){
                builder.append((char) data);
            }
        }catch (Exception e){
            throw new RuntimeException("Error processing file '" + filePath + "'", e);
        }
        return builder.toString();
    }

    public static void createAFile(String source, String target){
        try{
            FileOutputStream fos = new FileOutputStream(target);
            fos.write(source.getBytes());
            fos.close();
        }catch (Exception e){
            throw new RuntimeException("Error creating file '" + target + "'", e);
        }
    }

}
