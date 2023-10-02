package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

/**
 * Classe représentant un "animateur de temps" dont le but est de modifier périodiquement via un accélerateur de temps.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean bean;
    private final SimpleBooleanProperty running;
    private final ObjectProperty < TimeAccelerator > accelerator;
    private boolean firstCall;
    private long firstNow;
    private ZonedDateTime simulatedInitialTime;

    /**
     * Construit un animateur de temps.
     *
     * @param bean le bean correspondant à l'instant d'observation.
     */

    public TimeAnimator(DateTimeBean bean) {
        this.bean = bean;
        accelerator = new SimpleObjectProperty <>();
        firstCall = false;
        running = new SimpleBooleanProperty(false);
        simulatedInitialTime = bean.getZonedDateTime();
        firstNow = 0;
    }

    /**
     * @return la propriété running.
     */

    public ReadOnlyBooleanProperty getRunState() {
        return running;
    }


    /**
     * Redéfinition des méthodes de Application.
     */
    @Override
    public void handle(long now) {

        //Premier appel de handle
        if (firstCall) {
            firstCall = false;

            //Initialise le moment ou la méthode est appelée pour la première fois.
            firstNow = now;
            simulatedInitialTime = bean.getZonedDateTime();
        }

        bean.setZonedDateTime(accelerator.getValue().adjust(simulatedInitialTime, now - firstNow));
    }


    @Override
    public void start() {
        running.setValue(Boolean.TRUE);
        firstCall = true;
        super.start();
    }

    @Override
    public void stop() {
        running.setValue(false);
        super.stop();
    }

    /**
     * @return la propriété accelerator
     */

    public ObjectProperty < TimeAccelerator > acceleratorProperty() {
        return accelerator;
    }

    /**
     * @return la valeur de la propriété accelerator.
     */

    public TimeAccelerator getAccelerator() {
        return accelerator.getValue();
    }

    /**
     * Méthode permettant de modifier la valeur de la propriété.
     *
     * @param accelerator nouveau accelerator
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.setValue(accelerator);
    }
}
