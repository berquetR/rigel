package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Classe publique et instanciable qui est un bean JavaFX contenant la position de l'observateur en degrés
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class ObserverLocationBean {

    /**
     * Propriétés du bean dont les valeurs correspondent à la longitude et la latitude de la position de l'observateur en degrés.
     */

    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;

    /**
     * Liens du bean correspondant aux coordonnées horizontales de l'observateur.
     */

    private final ObjectBinding<GeographicCoordinates> coordinates;

    /**
     * Construit la position de l'observeur en initialisant les valeurs des propriétés et du lien à nulle.
     */

    public ObserverLocationBean() {
        lonDeg = new SimpleDoubleProperty();
        latDeg = new SimpleDoubleProperty();
        coordinates = Bindings.createObjectBinding(() -> GeographicCoordinates.ofDeg(lonDeg.getValue(), latDeg.getValue()), lonDeg, latDeg);
    }

    /**
     *
     * @return la propriété lonDeg.
     */

    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     *
     * @return la valeur de la propriété lonDeg.
     */

    public double getLonDeg() {
        return lonDeg.getValue();
    }

    /**
     * Méthode permettant de modifier la valeur de la propriété lonDeg.
     * @param lonDeg nouvelle valeur de lonDeg
     */

    public void setLonDeg(double lonDeg) {
        this.lonDeg.setValue(lonDeg);
    }

    /**
     *
     * @return la propriété latDeg.
     */
    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     *
     * @return la valeur de la propriété latDeg.
     */

    public double getLatDeg() {
        return latDeg.getValue();
    }

    /**
     * Méthode permettant de modifier l valeur de la propriété latDeg.
     * @param latDeg nouvelle valeur de latDeg
     */

    public void setLatDeg(double latDeg) {
        this.latDeg.setValue(latDeg);
    }

    /**
     *
     * @return la propriété coordinates.
     */

    public ObjectBinding<GeographicCoordinates> coordinatesBinding() {
        return coordinates;
    }

    /**
     *
     * @return la valeur de la propriété coordinates.
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.getValue();
    }

}
