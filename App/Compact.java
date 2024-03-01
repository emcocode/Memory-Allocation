package App;

import java.util.ArrayList;
import java.util.Arrays;

public class Compact {
    private ArrayList<ArrayList<Integer>> compactedList;
    
    public ArrayList<ArrayList<Integer>> compact(ArrayList<ArrayList<Integer>> oldList) {
        compactedList = new ArrayList<>();
        int counter = 0;
        int diff;
        for (ArrayList<Integer> block : oldList) {
            diff = 0;
            if (counter == 0) {
                if (block.get(1) > 0) {
                    ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(block.get(0), 0, (block.get(2) - block.get(1))));
                    compactedList.add(newBlock);
                } else {
                    compactedList.add(block);
                }
            } else {
                diff = (block.get(1) - compactedList.get(counter-1).get(2) - 1);
                if (diff > 0) {
                    ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(block.get(0), (block.get(1) - diff), (block.get(2) - diff)));
                    compactedList.add(newBlock);
                } else {
                    compactedList.add(block);
                }
            }
            counter += 1;
        }
        return compactedList;
    }
    
}
