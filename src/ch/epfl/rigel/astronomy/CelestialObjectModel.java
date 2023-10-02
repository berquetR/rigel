package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Représente un modèle d'objet céleste, c-à-d une manière de calculer les caractéristiques de cet objet à un instant donné.
 *
 * @param <O>
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud (302860)
 */
public interface CelestialObjectModel< O > {

    /**
     * @param daysSinceJ2010                 correspondant au nombre de jours en entre le moment pour lequel on souhaite calculer le modèle et l'époque J2010
     * @param eclipticToEquatorialConversion correspondant à une conversion de coordonées
     * @return l'objet modélisé par le modèle pour le nombre (éventuellement négatif) de jours après l'époque J2010 donné, en utilisant la conversion donnée pour obtenir ses coordonnées équatoriales à partir de ses coordonnées écliptiques.
     */
    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
