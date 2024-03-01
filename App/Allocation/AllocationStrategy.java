package App.Allocation;

import java.util.ArrayList;

import App.MemoryKeeper;

public interface AllocationStrategy {
    
    public String getFit();

    public ArrayList<ArrayList<String>> getErrors();

    public void allocation(ArrayList<String> cmd, MemoryKeeper mKeeper);
    
}
