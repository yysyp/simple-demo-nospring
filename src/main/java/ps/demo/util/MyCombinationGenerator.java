package ps.demo.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yunpeng.song
 */
public class MyCombinationGenerator {

    public static void main(String[] args) {
        List<String> items = Arrays.asList("A", "B", "C");
        combinationList(items, null, 2, new int[]{1});
    }

    public static void combinationList(List<String> items, LinkedList<String> oneComb, int length, int[] index) {
        LinkedList<String> cpLine = new LinkedList<>();
        if (oneComb != null) {
            cpLine.addAll(oneComb);
        }
        if (length <= 0) {
            System.out.println("[" + index[0] + "]: " + cpLine);
            index[0]++;
            return;
        }

        int nextSpots = length - 1;
        for (int i = 0, n = items.size(); i < n; i++) {
            cpLine.add(items.get(i));
            combinationList(items, cpLine, nextSpots, index);
            cpLine.removeLast(); // means current one combination already print out, so remove.
        }

    }
}
