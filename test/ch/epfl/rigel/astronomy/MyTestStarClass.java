package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTestStarClass {
    @Test
    public void testColorTemperature (){
        int d=10515;
        int b= 3800;
        assertEquals(d,new Star(24436, "Rigel", EquatorialCoordinates.of(0, 0), 0, -0.03f).colorTemperature());
        assertEquals (b, new Star(3, "other",EquatorialCoordinates.of(0,0 ),0, 1.50f).colorTemperature());
    }
}
