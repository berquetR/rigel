package ch.epfl.rigel.astronomy;

import java.time.*;

import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Représente une époque astronomique.
 *
 * @author: Romain Berquet (316122)
 */

public enum Epoch {

    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(12, 0), ZoneOffset.UTC)),
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.of(0, 0), ZoneOffset.UTC));

    /**
     * attributs nécessaires aux différents calculs de la classe représentant le nombre de millisecondes par jour et par siècle julien.
     * l'attribut zonedDateTime représente les couples dates/heures pour les deux époques.
     */

    private static final double MILLI_SECONDS_PER_DAY = 8.64e+7;
    private static final double MILLI_SECONDS_PER_JULIAN_CENTURY = 3.15576e+12;
    private final ZonedDateTime zonedDateTime;

    /**
     * Consruit un membre du type énuméré avec une date  précise donnée.
     *
     * @param zonedDateTime
     */

    Epoch(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    /**
     * Retourne le nombre de jours entre l'époque à laquelle on l'applique et l'instant when.
     *
     * @param when
     * @return un double correspondant à un nombre de jours.
     */

    public double daysUntil(ZonedDateTime when) {
        return (zonedDateTime.until(when, MILLIS)) / MILLI_SECONDS_PER_DAY;
    }

    /**
     * Retourne le nombre de siècles juliens entre l'époque à laquelle on l'applique et when (assez similaire à la méthode ci-dessus).
     *
     * @param when
     * @return un double correspondant à un nombre de siècles juliens.
     */

    public double julianCenturiesUntil(ZonedDateTime when) {
        return zonedDateTime.until(when, MILLIS) / MILLI_SECONDS_PER_JULIAN_CENTURY;
    }
}