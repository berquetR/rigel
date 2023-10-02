package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.function.Function;

import static java.lang.Math.*;

/**
 * Représente une projection stéréographique de coordonnées horizontales.
 *
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud (302860)
 */
public final class StereographicProjection implements Function < HorizontalCoordinates, CartesianCoordinates > {
    /**
     * Center représente le centre du cercle cercle sur lequel la projection est centrée. Les autres attributs représente les coordonnées de ce centre et valeurs trigonométriques.
     */
    private final HorizontalCoordinates center;
    private final double centerLatitudeCos;
    private final double centerLatitudeSin;

    /**
     * Construit la projection centrée sur center.
     *
     * @param center centre du cercle sur lequel la projection est centrée.
     */
    public StereographicProjection(HorizontalCoordinates center) {

        this.center = center;
        centerLatitudeCos = cos(center.lat());
        centerLatitudeSin = sin(center.lat());
    }

    /**
     * retourne les coordonnées du centre du cercle correspondant à la projection du parallèle passant par le point hor.
     *
     * @param hor
     * @return des coordonnées cartésiennes.
     */

    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {

        //Ordonné du centre du cerlce.
        double centerYCoordinate = centerLatitudeCos / ((centerLatitudeSin) + Math.sin(hor.lat()));

        return CartesianCoordinates.of(0, centerYCoordinate);
    }

    /**
     * @param parallel
     * @return le rayon du cercle correspondant à la projection du parallèle passant par le point de coordonnées hor.
     */

    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        double centerRadius = cos(parallel.lat()) * 1 / (sin(parallel.lat()) + centerLatitudeSin);
        return centerRadius;
    }

    /**
     * @param rad
     * @return le diamètre projeté d'une sphère de taille angulaire rad centrée au centre de projection
     */
    public double applyToAngle(double rad) {
        double diameter = 2 * Math.tan(rad / 4);
        return diameter;

    }

    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {

        double deltaLambda = azAlt.lon() - center.lon();
        double cosDeltaLambda = cos(deltaLambda);

        double sinAzAlt = sin(azAlt.lat());
        double cosAzAlt = cos(azAlt.lat());

        double d = 1 / (1 + sinAzAlt * centerLatitudeSin + cosAzAlt * centerLatitudeCos * cosDeltaLambda);

        double xCoordinate = d * cos(azAlt.lat()) * sin(deltaLambda);
        double yCoordinate = d * (sinAzAlt * centerLatitudeCos - cosAzAlt * centerLatitudeSin * cosDeltaLambda);

        return CartesianCoordinates.of(xCoordinate, yCoordinate);
    }

    /**
     * Retourne les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes xy.
     *
     * @param xy
     * @return des cordonées horizontales.
     */

    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {

        //Calcul de constantes nécessaires à l'application de la formule.

        double p = sqrt(xy.x() * xy.x() + xy.y() * xy.y());
        double pp = p * p;

        double sinC = (2 * p) / (pp + 1);
        double cosC = (1 - pp) / (pp + 1);

        //Calcul de la longitude au moyen de la formule donnée.
        double numerator = (xy.x() * sinC);
        double denominator = (p * centerLatitudeCos * cosC - xy.y() * centerLatitudeSin * sinC);
        double longitude;
        if (numerator == 0 && denominator == 0) {
            longitude = center.lon();
        } else {
            longitude = Math.atan2(numerator, denominator) + center.lon();
        }

        double normalizedLongitude = Angle.normalizePositive(longitude);

        //Calcul de la latitude au moyen de la formule donnée.

        double numerator1 = (xy.y() * sinC * centerLatitudeCos);
        double latitude;
        if (numerator1 == 0 && p == 0)
            latitude = center.lat();
        else
            latitude = asin(cosC * centerLatitudeSin + (numerator1 / p));


        return HorizontalCoordinates.of(normalizedLongitude, latitude);

    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object otherStereoGraphicProjection) {
        throw new UnsupportedOperationException();
    }


    @Override
    public final String toString() {
        return String.format("latitude = %s" + "longitude = %s", center.lat(), center.lon());

    }
}
