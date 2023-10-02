package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AsterismLoaderTest {
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private static final String ASTERISM_FILE_NAME = "/asterisms.txt";

    @Test
    void loadCorrectly() throws IOException {

        StarCatalogue.Builder catalogueBuilder;
        StarCatalogue catalogue;
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            catalogueBuilder = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);

            try (InputStream asterismStream = getClass().getResourceAsStream(ASTERISM_FILE_NAME)) {
                catalogue = catalogueBuilder.loadFrom(asterismStream, AsterismLoader.INSTANCE).build();
            }

            Queue<Asterism> a = new ArrayDeque<>();
            Star beltegeuse = null;
            for (Asterism ast : catalogue.asterisms()) {
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Rigel")) {
                        a.add(ast);
                    }
                }
            }
            int astCount = 0;
            for (Asterism ast : a) {
                ++astCount;
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Betelgeuse")) {
                        beltegeuse = s;
                    }
                }
            }
            assertNotNull(beltegeuse);
            assertEquals(2, astCount);
        }
    }
}
