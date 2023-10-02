package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.*;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Enum représentant un chargeur de catalogue content les ufos.
 */

public enum  UFODataBaseLoader implements UFOCatalogue.Loader {
    INSTANCE;


    @Override
    public void load(InputStream inputStream, UFOCatalogue.Builder builder) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {

            String country;
            String city;
            double secondDuration;
            ZonedDateTime zonedDateTime;
            String[] splittedLine;
            String line;
            String [] wholeDate;
            String [] yearAndHour;
            String [] hourTab;
            int year;
            int month;
            int day;
            int hour;
            int minute;
            double lon;
            double lat;

            in.readLine();


            while ((line=in.readLine())!= null) {

                splittedLine = line.split(",");

                //Split le jour le mois et l'année séparées par / l'année est stockée avec l'heure.
                wholeDate = splittedLine [0].split("/");

                //Le mois et le jour correspondent aux deux premières du tableau.
                month = Integer.parseInt(wholeDate[0]);
                day = Integer.parseInt(wholeDate[1]);

                //Split la date qui est séparée de l'heure avec un espace.
                yearAndHour = wholeDate[2].split(" ");
                year = Integer.parseInt(yearAndHour[0]);

                //Split les heures et les minutes séparées par : .
                hourTab= yearAndHour[1].split(":");

                if ( (Integer.parseInt(hourTab[0])) != 24)
                hour = Integer.parseInt(hourTab[0]);
                else
                    hour = 0;

                minute = Integer.parseInt(hourTab[1]);

                country = splittedLine[3];

                city = splittedLine[1] + ", " + country;

                secondDuration = Double.parseDouble(splittedLine[5]);

                lon = Double.parseDouble(splittedLine[10]);

                lat = Double.parseDouble(splittedLine[9]);



                zonedDateTime =ZonedDateTime.of(LocalDate.of(year, Month.of(month), day), LocalTime.of(hour, minute), ZoneOffset.UTC);

                if (!(splittedLine[4].isEmpty() && splittedLine [10].isEmpty() && splittedLine [9].isEmpty() ))

                builder.addSatellite(new UFO(city, zonedDateTime, secondDuration, Math.abs(lon), lat));

            }

        }
    }
}
