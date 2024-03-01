package App.Allocation;

import java.util.ArrayList;
import java.util.Arrays;

import App.MemoryKeeper;

public class FirstFit implements AllocationStrategy {
    private ArrayList<ArrayList<String>> errorList;
    
    public String getFit() {
        return "First fit";
    }

    public ArrayList<ArrayList<String>> getErrors() {
        return errorList;
    }

    
    public void allocation(ArrayList<String> cmd, MemoryKeeper mKeeper) { // Method for placing block in memory. We recieve command and memory
        int position = 0;
        int id = Integer.parseInt(cmd.get(1));
        int blockSize = Integer.parseInt(cmd.get(2));

        for (ArrayList<Integer> block : mKeeper.getMemory()) { // EX: GIVET: A:2:200 -> ID:START:END - 2:100:199
            try {
                if (position == 0 && (mKeeper.getMemory().size() == 1)) { // Om vi står först och memory bara har ett block
                    if (blockSize < block.get(1)) { // Får blocket plats innan första?
                        ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, 0, (blockSize - 1))); // Skapar arraylist av cmd
                        mKeeper.addToMemorySpecific(0, newBlock);
                        break;
                    }  else { // Får inte plats före första
                        if ((mKeeper.getMaxMemory() - block.get(2)) > Integer.parseInt(cmd.get(2))) {
                            ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, (block.get(2) + 1), (block.get(2) + blockSize))); // Skapar arraylist av cmd
                            mKeeper.addToMemory(newBlock);
                            break;
                        } else { // Doesnt fit
                            cmd.add(Integer.toString(mKeeper.getLargestFreeBlock()));
                            mKeeper.addToErrorList(cmd);
                            break;
                        }
                    }
                } else if (position == 0 && !(mKeeper.getMemory().size() == position + 1)) { // Om vi står först och memory har fler än 1 block
                    if (blockSize < (mKeeper.getMemory().get(1).get(1) - mKeeper.getMemory().get(0).get(2))) { // Får blocket plats mellan första och andra
                        ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, (block.get(2) + 1), (block.get(2) + blockSize))); // Skapar arraylist av cmd
                        mKeeper.addToMemorySpecific(1, newBlock);
                        break;
                    }  else { // Blocket får inte plats
                        position += 1;
                        continue;
                    }
                } else {
                    if (blockSize < ((mKeeper.getMemory().get(position).get(1) - mKeeper.getMemory().get(position - 1).get(2)))) {
                        ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, (mKeeper.getMemory().get(position - 1).get(2) + 1), (mKeeper.getMemory().get(position - 1).get(2) + blockSize))); // Skapar arraylist av cmd
                        mKeeper.addToMemorySpecific(position, newBlock);
                        break;
                    } else {
                        if (position == (mKeeper.getMemory().size() - 1)) {
                            if ((mKeeper.getMaxMemory() - mKeeper.getMemory().get(mKeeper.getMemory().size() - 1).get(2)) > blockSize) {
                                ArrayList<Integer> newBlock = new ArrayList<>(Arrays.asList(id, (block.get(2) + 1), (block.get(2) + blockSize))); // Skapar arraylist av cmd
                                mKeeper.addToMemory(newBlock);
                                break;
                            } else {
                                cmd.add(Integer.toString(mKeeper.getLargestFreeBlock()));
                                mKeeper.addToErrorList(cmd);
                                System.out.println("Unable to allocate " + cmd.get(2)+ " bytes for block id " + cmd.get(1) + ". There is no memory slot big enough, try compacting.");
                            }
                        } else {
                            position += 1;
                            continue;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException index) {
                mKeeper.addToErrorList(cmd);
                System.out.println("Something went wrong...");
            }
        }
    }
}
