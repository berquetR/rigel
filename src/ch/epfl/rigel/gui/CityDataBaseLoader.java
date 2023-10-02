package ch.epfl.rigel.gui;

import java.io.*;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Classse représentant un chargeur de fichier pour les villes.
 */

public enum CityDataBaseLoader implements CityCatalogue.Loader {
    INSTANCE;

    private final static int NAME = 1;
    private final static int LON = 3;
    private final static int LAT = 2;
    private static final int COUNTRY = 6;


    @Override
    public void load(InputStream inputStream, CityCatalogue.Builder builder) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            String line;
            String[] splittedLine;
            String name;
            double lon;
            double lat;




            in.readLine();

            while ((line = in.readLine()) != null) {

                splittedLine = line.split(",");
                if(splittedLine[NAME] == "" || splittedLine[NAME].isEmpty())
                    name = "?";
                else

                    name =splittedLine[NAME] + ", " + splittedLine [COUNTRY];

                if (splittedLine[LAT]== "" || splittedLine[LAT].isEmpty())
                    lat = 0.0;
                else
                lat = Double.parseDouble(splittedLine [LAT]);

                if (splittedLine[LON]=="" || splittedLine[LON].isEmpty())
                    lon=0.0;
                else
                lon = Double.parseDouble(splittedLine[LON]);

                builder.addCity(new City(name, lat, lon));

            }

        }
    }
}
