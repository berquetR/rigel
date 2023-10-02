package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import static java.lang.Math.*;


/**
 * Contient un seul élément nommé SUN et représente un modèle du soleil.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */
public enum SunModel implements CelestialObjectModel < Sun > {
    SUN;

    /**
     * Caractéristiques du Soleil permettant de calculer son modèle à un instant donné.
     */

    // Longitude à J2010 en radian.
    private final static double LONGITUDE_AT_J2010 = Angle.ofDeg(279.557208);

    // Longitude au périgé en radian.
    private final static double LONGITUDE_AT_PERIGEE = Angle.ofDeg(283.112438);

    // Excentricité de l'orbite Terre/Soleil.
    private final static double ORBIT_EXCENTRICITY = 0.016705;

    //Taille angulaire à une distance de 1 UA de la Terre.
    private final static double THETA0 = Angle.ofDeg(0.533128);

    // Année tropique, vitesse angulaire moyenne de rotation de la Terre autour du Soleil.
    private final static double TROPIC_YEAR = Angle.TAU * 1 / 365.242191;


    /**
     * Retourne le soleil modélisé par le modèle pour le nombre (éventuellement négatif) de jours après l'époque J2010 donné, en utilisant la conversion donnée pour obtenir ses coordonnées équatoriales à partir de ses coordonnées écliptiques.
     *
     * @param daysSinceJ2010                 nombre de jours depuis l'époque J2010
     * @param eclipticToEquatorialConversion conversion des coordonnées
     * @return le modèle du Soleil en fonction des différents arguments passés.
     */

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        // Calcul de l'anomalie moyenne.
        double meanAnomaly = TROPIC_YEAR * daysSinceJ2010 + (LONGITUDE_AT_J2010 - LONGITUDE_AT_PERIGEE);

        // Calcul de l'anomalie vraie.
        double trueAnomaly = meanAnomaly + 2 * ORBIT_EXCENTRICITY * sin(meanAnomaly);

        // Calcul de la taille angulaire.
        double angularSize = THETA0 * (((1 + ORBIT_EXCENTRICITY * cos(trueAnomaly)) / ((1 - pow(ORBIT_EXCENTRICITY, 2)))));

        //Calcul de la longitude écliptique du Soleil, normalisé à l'intervalle [0;TAU] pour pouvoir ensuite être convertie.
        double eclipticLongitude = Angle.normalizePositive(trueAnomaly + LONGITUDE_AT_PERIGEE);

        // On créer des coordonnées ecliptique qui vont permettre de créer le modèle du Soleil.
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(eclipticLongitude, 0);

        // On convertit les coordonnées crées ci dessus.
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);


        return new Sun(eclipticCoordinates, equatorialCoordinates, (float) angularSize, (float) meanAnomaly);


    }

}