package FlightBoard;

import java.util.Scanner;

/**@author Sarah
*@author Bella
*/
public class FlightBoardApp{
    public static void main(String[] args){
        try(Scanner input = new Scanner(System.in)){
            System.out.print("Airport name: ");
            Airport airport = new Airport(input.nextLine().trim());

            // Add one or more zones.
            do{
                int gates = readInt(input, "Number of gates: ", 1);
                boolean international =
                        readYesNo(input, "International zone? (y/n): ");

                airport.addZone(gates, international);
            }while(readYesNo(input, "Add another zone? (y/n): "));

            SimulationEngine simulation = new SimulationEngine(airport);

            // Import one or more databases.
            boolean hasFlights = false;

            do{
                System.out.print("CSV file path: ");
                String fileName = input.nextLine().trim();

                boolean international =
                        readYesNo(input, "International database? (y/n): ");

                if(international && !airport.acceptsInternational()){
                    System.out.println("Add a domestic database: this airport has no international zone.");
                    continue;
                }

                if(simulation.importDatabase(fileName, international)){
                    hasFlights = true;
                }else{
                    System.out.println("No flights imported. Please try another file.");
                }

            }while(!hasFlights || readYesNo(input, "Import another database? (y/n): "));

            System.out.println("\n1. Live simulation");
            System.out.println("2. Recursive simulation");

            int mode;
            do{
                mode = readInt(input, "Choose 1 or 2: ", 1);
                if(mode > 2){
                    System.out.println("Please choose 1 or 2.");
                }
            }while(mode > 2);

            if(mode == 1){
                int secondsPerHour = readInt(
                        input,
                        "Real seconds per simulated hour (600 = 10 seconds per minute): ",
                        1);

                simulation.runLive(secondsPerHour);
            }else{
                int runs = readInt(
                        input, "Simulated minutes to run: ", 0);

                simulation.runRecursive(runs);
            }
        }
    }

    private static int readInt(Scanner input, String prompt, int minimum){
        while(true){
            System.out.print(prompt);

            try{
                int value = Integer.parseInt(input.nextLine().trim());

                if(value >= minimum){
                    return value;
                }

                System.out.println("Enter a number of at least " + minimum + ".");
            }catch(NumberFormatException e){
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static boolean readYesNo(Scanner input, String prompt){
        while(true){
            System.out.print(prompt);
            String answer = input.nextLine().trim();

            if(answer.equalsIgnoreCase("y")
                    || answer.equalsIgnoreCase("yes")){
                return true;
            }

            if(answer.equalsIgnoreCase("n")
                    || answer.equalsIgnoreCase("no")){
                return false;
            }

            System.out.println("Please enter y or n.");
        }
    }
}
