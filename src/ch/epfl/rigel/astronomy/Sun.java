package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Représente le Soleil à un instant donné.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud ((302860)
 */

public final class Sun extends CelestialObject {

    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * Construit le Soleil.
     *
     * @param eclipticPos   la position du soleil en coordonées écliptiques.
     * @param equatorialPos la position du soleil en coordonées  équatoriales.
     * @param angularSize   la taille angulaire du soleil.
     * @param meanAnomaly   l'anomalie moyenne.
     * @throws NullPointerException si la postion écliptique est nulle.
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);

        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;

    }

    /**
     * @return la position écliptique.
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * @return l'anomalie moyenne.
     */
    public float meanAnomaly() {
        return meanAnomaly;
    }
}
