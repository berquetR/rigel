package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.Angle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Enum représentant un chargeur de catalogue contenant les satellites artificiels.
 */

public enum ArtificialSatelliteDatabaseLoader implements ArtificialSatelliteCatalogue.Loader {

    INSTANCE;


    @Override
    public void load(InputStream inputStream, ArtificialSatelliteCatalogue.Builder builder) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            String line;
            String[] splittedLine;
            String country;
            double lon;

            in.readLine();


            while ((line = in.readLine()) != null) {

                splittedLine = line.split(",");


                if (splittedLine[7].equals("GEO") && !((splittedLine[9]).isEmpty())) {

                    lon = Math.abs(Double.parseDouble(splittedLine[9]));
                    country = splittedLine[1];
                    builder.addSatellite(new ArtificialSatellite(country, Angle.ofDeg(lon)));
                }

                if (splittedLine[8].equals("GEO") && !((splittedLine[10]).isEmpty())) {

                    lon = Math.abs(Double.parseDouble(splittedLine[10]));
                    country = splittedLine[1];
                    builder.addSatellite(new ArtificialSatellite(country, Angle.ofDeg(lon)));
                }

                if (splittedLine[9].equals("GEO") && !((splittedLine[11]).isEmpty())) {

                    lon = Math.abs(Double.parseDouble(splittedLine[11]));
                    country = splittedLine[1];
                    builder.addSatellite(new ArtificialSatellite(country, Angle.ofDeg(lon)));
                }
                if (splittedLine[10].equals("GEO") && !((splittedLine[12]).isEmpty())) {

                    lon = Math.abs(Double.parseDouble(splittedLine[12]));
                    country = splittedLine[1];
                    builder.addSatellite(new ArtificialSatellite(country, Angle.ofDeg(lon)));
                }


            }


        }

    }
}

