package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.SunModel.SUN;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTestSunModelClass {

    @Test
public void TestAt (){
    double c1=SunModel.SUN.at(27 + 31, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2010,  Month.FEBRUARY, 27),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().ra();

    double c2= SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY,
            27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr();

    double c3=SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY,
            27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg();

   //assertEquals ( 5.9325494700300885,c1);
    assertEquals(8.392682808297808 ,c2);
    assertEquals(19.35288373097352, c3);


}
}
