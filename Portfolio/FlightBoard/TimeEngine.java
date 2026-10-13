package FlightBoard;

import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A class to handle and simulate real time down to a specified scale.
 * @author Sarah
 */

public class TimeEngine {
    private long startTime;
    private double secondsToMinute;
    private int secondsToHour;
    private int secondsToDay;

    /**
     * Start TimeEngine. Captures current nanotime, and sets time scale for conversions.
     * @param secondstoHour How many seconds to equal an hour of scaled time.
     */
    public TimeEngine(int secondstoHour) {
        if (secondstoHour <= 0) {
            throw new IllegalArgumentException("Seconds per hour must be positive.");
        }

        startTime = System.nanoTime();
        secondsToMinute = secondstoHour / 60.0;
        this.secondsToHour = secondstoHour;
        secondsToDay = secondstoHour * 24;
    }

    /**
     * @return Passed minutes since initialisation.
     */
    public int passedMinutes(){
        long duration = System.nanoTime() - startTime;
        double actualSeconds = duration / 1_000_000_000.0;

        return (int) (actualSeconds / secondsToMinute);
    }

    /**
     * Returns the actual time within a day from minutes passed since midnight.
     * @param minutesPassed Minutes passed since midnight.
     * @return Current time.
     */
    public String dayCycle(int minutesPassed){
        int withinDay = minutesPassed % 1440;

        LocalTime clockTime = LocalTime.MIDNIGHT.plusMinutes(withinDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return (clockTime.format(formatter));
    }

    /**
     * Similar to dayCycle except it uses the passed minutes from time relative to initialisation.
     * @return Current time.
     */
    public String currentDayCycle(){
        int withinDay = passedMinutes() % 1440;

        LocalTime clockTime = LocalTime.MIDNIGHT.plusMinutes(withinDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return (clockTime.format(formatter));
    }

    /**
     * @return Real life seconds to simulated minute.
     */
    public double getSecondsToMinute(){
        return secondsToMinute;
    }

}
