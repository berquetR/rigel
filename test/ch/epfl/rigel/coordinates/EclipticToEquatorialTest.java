package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EclipticToEquatorialTest {

    ZonedDateTime testValue= ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6), LocalTime.of(0,0), ZoneOffset.UTC);
    private EclipticToEquatorialConversion testConverter = new EclipticToEquatorialConversion(testValue);

    @Test
    public void obliquityTest() {
        //assertEquals(23.43805531, Angle.toDeg(testConverter.getObliquity()));
    }

    @Test
    public void correctConversion() {
        EquatorialCoordinates converted = testConverter.apply(EclipticCoordinates.of(Angle.ofDeg(139.686111), Angle.ofDeg(4.875278)));
        assertEquals(9.581478, converted.raHr());
        assertEquals(19.535003, converted.decDeg());
    }


}
