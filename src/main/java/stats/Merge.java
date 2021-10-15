package stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Merge implements Comparator<String[]> {

    //parameters: an arraylist produced by TidMatching.genMatchedSet()
    //returns: an arraylist of string arrays, each string array being a line in a .csv file
    //each line of the .csv contains: tid, students taught, average, stdev, highest high, highest low, mean range
    public static ArrayList<String[]> mergeProfs(ArrayList<String[]> matched) {
        ArrayList<String[]> output = new ArrayList<>();
        ArrayList<String[]> sorted = (ArrayList<String[]>) matched.clone();
        Collections.sort(sorted, new Merge());

        while (sorted.size() > 0) {
            //data from first session, will become cumulative data
            int tid = Integer.parseInt(sorted.get(0)[5]); //tid
            int taught = Integer.parseInt(sorted.get(0)[6]); //students taught
            double average = Double.parseDouble(sorted.get(0)[7]); //average
            double stdev = Double.parseDouble(sorted.get(0)[8]); //stdev
            int hhigh = Integer.parseInt(sorted.get(0)[9]); //highest high
            int hlow = Integer.parseInt(sorted.get(0)[10]); //highest low
            double arange = Integer.parseInt(sorted.get(0)[9]) - Integer.parseInt(sorted.get(0)[10]); // average range

            sorted.remove(0);

            int i = 1; //number of classes thus far, used to calculate average range

            while (sorted.size() > 0) {
                if (!sorted.get(0)[5].equals(tid + "")) break; //goes to next professor

                //data from new session
                int ntaught = Integer.parseInt(sorted.get(0)[6]); //students taught
                double naverage = Double.parseDouble(sorted.get(0)[7]); //average
                double nstdev = Double.parseDouble(sorted.get(0)[8]); //stdev
                int nhigh = Integer.parseInt(sorted.get(0)[9]); //highest high
                int nlow = Integer.parseInt(sorted.get(0)[10]); //highest low
                int nrange = Integer.parseInt(sorted.get(0)[9]) - Integer.parseInt(sorted.get(0)[10]); // average range

                //calculating new cumulative
                stdev = Math.pow(mergeVariance(average, naverage, taught, ntaught, stdev * stdev, nstdev * nstdev), 0.5);
                average = (average * taught + naverage * ntaught) / (taught + ntaught);
                taught = taught + ntaught;
                hhigh = Math.max(hhigh, nhigh);
                hlow = Math.max(hlow, nlow);
                arange = (arange * i + nrange) / ((double) (i + 1));

                sorted.remove(0);
                i++;
            }

            String[] nextOut = {tid + "", taught + "", average + "", stdev + "", hhigh + "", hlow + "", arange + ""};
            output.add(nextOut);
        }
        return output;
    }

    //parameters: mean, sample size, variance of two sets
    //returns: the variance of the union of the two sets
    /* I derived the algorithm as follows: assume that the variance of the union of the two sets can be calculated
     * from the mean, sample size, and variance of the sets. This means all sets with the same mean, size, and variance
     * are statistically interchangeable for the purpose of calculating the variance of the union. Thus we do not need
     * to know the specific elements of the sets in question, or we could even replace them with another set that has
     * equal mean, size, and variance for the purpose of the calculation.
     * Since variance is the sum of the squares of the difference between each value of a set and the set mean, we can
     * create an equivalent set to any even-sized set with mean M, size S, and variance V out of the union of two lists
     * of size S/2, the first list containing only the values M-V^(1/2) and the second containing M+V^(1/2). The union
     * of these two lists would, of course, have the same M S and V as the original.
     * This is convenient for combining two sets 1 and 2 with mean, size, and variance m1 m2 s1 s2 v1 v2, because we can
     * approximate both sets as such. Let this new combined set be set 3, with mean m3, size s3, and variance v3.
     * s3 = s1+s2
     * m3 = (m1*s1+m2*s2)/s3
     * and v3 can be calculated by the above method of dividing a set into two lists:
     * ((s1/2)(m3-(m1-v1^(1/2)))^2+(s1/2)(m3-(m1+v1^(1/2)))^2+(s2/2)(m3-(m2-v2^(1/2)))^2+(s2/2)(m3-(m2+v2^(1/2))))^2/s3
     */
    public static double mergeVariance(double m1, double m2, int s1, int s2, double v1, double v2) {
        int s3 = s1 + s2;
        double m3 = (m1 * s1 + m2 * s2) / s3;
        double v3 = (((double) s1 / 2.0) * Math.pow(m3 - (m1 - Math.pow(v1, 0.5)), 2)
                + ((double) s1 / 2.0) * Math.pow(m3 - (m1 + Math.pow(v1, 0.5)), 2)
                + ((double) s2 / 2.0) * Math.pow(m3 - (m2 - Math.pow(v2, 0.5)), 2)
                + ((double) s2 / 2.0) * Math.pow(m3 - (m2 + Math.pow(v2, 0.5)), 2)) / ((double) s3);
        return v3;
    }

    @Override
    public int compare(String[] o1, String[] o2) {
        return Integer.parseInt(o1[5]) - Integer.parseInt(o2[5]);
    }
}