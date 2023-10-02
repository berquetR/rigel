package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTestPlanetModel {
    @Test
    public void atTest() {
        //Mercure différentes unités
        double test1 = PlanetModel.MERCURY.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr();
        assertEquals(16.8200745658971, test1);

        double test2 = PlanetModel.MERCURY.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg();
        assertEquals(-24.500872462861, test2);

        //Jupiter magnitude
        double test3 = PlanetModel.JUPITER.at(-2231.0, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).magnitude();
         assertEquals (-1.9885659217834473 , test3);

        //Jupiter taille angulaire.
        double test4 = Angle.toDeg(PlanetModel.JUPITER.at(-2231.0, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).angularSize()) * 3600;
        assertEquals(35.11141185362771, test4);

        double test5 = PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr();
        assertEquals(11.18715493470968, test5);

        double test6= PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg();
        assertEquals (6.35663550668575, test6);
    }
}