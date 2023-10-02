package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clsse représentant un catalogue de ufos.
 */

public final class UFOCatalogue {

    private final List <UFO> Ufos;

    public UFOCatalogue(List < UFO > ufos) {
        Ufos = Collections.unmodifiableList(ufos);
    }

    public List < UFO > UFOs() {
        return Ufos;
    }

    public interface Loader {

        void load (InputStream inputStream, Builder builder) throws IOException;
    }

    public static class Builder {

        private List <UFO> UFOs;

        public Builder () {
            UFOs = new ArrayList <>();
        }

        public UFOCatalogue.Builder addSatellite (UFO ufo) {
            UFOs.add(ufo);
            return this;
        }

        public UFOCatalogue.Builder loadFrom (InputStream inputStream, UFOCatalogue.Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        public UFOCatalogue build () {
            return new UFOCatalogue(UFOs);
        }


    }

}

