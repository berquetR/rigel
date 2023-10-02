package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Type énuméré public et immuable représentant un chargeur d'astérismes dans un catalogue
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public enum AsterismLoader implements StarCatalogue.Loader {

    INSTANCE;

    /**
     * Redéfintion de la méthode abstraite load de l'interface StarCatalogue.Loader
     *
     * @param inputStream Source de données (ici les astérismes seront passés en argument)
     * @param builder
     * @throws IOException
     */

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {

            String line;

            Map < Integer, Star > idStarMap = new HashMap <>();
            for (Star star : builder.stars()) {
                idStarMap.put(star.hipparcosId(), star);
            }

            while ((line = in.readLine()) != null) {
                String[] splittedLine = line.split(",");

                List < Star > asterismStars = new ArrayList <>();

                // Cette boucle for ajoute les étoiles déjà contenues dans le builder à la List définie au dessus si elles appartiennent à un asterisme
                // Pour contrôler cette éventuelle appartenance, on regarde si l'Hipparcos ID provenant de l'input stream est une key de la map définie plus haut
                // Cette List sert ensuite à la construction d'un astérisme (après la boucle)

                for (String s : splittedLine) {
                    if (idStarMap.containsKey(Integer.parseInt(s)))
                        asterismStars.add(idStarMap.get(Integer.parseInt(s)));
                }
                builder.addAsterism(new Asterism(asterismStars));
            }
        }
    }
}