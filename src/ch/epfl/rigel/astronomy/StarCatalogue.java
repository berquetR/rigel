package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Classe publique, finale et immuable représentant un catalogue d'étoiles
 * Elle comporte deux attributs privés et finaux
 * - une List d'étoiles
 * - une table associative contenant les astérismes en clés et chaque valeur est une List d'entiers représentant
 * les indices des étoiles dans la List stars de l'astérisme en clé
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class StarCatalogue {

    private final List < Star > stars;
    private final Map < Asterism, List < Integer > > map = new HashMap <>();

    public StarCatalogue(List < Star > stars, List < Asterism > asterisms) {

        this.stars = List.copyOf(stars);
        Map < Star, Integer > starIntegerMap = new HashMap <>();
        for (Star star : stars) {
            starIntegerMap.put(star, stars.indexOf(star));
        }

        // Boucle remplissant la table associative
        // Si une étoile de l'astérisme a se trouve dans la List stars alors on ajoute son index à la List indices
        for (Asterism a : asterisms) {
            List < Integer > indices = new ArrayList <>();
            for (Star s : a.stars()) {
                if (!starIntegerMap.containsKey(s)) throw new IllegalArgumentException();
                indices.add(starIntegerMap.get(s));
            }
            map.put(a, indices);
        }
    }

    /**
     * @return la List des étoiles du catalogue
     */
    public List < Star > stars() {
        return List.copyOf(stars);
    }

    /**
     * @return une copie de l'ensemble des astérismes du catalogue
     */
    public Set < Asterism > asterisms() {

        return Set.copyOf(map.keySet());

    }


    /**
     * @param asterism l'astérisme dont on veut avoir la List d'indices
     * @return une copie de la liste d'indices de l'astérisme passé en argument
     * Lance une IllegalArgumentException si l'astérisme passé en argument n'est pas contenu dans la table associative
     */
    public List < Integer > asterismIndices(Asterism asterism) {
        if (!map.containsKey(asterism)) throw new IllegalArgumentException();

        return List.copyOf(map.get(asterism));
    }

    /**
     * Interface publique imbriquée représentant un chargeur de catalogue
     */
    public interface Loader {
        /**
         * Charge un catalogue à partir d'un flow de données, ces données sont stockés dans le bâtisseur passé en argument
         * Elle est redéfinie spécifiquement dans les chargeurs d'astérismes et de données HYG
         *
         * @param inputStream
         * @param builder
         * @throws IOException
         */
        void load(InputStream inputStream, Builder builder) throws IOException;
    }

    /**
     * Bâtisseur de catalogue d'étoiles final statique et imbriqué dans la classe StarCatalogue
     */
    public final static class Builder {

        private List < Star > s;
        private List < Asterism > a;

        public Builder() {
            s = new ArrayList <>();
            a = new ArrayList <>();
        }

        /**
         * Ajoute une étoile à la List d'étoiles du bâtisseur
         *
         * @param star
         * @return le bâtisseur juste après l'ajout de l'étoile passée en argument
         */
        public Builder addStar(Star star) {
            s.add(star);
            return this;
        }

        /**
         * @return une vue non modifiable de la List d'étoiles du bâtisseur
         */
        public List < Star > stars() {
            return Collections.unmodifiableList(s);
        }

        /**
         * Ajoute un astérisme à la List d'astérismes du bâtisseur
         *
         * @param asterism
         * @return le bâtisseur juste après l'ajout de l'astérisme passé en argument
         */
        public Builder addAsterism(Asterism asterism) {
            a.add(asterism);
            return this;
        }

        /**
         * @return une vue non modifiable de la List d'astérismes du bâtisseur
         */
        public List < Asterism > asterisms() {
            return Collections.unmodifiableList(a);
        }

        /**
         * Fait appel à la méthode load du chargeur passé en argument et charge le bâtisseur this
         *
         * @param inputStream
         * @param loader
         * @return le bâtisseur après chargement
         * @throws IOException
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        public StarCatalogue build() {
            return new StarCatalogue(s, a);
        }

    }
}