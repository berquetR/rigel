package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Classe publique et finale qui est un bean JavaFX contenant l'instant d'observation c-à-d le triplet (date, heure, fuseau horaire) d'observation.
 * Version modifiable et observable de ZonedDateTime.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class DateTimeBean {

    /**
     * Trois propriétés du bean, dont les valeurs correspondent à la date, l'instant et le fuseau horaire de l'instant d'observation.
     */

    private final ObjectProperty < LocalDate > date;
    private final ObjectProperty < LocalTime > time;
    private final ObjectProperty < ZoneId > zone;

    /**
     * Construit un bean contenant l'instant d'observation en initialisant les valeurs des propriétés à null.
     */

    public DateTimeBean() {
        date = new SimpleObjectProperty <>();
        time = new SimpleObjectProperty <>();
        zone = new SimpleObjectProperty <>();
    }

    /**
     * Méthodes d'accès et de modification de la propriété date.
     */

    /**
     * @return la propriété date.
     */

    public ObjectProperty < LocalDate > dateProperty() {
        return date;
    }

    /**
     * @return la valeur de la propriété date.
     */

    public LocalDate getDate() {
        return date.getValue();
    }

    /**
     * Méthode permettant de modifier la valeur de la propiété date.
     *
     * @param date la nouvelle date
     */

    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }

    /**
     * Méthodes d'accès et de modification de la propriété time.
     */

    /**
     * @return la propriété time
     */

    public ObjectProperty < LocalTime > timeProperty() {
        return time;
    }

    /**
     * @return la valeur de la propriété time.
     */

    public LocalTime getTime() {
        return time.getValue();
    }

    /**
     * Méthode permettant de modifier la valeur de la propriété time.
     *
     * @param time la nouvelle heure.
     */

    public void setTime(LocalTime time) {
        this.time.setValue(time);
    }

    /**
     * Méthodes d'accès et de modification de l'attribut zone
     */

    /**
     * @return la propriété zone.
     */
    public ObjectProperty < ZoneId > zoneProperty() {
        return zone;
    }

    /**
     * @return la valeur de la propriété zone.
     */

    public ZoneId getZone() {
        return zone.getValue();
    }

    /**
     * Méthode permettant de modifier la valeur de la propriété zone.
     *
     * @param zone nouveau fuseau horaire
     */

    public void setZone(ZoneId zone) {
        this.zone.setValue(zone);
    }

    /**
     * Méthode retournant l'instant d'observation sous la forme d'une valeur de type ZonedDateTime.
     *
     * @return valeur de type ZonedDateTime.
     */

    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZone());
    }

    /**
     * Méthode qui modifie l'instant d'observation pour qu'il soit égal à la valeur de type ZonedDateTime qu'on lui passe en argument
     * Cette méthode modifie les propriétés en appelant les méthodes qui agissent dessus à savoir les setters
     *
     * @param instant l'instant d'observation
     */

    public void setZonedDateTime(ZonedDateTime instant) {
        setDate(instant.toLocalDate());
        setTime(instant.toLocalTime());
        setZone(instant.getZone());
    }

}
