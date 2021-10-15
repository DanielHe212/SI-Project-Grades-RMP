package stats.apiaccess;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import stats.Utils;

public class UBCGrades {
    private static String baseURL = "https://ubcgrades.com/api/v2/";

    //parameter: none
    //returns: arraylist of strings containing all year sessions at UBC available in api
    public static ArrayList<String> getYears(){
        String years = baseURL + "yearsessions/UBCV";

        JsonElement result = Utils.parseToJson(Utils.getFromRequest(years));
        ArrayList<String> output = new ArrayList<>();

        try {
            JsonArray yrs = result.getAsJsonArray();
            for (JsonElement s : yrs) {
                output.add(s.getAsString());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return output;
    }

    //parameter: a year as a string
    //returns: arraylist of strings containing all subjects in that year
    public static ArrayList<String> getSubjects(String year){
        String subjects = baseURL + "subjects/UBCV/" + year;

        JsonElement result = Utils.parseToJson(Utils.getFromRequest(subjects));
        ArrayList<String> output = new ArrayList<>();

        try {
            JsonArray subs = result.getAsJsonArray();
            for (JsonElement s : subs) {
                JsonObject subject = s.getAsJsonObject();
                output.add(subject.get("subject").getAsString());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return output;
    }

    //parameter: a year and subject as a string
    //returns: arraylist of strings containing all courses in that year and subject
    public static ArrayList<String> getCourses(String yearsubject){
        String courses = baseURL + "courses/UBCV/" + yearsubject;

        JsonElement result = Utils.parseToJson(Utils.getFromRequest(courses));
        ArrayList<String> output = new ArrayList<>();

        try {
            JsonArray cs = result.getAsJsonArray();
            for (JsonElement s : cs) {
                JsonObject course = s.getAsJsonObject();
                output.add(course.get("course").getAsString());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return output;
    }

    //parameter: a year, subject, and course as a string
    //returns: arraylist of strings containing all sections in that year, subject, and course
    public static ArrayList<String> getSections(String yearsubjectcourse){
        String sections = baseURL + "sections/UBCV/" + yearsubjectcourse;

        JsonElement result = Utils.parseToJson(Utils.getFromRequest(sections));
        ArrayList<String> output = new ArrayList<>();

        try {
            JsonArray sects = result.getAsJsonArray();
            for (JsonElement s : sects) {
                output.add(s.getAsString());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return output;
    }

    //parameter: a year, subject, course, and section as a string
    //returns: string array of useful values grabbed from the api about this section
    public static String[] getSectionData(String section){
        String sections = baseURL + "grades/UBCV/" + section;

        JsonElement result = Utils.parseToJson(Utils.getFromRequest(sections));

        String[] key = {"year", "session", "subject", "course", "section", "educators", "enrolled", "average", "stdev", "high", "low"};
        String[] data = new String[key.length];

        try {
            JsonObject sec = result.getAsJsonObject();
            for (int i = 0; i < key.length; i++) {
                data[i] = sec.get(key[i]).getAsString();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return data;
    }

    //parameter: none
    //returns: arraylist of string arrays containing data from every course section at ubc available in the api
    public static ArrayList<String[]> getAllSectionsData(){
        ArrayList<String> allYears = getYears();

        ArrayList<String> allSubjects = new ArrayList<>();
        for(String s : allYears){
            ArrayList<String> subjects = getSubjects(s);
            for(String subject : subjects) {
                allSubjects.add(s + "/" + subject);
            }
        }

        ArrayList<String> allCourses = new ArrayList<>();
        for(String s : allSubjects) {
            ArrayList<String> courses = getCourses(s);
            for (String course : courses){
                allCourses.add(s + "/" + course);
            }
        }

        ArrayList<String> allSections = new ArrayList<>();
        for(String s : allCourses) {
            ArrayList<String> sections = getSections(s);
            for (String section : sections){
                allSections.add(s + "/" + section);
            }
        }

        ArrayList<String[]> allSectionsData = new ArrayList<>();
        for(String s : allSections){
            allSectionsData.add(getSectionData(s));
        }

        return allSectionsData;
    }
}