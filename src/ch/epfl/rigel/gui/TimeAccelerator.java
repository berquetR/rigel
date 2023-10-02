package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Interface représentant un "accélérateur de temps", fonction permettant de calculer le temps simulé et généralement accéléré en fonction du temps réel.
 *
 * @author: Romain Berquet (316122)
 * @author: Victor Gergaud (302860)
 */

@FunctionalInterface
public interface TimeAccelerator {

    /**
     * Méthode définissant un accélerateur de temps de type continue.
     *
     * @param alpha facteur d'accélération du temps
     * @return un accélérateur de temps.
     */
    static TimeAccelerator continuous(int alpha) {
        return ((simulatedInitialTime, spentTime) -> {
            ZonedDateTime simulatedActualTime = simulatedInitialTime.plusNanos(spentTime * alpha);
            return simulatedActualTime;
        }
        );


    }

    /**
     * Méthode définissant un accélerateur de temps de type discret.
     *
     * @param frequency fréquence d'avanement
     * @param step      pas
     * @return un accélérateur de temps.
     */
    static TimeAccelerator discrete(int frequency, Duration step) {
        return ((simulatedInitialTime, spentTime) -> {
            ZonedDateTime simulatedActualTime = simulatedInitialTime.plusNanos((long) ((Math.floor(frequency * 1e-9 * spentTime) * step.toNanos())));
            return simulatedActualTime;
        });
    }

    /**
     * Méthode définissant un accélerateur de temps.
     *
     * @param simulatedInitialTime temps simulé initital, au début de la simulation
     * @param spentTime            durée écoulée qui correspond à la différence entre le temps réel actuel et le temps réel initiale.
     * @return l'instant précis simulé.
     */

    ZonedDateTime adjust(ZonedDateTime simulatedInitialTime, long spentTime);
}