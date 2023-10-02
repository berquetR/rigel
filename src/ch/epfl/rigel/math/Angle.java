package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Regroupe des méhodes et des constantes permettant de travailler sur des angles représentés par des valeurs de type double.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */
public final class Angle {
    /**
     * Constantes représentant respectivement: TAU, un radian en heure, le nombre de secondes d'arcs par radian.
     */
    public final static double TAU = 2 * Math.PI;
    private final static double RAD_PER_HOUR = TAU / 24;
    private final static double SEC_PER_HOUR = (360 * 3600) / TAU;
    private final static double MIN_PER_DEG =3600;
    private final static double SEC_PER_MIN = 60;
    private final static RightOpenInterval INTERVAL_NORMALIZE= RightOpenInterval.of(0, TAU);
    private final static RightOpenInterval INTERVAL_DMS = RightOpenInterval.of(0, 60);


    /**
     * Classe non instanciable.
     */

    private Angle() {
    }

    /**
     * Normalise l'angle rad en le réduisant à l'intervalle [0,τ[.
     *
     * @param rad un angle quelconque en radian.
     * @return l'angle réduit à l'intervalle indiqué ci-dessous ou l'angle si celui appartient déjà à l'intervalle.
     */
    public static double normalizePositive(double rad) {
            return INTERVAL_NORMALIZE.reduce(rad);

        }


    /**
     * Retourne l'angle sec qui est en secondes d'arcs, en radian.
     *
     * @param sec un angle en secondes d'arc.
     * @return l'angle sec en radian.
     */
    public static double ofArcsec(double sec) {
        return sec / SEC_PER_HOUR;
    }

    /**
     * Retourne l'angle passé en argument qui est en deg, min et sec en radian.
     *
     * @param deg
     * @param min
     * @param sec ces trois paramètres définissent l'angle à convertir.
     * @return l'angle passé en argument en radian.
     */
    public static double ofDMS(int deg, int min, double sec) {
        Preconditions.checkArgument(deg>=0);

        Preconditions.checkArgument(INTERVAL_DMS.contains(min));
        Preconditions.checkArgument(INTERVAL_DMS.contains(sec));

        double X = deg * MIN_PER_DEG + min * SEC_PER_MIN + sec;

        return ofArcsec(X);
    }

    /**
     * Retourne l'angle deg en radian.
     *
     * @param deg, angle en degré.
     * @return l'angle en radian.
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Retourne l'angle rad en degré.
     *
     * @param rad, angle en radian.
     * @return l'angle rad en degré.
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);

    }

    /**
     * Retrourne l'angle hr en radian
     *
     * @param hr, angle en heure.
     * @return l'angle hr en radian.
     */
    public static double ofHr(double hr) {
        return hr * RAD_PER_HOUR;
    }

    /**
     * Retourne l'angle rad en heure.
     *
     * @param rad, angle en radian.
     * @return l'angle rad en heure.
     */
    public static double toHr(double rad) {
        return rad * 1 / RAD_PER_HOUR;
    }


}
