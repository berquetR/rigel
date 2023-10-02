package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe publique et instanciable qui est un bean JavaFX contenant les paramètres déterminant la portion du ciel visible sur l'image.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class ViewingParametersBean {

    /**
     * Propriétés du bean correspondant au champs de vue et au centre de la projection stéréographique.
     */

    private final DoubleProperty fieldOfViewDeg;
    private final ObjectProperty < HorizontalCoordinates > center;

    /**
     * Construit le bean des paramètres d'observation en initialisant les valeurs des propriétés à null.
     */

    public ViewingParametersBean() {
        fieldOfViewDeg = new SimpleDoubleProperty();
        center = new SimpleObjectProperty <>();
    }

    /**
     * Méthodes d'accès et de modification de l'attribut fieldOfViewDeg
     */

    /**
     * @return la propriété fieldOfViewDeg
     */

    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * @return la valeur de la propriété FieldOfViewDeg
     */

    public Double getFieldOfViewDeg() {
        return fieldOfViewDeg.getValue();
    }

    /**
     * Méthode permettant de modifier la valeur de la propruete fieldOfViewDeg
     *
     * @param fieldOfViewDeg nouveau champs de vue
     */

    public void setFieldOfViewDeg(Double fieldOfViewDeg) {
        this.fieldOfViewDeg.setValue(fieldOfViewDeg);
    }

    /**
     * Méthodes d'accès et de modification de l'attribut center
     */

    /**
     * @return la propriété center
     */

    public ObjectProperty < HorizontalCoordinates > centerProperty() {
        return center;
    }

    /**
     * @return la valeur de la propriété center
     */

    public HorizontalCoordinates getCenter() {
        return center.getValue();
    }

    /**
     * Méthode permettant de modifier la valeur de la propriété center
     *
     * @param center nouveau center de projection
     */

    public void setCenter(HorizontalCoordinates center) {
        this.center.setValue(center);
    }
}
