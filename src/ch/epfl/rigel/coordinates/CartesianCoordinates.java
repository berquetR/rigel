package ch.epfl.rigel.coordinates;

/**
 * Représente des coordonnées cartésiennes.
 *
 * @author: Romain Berquet (316122)
 */
public final class CartesianCoordinates {
    /**
     * Attributs représentants respectivement l'abscisse et l'ordonée.
     */
    private final double x;
    private final double y;

    /**
     * Construit des coordonnées cartésiennes.
     *
     * @param x abscisse.
     * @param y ordonée.
     */
    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construit des coordonées cartésiennes, méthode pouvant être appelée en dehors de la classe.
     *
     * @param x abscisse.
     * @param y ordonée.
     * @return des coordonnées cartésiennes nouvellement construites à partir des paramètres x et y.
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * @return l'abscisse.
     */

    public double x() {
        return x;
    }

    /**
     * @return l'ordonnée.
     */
    public double y() {
        return y;
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object otherCartesianCoordinates) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String toString() {
        return String.format("abscisse = %s , " + "ordonnée = %s", x, y);
    }
}
