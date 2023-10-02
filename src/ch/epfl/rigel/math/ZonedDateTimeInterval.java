package ch.epfl.rigel.math;

import java.time.ZonedDateTime;

public interface ZonedDateTimeInterval {

     static  boolean containsZonedDateTime (ZonedDateTime zonedDateTime, int hourOfRisingSun, int hourOfFallingSun) {

        int hour = zonedDateTime.getHour();
        ClosedInterval closedInterval = ClosedInterval.of(hourOfRisingSun, hourOfFallingSun);
        return closedInterval.contains(hour);
    }

    static boolean drawUfo (ZonedDateTime zonedDateTimeOfStartingApparition, double duration, ZonedDateTime currentTime ){
         ZonedDateTime disparition = zonedDateTimeOfStartingApparition.plusSeconds((long) duration);

         if (duration <= 60) {
             System.out.println(disparition.getSecond());
             System.out.println(zonedDateTimeOfStartingApparition.getSecond());

             ClosedInterval secondInterval = ClosedInterval.of(zonedDateTimeOfStartingApparition.getSecond(), disparition.getSecond());
             return (secondInterval.contains(currentTime.getSecond()) && zonedDateTimeOfStartingApparition.getYear() == currentTime.getYear() && zonedDateTimeOfStartingApparition.getMonthValue() == currentTime.getMonthValue() && zonedDateTimeOfStartingApparition.getDayOfMonth() == currentTime.getDayOfMonth() && currentTime.getHour() == zonedDateTimeOfStartingApparition.getHour() && currentTime.getMinute() == zonedDateTimeOfStartingApparition.getMinute());
         }
         if (duration > 60 && duration <= 3600) {
             ClosedInterval minuteInterval = ClosedInterval.of(zonedDateTimeOfStartingApparition.getMinute(), disparition.getMinute());
             return (minuteInterval.contains (currentTime.getMinute()) && zonedDateTimeOfStartingApparition.getYear() == currentTime.getYear() && zonedDateTimeOfStartingApparition.getMonthValue() == currentTime.getMonthValue() && zonedDateTimeOfStartingApparition.getDayOfMonth() == currentTime.getDayOfMonth() && currentTime.getHour()==zonedDateTimeOfStartingApparition.getHour());
         }
         if (duration > 3600 && duration <= 86400) {
             ClosedInterval hourInterval = ClosedInterval.of(zonedDateTimeOfStartingApparition.getHour(), disparition.getHour());
             return (hourInterval.contains(currentTime.getHour()) && zonedDateTimeOfStartingApparition.getYear() == currentTime.getYear() && zonedDateTimeOfStartingApparition.getMonthValue() == currentTime.getMonthValue() && zonedDateTimeOfStartingApparition.getDayOfMonth() == currentTime.getDayOfMonth());
         }
         if (duration > 86400 && duration <= 2.628e+6) {
             ClosedInterval dayInterval = ClosedInterval.of(zonedDateTimeOfStartingApparition.getDayOfMonth(), disparition.getDayOfMonth());
             return  (dayInterval.contains(currentTime.getDayOfMonth()) &&  zonedDateTimeOfStartingApparition.getYear() == currentTime.getYear() && zonedDateTimeOfStartingApparition.getMonthValue() == currentTime.getMonthValue());

         }
         else return false;
    }
}
