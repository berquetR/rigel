package ch.epfl.rigel.astronomy;

        import ch.epfl.rigel.Preconditions;
        import ch.epfl.rigel.coordinates.EquatorialCoordinates;
        import ch.epfl.rigel.math.ClosedInterval;

        import java.util.Locale;

/**
 * Représente la Lune à un instant donné.
 *
 * @author: Romain Berquet(316122)
 * @author: Victor Gergaud (302860)
 */
public final class Moon extends CelestialObject {

    private final static ClosedInterval INTERVAL = ClosedInterval.of(0, 1);
    private final float phase;


    /**
     * Construit une Lune.
     *
     * @param equatorialPos postion en coordonnées équatoriales
     * @param angularSize   taille angulaire
     * @param magnitude     magnitude
     * @param phase         phase
     * @throws IllegalArgumentException si la phase n'est pas contenue dans l'intervall [0,1]
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {

        super("Lune", equatorialPos, angularSize, magnitude);

        this.phase = (float) Preconditions.checkInInterval(INTERVAL, phase);
    }


    @Override
    public String info() {
        double phase100 = 100.0 * phase;
        String string = String.format(Locale.ROOT, "%.1f", phase100);
        return super.info() + " (" + string + "%)";
    }

    public float phase() {
        return phase;
    }


}