package App;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ReadWrite {
    private ArrayList<ArrayList<String>> instructions;
    private ArrayList<ArrayList<String>> prematurePrints;
    private int outFileCounter;

    public ArrayList<ArrayList<String>> reader(File inputFile) {
        outFileCounter = 1;
        instructions = new ArrayList<>();
        prematurePrints = new ArrayList<>();
        try {
            Scanner scan = new Scanner(inputFile);
            while (scan.hasNextLine()) {
                ArrayList<String> row = new ArrayList<>(Arrays.asList(scan.nextLine().split(";")));
                instructions.add(row);
            }
            scan.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return instructions;
    }

    public File createOutputFile(String fileName, boolean premature) {
        String outName = fileName.replace(".in", "");
        if (premature) {
            try {
                File newOut = new File("App/" + outName + ".out" + Integer.toString(outFileCounter) + ".txt");
                outFileCounter += 1;
                return newOut;
            } catch (Exception e) {
                System.out.println("FAIL VID FILE CREATION" + e);
            }
        } else {
            try {
                File newOut = new File("App\\" + outName + ".out.txt");
                return newOut;
            } catch (Exception e) {
                System.out.println("FAIL VID FILE CREATION" + e);
            }
        }
        return null;
        
    }

    public void write(ArrayList<String> printList, String fileName, boolean premature) {
        File outfile = createOutputFile(fileName, premature);
        try {
            FileWriter writer = new FileWriter(outfile);
            for (String row : printList) {
                writer.write(row + "\n");                
            }
            writer.close();

        } catch (Exception e) {
            System.out.println("fail" + e);
        }
    }

    public void fileToPrint(ArrayList<String> newInFile, int fileNumberSlot) {
        try {
            for (String row : newInFile) {
                prematurePrints.get(fileNumberSlot).add(row);
            }
            prematurePrints.get(fileNumberSlot).add("");
        } catch (Exception e) {
            prematurePrints.add(newInFile);
            prematurePrints.get(fileNumberSlot).add("");
        }
        
    }

    public void printAllPremature(String fileName) {
        for (ArrayList<String> prePrint : prematurePrints) {
            write(prePrint, fileName, true);
        }
    }

}
