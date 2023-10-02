package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import static ch.epfl.rigel.math.Angle.TAU;

/**
 * Représente des coordonées sphériques spécifiques, le coordonnées écliptiques.
 */

public final class EclipticCoordinates extends SphericalCoordinates {

    /**
     * Intervalles permettant la mise en place de vérification de variables.
     */

    private static final RightOpenInterval LON_INTERVAL = RightOpenInterval.of(0, TAU);
    private static final ClosedInterval LAT_INTERVAL = ClosedInterval.symmetric(TAU / 2);

    /**
     * Construit des coordonnées écliptiques en fonction de lon et de lat.
     *
     * @param lon longitude
     * @param lat latitude
     */
    private EclipticCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Permet de construire des coordonnées écliptiques en dehors de la classe.
     *
     * @param lon longitude
     * @param lat latitude
     * @return des coordonnnées écliptiques en fonction de lon et de lat.
     */
    public static EclipticCoordinates of(double lon, double lat) {

        return new EclipticCoordinates(Preconditions.checkInInterval(LON_INTERVAL, lon), Preconditions.checkInInterval(LAT_INTERVAL, lat));

    }

    /**
     * @return la longitude écliptique.
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * @return la longitude écliptique en degré.
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @return la latitude écliptique.
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * @return la latitude écliptique en degré.
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format("λ = %.4f °" + ", β= %.4f °", lonDeg(), latDeg());


    }


}