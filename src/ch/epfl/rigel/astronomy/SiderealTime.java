package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Permet de calculer le temps sidéral.
 *
 * @author: Romain Berquet(316122)
 */
public final class SiderealTime {

    private final static double NOMBRE_OF_MILLISECOND_PER_HOUR = 3.6e+6;
    private final static Polynomial POLYNOMIAL_S0 = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private final static Polynomial POLYNOMIAL_S1 = Polynomial.of(1.002737909, 0);

    /**
     * Classe non instanciable, cette classe est réservée uniquement à faire des calculs.
     */

    private SiderealTime() {
    }

    /**
     * Calcul le temps sidéral de Greenwich, en radians et compris dans l'intervalle [0,τ[,
     * pour le couple date/heure when.
     *
     * @param when couple date/heure
     * @return le temps sidéral de Greenwich pour le couple date/heure when.
     */

    public static double greenwich(ZonedDateTime when) {

        //Calcul du nombre de siècles séparant l'époque J2000 et début du jour de when, puis S0 (premier terme de la formule du calcul du temps sidéral).
        ZonedDateTime zone = when.withZoneSameInstant(ZoneOffset.UTC);
        double T = J2000.julianCenturiesUntil(zone.truncatedTo(DAYS));
        double S0 = POLYNOMIAL_S0.at(T);


        //Calcul du nombre d'heures séparant le début du jour contenant l'instant et l'instant lui-même, puis S1(second terme de la formule du calcul du temps sidéral).
        ZonedDateTime zone2 = zone.truncatedTo(DAYS);
        double nombreMillis = zone2.until(zone, ChronoUnit.MILLIS);
        double t = nombreMillis / NOMBRE_OF_MILLISECOND_PER_HOUR;
        double S1 = POLYNOMIAL_S1.at(t);

        return Angle.normalizePositive(Angle.ofHr(S0 + S1));
    }

    /**
     * Calcul le temps sidéral local, en radians et compris dns l'intervalle [0, τ[, pour le couple date/heure when et la position where.
     *
     * @param when  couple date/heure.
     * @param where position.
     * @return le temps sidéral local pour le couple date/heure when et la position where.
     */

    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(SiderealTime.greenwich(when) + where.lon());
    }
}
