package App.Allocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import App.MemoryKeeper;

public class WorstFit implements AllocationStrategy {
    private ArrayList<ArrayList<String>> errorList;
    private ArrayList<Integer> slotList;
    
    public String getFit() {
        return "Worst fit";
    }

    public ArrayList<ArrayList<String>> getErrors() {
        return errorList;
    }
    
    public void allocation(ArrayList<String> cmd, MemoryKeeper mKeeper) { // Method for placing block in memory. We recieve command and memory
        int id = Integer.parseInt(cmd.get(1));
        int blockSize = Integer.parseInt(cmd.get(2));
        slotList = new ArrayList<>();

        for (ArrayList<Integer> block : mKeeper.getMemory()) { // Add following: ID:SIZE:BEFORE/AFTER (before=1, after=0)
            if (mKeeper.getMemory().get(0) == block) { // first
                if (block.get(1) > 0) {
                    slotList.add(block.get(1));
                }
            } else { // not first
                if ((block.get(1) - mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1).get(2) - 1) > 1) {
                    slotList.add((block.get(1) - mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1).get(2) - 1));
                }
            }
            if (block == mKeeper.getMemory().get(mKeeper.getMemory().size() - 1) && (((mKeeper.getMaxMemory() - 1) - block.get(2)) > 1)) { // after last
                slotList.add(((mKeeper.getMaxMemory() - 1) - block.get(2)));
            }
        }

        Collections.sort(slotList, Collections.reverseOrder());
        int biggestSlot = 0;

        if (slotList.size() > 0) {
            biggestSlot = slotList.get(0);
        }
        if (biggestSlot >= blockSize) {
            if (biggestSlot != 0) {
                for (ArrayList<Integer> block : mKeeper.getMemory()) {
                    if (mKeeper.getMemory().get(0) == block) { // first
                        if (block.get(1) == biggestSlot) {
                            ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, 0, (blockSize - 1))); // Skapar arraylist av cmd
                            mKeeper.addToMemorySpecific(0, newBlock);
                            break;
                        } else if (mKeeper.getMemory().size() == 1) {
                            if ((mKeeper.getMaxMemory() - block.get(2)) == biggestSlot) {
                                ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, (block.get(2) + 1), ((block.get(2) + 1) + blockSize))); // Skapar arraylist av cmd
                                mKeeper.addToMemorySpecific(1, newBlock);
                                break;
                            }
                        }
                    } else { // not first
                        if (((block.get(1) - 1) - mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1).get(2)) == biggestSlot) {
                            ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, (mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1).get(2) + 1), (mKeeper.getMemory().get(mKeeper.getMemory().indexOf(block) - 1).get(2) + blockSize))); // Skapar arraylist av cmd
                            mKeeper.addToMemorySpecific(mKeeper.getMemory().indexOf(block), newBlock);
                            break;
                        }
                    }
                    if ((block == mKeeper.getMemory().get(mKeeper.getMemory().size() - 1)) && (((mKeeper.getMaxMemory() - 1) - block.get(2)) == biggestSlot)) { // after last
                            ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, (block.get(2) + 1), ((block.get(2) + blockSize)))); // Skapar arraylist av cmd
                            mKeeper.addToMemory(newBlock);
                            break;                        
                    }
                }
            }
        } else {
            cmd.add(Integer.toString(mKeeper.getLargestFreeBlock()));
            mKeeper.addToErrorList(cmd);
        }
        
       

    }
}
