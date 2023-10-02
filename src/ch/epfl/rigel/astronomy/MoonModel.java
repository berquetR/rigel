package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import static java.lang.Math.*;

/**
 * Contient un seul élément nommé MOON et représente un modèle de la Lune.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public enum MoonModel implements CelestialObjectModel < Moon > {

    MOON;

    private static final double MEAN_LONGITUDE = Angle.ofDeg(91.929336);
    private static final double MEAN_PERIGEE_LONGITUDE = Angle.ofDeg(130.143076);
    private static final double LONGITUDE_NODE = Angle.ofDeg(291.682547);
    private static final double COS_ORBIT_TILT = cos(Angle.ofDeg(5.145396));
    private static final double SIN_ORBIT_TILT = sin(Angle.ofDeg(5.145396));
    private static final double ORBIT_EXCENTRICITY = 0.0549;

    //Constantes
    private static final double C1 = Angle.ofDeg(13.1763966);
    private static final double C2 = Angle.ofDeg(0.1114041);
    private static final double C3 = Angle.ofDeg(1.2739);
    private static final double C4 = Angle.ofDeg(0.1858);
    private static final double C5 = Angle.ofDeg(0.37);
    private static final double C6 = Angle.ofDeg(6.2886);
    private static final double C7 = Angle.ofDeg(0.214);
    private static final double C8 = Angle.ofDeg(0.6583);
    private static final double C9 = Angle.ofDeg(0.0529539);
    private static final double C10 = Angle.ofDeg(0.16);


    // Taille angulaire de la Lune vue depuis la Terre à une distance égale au demi-grand axe de l'orbite
    private static final double THETA0 = Angle.ofDeg(0.5181);

    /**
     * Cette méthode calcule plusieurs constantes nécessaires au calcul de la position ecliptique de la Lune à un moment donné
     * Les étapes sont détaillées.
     *
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return un nouvel objet Moon représentant la Lune pour le moment rentré en argument
     */

    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        // Calcul de la longitude orbitale moyenne de la Lune
        double meanOrbitalLongitude = daysSinceJ2010 * C1 + MEAN_LONGITUDE;

        // Calcul de l'anomalie moyenne de la Lune
        double meanAnomaly = meanOrbitalLongitude - C2 * daysSinceJ2010 - MEAN_PERIGEE_LONGITUDE;

        // Calcul de plusieurs termes de correction

        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double sinMeanAnomaly = sin(sun.meanAnomaly());

        double evection = C3 * sin(2 * (meanOrbitalLongitude - sun.eclipticPos().lon()) - meanAnomaly);

        double yearEquationCorrection = C4 * sinMeanAnomaly;
        double correction3 = C5 * sinMeanAnomaly;

        // Obtention de l'anomalie corrigée
        double correctedAnomaly = meanAnomaly + evection - yearEquationCorrection - correction3;

        // Calcul de plusieurs termes de correction
        double centreEquationCorrection = C6 * sin(correctedAnomaly);
        double correction4 = C7 * sin(2 * correctedAnomaly);

        // Obtention de la longitude orbitale corrigée
        double correctedOrbitalLongitude = meanOrbitalLongitude + evection + centreEquationCorrection - yearEquationCorrection + correction4;

        // Calcul du terme correcteur variation
        double variation = C8 * sin(2 * (correctedOrbitalLongitude - sun.eclipticPos().lon()));

        // Finalement, calcul de la longitude orbitale vraie de la Lune
        double trueOrbitalLongitude = correctedOrbitalLongitude + variation;


        // Calculs des longitudes moyennes et corrigées du noeud ascendant
        double meanLongitudeNode = LONGITUDE_NODE - C9 * daysSinceJ2010;
        double correctedLongitudeNode = meanLongitudeNode - C10 * sin(sun.meanAnomaly());

        // Calculs de la longitude et latitude ecliptique de la Lune
        double variable = sin(trueOrbitalLongitude - correctedLongitudeNode);
        double eclipticLon = atan2(variable * COS_ORBIT_TILT, cos(trueOrbitalLongitude - correctedLongitudeNode)) + correctedLongitudeNode;
        double eclipticLat = asin(variable * SIN_ORBIT_TILT);

        // Calcul de la taille angulaire de la Lune
        double distanceEarthMoon = (1 - pow(ORBIT_EXCENTRICITY, 2)) / (1 + ORBIT_EXCENTRICITY * cos(correctedAnomaly + centreEquationCorrection));
        float angularSize = (float) (THETA0 / distanceEarthMoon);

        // Calcul de la phase de la Lune
        float moonPhase = (float) (1 - cos(trueOrbitalLongitude - sun.eclipticPos().lon())) / 2;

        EclipticCoordinates coordinates = EclipticCoordinates.of(Angle.normalizePositive(eclipticLon), eclipticLat);

        return new Moon(eclipticToEquatorialConversion.apply(coordinates), angularSize, 0, moonPhase);

    }
}