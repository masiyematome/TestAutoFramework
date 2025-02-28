package api.utilities;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import org.slf4j.*;
import java.io.*;
import java.util.*;

@Getter
public class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);
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
            LogHelper.logInfo(this,String.format("Properties loaded successfully from '%s' ", filePath));

        }catch (IOException e){
            LogHelper.logError(this,"Couldn't read properties file " + e);
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
            LOG.info("Data from '{}' loaded successfully", filePath);
        }catch (IOException | CsvValidationException e){
            LogHelper.logError(this,"Couldn't read csv file " + e);
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

    public String getBaseURI(){
        if(properties == null) throw new NullPointerException("Cannot load property 'baseURI' because properties=null");
        return properties.getProperty("baseURI");
    }

    public List<List<String>> getCsvData(){
        if(csvData == null) throw new NullPointerException("Couldn't retrieve csv test data - no data was loaded into the csvData list");
        return csvData;
    }

}
