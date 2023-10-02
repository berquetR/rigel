package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import static ch.epfl.rigel.math.Angle.TAU;
import static java.lang.Math.*;

/**
 * Représente des coordonnées sphériques spécifiques, les coordonnées horizontales.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class HorizontalCoordinates extends SphericalCoordinates {

    /**
     * Intervalles permettant la mise en place de vérification de variables.
     */

    private static RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0, TAU);
    private static ClosedInterval ALT_INTERVAL = ClosedInterval.symmetric(TAU / 2);
    /**
     * Attributs représentant le cosinus et le sinus de l'altitude.
     */
    private final double altitudeCos = cos(alt());
    private final double altitudeSin = sin(alt());

    /**
     * Construit des coordonnées horizontales.
     *
     * @param az
     * @param alt
     */

    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     * Méthodes permettant de construire des coordonnées horizontales en dehors de la classe.
     *
     * @param az  azimut.
     * @param alt altitude.
     * @return des coordonnées horizontales en fonctiond de az et alt.
     * @throws IllegalArgumentException ssi az n'appartient pas à l'intervalle  [0°, 360°[ ou si alt n'appartient pas à [–90°, +90°].
     */
    public static HorizontalCoordinates of(double az, double alt) {
        return new HorizontalCoordinates(Preconditions.checkInInterval(AZ_INTERVAL, az), Preconditions.checkInInterval(ALT_INTERVAL, alt));

    }

    /**
     * Même comportment que la méthode ci-dessus mais dans le cas où l'azimut et la l'altitude sont en degré.
     *
     * @param azDeg  azimut  en degré
     * @param altDeg altitude en degré
     * @return des coordonnées horizontales en fonctiond de az et alt.
     */

    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        double azRad = Angle.ofDeg(azDeg);
        double altRad = Angle.ofDeg(altDeg);

        return HorizontalCoordinates.of(azRad, altRad);
    }


    /**
     * @return azimut
     */
    public double az() {
        return super.lon();
    }

    /**
     * azimut en degré.
     *
     * @return
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * Associe l'octant dans lequel se situe l'azimut à une châines correspondant aux quatres points cardinaux.
     *
     * @param n
     * @param e
     * @param s
     * @param w
     * @return une chaîne correspondant à l'octant dans lequel se trouve l'azimut du récepteur, chaîne formée en combinant les chaînes n, e, s et w correspondant aux quatre points cardinaux.
     */
    public String azOctantName(String n, String e, String s, String w) {
        switch ((int) (Math.round(az() * 8 / TAU))) {
            case 1:
                return n + e;
            case 2:
                return e;
            case 3:
                return s + e;
            case 4:
                return s;
            case 5:
                return s + w;
            case 6:
                return w;
            case 7:
                return n + w;
            case 8:
            case 0:
                return n;
            default:
                throw new IllegalStateException();
        }

    }

    /**
     * @return altitude.
     */
    public double alt() {
        return super.lat();
    }

    /**
     * @return altitude en degré.
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * Calcul la distance angulaire entre le récepeteur this et le point donné en argument that.
     *
     * @param that coordonnées quelconques.
     * @return la distance angualaire en this et that.
     */

    public double angularDistanceTo(HorizontalCoordinates that) {
        return acos(altitudeSin * sin(that.alt()) + altitudeCos * cos(that.alt()) * cos(az() - that.az()));
    }

    @Override
    public String toString() {
        return String.format("az= %.4f °" + ", alt= %.4f °", azDeg(), altDeg());

    }


}
