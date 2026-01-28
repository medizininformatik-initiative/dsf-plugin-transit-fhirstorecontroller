import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DeleteFromYaml {

    //Before running the test run "mvn clean"
    //otherwise a RuntimeException might be thrown if the store is already deleted
    @Test
    public void deleteTest(){
        String businessKey = "51ca9fd5-fe25-483a-87a9-eb6d8f214f8p";
        String projectIdentifier = "we-transit2";
        String storeId = businessKey + "-" + projectIdentifier;

        //Delete entry in values.yaml
        URL url = getClass().getResource("/TestYaml/values.yaml");
        File values  = new File(url.getPath());

        Map<String, Object> data = loadYaml(values);
        List<String> stores = (List<String>) data.get("stores");
        if (stores == null) {
            stores = new ArrayList<>();
        }

        List<String> storeResults = stores.stream().filter(e -> e.equals(storeId.replace("-",""))).toList();
        if (storeResults.size() != 1) {
            throw new RuntimeException("Could not find exactly one store in yaml. StoreCount: " + storeResults.size());
        }

        stores.remove(storeResults.get(0));
        writeYaml(data, values);
        data = null;

        //Delete entries in dmu-stores-db.yaml
        URL urlDb = getClass().getResource("/TestYaml/dmu-stores-db.yaml");
        File db = new File(urlDb.getPath());

        data = loadYaml(db);

        Map<String, Object> spec = (Map<String, Object>) data.get("spec");
        Map<String, String> databases = (Map<String, String>) spec.get("databases");

        String dbId = storeId.replace("-","");
        databases.remove(dbId);
        writeYaml(data, db);
    }

    private Map<String, Object> loadYaml(File file)
    {
        // Load YAML
        Yaml yaml = new Yaml();
        Map<String, Object> data;
        try (InputStream input = new FileInputStream(file)) {
            data = yaml.load(input);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return data;
    }

    private void writeYaml(Map<String, Object> data, File file){
        // Write back to file
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);

        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

}
