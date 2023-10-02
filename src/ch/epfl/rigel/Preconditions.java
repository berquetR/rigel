package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Vérifie la véracité des arguments passés en argument d'une méthode.
 *
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud (302860)
 */
public final class Preconditions {

    /**
     * Empêche la création d'instances de la classe.
     */

    private Preconditions() {
    }

    /**
     * Lève une exception si son argument est faux, ne fait rien sinon.
     *
     * @param isTrue représente la validité d'un evariable par rapport aux exigences d'une méthode
     * @throws IllegalArgumentException si l'argumet isTrue est faux.
     */

    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Lève une exception si la valeur n'appartient pas à l'intervalle et sinon retourne uniquement la valeur.
     *
     * @param interval l'intervalle sur lequel on fait le teste d'appartenance.
     * @param value    la valeur que l'on teste.
     * @return la valeur passée en argument ou rien.
     * @throws IllegalArgumentException si value n'apartient pas à l'intervalle.
     */

    public static double checkInInterval(Interval interval, double value) {
        if (!interval.contains(value)) {
            throw new IllegalArgumentException();
        } else return value;
    }
}
