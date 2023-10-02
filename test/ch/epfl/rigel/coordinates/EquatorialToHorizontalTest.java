package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquatorialToHorizontalTest {

    ZonedDateTime testValue = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6), LocalTime.of(0,0), ZoneOffset.UTC);
    private EquatorialToHorizontalConversion testConverter = new EquatorialToHorizontalConversion(testValue, GeographicCoordinates.ofDeg(0, 52));

    @Test
    public void correctConversion() {
        HorizontalCoordinates converted = testConverter.apply(EquatorialCoordinates.of(3, Angle.ofDMS(23, 13, 10)));
        assertEquals(283.271027, converted.azDeg());
        assertEquals(19.334345, converted.altDeg());
    }

    @Test
    public void correctConversionWithNull() {
        HorizontalCoordinates converted = testConverter.apply(EquatorialCoordinates.of(0, 0));
        assertEquals(Angle.ofDeg(275.39), converted.az());
        assertEquals(Angle.ofDeg(4.13), converted.alt());
    }

}
