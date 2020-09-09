import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Properties;
import java.util.*;

public class PropertiesLoader {
    // =========================================================================================
    public static Properties load(String fileName) {
        Properties props = new Properties();
        try {
            String propertyFileContents = readContentFromFile(fileName);
            props.load(new StringReader(propertyFileContents.replace("\\", "\\\\")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return props;
    }
    // =========================================================================================
    public static Map<String,List<String>> loadToHashMap(String fileName) {
        Properties props = new Properties();
		Map<String,List<String>> hm = new HashMap<String,List<String>>();
        try {
			ArrayList<String> lineArray = readListFromFile(fileName);
			for(int i=0;i<lineArray.size();i++) {
				String line = lineArray.get(i);
				if(line.trim().equals("")) {
					continue;
				}
				if(line.trim().startsWith("#")) {
					continue;
				}
				String[] arr = line.split("=");
				String key = "";
				String val = "";
				if(arr.length == 2) {
					key = arr[0];
					val = arr[1];
				}
				if(key.trim().equals("")) {
					continue;
				}
				if(val.trim().equals("")) {
					continue;
				}
				List valueList = (List)hm.get(key);
				if(valueList == null) {
					valueList = new ArrayList<String>();
					hm.put(key, valueList);
				}
				valueList.add(val);
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hm;
    }

    // =========================================================================================

    // =========================================================================================
    private static String readContentFromFile(String fileName) throws Exception {
        String returnString = "";
        BufferedReader input = new BufferedReader(new FileReader(new File(fileName)));
        try {
            String line = null; // not declared within while loop
            while ((line = input.readLine()) != null) {
                returnString = returnString + line + "\r\n";
            }
        } finally {
            input.close();
        }
        return returnString;
    }
    // =========================================================================================
    public static ArrayList<String> readListFromFile(String fileName) throws Exception {
        ArrayList<String> returnArray = new ArrayList<String>();
        BufferedReader input = new BufferedReader(new FileReader(new File(fileName)));
        try {
            String line = null; // not declared within while loop
            while ((line = input.readLine()) != null) {
                returnArray.add(line);
            }
        } finally {
            input.close();
        }
        return returnArray;
    }
    // =========================================================================================

    // =========================================================================================
    public static void main(String args[]) {
        Properties props =PropertiesLoader.load("input.properties");
    }
}
