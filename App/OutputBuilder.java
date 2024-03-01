package App;

import java.util.ArrayList;
import java.util.Collections;

import App.Allocation.AllocationStrategy;

public class OutputBuilder {
    private ArrayList<String> printList;
    private ArrayList<String> tempList;
    
    public ArrayList<String> populatePrintList(AllocationStrategy strategy, ArrayList<ArrayList<String>> instructions, MemoryKeeper mKeeper) {
        printList = new ArrayList<>();
        printList.add(strategy.getFit());
        double largestFreeMemory = mKeeper.getLargestFreeBlock();
        tempList = new ArrayList<>();

        printList.add("Allocated blocks");
        for (ArrayList<Integer> block : mKeeper.getMemory()) { // Add allocated blocks
            tempList.add(block.get(0) + ";" + block.get(1) + ";" + block.get(2));
        }
        Collections.sort(tempList);
        for (String block : tempList) {
            printList.add(block);
        }

        printList.add("Free blocks");
        for (ArrayList<Integer> block : mKeeper.getMemory()) { // Add free blocks
            if (block == mKeeper.getMemory().get(0)) { // First block
                if (block.get(1) != 0) { // If there is free block first
                    printList.add("0;" + (block.get(1) - 1));
                    mKeeper.addToFreeMemory(block);
                    mKeeper.addToTotalFreeMemory(block.get(1));
                    mKeeper.setLargestFreeBlock(block.get(1));
                } if (mKeeper.getMemory().size() == 1) {
                    if ((mKeeper.getMaxMemory() - block.get(2)) > 1) {
                        printList.add((block.get(2) + 1) + ";" + (mKeeper.getMaxMemory() - 1));
                    }
                }
            } else {
                if ((block.get(1) - mKeeper.getMemory().get((mKeeper.getMemory().indexOf(block) - 1)).get(2)) > 1) { // If there is a free block
                    mKeeper.addToFreeMemory(block);
                    printList.add(((mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1)).get(2) + 1) + ";" + (block.get(1) - 1));
                    mKeeper.addToTotalFreeMemory(((block.get(1) - 1) - ((mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1)).get(2) + 1) + 1));
                    if (((block.get(1) - 1) - ((mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1)).get(2) + 1) + 1) > largestFreeMemory) {
                        largestFreeMemory = ((block.get(1) - 1) - ((mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1)).get(2) + 1) + 1);
                    }
                }
                if (block == mKeeper.getMemory().get(mKeeper.getMemory().size() - 1)) { // If we are on the last one
                    if (block.get(2) != (mKeeper.getMaxMemory() - 1)) {
                        printList.add((block.get(2) + 1) + ";" + (mKeeper.getMaxMemory() - 1));
                        if ((mKeeper.getMaxMemory() - (block.get(2) + 1)) > largestFreeMemory) {
                            largestFreeMemory = (mKeeper.getMaxMemory() - (block.get(2) + 1));
                        }
                    }
                }
            }
        }

        double fragment = (Math.round(((1- largestFreeMemory / mKeeper.getTotalFreeMemory()))*1000000)/1000000.0);
        String fragString = Double.toString(fragment);
        if (fragString.length() < 8) { // Making sure we have 6 decimals.
            for (int i = fragString.length(); i < 8; i++) {
                fragString += "0";
            }
        }
        printList.add("Fragment\n" + fragString);


        printList.add("Errors");
        if (mKeeper.getErrorList().size() == 0) {
            printList.add("None");
        }

        for (ArrayList<String> command : instructions) {
            if (mKeeper.getErrorList().contains(command)) {
                if (command.get(0).equals("A")) {
                    printList.add("A;" + (instructions.indexOf(command) + 1) + ";" + command.get(3));
                } else {
                    printList.add("D;" + (instructions.indexOf(command) + 1) + ";" + command.get(2));
                }                
            }
        }

        return printList;
    }

}
