package App;

import java.io.File;
import java.util.ArrayList;

import App.Allocation.*;

public class Memory {
    private ArrayList<ArrayList<String>> firstInstructions;
    private ArrayList<ArrayList<String>> bestInstructions;
    private ArrayList<ArrayList<String>> worstInstructions;
    private ArrayList<String> outputMessage;
    private int maxSize;


    public void allocate(String input) {
        AllocationStrategy first = new FirstFit();
        AllocationStrategy best = new BestFit();
        AllocationStrategy worst = new WorstFit();
        Allocate allocate = new Allocate();
        OutputBuilder output = new OutputBuilder();

    
        File inputFileName = new File("App/" + input + ".txt");       
        ReadWrite io = new ReadWrite();
       
        firstInstructions = io.reader(inputFileName);
        bestInstructions = io.reader(inputFileName);
        worstInstructions = io.reader(inputFileName);
        maxSize = Integer.parseInt(firstInstructions.get(0).get(0));

        MemoryKeeper memoryFirstFit = new MemoryKeeper(maxSize);
        MemoryKeeper memoryBestFit = new MemoryKeeper(maxSize);
        MemoryKeeper memoryWorstFit = new MemoryKeeper(maxSize);

        AllocationStrategy[] strategies = {first, best, worst};
        for (AllocationStrategy strategy : strategies) { // For each strategy, we allocate and then sum them up in output files according to assignment.
            if (strategy == first) {
                allocate.allocate(firstInstructions, strategy, memoryFirstFit, output, io);
                outputMessage = output.populatePrintList(first, firstInstructions, memoryFirstFit);
                outputMessage.add("");
            } else if (strategy == best) {
                allocate.allocate(bestInstructions, strategy, memoryBestFit, output, io);
                outputMessage.addAll(output.populatePrintList(best, bestInstructions, memoryBestFit));
                outputMessage.add("");
            } else {
                allocate.allocate(worstInstructions, strategy, memoryWorstFit, output, io);
                outputMessage.addAll(output.populatePrintList(worst, worstInstructions, memoryWorstFit));
            }
            
        }

        io.printAllPremature(input);
        io.write(outputMessage, input, false);

        System.out.println("Memory allocation from '" + inputFileName + "' complete.");
        
    }


}