package App;

import java.util.ArrayList;

import App.Allocation.*;

public class Allocate {
    private Deallocate d;
    private Compact c;

    public Allocate() {}
    
    public void allocate(ArrayList<ArrayList<String>> instructions, AllocationStrategy strategy, MemoryKeeper mKeeper, OutputBuilder output, ReadWrite io) {
        c = new Compact();
        d = new Deallocate();
        int maxSize = Integer.parseInt(instructions.get(0).get(0));
        instructions.remove(0);
        int deallocateValue = 0;
        int extraOutFiles = 0;
        for (ArrayList<String> cmd : instructions) {
            if (cmd.get(0).equals("A")) { // EX: GIVET: A:2:200 -> ID:START:END - 2:100:199
                if (Integer.parseInt(cmd.get(2)) <= maxSize) { // If there is room in the memory
                    if (mKeeper.getMemory().size() == 0) {   // Adds immidately if empty
                        ArrayList<Integer> block = new ArrayList<>();
                        block.add(Integer.parseInt(cmd.get(1)));
                        block.add(0);
                        block.add(Integer.parseInt(cmd.get(2)) - 1);
                        mKeeper.addToMemory(block);
                        mKeeper.addMemoryUsed(Integer.parseInt(cmd.get(2)));
                    } else {    // Adds when not empty
                        strategy.allocation(cmd, mKeeper);
                        mKeeper.addMemoryUsed(Integer.parseInt(cmd.get(2)));
                    }
                } else {    // If not enough room in memory
                    cmd.add(Integer.toString(mKeeper.getLargestFreeBlock()));
                    mKeeper.addToErrorList(cmd);
                }
            } else if (cmd.get(0).equals("D")) {
                deallocateValue = d.deallocate(Integer.parseInt(cmd.get(1)), mKeeper);
                if (deallocateValue != 0) { // Remove from memory
                    mKeeper.removeMemoryUsed(deallocateValue);
                } else {    // Add to errorlist
                    for (int i = 0; i < instructions.indexOf(cmd); i++) {    
                        if (instructions.get(i).get(0).equals("A") && (instructions.get(i).get(1).equals(cmd.get(1)))) {
                            if (cmd.size() < 3) {
                                cmd.add("1");
                                mKeeper.addToErrorList(cmd);
                                break;
                            }
                        }
                    }
                    if (cmd.size() == 2) {
                            cmd.add("0");
                            mKeeper.addToErrorList(cmd);
                    }
                }
            } else if (cmd.get(0).equals("C")) {
                mKeeper.setMemory(c.compact(mKeeper.getMemory()));
            } else if (cmd.get(0).equals("O")) {
                io.fileToPrint(output.populatePrintList(strategy, instructions, mKeeper), extraOutFiles);
                extraOutFiles += 1;
            } else {
                System.out.println("Unknown command! " + cmd);
            }
        }        
    }

    
}
