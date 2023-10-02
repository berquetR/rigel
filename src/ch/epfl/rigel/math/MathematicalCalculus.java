package ch.epfl.rigel.math;

import ch.epfl.rigel.coordinates.CartesianCoordinates;

/**
 * Interface permettant la mise en place de différents calcul mathématique.
 *
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud(302860)
 */

public interface MathematicalCalculus {


    /**
     * Méthode permettant de calculer l'arrondi à la centaine la plus proche d'une valeur.
     * @param n valeur à arrondir
     * @return la valeur n arrondie à la centaine la plus proche.
     */

    static int roundToNearestHundred(float n) {

        if (n % 100 < 50) return (int)(n - n % 100);
        else return (int)(100 - (n % 100) + n);
    }
    /**
     * Méthode privée permettant de calculer la distance cartésienne entre deux points.
     *
     * @param a point a
     * @param b point b
     * @return la distance entre le point a et le point b.
     */
    static double distanceBetween(CartesianCoordinates a, CartesianCoordinates b) {
        double c = b.x()-a.x();
        double d = b.y()-a.y();
        return Math.sqrt(c * c + d * d);
    }
}
