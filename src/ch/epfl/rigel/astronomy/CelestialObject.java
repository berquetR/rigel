package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Objects;

/**
 * Représente des objets célestes.
 *
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud(302860)
 */

public class CelestialObject {

    private static final ClosedInterval MAGNITUDE_TO_BE_CLIPPED = ClosedInterval.of(-2, 5);
    /**
     * Attributs représentant les caractéristiques d'un objet céleste.
     */

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;

    /**
     * Construit un objet celèste.
     *
     * @param name          nom
     * @param equatorialPos position en coordonnées équatoriales.
     * @param angularSize   taille angulaire
     * @param magnitude     magnitude
     * @throws IllegalArgumentException si la taille angulaire est négative.
     * @throws NullPointerException     si le nom ou la position équatorial sont nuls.
     */

    public CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {

        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        Preconditions.checkArgument(angularSize >= 0);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }


    /**
     * @return le nom de l'objet celèste.
     */

    public String name() {
        return name;
    }

    /**
     * @return la position équatoriale
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     * @return la taille angulaire
     */
    public double angularSize() {
        return angularSize;
    }

    /**
     * @return la magnitude
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     * Méthode determinant le diamètre à dessiner d'un objet célèste dans le plan.
     *
     * @param magnitude  magnitude de l'objet célèste
     * @param projection projection stéréographique
     * @return le diamètre de le plan de l'objet à dessiner.
     */


    public double magnitudeTransformation(double magnitude, StereographicProjection projection) {


        double clippedMagnitude = MAGNITUDE_TO_BE_CLIPPED.clip(magnitude);

        double sizeFactor = (99.0 - 17.0 * clippedMagnitude) / 140.0;

        double diameter = sizeFactor * projection.applyToAngle(Angle.ofDeg(0.50));


        return diameter;
    }

    /**
     * @return les informations sur l'objet céleste.
     */

    public String info() {
        return name();
    }

    @Override
    public String toString() {
        return info();
    }


}
