package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Représente un intervalle fermé.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */
public final class ClosedInterval extends Interval {

    /**
     * Appel le constructeur de la classe mère (Interval).
     *
     * @param higherBound correspondant à la borne supérieur de l'intervalle.
     * @param lowerBound  correspondant à la borne inférieur de l'intervalle.
     */

    private ClosedInterval(double lowerBound, double higherBound) {
        super(lowerBound, higherBound);
    }

    /**
     * Construit un intervalle en dehors de la classe.
     *
     * @param low  correspondant à la borne inférieur de l'intervalle.
     * @param high correspondant à la borne supérieur de l'intervalle.
     * @return un intervalle
     * @throws IllegalArgumentException si la borne inférieure est supérieur ou égale à la borne inférieur.
     */
    public static ClosedInterval of(double low, double high) {

        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);

    }

    /**
     * Construit un intervalle centré sur 0 et symmétrique.
     *
     * @param size correspondant à la taille de l'intervalle.
     * @return un interval
     * @throws IllegalArgumentException si la taille de l'intervalle passée en argument est nulle ou négative.
     */
    public static ClosedInterval symmetric(double size) {

        Preconditions.checkArgument(size > 0);
        return new ClosedInterval(-size / 2, size / 2);

    }

    @Override
    public boolean contains(double v) {
        return v >= low() && v <= high();
    }

    /**
     * Effectue la fonction mathématique "clip".
     *
     * @param v, une valeure quelconque.
     * @return la borne inférieure de l'interalle si v lui est inférieur ou égale, la borne supérieur si v est supérieure ou égale à cette dernière ou v autrement.
     */
    public double clip(double v) {
        if (v <= low()) {
            return low();
        }
        if (v >= high()) {
            return high();
        } else {
            return v;
        }
    }


    @Override
    public String toString() {
        return String.format(Locale.ROOT, "]%s,%s[", low(), high());
    }


}
