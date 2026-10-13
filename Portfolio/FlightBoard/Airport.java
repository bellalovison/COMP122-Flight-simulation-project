package FlightBoard;
import java.util.ArrayList;

/**
 * Class to simulate an airport's flights board. Requires manual input of adding and removing flights.
 * @author Sarah
 * @author Bella
 */
public class Airport{
    private String airportName;
    private ArrayList<Zone> zones = new ArrayList<Zone>();
    
    private ArrayList<Flight> flightsA = new ArrayList<Flight>();
    private ArrayList<Flight> flightsD = new ArrayList<Flight>();
    private ArrayList<Flight> flightsAIntl = new ArrayList<Flight>();
    private ArrayList<Flight> flightsDIntl = new ArrayList<Flight>();
    private boolean acceptsInternational;
    private boolean acceptsDomestic;

    public Airport(String airportName){
        this.airportName = airportName;
    }
    
    /**
     * Adding a zone into the airport.
     * @param numGates Number of gates for the zone.
     * @param isInternational Whether the zone links to customs/can accept international flights.
     */
    public void addZone(int numGates, boolean isInternational){
        if(isInternational){
            acceptsInternational = true;
        }else{
            acceptsDomestic = true;
        }

        zones.add(new Zone(numGates, isInternational));
    }

    /**
     * Adds a flight into the airport.
     * @param flight The flight itself.
     * @param isDeparture Whether its a departing or arrving flight.
     * @param assignGate If the flight added doesn't need to be assigned a gate yet.
     * @return true if the flight as been added.
     */
    public boolean addFlight(Flight flight, boolean isDeparture, boolean assignGate){
        if(flight == null){
            return false;
        }

        if(flight.isInternational() && !acceptsInternational){
            System.err.println("Cannot accept " + flight.getAirline() + " " + flight.getFlightNumber() + ": Airport cannot accept international flights.");
            return false;
        }

        if(!flight.isInternational() && !acceptsDomestic){
            System.err.println("Cannot accept " + flight.getAirline() + " " + flight.getFlightNumber() + ": Airport cannot accept domestic flights.");
            return false;
        }


        ArrayList<Flight> flightList;

        if(isDeparture){
            if(flight.isInternational()){
                flightList = flightsDIntl;
            }else{
                flightList = flightsD;
            }
        }else{
            if(flight.isInternational()){
                flightList = flightsAIntl;
            }else{
                flightList = flightsA;
            }
        }

        if(!flightList.contains(flight)){
            flightList.add(flight);
        }

        if(!assignGate){
            return true;
        }

        if(!findGate(flight).equals("--")){
            return true;
        }

        for(Zone z: zones){
            if(z.addFlight(flight)){
                return true;
            }
        }

        return false;
    }
    
    /**
     * Removes a flight from the airport.
     * @param flight The flight itself.
     * @param isDeparture What board to look into for removing the flight.
     * @return true if the flight has been removed.
     */
    public boolean removeFlight(Flight flight, boolean isDeparture, boolean removeFromBoard){
        if(flight == null){
            return false;
        }

        boolean removed = false;

        if(removeFromBoard){
            ArrayList<Flight> flightList;

            if(isDeparture){
                if(flight.isInternational()){
                    flightList = flightsDIntl;
                }else{
                    flightList = flightsD;
                }
            }else{
                if(flight.isInternational()){
                    flightList = flightsAIntl;
                }else{
                    flightList = flightsA;
                }
            }

            removed = flightList.remove(flight);
        }

        boolean gateReleased = false;

        for(Zone z: zones){
            if(z.removeFlight(flight)){
                gateReleased = true;
                break;
            }
        }

        if(removeFromBoard){
            return removed;
        }

        return gateReleased;
    }

    /**
     * Moves a flight from arrival to departure, turning it around.
     * @param flight The flight itself.
     */
    public void turnAround(Flight flight){
        if(flight == null){
            return;
        }

        if(flight.isInternational()){
            if(flightsAIntl.remove(flight)){
                flightsDIntl.add(flight);
            }
        }else{
            if(flightsA.remove(flight)){
                flightsD.add(flight);
            }
        }
    }

    /**
     * Finds the flight's gate.
     * @param flight The flight itself.
     * @return Gate.
     */
    public String findGate(Flight flight){
        int counter = 1;
        for(Zone z: zones){
            if(z.findFlight(flight) != 0){
                return t(counter) + z.findFlight(flight);
            }
            counter++;
        }

        return "--";
    }

    /**
     * @return true if the airport accepts international flights.
     */
    public boolean acceptsInternational(){
        return acceptsInternational;
    }

    /**
     * @return Airport name.
     */
    public String getName(){
        return airportName;
    }

    /**
     * The current airport board.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n\n---------------- Domestic Departures ----------------");
        s.append("\n");
        s.append("%-27s %-8s %-15s %-35s %-10s".formatted("Airline", "Flight", "City", "Gate", "Status"));
        s.append("\n");

        for(Flight f: flightsD){
            s.append("\n");
            s.append("%-27s %-8s %-15s %-35s %-10s".formatted(f.getAirline(), f.getFlightNumber(), f.getCity(), findGate(f), f.getStatus()));
        }

        s.append("\n---------------- International Departures ----------------");

        for(Flight f: flightsDIntl){
            s.append("\n");
            s.append("%-27s %-8s %-15s %-35s %-10s".formatted(f.getAirline(), f.getFlightNumber(), f.getCity(), findGate(f), f.getStatus()));
        }

        s.append("\n\n---------------- Arrival  Board -----------------");
        s.append("\n");
        s.append("%-27s %-8s %-15s %-35s %-10s".formatted("Airline", "Flight", "City", "Gate", "Status"));
        s.append("\n");
        
        for(Flight f: flightsA){
            s.append("\n");
            s.append("%-27s %-8s %-15s %-35s %-10s".formatted(f.getAirline(), f.getFlightNumber(), f.getCity(), findGate(f), f.getStatus()));
        }
        for(Flight f: flightsAIntl){
            s.append("\n");
            s.append("%-27s %-8s %-15s %-35s %-10s".formatted(f.getAirline(), f.getFlightNumber(), f.getCity(), findGate(f), f.getStatus()));
        }

        return s.toString();
    }

    /**
     * Utility method. Returns alphabetical version of number (i.e. 1 --> A).
     * @param number Number to translate.
     * @return Letter translated.
     */
    public String t(int number) {
        StringBuilder s = new StringBuilder();
        while (number > 0) {
            int ltrNum = (number - 1) % 26;
            s.insert(0, (char) ('A' + ltrNum));
            number = (number - 1) / 26;
        }
        return s.toString();
    }
}