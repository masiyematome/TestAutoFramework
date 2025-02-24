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
        loadProperties(properties);
        loadTestData(csvFile);
    }

    private void loadProperties(String filePath){
        properties = new Properties();
        try(FileInputStream inputStream = new FileInputStream(filePath)){
            properties.load(inputStream);
            LOG.info("Properties loaded successfully from '{}'", filePath);
        }catch (IOException e){
            LOG.error("Couldn't read properties file ", e);
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
            LOG.info("Csv '{}' read successfully", filePath);
        }catch (IOException | CsvValidationException e){
            LOG.error("Couldn't read csv file", e);
        }
    }

    public static FileUtil getInstance(String properties, String csvFile){
        if(instance == null){
            instance = new FileUtil(properties, csvFile);
        }
        return instance;
    }

    public String getTest(){
        return properties.getProperty("test");
    }

}
