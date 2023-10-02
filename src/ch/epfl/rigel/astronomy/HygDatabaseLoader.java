package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Type énuméré public et immuable représentant un chargeur de données HYG dans un catalogue.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public enum HygDatabaseLoader implements StarCatalogue.Loader {



    INSTANCE;

    /**
     * Définition de constantes correspondants aux numéros de colonne (en commençant de 0, on a donc soustrait 1 aux indices de l'énoncé) des données
     * dans le catalogue HYG.
     */

    private final static int HIP = 1;
    private final static int PROPER = 6;
    private final static int MAG = 13;
    private final static int CI = 16;
    private final static int RARAD = 23;
    private final static int DECRAD = 24;
    private final static int BAYER = 27;
    private final static int CON = 29;

    /**
     * Redéfintion de la méthode abstraite load de l'interface StarCatalogue.Loader.
     * @param inputStream Source de données (ici les astérismes seront passés en argument)
     * @param builder
     * @throws IOException
     */

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            String line;
            String[] splittedLine;
            int hipparcosId;
            String proper;
            String bayer;
            double rarad;
            double decrad;
            float mag;
            float ci;

            // Lecture de la première ligne dont on ne veut pas récupérer les données (elles contiennent les titres des colonnes)
            in.readLine();

            while ((line = in.readLine()) != null) {

                // On découpe chaque ligne venant de inputStream par colonne
                splittedLine = line.split(",", 37);

                // On accède à l'Hypparcos ID dans le tableau de String splittedLine
                // On l'initialise à 0 par défaut s'il n'est pas présent dans les données
                if (splittedLine[HIP].isEmpty()) hipparcosId = 0;
                else hipparcosId = Integer.parseInt(splittedLine[HIP]);

                // Même chose ici pour le nom propre
                // La valeur par défaut est différente
                proper = splittedLine[PROPER];
                if (splittedLine[PROPER].isEmpty()) {
                    bayer = splittedLine[BAYER];
                    if (splittedLine[BAYER].isEmpty()) bayer = "?";
                    proper = bayer + " " + splittedLine[CON];
                }

                // On accède à l'ascension droite et à la déclinaison
                // Pas besoin de définir un cas par défaut car elles sont toujours données
                rarad = Double.parseDouble(splittedLine[RARAD]);
                decrad = Double.parseDouble(splittedLine[DECRAD]);

                // On accède à la magnitude dans le tableau de String splittedLine
                // On l'initialise à 0 par défaut s'il n'est pas présent dans les données
                if (splittedLine[MAG].isEmpty()) mag = 0;
                else mag = (float) Double.parseDouble(splittedLine[MAG]);

                // On accède à l'indice de couleur dans le tableau de String splittedLine
                // On l'initialise à 0 par défaut s'il n'est pas présent dans les données
                if (splittedLine[CI].isEmpty()) ci = 0;
                else ci = (float) Double.parseDouble(splittedLine[CI]);

                // Ces données sont enfin utilisées pour ajouter une nouvelle étoile au builder

                builder.addStar(new Star(hipparcosId, proper, EquatorialCoordinates.of(rarad, decrad), mag, ci));
            }
        }
    }

}