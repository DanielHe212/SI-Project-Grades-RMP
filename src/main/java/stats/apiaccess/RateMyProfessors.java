package stats.apiaccess;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import stats.Utils;

import java.util.ArrayList;

public class RateMyProfessors {


    //parameter: none
    //returns: arraylist of string arrays containing all professors from UBC at ratemyprofessors.com
    public static ArrayList<String[]> getAllProfessors(){
        ArrayList<String[]> profs = new ArrayList<>();
        int i = 1;
        while(getMoreProfs(profs, i))i++;

        return profs;
    }

    //paramater: arraylist of string arrays containing all professors so far, a page number
    //returns: true if there are still more professors remaining, false otherwise
    //the original arraylist of string arrays is modified to add more professors from the current page
    public static boolean getMoreProfs(ArrayList<String[]> profs, int page){
        JsonElement result = Utils.parseToJson(Utils.getFromRequest("https://www.ratemyprofessors.com/filter/professor/?&page=" + page + "&filter=teacherlastname_sort_s+asc&query=*%3A*&queryoption=TEACHER&queryBy=schoolId&sid=1413"));
        try{
            JsonObject jo = result.getAsJsonObject();
            JsonArray ps = jo.get("professors").getAsJsonArray();
            for(JsonElement p : ps){
                JsonObject po = p.getAsJsonObject();
                String[] pinfo = new String[4]; //first name, middle name, last name, pid
                pinfo[0] = po.get("tLname").getAsString();
                pinfo[1] = po.get("tMiddlename").getAsString();
                pinfo[2] = po.get("tFname").getAsString();
                pinfo[3] = po.get("tid").getAsString();
                profs.add(pinfo);
            }
            return (jo.get("remaining").getAsInt() > 0);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}