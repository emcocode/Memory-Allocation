package App;

import java.util.ArrayList;

public class MemoryKeeper {
    private ArrayList<ArrayList<Integer>> memory;
    private ArrayList<ArrayList<String>> errorList;
    private ArrayList<ArrayList<Integer>> freeMemory;
    int maxMemory;
    int memoryUsed;
    int largestFreeMemory;
    int totalFreeMemory;

    public MemoryKeeper(int totalMemory) {
        maxMemory = totalMemory;
        memoryUsed = 1; // 1 is set because we also use index value 0 as a byte.
        memory = new ArrayList<>();
        freeMemory = new ArrayList<>();
        errorList = new ArrayList<>();
        totalFreeMemory = totalMemory;
    }

    public void setMemory(ArrayList<ArrayList<Integer>> newMemory) {
        memory = newMemory;
    }

    public void addToMemory(ArrayList<Integer> newBlock) {
        memory.add(newBlock);
    }

    public void addToMemorySpecific(int slot, ArrayList<Integer> newBlock) {
        memory.add(slot, newBlock);
    }

    public void removeFromMemory(ArrayList<Integer> removedBlock) {
        memory.remove(removedBlock);
    }

    public void addToFreeMemory(ArrayList<Integer> newFreeBlock) {
        freeMemory.add(newFreeBlock);
    }

    public ArrayList<ArrayList<Integer>> getMemory() {
        return memory;
    }

    public void addMemoryUsed(int addedMemory) {
        memoryUsed += addedMemory;
    }

    public void removeMemoryUsed(int removedMemory) {
        memoryUsed -= removedMemory;
    }

    public void addToErrorList(ArrayList<String> newError) {
        errorList.add(newError);
    } 

    public ArrayList<ArrayList<String>> getErrorList() {
        return errorList;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void addToTotalFreeMemory(int freeMemory) {
        totalFreeMemory += freeMemory;
    }

    public int getTotalFreeMemory() {
        totalFreeMemory = 0;
        int pos = 0;
        if (memory.size() == 0) {
            return maxMemory;
        } else if (memory.size() == 1) {
            if ((memory.get(0).get(1)) > 0) { // Dubbelkolla denna siffran
                totalFreeMemory += (memory.get(0).get(1) + 1);
            } if ((maxMemory - (memory.get(0).get(2))) > 0) {
                totalFreeMemory += (maxMemory - (memory.get(0).get(2)) - 1);
            }
            return totalFreeMemory;
        } else {
            for (ArrayList<Integer> block : memory) {
                if (pos == 0) { // first
                    if (block.get(1) > 0) {
                        totalFreeMemory += block.get(1);
                    }
                } else if (pos != (memory.size() - 1)) { // not first, not last
                    if (((block.get(1) - 1) - memory.get(pos - 1).get(2)) > 0) {
                        totalFreeMemory += ((block.get(1) - 1) - memory.get(pos - 1).get(2));
                    }
                } else { // last
                    if (((block.get(1) - 1) - memory.get(pos - 1).get(2)) > 0) {
                        totalFreeMemory += ((block.get(1) - 1) - memory.get(pos - 1).get(2));
                    }
                    if (maxMemory - block.get(2) > 0) {
                        totalFreeMemory += maxMemory - block.get(2) - 1;
                    }
                }
                pos += 1;
            }
        return totalFreeMemory;
        }
    }

    public void setLargestFreeBlock(int largestFreeBlock) {
        largestFreeMemory = largestFreeBlock;
    }

    public int getLargestFreeBlock() {
        int biggestFree = 0;
        int pos = 0;
        if (memory.size() == 0) {
            return maxMemory;
        } else if (memory.size() == 1) {
            if ((memory.get(0).get(1) + 1) > (maxMemory - memory.get(0).get(2))) { // Dubbelkolla denna siffran
                return (memory.get(0).get(1) + 1);
            } else {
                return (maxMemory - memory.get(0).get(2) - 1);
            }
        } else {
            for (ArrayList<Integer> block : memory) {
                if (pos == 0) { // first
                    if (block.get(1) > 0) {
                        biggestFree = block.get(1);
                    }
                } else if (pos != (memory.size() - 1)) { // not first, not last
                    if (((block.get(1) - 1) - memory.get(pos - 1).get(2)) > biggestFree) {
                        biggestFree = ((block.get(1) - 1) - memory.get(pos - 1).get(2));
                    }
                } else { // last
                    if (((block.get(1) - 1) - memory.get(pos - 1).get(2)) > biggestFree) {
                        biggestFree = ((block.get(1) - 1) - memory.get(pos - 1).get(2));
                    }
                    if (maxMemory - block.get(2) > biggestFree) {
                        biggestFree = maxMemory - block.get(2) - 1;
                    }
                }
                pos += 1;
            }
            return biggestFree;
        }
    }
}
