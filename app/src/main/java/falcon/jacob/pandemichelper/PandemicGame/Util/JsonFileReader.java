package falcon.jacob.pandemichelper.PandemicGame.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JsonFileReader {

    public JSONObject readFile(String filePath){
        JSONObject jsonObject = new JSONObject();
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (Exception error){
            error.printStackTrace();
        }
        return jsonObject;
    }
}
