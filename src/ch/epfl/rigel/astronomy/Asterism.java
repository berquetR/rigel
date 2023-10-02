package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

        import java.util.List;

/**
 * Représente un astérisme qui correspond à une liste d'étoiles.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class Asterism {

    /**
     * Attribut représentant la liste d'étoiles.
     */

    private final List < Star > stars;

    /**
     * Construit un astérisme.
     *
     * @param stars
     */
    public Asterism(List < Star > stars) {

        Preconditions.checkArgument(stars != null && !stars.isEmpty());
        this.stars = List.copyOf(stars);
    }

    /**
     * @return la liste d'étoiles qui appartienent à l'astérisme.
     */
    public List < Star > stars() {
        return stars;
    }
}
