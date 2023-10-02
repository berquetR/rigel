package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import static ch.epfl.rigel.math.Angle.TAU;

/**
 * Représente des coordonnées sphériques spécifiques, les cordonnées équatoriales.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    /**
     * Intervalles permettant la mise en place de vérification de variables.
     */

    private static final RightOpenInterval RA_INTERVAL = RightOpenInterval.of(0, TAU);
    private static final ClosedInterval DEC_INTERVAL = ClosedInterval.symmetric(TAU / 2);

    /**
     * Construit des coordonnées équatoriales.
     *
     * @param ra  ascension droite
     * @param dec declinaison
     */
    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
    }

    /**
     * Permet de construire des coordonnées équatoriales en dehors de la classe.
     *
     * @param ra  ascension droite
     * @param dec declinaison
     * @return des coordonnées équatoriales en fonction de ra et de dec.
     * @throws IllegalArgumentException ssi
     */
    public static EquatorialCoordinates of(double ra, double dec) {

        return new EquatorialCoordinates(Preconditions.checkInInterval(RA_INTERVAL, ra), Preconditions.checkInInterval(DEC_INTERVAL, dec));
    }


    /**
     * @return l'ascension droite.
     */
    public double ra() {
        return super.lon();
    }

    /**
     * @return l'ascension droite en degré.
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * @return l'ascension droite en heure.
     */
    public double raHr() {
        return Angle.toHr(super.lon());
    }

    /**
     * @return la déclinaison.
     */
    public double dec() {
        return super.lat();
    }

    /**
     * @return la déclinaison en degré.
     */
    public double decDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format("ra = %.4f h" + ", dec = %.4f °", raHr(), decDeg());

    }
}
