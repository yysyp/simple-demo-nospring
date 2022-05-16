package ps.demo.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MyPermutationGenerator {

    public static void main(String[] args) {
        List<String> items = Arrays.asList("A", "B", "C");
        permutationList(items, null, 2, new int[]{1});
    }

    public static void permutationList(List<String> items, LinkedList<String> onePerm, int length, int[] index) {
        LinkedList<String> cpLine = new LinkedList<>();
        if (onePerm != null) {
            cpLine.addAll(onePerm);
        }
        if (length <= 0) {
            System.out.println("[" + index[0] + "]: " + cpLine);
            index[0]++;
            return;
        }

        int nextSpots = length - 1;
        for (int i = 0, n = items.size(); i < n; i++) {
            cpLine.add(items.get(i));
            permutationList(items, cpLine, nextSpots, index);
            cpLine.removeLast();
        }

    }

}
