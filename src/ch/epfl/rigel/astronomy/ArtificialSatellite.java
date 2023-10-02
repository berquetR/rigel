package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Random;

/**
 * Classe représentant un satellte artificiel.
 */

public final class ArtificialSatellite {

    private final String country;
    private final EquatorialCoordinates coordinates;

    public ArtificialSatellite (String name, double lon) {
        Random random = new Random();
        this.country = name;
        coordinates = EquatorialCoordinates.of(lon, random.nextDouble());
    }

    public EquatorialCoordinates coordinates () {
        return coordinates;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString (){
        return country;
    }


}
