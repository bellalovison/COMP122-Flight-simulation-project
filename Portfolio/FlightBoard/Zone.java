package FlightBoard;
import java.util.ArrayList;

/**
 * Class to capture airport terminal information.
 * @author Sarah
 * @author Bella
 */
public class Zone{
   private int numGates;
   private boolean isInternational;
   private ArrayList<Gate> gates = new ArrayList<Gate>(); 
   private int numGatesOccupied;

   /**
    * Zone constructor, creates the zone and fills it with empty gates
    */
   public Zone(int numGates, boolean isInternational){
      if(numGates < 0){
         throw new IllegalArgumentException("Number of gates cannot be negative.");
      }
      
      this.numGates = numGates;
      this.isInternational = isInternational;
      numGatesOccupied = 0;
      
      for(int i = 0; i < numGates; i++){
         gates.add(new Gate());
      }
   }

   /**
    * @param flight Flight to add.
    * @return true if the flight was added, false it doesn't meet the requirements or the zone is full.
    */
   public boolean addFlight(Flight flight){
      if(flight == null || flight.isInternational() != isInternational || numGatesOccupied == numGates){
         return false;
      }
      
      for(Gate g: gates){
         if(g.addFlight(flight)){
            numGatesOccupied++;
            return true;
         }
      }
      
      return false;
   }
   
   /**
    * @param flight Flight to remove.
    * @return true if the flight was removed, false if the flight isn't in the zone.
    */
   public boolean removeFlight(Flight flight){
      for(Gate g: gates){
         if(g.isOccupied()){
            if(g.containsFlight(flight)){
               g.removeFlight();
               numGatesOccupied--;
               return true;
            }
         }
      }

      return false;
   }

   /**
    * @param flight Flight to find.
    * @return Gate number (i.e. index of gate + 1), or if not found 0.
    */
   public int findFlight(Flight flight){
      int counter = 1;

      for(Gate g: gates){
         if(g.isOccupied()){
            if(g.containsFlight(flight)){
               return counter;
            }
         }
         counter++;
      }
      
      return 0;
   }
}
