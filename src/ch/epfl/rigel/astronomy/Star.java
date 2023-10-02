package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Représente une étoile.
 *
 * @author: Romain Berquet (316122)
 */
public final class Star extends CelestialObject {

    private final static ClosedInterval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);
    /**
     * Attributs représentants respectivement le numéro Hipparcos de l'étoile, l'index de couleur, et la couleur de l'étoile.
     */

    private final int hipparcosId;
    private final float colorIndex;

    /**
     * Construit un soleil.
     *
     * @param hipparcosId   numéro Hipparcos
     * @param name          nom
     * @param equatorialPos coordonnées equatoriales
     * @param magnitude     magnitude
     * @throws IllegalArgumentException si le numéro Hipparcos est négatif ou si l'indice de couleur n'est pas compris dans l'intervalle: [-0.5, 5.5].
     *                                  (Ou si les informations passées ne respectent pas les restrictions de la classe mère).
     * @throws NullPointerException     si le nom ou la position équatorial sont nulles (restrictions de la classe mère).
     */

    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {

        super(name, equatorialPos, 0, magnitude);

        Preconditions.checkArgument(hipparcosId >= 0);

        this.hipparcosId = hipparcosId;
        this.colorIndex = (float) Preconditions.checkInInterval(COLOR_INDEX_INTERVAL, colorIndex);
        int colorTempearture = colorTemperature();

    }


    /**
     * Retourne le numéro Hipparcos de l'étoile
     *
     * @return le numéro Hipparcos de l'étoile.
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * Retourne la température de couleur de l'étoile, en degrés Kelvin, arrondie par défaut (c-à-d à l'entier inférieur le plus proche).
     *
     * @returnla couleur de l'étoile.
     */
    public int colorTemperature() {
        double variable = 0.92 * colorIndex;
        return (int) ((4600 * ((1 / (variable + 1.7)) + (1 / (variable + 0.62)))));

    }
}
