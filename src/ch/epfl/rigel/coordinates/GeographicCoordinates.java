package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Classe représentant des coordonnées sphériques spécifiques, les coordonnées sphériques.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class GeographicCoordinates extends SphericalCoordinates {

    /**
     * Intervalles permettant la mise en place de vérification de variables.
     */

    private static final RightOpenInterval LON_DEG_INTERVAL = RightOpenInterval.symmetric(360);
    private static final ClosedInterval LAT_DEG_INTERVAL = ClosedInterval.symmetric(180);

    /**
     * Construit des coordonnées géographiques.
     *
     * @param longitude
     * @param latitude
     */

    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Méthode de test sur la longitude.
     *
     * @param lonDeg longitude en degré
     * @return vrai ssi l'angle qui lui est passé représente une longitude valide en degrés,
     */

    public static boolean isValidLonDeg(double lonDeg) {

        return LON_DEG_INTERVAL.contains(lonDeg);
    }

    /**
     * Méthode de test sur la latitude.
     *
     * @param latDeg latitude en degré
     * @return vrai ssi l'angle qui lui est passé représente une latitude valide en degrés.
     */

    public static boolean isValidLatDeg(double latDeg) {
        return LAT_DEG_INTERVAL.contains(latDeg);
    }

    /**
     * Méthode de construction de coordonnées géographiques accessible en dehors de la classe.
     *
     * @param lonDeg longitude en degré
     * @param latDeg latitude en degré
     * @return des coordonnées géographiques en fonction de lonDeg et latDeg (convertis en radian).
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Preconditions.checkArgument(isValidLonDeg(lonDeg) && isValidLatDeg(latDeg));
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    @Override
    public double lon() {
        return super.lon();
    }

    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    @Override
    public double lat() {
        return super.lat();
    }

    @Override
    public double latDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format("lon = %.4f ° ," + "lat = %.4f °", lonDeg(), latDeg());

    }
}