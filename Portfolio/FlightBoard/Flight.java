package FlightBoard;
import java.util.Objects;

/**
 * A class to contain flight information.
 * @author Sarah
 * @author Bella
 */
public class Flight{
   private String airline;
   private String flightNumber;
   private String city;
   private boolean isInternational;
   private String status;

   /**
    * Flight constructor.
    * @param airline E.g. Air New Zealand
    * @param flightNumber E.g. 1234
    * @param city The city the flight is departing to/arrving from.
    * @param isInternational Whether or not the flight arrived/departured internationally.
    */
   public Flight(String airline, String flightNumber, String city, boolean isInternational){
      this.airline = airline;
      this.flightNumber = flightNumber;
      this.city = city;
      this.isInternational = isInternational;
   }

   /**
    * @return Airline name.
    */
   public String getAirline(){ 
      return airline; 
   }

   /**
    * @return Airline code.
    */
   public String getFlightNumber(){ 
      return flightNumber; 
   }

   /**
    * @return City.
    */
   public String getCity(){
      return city;
   }
   
   /**
    * @return Whether or not the flight is internationally arriving/departing.
    */
   public boolean isInternational(){ 
      return isInternational; 
   }

   /**
    * Status setter.
    * @param status to set to the flight.
    */
   public void setStatus(String status){
      this.status = status;
   }

   /**
    * @return Flight status.
    */
   public String getStatus(){
      return status;
   }

   @Override 
   /**
    * Compares flights.
    * @param o Flight to compare.
    * @return true if they match, otherwise false.
    */
   public boolean equals(Object o){
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      Flight other = (Flight) o;

      if(airline.equals(other.airline) && flightNumber.equals(other.flightNumber) && city.equals(other.city)){
         return true;
      }
      
      return false;
   }

   @Override
   /**
    * Hashcode of flight.
    */
   public int hashCode(){
      String hashValue = airline + flightNumber + city;
      return Objects.hash(hashValue);
   }
}


