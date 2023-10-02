package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant le catalogue des satellites qui vont être affiché dans le ciel.
 *
 * @author Romain Berquet (316122)
 *  @author Victor Gergaud (302860)
 */

public class ArtificialSatelliteCatalogue {

    /**
     * Liste contenant l'ensemble des satellites artificiels à dessiner sur le ciel.
     */

    private final List <ArtificialSatellite> satelliteList;

    /**
     * Construit la liste des satellites.
     * @param satelliteList liste des satellites
     */

    public ArtificialSatelliteCatalogue(List < ArtificialSatellite > satelliteList) {
        this.satelliteList = Collections.unmodifiableList(satelliteList);
    }

    /**
     * Méthode d'accces de la liste de satellite.
     * @return satelliteList.
     */

    public List < ArtificialSatellite > satellites() {
        return satelliteList;
    }

    /**
     * Interface imbriquée représentant un chargeur de satellites.
     */

    public interface Loader {
        void load (InputStream inputStream, Builder builder) throws IOException;
    }

    /**
     * Classe imbriquée représentant un bâtisseur de liste de satellite artificiels.
     */

    public static class Builder {

        private List <ArtificialSatellite> satellites;

        public Builder () {
            satellites = new ArrayList <>();
        }

        public Builder addSatellite (ArtificialSatellite satellite) {
            satellites.add(satellite);
            return this;
        }

        public Builder loadFrom (InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        public ArtificialSatelliteCatalogue build () {
            return new ArtificialSatelliteCatalogue(satellites);
        }


    }


}
