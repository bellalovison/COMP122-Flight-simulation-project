package FlightBoard;

/**
 * A class to contain gate information.
 * @author Sarah
 * @author Bella
 */

public class Gate{
   private boolean isOccupied;
   private Flight flight;

   /**
    * Gate Constructor.
    */
   public Gate(){
      this.isOccupied = false;
      this.flight = null;
   }
   
   /**
    * @return true if the gate is occupied.
    */
   public boolean isOccupied(){
      return isOccupied;
   }

   /**
    * @param plane Flight to add.
    * @return true if the plane was assigned, false if the gate is occupied.
    */
   public boolean addFlight(Flight flight){
      if(isOccupied || flight == null){
         return false;
      }
        
      this.flight = flight;
      this.isOccupied = true;
      return true;
   }

   /**
   * Removes the flight from the gate.
   */
   public void removeFlight(){
      this.flight = null;
      this.isOccupied = false;
   }   

   /**
    * @param o Flight to comare.
    * @return true if the flight matches this gate's flight.
    */
   public boolean containsFlight(Flight o){
    return flight != null && flight.equals(o);
   }
}