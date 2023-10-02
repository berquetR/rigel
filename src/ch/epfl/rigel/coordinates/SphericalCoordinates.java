package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Classe servant de classe mère à toutes les classes représentant des coordonnées sphériques.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

abstract class SphericalCoordinates {

    /**
     * Attributs représentant respectivement la longitude et la latitude en radian.
     */

    private final double longitude;
    private final double latitude;


    /**
     * Construit des coordonnées sphériques.
     *
     * @param longitude
     * @param latitude
     */
    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return la longitude.
     */

    double lon() {
        return longitude;
    }

    /**
     * @return la longitude en degré.
     */
    double lonDeg() {
        return Angle.toDeg(longitude);
    }

    /**
     * @return la lattitude.
     */
    double lat() {
        return latitude;
    }

    /**
     * @return la latitude en degré.
     */
    double latDeg() {
        return Angle.toDeg(latitude);
    }

    @Override
    public final boolean equals(Object otherPolynomial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

}