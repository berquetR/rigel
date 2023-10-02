package ch.epfl.rigel.coordinates;


import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;
import static java.lang.Math.*;

/**
 * Représente un changement de système de coordonnées depuis les coordonnées écliptiques vers les coordonnées équatoriales, à un instant donné.
 *
 * @author Victor Gergaud (302860)
 */

public final class EclipticToEquatorialConversion implements Function < EclipticCoordinates, EquatorialCoordinates > {

    private final static Polynomial OBLIQUITY_POLYNOMIAL = Polynomial.of(Angle.ofArcsec(0.00181), Angle.ofArcsec(-0.0006), Angle.ofArcsec(-46.815), Angle.ofDMS(23, 26, 21.45));
    private final double cosObliquity;
    private final double sinObliquity;

    /**
     * Construit un convertisseur d'ecliptic vers equatorial
     * Effectue aussi le calcul de l'obliquité de l'écliptique et du paramètre T en fonction de la date when
     *
     * @param when, étant un objet ZonedDateTime, représentant la date à laquelle on veut effectuer les conversions
     */

    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double T = J2000.julianCenturiesUntil(when);
        /**
         * Dans ces attributs privés et finaux sont stockées des valeurs dont le calcul est effectué dans le constructeur de la classe
         * du fait qu'elle ne dépendent pas des valeurs à convertir
         */
        double obliquity = OBLIQUITY_POLYNOMIAL.at(T);
        cosObliquity = cos(obliquity);
        sinObliquity = sin(obliquity);
    }

    /**
     * Méthode permettant une conversion de coordonnées.
     *
     * @param ecl, les coordonnées à convertir
     * @return les coordonnées ecl converties en coordonnées équatoriales (type EquatorialCoordinates)
     */

    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {

        double alpha = atan2(sin(ecl.lon()) * cosObliquity - tan(ecl.lat()) * sinObliquity, cos(ecl.lon()));

        double delta = asin(sin(ecl.lat()) * cosObliquity + cos(ecl.lat()) * sinObliquity * sin(ecl.lon()));

        return EquatorialCoordinates.of(Angle.normalizePositive(alpha), delta);
    }


    @Override
    public final boolean equals(Object otherEclipticToEquatorialConversion) {
        throw new UnsupportedOperationException();
    }


    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

}