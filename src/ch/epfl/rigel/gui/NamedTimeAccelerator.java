package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Type énuméré représentant un accélerateur de temps nommé, pair nom de l'accelérateur et accelérateur.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public enum NamedTimeAccelerator {

    /**
     * Six membres disponibles, 4 continues et 2 discrets.
     */


    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000×", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(60, Duration.ofSeconds(86164)));

    /**
     * Caractéristiques de chaque accelérateur: le nom et l'accelérateur.
     */

    private final String name;
    private final TimeAccelerator accelerator;


    /**
     * Construit un type énuméré en fonction des caractéristiques
     *
     * @param name        nom de l'accelérateur
     * @param accelerator type d'accelérateur
     */

    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * @return le nom de l'acelérateur
     */

    public String getName() {
        return name;
    }

    /**
     * @return le type de l'accelérateur et sa valeur
     */

    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    @Override
    public String toString() {
        return getName();
    }
}
