package ch.epfl.rigel.math;

/**
 * @author: Romain Berquet Berquet (316122)
 * @author: Victor Gergaud (302860)
 * <p>
 * Représente un intervalle mathématique.
 */
public abstract class Interval {

    /**
     * Attributs représentants respectivement les bornes inférieurs et supérieurs de l'intervalle.
     */

    private final double lowBound;
    private final double highBound;

    /**
     * Construit un intervalle à partir de ses bornes.
     *
     * @param lowBound  borne inférieur de l'intervalle.
     * @param highBound borne supérieur de l'intervalle.
     */

    protected Interval(double lowBound, double highBound) {
        this.lowBound = lowBound;
        this.highBound = highBound;
    }

    /**
     * @return la borne inférieur de l'intervalle.
     */
    public double low() {
        return lowBound;
    }

    /**
     * @return la borne supérieur de l'intervalle.
     */
    public double high() {
        return highBound;
    }

    /**
     * @return la taille de l'intervalle.
     */
    public double size() {
        return highBound - lowBound;
    }

    /**
     * Teste si un valeur v appartient à l'intervalle.
     *
     * @param v un valeur quelconque.
     * @return vrai si la valeur appartient à l'intervalle et autrement faux.
     */
    public abstract boolean contains(double v);

    @Override
    public final boolean equals(Object otherInterval) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

}

