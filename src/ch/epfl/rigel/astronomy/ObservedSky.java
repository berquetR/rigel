package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.MathematicalCalculus;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Classe représentant le "ciel" pour un instant donné c'est à dire,  un ensemble d'objets célèstes projetés dans le plan par une projection stéréographique.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */


public final class ObservedSky {

    private final EquatorialToHorizontalConversion equatorialToHorizontalConversion;

    private final EclipticToEquatorialConversion eclipticToEquatorialConversion;




    /**
     * Catalogue contenant l'ensemble des étoiles et des astérismes.
     */
    private final StarCatalogue catalogue;

    private final ArtificialSatelliteCatalogue satelliteCatalogue;

    private final UFOCatalogue ufoCatalogue;

    /**
     * Modèles du soleil et de la lune.
     */
    private final Sun sun;
    private final Moon moon;

    /**
     * Liste représentant l'ensemble des planètes.
     */
    private final List < Planet > planets;


    /**
     * Tableaux contenant les coordonnées projetées dans le plan  des différentes planètes et des étoiles.
     */
    private final double[] planetPositions;
    private final double[] starPositions;
    private final double [] satellitePosition;
    private final double [] ufosPosition;
    private final Map < celestialObjects, CartesianCoordinates[] > map;

    /**
     * Cordonnées du soleil et de la lune projetées dans le plan du soleil et de la lune.
     */
    private final CartesianCoordinates sunPosition;
    private final CartesianCoordinates moonPosition;

    /**
     * Construit une photographie du ciel à un instant et un endroit d'observation donnés.
     *
     * @param instantOfObservation  l'instant d'observation
     * @param positionOfObservation position d'observation
     * @param projection            projection à utiliser
     * @param catalogue             ensemble contenant les étoiles et les astérismes
     */

    public ObservedSky(ZonedDateTime instantOfObservation, GeographicCoordinates positionOfObservation, StereographicProjection projection, StarCatalogue catalogue, ArtificialSatelliteCatalogue satelliteCatalogue, UFOCatalogue ufoCatalogue) {

        this.catalogue = catalogue;

        this.ufoCatalogue = ufoCatalogue;

        this.satelliteCatalogue = satelliteCatalogue;

        double daysSinceJ2010 = Epoch.J2010.daysUntil(instantOfObservation);

         eclipticToEquatorialConversion = new EclipticToEquatorialConversion(instantOfObservation);

        // Modélisation du Soleil, de la Lune et des planètes au moment de l'observation.

        sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);

        moon = MoonModel.MOON.at(daysSinceJ2010, eclipticToEquatorialConversion);

        planets = listOfPlanetConstructor(daysSinceJ2010, eclipticToEquatorialConversion);


        // Calculs des positions projetées de la Lune et du Soleil.

         equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(instantOfObservation, positionOfObservation);

        sunPosition = projection.apply(equatorialToHorizontalConversion.apply(sun.equatorialPos()));

        moonPosition = projection.apply(equatorialToHorizontalConversion.apply(moon.equatorialPos()));

        // Calculs des positions projetées des planètes et des étoiles.

        planetPositions = planetPositionsConstructor(planets, equatorialToHorizontalConversion, projection);

        starPositions = starsPositionsConstructor(catalogue, equatorialToHorizontalConversion, projection);

        satellitePosition = satellitePositionsConstructor(satelliteCatalogue, equatorialToHorizontalConversion, projection);

        ufosPosition = ufosPosition(ufoCatalogue, equatorialToHorizontalConversion, projection);

        // Création d'une table associative associant a un objet celeste, un tableau avec les coordonnées cartésiennes
        map = celestialObjectsMapConstructor(starPositions, planetPositions, moonPosition, sunPosition);


    }

    /**
     * @return le modèle du soleil.
     */

    public Sun sun() {
        return sun;
    }

    /**
     * @return la position en coordonnées cartésiennes du modèle du Soleil.
     */

    public CartesianCoordinates sunPosition() {
        return sunPosition;
    }

    /**
     * @return le modèle de la Lune.
     */

    public Moon moon() {
        return moon;
    }

    /**
     * @return la position en coordonnées cartésiennes du modèle de la Lune.
     */

    public CartesianCoordinates moonPosition() {
        return moonPosition;
    }

    /**
     * @return une liste immuable identique à la liste contenant l'ensemble des planètes.
     */

    public List < Planet > planets() {
        return planets;
    }

    public List <UFO> ufos () { return ufoCatalogue.UFOs();}

    public double [] ufoPositions () { return ufosPosition;
    }

    /**
     * @return les coordonnées des planètes.
     */

    public double[] planetsPositions() {
        return planetPositions;
    }

    public EquatorialToHorizontalConversion equatorialToHorizontalConversion () {
        return equatorialToHorizontalConversion;
    }

    public EclipticToEquatorialConversion eclipticToEquatorialConversion () {
        return eclipticToEquatorialConversion;
    }

    /**
     * @return la liste contenant toutes les étoiles.
     */
    public List < Star > stars() {
        return catalogue.stars();
    }

    /**
     * @return les coordonnées des étoiles.
     */
    public double[] starPositions() {
        return starPositions;
    }

    public double [] satellitePositions () {
        return satellitePosition;
    }


    public List <ArtificialSatellite> artificialSatellites () {
        return satelliteCatalogue.satellites();
    }


    /**
     * @return un ensemble immuable contenant l'ensemble des astérismes.
     */

    public Set < Asterism > asterisms() {
        return catalogue.asterisms();

    }

    /**
     * @param asterism un astérisme
     * @return la liste des index des étoiles d'un astérisme donné.
     */

    public List < Integer > asterismIndices(Asterism asterism) {
        return catalogue.asterismIndices(asterism);
    }

    /**
     * Méthode permettant de déterminer l'objet célèste le plus proche de point se trouvant à une distance inférieure de maxDistance.
     *
     * @param point       point du plan pour lequel on cherche l'objet célèste
     * @param maxDistance distance maximale
     * @return l'objet cèleste le plus proche de point.
     */

    public Optional < CelestialObject > objectClosestTo(CartesianCoordinates point, double maxDistance) {

        // L'objet le plus proche est initialisé à un optional nul.
        Optional < CelestialObject > closestObject = Optional.empty();


        double min = Double.POSITIVE_INFINITY;
        double distance;

        // Cette boucle détermine l'objet le plus proche parmis tous les objets celestes (voir enum)
        for (celestialObjects c : celestialObjects.values()) {
            int index = 0;
            for (CartesianCoordinates coordinates : map.get(c)) {
                distance = MathematicalCalculus.distanceBetween(point, coordinates);
                if (distance < maxDistance && min > distance) {
                    min = distance;
                    switch (c) {
                        case SUN:
                            closestObject = Optional.of(sun);
                            break;
                        case MOON:
                            closestObject = Optional.of(moon);
                            break;
                        case STAR:
                            closestObject = Optional.of(stars().get(index));
                            break;
                        case PLANET:
                            closestObject = Optional.of(planets().get(index));
                            break;
                        default:
                            closestObject = Optional.empty();
                    }
                }
                index++;
            }
        }

        return closestObject;
    }

    /**
     * Méthodes permettant d'alleger le constructeur de la classe.
     */
    private List < Planet > listOfPlanetConstructor(double daysSinceJ2010, EclipticToEquatorialConversion conversion) {
        List < Planet > planets = new ArrayList <>();

        Planet mercury = PlanetModel.MERCURY.at(daysSinceJ2010, conversion);
        planets.add(mercury);

        Planet venus = PlanetModel.VENUS.at(daysSinceJ2010, conversion);
        planets.add(venus);

        Planet mars = PlanetModel.MARS.at(daysSinceJ2010, conversion);
        planets.add(mars);

        Planet jupiter = PlanetModel.JUPITER.at(daysSinceJ2010, conversion);
        planets.add(jupiter);

        Planet saturn = PlanetModel.SATURN.at(daysSinceJ2010, conversion);
        planets.add(saturn);

        Planet uranus = PlanetModel.URANUS.at(daysSinceJ2010, conversion);
        planets.add(uranus);

        Planet neptune = PlanetModel.NEPTUNE.at(daysSinceJ2010, conversion);
        planets.add(neptune);

        return List.copyOf(planets);

    }


    private double[] planetPositionsConstructor(List < Planet > planets, EquatorialToHorizontalConversion equatorialToHorizontalConversion, StereographicProjection projection) {
        int i = 0;
        double[] pPositions = new double[14];
        for (Planet planet : planets) {
            CartesianCoordinates coordinates = projection.apply(equatorialToHorizontalConversion.apply(planet.equatorialPos()));
            pPositions[i] = coordinates.x();
            ++i;
            pPositions[i] = coordinates.y();
            ++i;
        }
        return Arrays.copyOf(pPositions, pPositions.length);
    }


    private double[] starsPositionsConstructor(StarCatalogue catalogue, EquatorialToHorizontalConversion equatorialToHorizontalConversion, StereographicProjection projection) {
        int j = 0;
        double[] sPositions = new double[catalogue.stars().size() * 2];
        List < Star > stars = catalogue.stars();
        for (Star s : stars) {
            CartesianCoordinates coordinates = projection.apply(equatorialToHorizontalConversion.apply(s.equatorialPos()));
            sPositions[j] = coordinates.x();
            ++j;

            sPositions[j] = coordinates.y();
            ++j;
        }

        return Arrays.copyOf(sPositions, sPositions.length);
    }
    private double [] satellitePositionsConstructor (ArtificialSatelliteCatalogue catalogue, EquatorialToHorizontalConversion equatorialToHorizontalConversion, StereographicProjection projection) {
        int i= 0;
        double [] sPositions = new double [catalogue.satellites().size() * 2];
        List <ArtificialSatellite> list = catalogue.satellites();

        for (ArtificialSatellite satellite: list) {
            CartesianCoordinates coordinates = projection.apply(equatorialToHorizontalConversion.apply(satellite.coordinates()));
            sPositions [i] = coordinates.x();
            i++;
            sPositions [i] = coordinates.y();
            i++;
        }
        return Arrays.copyOf(sPositions, sPositions.length);
    }

    private double [] ufosPosition (UFOCatalogue ufoCatalogue, EquatorialToHorizontalConversion equatorialToHorizontalConversion, StereographicProjection projection) {
        int i = 0;

        List <UFO> ufos = ufoCatalogue.UFOs();
        double [] uPostions = new double [ufos.size() * 2 ];

        for (UFO ufo: ufos) {

            CartesianCoordinates coordinates = projection.apply(equatorialToHorizontalConversion.apply(ufo.coordinates()));
            uPostions [i] = coordinates.x();
            i++;
            uPostions [i] = coordinates.y();
            i++;
        }
        return Arrays.copyOf(uPostions, uPostions.length);
    }



    private Map < celestialObjects, CartesianCoordinates[] > celestialObjectsMapConstructor(double[] starPositions, double[] planetPositions, CartesianCoordinates moonPosition, CartesianCoordinates sunPosition) {
        Map < celestialObjects, CartesianCoordinates[] > map = new HashMap <>();

        CartesianCoordinates[] moonPos = {moonPosition};
        map.put(celestialObjects.MOON, moonPos);

        CartesianCoordinates[] sunPos = {sunPosition};
        map.put(celestialObjects.SUN, sunPos);

        CartesianCoordinates[] starPos = new CartesianCoordinates[starPositions.length / 2];
        for (int i = 0; i < starPositions.length; i = i + 2) {
            starPos[i / 2] = CartesianCoordinates.of(starPositions[i], starPositions[i + 1]);
        }
        map.put(celestialObjects.STAR, starPos);

        CartesianCoordinates[] planetPos = new CartesianCoordinates[planetPositions.length / 2];
        for (int i = 0; i < planetPositions.length; i = i + 2) {
            planetPos[i / 2] = CartesianCoordinates.of(planetPositions[i], planetPositions[i + 1]);
        }
        map.put(celestialObjects.PLANET, planetPos);

        return map;
    }

    private enum celestialObjects {
        MOON, SUN, STAR, PLANET

    }


}