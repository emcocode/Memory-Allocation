package App;

public class App {
    
    public static void main(String[] args) {

        String input = "scenario1.in"; // Enter your input file name, make sure to exclude '.txt'
        Memory memory = new Memory();
        memory.allocate(input);
        
    }
}
