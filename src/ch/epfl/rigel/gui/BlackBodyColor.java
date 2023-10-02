package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.MathematicalCalculus;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Classe permettant d'obtenir la couleur d'un corps noir étant donnée sa température.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class BlackBodyColor {

    /**
     * Intervalle correspondant à la plage des températures permises par le fichier.
     */

    private static final Map < Integer, String > COLORS_MAP = loadColors();
    private static final ClosedInterval TEMPERATURE_RANGE = ClosedInterval.of(1000, 40000);


    private BlackBodyColor() {
    }


    /**
     * Méthode permettant de déterminer la couleur d'un objet célèste à partie de sa température.
     *
     * @param kelvinTemp température de l'objet célèste
     * @return la couleur associée à l'objet célèste selon sa température sous la forme d'une instance de Color.
     */

    public static Color colorForTemperature(float kelvinTemp) {

        int roundToNearestHundredTemperature = MathematicalCalculus.roundToNearestHundred((float) Preconditions.checkInInterval(TEMPERATURE_RANGE, kelvinTemp));

        return Color.web(COLORS_MAP.get(roundToNearestHundredTemperature));


    }

    /**
     * Méthode permettant de charger les différentes couleurs associées au température dans une Map.
     */

    private static Map <Integer, String> loadColors() {

        Map < Integer, String > map = new HashMap <>();

        try (InputStream colorDataStream = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt");
             BufferedReader in = new BufferedReader(new InputStreamReader(colorDataStream, US_ASCII))) {

            String line;

            while ((line = in.readLine()) != null) {
                if ((line.charAt(0) != '#') && (line.substring(10, 15).equals("10deg"))) {
                    map.put(Integer.parseInt(line.substring(1, 6).trim()), line.substring(80, 87));
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return map;
    }
}
