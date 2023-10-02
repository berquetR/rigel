package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

/**
 * Classe représentant une ville caractérisée par son nom et ss coordonnées géographiques.
 */

public class City {

    private final String name;
    private final GeographicCoordinates coordinates;



    public City(String name, double lat, double lon) {
        this.name = name;
        this.coordinates = GeographicCoordinates.ofDeg(lon, lat);

    }

    public String getName () {
        return name;
    }
    @Override
    public String toString() {
        return name;
    }

    public double getLon () {
        return coordinates.lonDeg();
    }
    public double getLat () {
        return coordinates.latDeg();
    }
}