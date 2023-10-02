package ch.epfl.rigel.coordinates;

/**
 * Représente une conversion des coordonnées equatoriales vers les coordonnées horizontales.
 *
 * @author Victor Gergaud (302860)
 */

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static java.lang.Math.*;

/**
 * Représente un changement de systèmes de coordonnées depuis les coordonnées équatoriales vers les coordonnées écliptiques, à un instant et pour un lieu donnés.
 */

public final class EquatorialToHorizontalConversion implements Function < EquatorialCoordinates, HorizontalCoordinates > {

    /**
     * Dans ces attributs privés et finaux sont stockées des valeurs dont le calcul est effectué dans le constructeur de la classe
     * du fait qu'elle ne dépendent pas des valeurs à convertir.
     */

    private final double observatorSiderealTime;
    private final double sinObservator;
    private final double cosObservator;

    /**
     * Construit un convertisseur d'equatorial vers horizontal.
     * Effectue aussi le calcul du temps sidéral à la position de l'observateur exprimée en coordonnées géographiques
     * ainsi que des valeurs trigonométriques obtenues à partir de la latitude de l'observateur.
     *
     * @param when  étant un objet ZonedDateTime, représentant la date à laquelle on veut effectuer les conversions.
     * @param where étant un objet GeographicCoordinates, représentant la position de l'observateur.
     */

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        observatorSiderealTime = SiderealTime.local(when, where);
        sinObservator = sin(where.lat());
        cosObservator = cos(where.lat());
    }

    /**
     * Méthodes permettant la conversion de coordonnées.
     *
     * @param equ, les coordonnées à convertir.
     * @return les coordonnées equa converties en coordonnées horizontales (type HorizontalCoordinates).
     */

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        //Calcul de l'angle horaire.
        double hourAngle = observatorSiderealTime - equ.ra();
        //Calcul de la hauteur.
        double cosEq = cos(equ.dec());
        double sinEq = sin(equ.dec());
        double heigh = asin(sinEq * sinObservator + cosEq * cosObservator * cos(hourAngle));
        //Calcul de l'azimut.
        double azimut = atan2(-cosEq * cosObservator * sin(hourAngle), sinEq - sinObservator * sin(heigh));

        return HorizontalCoordinates.of(Angle.normalizePositive(azimut), heigh);
    }


    @Override
    public final boolean equals(Object otherEquatorialToHorizontalConversion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

}