package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Représente un intervalle fermé à gauche et ouvert à droite.
 *
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud (302860)
 */
public final class RightOpenInterval extends Interval {
    /**
     * Appel le constructeur de la classe mère.
     *
     * @param higherBound borne superieure.
     * @param lowerBound  borne inferieure.
     */
    private RightOpenInterval(double lowerBound, double higherBound) {
        super(lowerBound, higherBound);
    }

    /**
     * Construit un intervalle ouvert à droite.
     *
     * @param low  borne inférieure.
     * @param high borne supérieure.
     * @return un intervalle en fonction de low et high.
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(high > low);
        return new RightOpenInterval(low, high);
    }

    /**
     * Créer un intervalle symmétrique centré sur 0 de taille size.
     *
     * @param size taille de l'intervalle.
     * @return un intervalle ouvert à droite centré sur 0 et de taille size.
     */

    public static RightOpenInterval symmetric(double size) {

        Preconditions.checkArgument(size > 0);
        return new RightOpenInterval(-size / 2, size / 2);

    }

    @Override
    public boolean contains(double v) {
        return (v >= low() && v < high());
    }

    /**
     * Calcul la fonction mathématique "reduce".
     *
     * @param v valeur quelconque.
     * @return la valeur de la fonction reduce pour v.
     */
    public double reduce(double v) {
        return low() + floorMod(v - low(), size());
    }

    /**
     * @param x valeur quelconque.
     * @param y valeur quelconque.
     * @return la valeur de la fonction floorMod pour x et y.
     */
    private double floorMod(double x, double y) {
        return x - (y * Math.floor(x / y));
    }


    public String toString(RightOpenInterval v) {
        return String.format(Locale.ROOT, "[%s,%s[", v.low(), v.high());
    }


}
