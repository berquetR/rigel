package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Représente une planète.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class Planet extends CelestialObject {



    /**
     * Construit une planète.
     *
     * @param name          nom
     * @param equatorialPos position en coordonnées équatoriales
     * @param angularSize   taille angulaire
     * @param magnitude     magnitude
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }

}
