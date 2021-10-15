package stats;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.ArrayList;

public class Utils {

    //parameter: a url
    //returns: the content of a request to the url as an inputstream
    public static InputStream getFromRequest(String sURL){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(sURL);
        HttpResponse httpresponse;
        try {
            httpresponse = httpclient.execute(httpget);
            return httpresponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //paramater: a file name
    //returns: an arraylist of string arrays parsed from filename.csv within the current directory
    public static ArrayList<String[]> parseCSV(String filename){
        ArrayList<String[]> output = new ArrayList<>();
        File profs = new File(System.getProperty("user.dir") + File.separator + filename + ".csv");
        try {
            BufferedReader in = new BufferedReader(new FileReader(profs));
            String line = in.readLine();
            while (line != null) {
                String[] ln = line.split(",");
                output.add(ln);
                line = in.readLine();
            }
            in.close();
        }catch (Exception e){
            System.out.println("Error, could not find/read file!");
            e.printStackTrace();
        }
        return output;
    }

    //paramater: an input stream containing json
    //returns: the json of the inputstream as a jsonelement
    public static JsonElement parseToJson(InputStream content){
        JsonElement root = JsonParser.parseReader(new InputStreamReader(content));

        return root;
    }

    //parameter: a file name and an arraylist of string arrays
    //returns: nothing
    //writes the contents of the arraylist as a csv file to filename.csv within current directory
    public static void compileCSV(String filename, ArrayList<String[]> data){
        String csvpath = System.getProperty("user.dir") + File.separator + filename + ".csv";
        File csv = new File(csvpath);

        try {
            csv.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv, false)));
            for(String[] s : data){
                pw.println(makeCSVLine(s));
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String makeCSVLine(String[] line){
        String out = "";
        for(String s : line){
            out += "," + s;
        }
        out = out.substring(1);
        return out;
    }
}