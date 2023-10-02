package ch.epfl.rigel.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant le cataloge des villes qui vont être afficher à l'utilisateur pour choisir sa position d'observation.
 * Son unique attribut correspond à une map dont la clé correspond au nom de la ville et sa valeur la position géographique de la ville.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */
public class CityCatalogue {

    private final List <City>  cities;

    /**
     * Construction de la map.
     */

    public CityCatalogue (List <City> city ) {
      this.cities  = Collections.unmodifiableList(city);
    }

    /**
     *
     * @return la map contenant les villes.
     */
   public List <City> getCities () {
       return cities;
    }

    /**
     * Interface imbriquée représentant un charguer de catalogue de villes.
     */

    public interface Loader {

        void load (InputStream inputStream, Builder builder) throws IOException;
    }

    /**
     * Bâtisseur de catalogue de villes final statique et imbriqué dans la classe CityCatalogue.
     */

    public static class Builder {
        /**
         * List correspondant aux noms des viles et leur positions géographiques associées, les coordonnées à la position i de la liste coordinates correpond aux coordonnées de la ville dont le nom se situe à la position i dans la liste names;
         */
        private List <City> cities;

        /**
         *
         */

        public Builder () {
             cities = new ArrayList <>();
         }

        /**
         * Ajoute un nom à la liste des noms de ville du batisseur.
         * @param city
         * @return le batisseur.
         */

         public Builder addCity(City city) {
             cities.add(city);
             return this;
         }


         public Builder loadFrom (InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
         }

         public CityCatalogue build () {
            return new CityCatalogue(cities);
         }



    }


}
