package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.List;

import static java.lang.Math.*;

/**
 * Type énuméré représentant les modèles des huits planètes du système solaire.
 *
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud (302860)
 */

public enum  PlanetModel implements CelestialObjectModel < Planet > {

    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    //Liste immuable constituée des huit modèles de planètes, dans leur ordre de déclaration (MERCURY, VENUS, etc.).
    public static final List < PlanetModel > ALL = List.of(values());
    //Temps qu'il faut à la Terre pour effectuer un tour, année tropique.
    private static final double TROPIC_YEAR = 365.242191;
    //Vitesse angulaire moyenne de la Terre
    private static final double EARTH_AVERAGE_ANGULAR_SPEED = Angle.TAU / TROPIC_YEAR;

    /**
     * Caractéristiques d'une planète la caractérisant et permettant de calculer son modèle.
     */

    private final String name;
    private final double revolutionPeriod;
    private final double longitudeATJ2020;
    private final double perigeeLongitude;
    private final double orbitExcentricity;
    private final double orbitSemiAxe;
    private final double orbitTilt;
    private final double nodeLongitude;
    private final double angularSizeAt1UA;
    private final double magnitudeAt1UA;

    private static double EARTH_MEAN_ANOMALY;





    /**
     * Construit le modèle d'une planète.
     *
     * @param name              nom
     * @param revolutionPeriod  période de révolution
     * @param longitudeAtJ2010  Longitude à J2010
     * @param perigeeLongitude  Longitude au périgée
     * @param orbitExcentricity Excentricité de l'orbite
     * @param orbitSemiAxe      Demi grand-axe de l'orbite
     * @param orbitTilt         Inclinaison de l'orbite à l'écliptique
     * @param nodeLongitude     Longitude du nœud ascendant
     * @param angularSizeAt1UA  Taille angulaire à 1 UA
     * @param magnitudeAt1UA    magnitude à 1 UA
     */
    PlanetModel(String name, double revolutionPeriod, double longitudeAtJ2010, double perigeeLongitude, double orbitExcentricity, double orbitSemiAxe, double orbitTilt, double nodeLongitude, double angularSizeAt1UA, double magnitudeAt1UA) {


        this.name = name;
        this.revolutionPeriod = revolutionPeriod;
        this.longitudeATJ2020 = Angle.ofDeg(longitudeAtJ2010);
        this.perigeeLongitude = Angle.ofDeg(perigeeLongitude);
        this.orbitExcentricity = orbitExcentricity;
        this.orbitSemiAxe = orbitSemiAxe;
        this.orbitTilt = Angle.ofDeg(orbitTilt);
        this.nodeLongitude = Angle.ofDeg(nodeLongitude);
        this.angularSizeAt1UA = angularSizeAt1UA;
        this.magnitudeAt1UA = magnitudeAt1UA;


    }

    /**
     * @param daysSinceJ2010                 nombre de jours séparant l'époque J2010 et l'instant pour lequel on calcul le modèle de la planète.
     * @param eclipticToEquatorialConversion conversion de coordonées de écliptiques vers les coordonnées équatoriales.
     * @return le modèle de la planète pour le moment donné.
     */

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {


        //Calcul de l'anomalie moyenne de la Terre.
        EARTH_MEAN_ANOMALY = planetMeanAnomaly(daysSinceJ2010, EARTH.revolutionPeriod, EARTH.longitudeATJ2020, EARTH.perigeeLongitude);

        //Calcul de l'anomalie vraie de la Terre.
        double earthTrueAnomaly = planetTrueAnomaly(EARTH_MEAN_ANOMALY, EARTH.orbitExcentricity);

        //Calcul du rayon de la Terre dans le plan de son orbite.
        double earthRadiusOrbitPlan = planetRadiusOrbitPlan(EARTH.orbitSemiAxe, EARTH.orbitExcentricity, earthTrueAnomaly);

        //Calcul de la longitude de la Terre dans le plan de son orbite.
        double earthLongitudeOrbitPlan = earthTrueAnomaly + EARTH.perigeeLongitude;


        //Calcul de l'anomalie moyenne de la planète.
        double planetMeanAnomaly = planetMeanAnomaly(daysSinceJ2010, revolutionPeriod, longitudeATJ2020, perigeeLongitude);

        //Calcul de l'anmoalie vrai de la planète.
        double planetTrueAnomaly = planetTrueAnomaly(planetMeanAnomaly, orbitExcentricity);

        //Calcul du rayon de la planète dans le plan de son orbite.
        double planetRadiusOrbitPlan = planetRadiusOrbitPlan(orbitSemiAxe, orbitExcentricity, planetTrueAnomaly);

        //Calcul de la longitude de la planète dans le plan de son orbite.
        double planetLongitudeOrbitPlan = planetTrueAnomaly + perigeeLongitude;

        //Calcul de la latitude éclitique héliocentrique.
        double planetEclipticHeliocentricLatitude = asin(sin(planetLongitudeOrbitPlan - nodeLongitude) * sin(orbitTilt));

        //Calcul de de la projection du rayon de la planète dans le plan de son orbite sur le plan de l'écliptique.
        double planetProjectedRadius = planetRadiusOrbitPlan * cos(planetEclipticHeliocentricLatitude);

        //Calcul de la projection de la longitude de la planète dans le plan de son orbite sur la plan de l'écliptique
        double planetProjectedOrbitPlanLongitude = atan2(sin(planetLongitudeOrbitPlan - nodeLongitude) * cos(orbitTilt), cos(planetLongitudeOrbitPlan - nodeLongitude)) + nodeLongitude;

        //Calcul de la distance séparant une planète de la Terre.
        double planetDistanceWithEarth = sqrt(pow(earthRadiusOrbitPlan, 2) + planetRadiusOrbitPlan * planetRadiusOrbitPlan - 2 * earthRadiusOrbitPlan * planetRadiusOrbitPlan * cos(planetLongitudeOrbitPlan - earthLongitudeOrbitPlan) * cos(planetEclipticHeliocentricLatitude));

        //Calcul de la taille angulaire.
        double angularSize = Angle.ofArcsec(angularSizeAt1UA) / planetDistanceWithEarth;

/**
 * Test si la planète est supérieur ou inférieur puis calcul des coordonnées ecliptiques et  equatoriales, puis de la taille angulaire et enfin de la magnitude afin de construire le modèle.
 */
        if (name.equals("Mercure") || name.equals("Vénus")) {

            double eclipticGeocentricLongitude = Angle.normalizePositive(PI + earthLongitudeOrbitPlan + atan2(planetProjectedRadius * sin(earthLongitudeOrbitPlan - planetProjectedOrbitPlanLongitude), earthRadiusOrbitPlan - (planetProjectedRadius * cos(earthLongitudeOrbitPlan - planetProjectedOrbitPlanLongitude))));

            return calcul(eclipticGeocentricLongitude, planetProjectedRadius, planetEclipticHeliocentricLatitude, planetProjectedOrbitPlanLongitude, planetLongitudeOrbitPlan, planetRadiusOrbitPlan, planetDistanceWithEarth, earthRadiusOrbitPlan, earthLongitudeOrbitPlan, eclipticToEquatorialConversion, angularSize);


        } else {
            double eclipticGeocentricLongitude = Angle.normalizePositive(planetProjectedOrbitPlanLongitude + atan2(earthRadiusOrbitPlan * sin(planetProjectedOrbitPlanLongitude - earthLongitudeOrbitPlan), planetProjectedRadius - (earthRadiusOrbitPlan * cos(planetProjectedOrbitPlanLongitude - earthLongitudeOrbitPlan))));

            return calcul(eclipticGeocentricLongitude, planetProjectedRadius, planetEclipticHeliocentricLatitude, planetProjectedOrbitPlanLongitude, planetLongitudeOrbitPlan, planetRadiusOrbitPlan, planetDistanceWithEarth, earthRadiusOrbitPlan, earthLongitudeOrbitPlan, eclipticToEquatorialConversion, angularSize);


        }


    }

    private Planet calcul(double eclipticGeocentricLongitude, double planetProjectedRadius, double planetEclipticHeliocentricLatitude, double planetProjectedOrbitPlanLongitude, double planetLongitudeOrbitPlan, double planetRadiusOrbitPlan, double planetDistanceWithEarth, double earthRadiusOrbitPlan, double earthLongitudeOrbitPlan, EclipticToEquatorialConversion eclipticToEquatorialConversion, double angularSize) {

        double eclipticGeocentricLatitude = planetEclipticGeocentricLatitude(planetProjectedRadius, planetEclipticHeliocentricLatitude, eclipticGeocentricLongitude, planetProjectedOrbitPlanLongitude, earthRadiusOrbitPlan, earthLongitudeOrbitPlan);

        double magnitude = planetMagnitude(eclipticGeocentricLongitude, planetLongitudeOrbitPlan, planetRadiusOrbitPlan, planetDistanceWithEarth, magnitudeAt1UA);

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(eclipticGeocentricLongitude, eclipticGeocentricLatitude);

        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Planet(name, equatorialCoordinates, (float) angularSize, (float) magnitude);


    }

    /**
     * Méthode privée permettant la non-duplication de code, calculant l'anomalie moyenne d'une planète.
     *
     * @param daysSinceJ2010   nombre de jours entre l'époque J2010 et le moment pour lequel on calcul le modèle.
     * @param revolutionPeriod période de revolution.
     * @param longitudeATJ2020 longitude à l'époque J2010.
     * @param perigeeLongitude longitude au périgée.
     * @return l'anomalie moyenne pour la planète donnée.
     */

    private double planetMeanAnomaly(double daysSinceJ2010, double revolutionPeriod, double longitudeATJ2020, double perigeeLongitude) {
        return (EARTH_AVERAGE_ANGULAR_SPEED * daysSinceJ2010 / revolutionPeriod) + (longitudeATJ2020 - perigeeLongitude);
    }

    /**
     * Méthode privée permettant la non-duplication de code, calculant l'anomalie vraie d'une planète.
     *
     * @param meanAnomaly       anomalie moyenne
     * @param orbitExcentricity execentricité de l'orbite
     * @return l'anomalie vraie pour la planète donnée.
     */

    private double planetTrueAnomaly(double meanAnomaly, double orbitExcentricity) {
        return meanAnomaly + 2 * orbitExcentricity * sin(meanAnomaly);
    }

    /**
     * Méthode privée permettant la non-duplication de code, calculant le rayon d'une planète dans la plan de son orbite.
     *
     * @param orbitSemiAxe      Demi grand-axe de l'orbite
     * @param orbitExcentricity excentricité de l'orbite
     * @param trueAnomaly       anomalie vraie
     * @return le rayon de la planète donnée dans le plan de son orbite.
     */

    private double planetRadiusOrbitPlan(double orbitSemiAxe, double orbitExcentricity, double trueAnomaly) {
        return (orbitSemiAxe * (1 - pow(orbitExcentricity, 2))) / (1 + orbitExcentricity * cos(trueAnomaly));

    }

    /**
     * Méthode privée permettant la non-duplication de code, calculant la latitude ecliptique d'une planète.
     *
     * @param projectedRadius         rayon projeté dan sle plan de l'orbite de la planète
     * @param planetLatitude          latitude de planète dans le plan de son orbite
     * @param longitude               longitude ecliptique de la planète
     * @param projectedLongitude      longitude de la planète projétée dans le plan de son orbite.
     * @param earthRadiusOrbitPlan    rayon de la Terre projetée dans le plan de son orbite.
     * @param earthLongitudeOrbitPlan longitude de la Terre projetée dans le plan de son orbite.
     * @return la latitude ecliptique de la planète donnée.
     */
    private double planetEclipticGeocentricLatitude(double projectedRadius, double planetLatitude, double longitude, double projectedLongitude, double earthRadiusOrbitPlan, double earthLongitudeOrbitPlan) {
        return atan((projectedRadius * tan(planetLatitude) * sin(longitude - projectedLongitude)) / (earthRadiusOrbitPlan * sin(projectedLongitude - earthLongitudeOrbitPlan)));
    }

    /**
     * Méthode privée permettant la non-duplication de code, calculant la magnitude d'une planète.
     *
     * @param longitude       longitude écliptique de la planète.
     * @param planetLongitude longitude de la planète projetée dans l eplan de son orbite.
     * @param planetRadius    rayon de la planète projetée dans le plan de son orbite.
     * @param distance        distance entre la Terre et la planète.
     * @param magnitudeAt1UA  la magnitude de la planète à 1 UA.
     * @return la magnitude de la planète dnnnée.
     */
    private double planetMagnitude(double longitude, double planetLongitude, double planetRadius, double distance, double magnitudeAt1UA) {
        double phase = (1 + cos(longitude - planetLongitude)) / 2;
        return magnitudeAt1UA + 5 * log10(planetRadius * distance / sqrt(phase));

    }



}


