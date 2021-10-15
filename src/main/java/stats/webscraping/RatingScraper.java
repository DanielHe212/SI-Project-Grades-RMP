package stats.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class RatingScraper {

    //parameter: data from Merge.mergeProfs()
    //returns: same data but with an extra value at the end
    //this value is the would take again rating as an integer percentage value
    public static ArrayList<String[]> getAllRatings(ArrayList<String[]> filtereddata){
        ArrayList<String[]> arr = new ArrayList<>();
        for(String[] s : filtereddata){
            int rating = getRating(s[0]);
            if(rating != -1){
                String[] s1 = new String[s.length + 1];
                for(int i = 0; i < s.length; i++){
                    s1[i] = s[i];
                }
                s1[s1.length - 1] = "" + rating;
                arr.add(s1);
            }
        }
        return arr;
    }

    // parameter: tid, the id of a professor on ratemyprofessors.com
    // returns: the would take again rating as an integer percentage, or -1 if no rating exists
    public static int getRating(String tid){
        try {
            Document doc = Jsoup.connect("https://www.ratemyprofessors.com/ShowRatings.jsp?tid=" + tid).get();
            // FeedbackItem__FeedbackNumber-uof32n-1
            // is the class of the <div> that contains ratings, including would take again and difficulty.
            // this line extracts all the divs with this class in the document body
            Elements es = doc.body().getElementsByClass("FeedbackItem__FeedbackNumber-uof32n-1");
            for(Element e : es){
                String s = e.text();
                if(s.equals("N/A") || s.contains(".")){ // either means no would take again rating or difficulty
                    continue;
                }
                if(s.endsWith("%")){ // this is the would take again rating
                    return Integer.parseInt(s.substring(0,s.length()-1));
                }
            }
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}