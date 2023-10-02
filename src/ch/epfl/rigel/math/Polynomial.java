package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Représente une fonction polynomiale.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class Polynomial {

    /**
     * Représente les coefficients de la fonction polynomiale.
     */
    private final double[] coeffs;

    /**
     * Construit une fonction polynomiale à partir des coefficients.
     *
     * @param coefficients représentant les coefficients de la fonction polynomiale.
     */
    private Polynomial(double[] coefficients) {
        this.coeffs = coefficients;
    }

    /**
     * Méthode permettant de constructruire des fonctions polynomiales en dehors de la classe.
     *
     * @param coefficientN représentant le coefficient de plus haut degré.
     * @param coefficients représentant les autres coefficients.
     * @return la fonction polynomiale avec les coefficients donnés.
     */

    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0);
        double[] coeffs = new double[coefficients.length + 1];
        coeffs[0] = coefficientN;
        System.arraycopy(coefficients, 0, coeffs, 1, coefficients.length);
        return new Polynomial(coeffs);
    }

    /**
     * Calcul la valeur de la fonction polynomiale pour une valeure donnée.
     *
     * @param x valeur pour laquelle on souhaite calculer la fonction.
     * @return la valeur de la fonction pour x.
     */

    public double at(double x) {
        double value = coeffs[0];
        for (int i = 1; i < coeffs.length; i++) {
            value = value * x + coeffs[i];
        }
        return value;
    }

    /**
     * @return la fonction polynomiale en charactères.
     */
    @Override
    public final String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < coeffs.length; i++) {
            if (coeffs[i] > 0) {
                if (string.length() == 0) {
                    if (coeffs[i] == 1) {
                        if (((coeffs.length - i - 1) != 1) && ((coeffs.length - i - 1) != 0))
                            string.append("x^" + (coeffs.length - i - 1));
                        else if ((coeffs.length - i - 1) == 1) string.append("x");
                        if ((coeffs.length - i - 1) == 0) string.append(coeffs[coeffs.length - 1]);
                    } else {
                        if (((coeffs.length - i - 1) != 1) && ((coeffs.length - i - 1) != 0))
                            string.append(coeffs[i] + "x^" + (coeffs.length - i - 1));
                        else if ((coeffs.length - i - 1) == 1) string.append(coeffs[i] + "x");
                        if ((coeffs.length - i - 1) == 0) string.append(coeffs[coeffs.length - 1]);
                    }
                } else {
                    if (coeffs[i] == 1) {
                        if (((coeffs.length - i - 1) != 1) && ((coeffs.length - i - 1) != 0))
                            string.append("+" + "x^" + (coeffs.length - i - 1));
                        else if ((coeffs.length - i - 1) == 1) string.append("+" + "x");
                        if ((coeffs.length - i - 1) == 0) string.append("+" + coeffs[coeffs.length - 1]);
                    } else {
                        if (((coeffs.length - i - 1) != 1) && ((coeffs.length - i - 1) != 0))
                            string.append("+" + coeffs[i] + "x^" + (coeffs.length - i - 1));
                        else if ((coeffs.length - i - 1) == 1) string.append("+" + coeffs[i] + "x");
                        if ((coeffs.length - i - 1) == 0) string.append("+" + coeffs[coeffs.length - 1]);
                    }

                }
            } else if (coeffs[i] < 0) {
                if (coeffs[i] == -1) {
                    if ((coeffs.length - i - 1) != 1) string.append("-x^" + (coeffs.length - i - 1));
                    else if ((coeffs.length - i - 1) == 1) string.append("-x");
                } else {
                    if (((coeffs.length - i - 1) != 1) && ((coeffs.length - i - 1) != 0))
                        string.append(coeffs[i] + "x^" + (coeffs.length - i - 1));
                    else if ((coeffs.length - i - 1) == 1) string.append(coeffs[i] + "x");
                }
                if ((coeffs.length - i - 1) == 0) string.append(coeffs[coeffs.length - 1]);
            }
        }
        return string.toString();
    }

    @Override
    public final boolean equals(Object otherPolynomial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

}
