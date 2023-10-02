package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;

/**
 * Classe représentant un ufo.
 */

public final class UFO  {

    private final ZonedDateTime apparitionDateTime;
    private final ZonedDateTime disparitionTime;
    private final double durationInSeconds;
    private final String infos;
    private final EquatorialCoordinates coordinates;

    public UFO(String infos, ZonedDateTime apparitionDateTime, double durationInSeconds, double lon,  double lat) {
        this.infos = infos;
        this.apparitionDateTime = apparitionDateTime;
        this.durationInSeconds = durationInSeconds;
        disparitionTime = apparitionDateTime.plusSeconds((long) durationInSeconds);
        coordinates = EquatorialCoordinates.of(Angle.ofDeg(lon), Angle.ofDeg(lat));
    }

    public EquatorialCoordinates coordinates () {
        return coordinates;
    }

    public ZonedDateTime apparition () {
        return apparitionDateTime;
    }

    public ZonedDateTime disparition () {
        return disparitionTime;
    }

    public double duration () {
        return  durationInSeconds;
    }

    @Override
    public String toString() {
        return infos + ", " + apparitionDateTime.toString();
    }
}
