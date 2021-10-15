package main;

import stats.Merge;
import stats.apiaccess.RateMyProfessors;
import stats.apiaccess.UBCGrades;
import stats.webscraping.RatingScraper;
import stats.webscraping.TidMatching;
import stats.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args){
        ArrayList<String[]> sections = UBCGrades.getAllSectionsData();
        //data header: year, session, subject, course, section, educators, enrolled, average, stdev, high, low
        Utils.compileCSV("sections", sections);

        ArrayList<String[]> profs = RateMyProfessors.getAllProfessors();
        //data header: tLname, tMiddlename, tFname, tid
        Utils.compileCSV("profs", profs);

        HashMap<String, String> namemap = TidMatching.genNameMap(Utils.parseCSV("profs"));
        ArrayList<String[]> matched = TidMatching.genMatchedSet(Utils.parseCSV("sections"), namemap);
        //data header: year, session, subject, course, section, tid, enrolled, average, stdev, high, low
        Utils.compileCSV( "filteredandmatched", matched);

        ArrayList<String[]> merged = Merge.mergeProfs(Utils.parseCSV("filteredandmatched"));
        //data: tid, total enrolled, average, stdev, hhigh, hlow, arange
        Utils.compileCSV( "merged", merged);

        ArrayList<String[]> rated = RatingScraper.getAllRatings(Utils.parseCSV("merged"));
        //data: tid, total enrolled, average, stdev, hhigh, hlow, arange, rating
        Utils.compileCSV( "rated", rated);
    }

    public static void printStrAL(ArrayList<String> al){
        for(String s : al){
            System.out.println(s);
        }
    }

    public static void printStrArrAL(ArrayList<String[]> al){
        for(String[] c : al){
            printStrArr(c);
        }
    }

    public static void printStrArr(String[] arr){
        for(String s : arr){
            System.out.print(s + ",");
        }
        System.out.println();
    }
}