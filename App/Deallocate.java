package App;

import java.util.ArrayList;

public class Deallocate {
    
    public int deallocate(int id, MemoryKeeper mKeeper) {
        for (ArrayList<Integer> block : mKeeper.getMemory()) {
            if (block.get(0) == id) {
                mKeeper.removeFromMemory(block);
                return (block.get(2) - block.get(1) + 1);
            }            
        }
        return 0;
    }
}
