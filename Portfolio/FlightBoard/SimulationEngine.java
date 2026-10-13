package FlightBoard;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

/**
 * Simulation engine to handle randomisation, importation of flight parameters, and overall running of the flight board.
 * @author Sarah
 */
public class SimulationEngine{
    private Random random = new Random();
    private ArrayList<Integer> usedNums = new ArrayList<Integer>();
    private ArrayList<Flight> allFlights = new ArrayList<Flight>();
    private ArrayList<ScheduledFlight> activeFlights = new ArrayList<ScheduledFlight>();
    private Airport airport;
    private int nextFlightMinute = random.nextInt(5) + 1;
    
    public SimulationEngine(Airport airport){  
        this.airport = airport;
    }

    /**
     * Method for importing databases of flights.
     * @param fileName Filename of CSV file.
     * @param isInternational Whether or not the database is of international flights.
     */
    public boolean importDatabase(String fileName, boolean isInternational){
        
        int flightsBefore = allFlights.size();
        File file = new File(fileName);
        try(Scanner scanner = new Scanner(file)){
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();

                if(line.isBlank()){
                    continue;
                }

                String[] data = line.split(",", -1);
                
                if(data.length < 3 || data[0].isBlank() || data[2].isBlank()){
                    throw new IllegalArgumentException("Invalid database row: " + line);
                }

                String flightNum = "0000";
                boolean numChkr = false;

                if(usedNums.size() >= 9999){
                    throw new IllegalStateException("Maximum amount of flights stored reached, cannot import more flights.");
                }

                while(!numChkr){
                    Integer randomNum = random.nextInt(9999) + 1;
                    if(!usedNums.contains(randomNum)){
                        usedNums.add(randomNum);
                        flightNum = "%04d".formatted(randomNum);
                        numChkr = true;
                    }
                }

                allFlights.add(new Flight(data[0], flightNum, data[2], isInternational));
            }
        }catch(FileNotFoundException e){
            System.err.println("File not found: " + e.getMessage());
            return false;
        }

        return allFlights.size() > flightsBefore;
    }
    
    /**
     * Runs a live, non-stop version where flights are added and removed dynamically.
     * @param secondsPerHour How many real-life seconds to simulated hour.
     */
    public void runLive(int secondsPerHour){
        initialise(random.nextInt(5) + 1);

        TimeEngine clock = new TimeEngine(secondsPerHour);
        int lastMinute = 0;

        displayBoard(0);

        try{
            while(!Thread.currentThread().isInterrupted()){
                int currentMinute = clock.passedMinutes();

                if(currentMinute > lastMinute){
                    while(lastMinute < currentMinute){
                        lastMinute++;
                        update(lastMinute);
                    }

                    displayBoard(lastMinute);
                }

                Thread.sleep(50);
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Starts recursive counted simulation.
     * @param runs
     */
    public void runRecursive(int runs){
        if(runs < 0){
            throw new IllegalArgumentException("Runs cannot be negative.");
        }

        if(runs > 100){
            runs = 100;
        }

        initialise(random.nextInt(5) + 1);
        runRecursiveSteps(runs, 0);
    }

    /**
     * Recursive function that manually does runs of flights.
     * @param runs Runs.
     * @param currentMinute currentMinute usually 0.
     */
    private void runRecursiveSteps(int runs, int currentMinute){
        if(runs == 0){
            displayBoard(currentMinute);
            return;
        }

        int nextMinute = currentMinute + 1;
        update(nextMinute);

        runRecursiveSteps(runs - 1, nextMinute);
    }

    /**
     * Initalise flights.
        * @param startingFlights Number of flights that come with starting.
     */
    public void initialise(int startingFlights){
    for(int i = 0; i < startingFlights; i++){
            if(!scheduleFlight(0)){
                break;
            }
        }
    }

    /**
     * Updates time.
     * @param currentMinute
     */
    private void update(int currentMinute){
        processDepartures(currentMinute);
        processArrivals(currentMinute);

        if(currentMinute >= nextFlightMinute){
            scheduleFlight(currentMinute);
            nextFlightMinute = currentMinute + random.nextInt(5) + 1;
        }
    }

    /**
     * Displays board.
     * @param currentMinute
     */
    private void displayBoard(int currentMinute){
    int hours = (currentMinute / 60) % 24;
    int minutes = currentMinute % 60;

        String board = "Simulation time: %02d:%02d%n".formatted(hours, minutes) + airport.toString();

        System.out.print("\033[H\033[2J" + board);
        System.out.flush();
    }

    /**
     * @param currentMinute
     * @return returns true if all flights were added successfully.
     */
    private boolean scheduleFlight(int currentMinute){
        ArrayList<Flight> availableFlights = new ArrayList<Flight>();

        for(Flight temp: allFlights){
            if(temp.isInternational() && !airport.acceptsInternational()){
                continue;
            }

            boolean alreadyActive = false;

            for(ScheduledFlight scheduled: activeFlights){
                if(temp.equals(scheduled.flight)){
                    alreadyActive = true;
                    break;
                }
            }

            if(!alreadyActive){
                availableFlights.add(temp);
            }
        }

        if(availableFlights.isEmpty()){
            return false;
        }

        Flight temp = availableFlights.get(random.nextInt(availableFlights.size()));

        Flight flight = new Flight(temp.getAirline(), temp.getFlightNumber(), temp.getCity(), temp.isInternational());
        flight.setStatus("Scheduled");

        int arrivalMinute = currentMinute + random.nextInt(5) + 1;
        activeFlights.add(new ScheduledFlight(flight, arrivalMinute));
        airport.addFlight(flight, false, false);
        
        return true;
    }

    /**
     * Process chunk of flights as they arrive into airport.
     * @param currentMinute
     */
    private void processArrivals(int currentMinute){
        for(ScheduledFlight scheduled: activeFlights){
            if(scheduled.arrived || currentMinute < scheduled.nextEventMinute){
                continue;
            }

            if(airport.addFlight(scheduled.flight, false, true)){
                scheduled.flight.setStatus("Arrived");
                scheduled.arrived = true;
                scheduled.nextEventMinute = currentMinute + 75;
            }else{
                scheduled.flight.setStatus("Delayed");
                scheduled.nextEventMinute = currentMinute + 15;
            }
        }
    }

    /**
     * Takes arrived flights and turns them around.
     * @param currentMinute
     */
    private void processDepartures(int currentMinute){
        for(int i = activeFlights.size() - 1; i >= 0; i--){
            ScheduledFlight scheduled = activeFlights.get(i);

            if(!scheduled.arrived){
                continue;
            }

            airport.turnAround(scheduled.flight);

            int departureMinute = scheduled.nextEventMinute;

            if(currentMinute >= departureMinute){
                if(!scheduled.departed){
                    airport.removeFlight(scheduled.flight, true, false);
                    scheduled.flight.setStatus("Departed");
                    scheduled.departed = true;
                }

                if(currentMinute >= departureMinute + 10){
                    if(airport.removeFlight(scheduled.flight, true, true)){
                        activeFlights.remove(i);
                    }
                }
            }else if(currentMinute >= departureMinute - 15){
                scheduled.flight.setStatus("Final Call");
            }else if(currentMinute >= departureMinute - 45){
                scheduled.flight.setStatus("Boarding");
            }else{
                scheduled.flight.setStatus("At Gate");
            }
        }
    }


    /**
     * A simple class to add a time attribute to a flight.
     * @author Sarah
     */
    private static class ScheduledFlight{
        Flight flight;
        int nextEventMinute;
        boolean arrived = false;
        boolean departed = false;

        /**
         * @param flight The flight itself.
         * @param nextEventMinute The next event at which the flight should be addressed. (i.e. changing from normal to delayed).
         */
        ScheduledFlight(Flight flight, int nextEventMinute){
            this.flight = flight;
            this.nextEventMinute = nextEventMinute;
        }
    }
}
