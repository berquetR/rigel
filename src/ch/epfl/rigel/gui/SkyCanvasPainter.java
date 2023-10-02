package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Transform;

import java.util.List;
import java.util.Set;

/**
 * Représente un "peintre du ciel", objet capable de dessiner le ciel sur un canevas.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class SkyCanvasPainter {

    /**
     * Attributs représentant le canevas, c'est à dire l'image en elle même et le contexte graphique permettant d'ajouter des éléments sur le canevas.
     */
    private final Canvas canvas;
    private final GraphicsContext ctx;
    private final Bounds canvasBounds;
    private final DateTimeBean dateTimeBean;

    public SkyCanvasPainter(Canvas canvas, DateTimeBean dateTimeBean) {
        this.canvas = canvas;
        this.dateTimeBean= dateTimeBean;
        ctx = canvas.getGraphicsContext2D();
        canvasBounds= canvas.getBoundsInLocal();
    }

    /**
     * Méthode permettant d'effacer le canevas, elle le re-initialise en affichant la couleur noir.
     */

    public void clear(ObservedSky observedSky, boolean dayNightMode) {

        if (dayNightMode) {
            ctx.setFill(skyColour(observedSky));
        }

        else {
            ctx.setFill(Color.BLACK);
        }
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    /**
     * Méthode permettant de dessiner les étoiles ainsi que les astérismes associés selon le ciel donné.
     *
     * @param sky        "photographie" à l'instant pour lequel on souhaite dessiner les étoiles et les astérismes, contenant les modèles des objets célèstes.
     * @param projection projection stéréographique
     * @param transform  transformation vers le canevas
     */


    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform transform) {

        //Création d'une liste contenant toutes les étoiles à dessiner puis on créé le tableau contenant leurs positions.
        List < Star > stars = sky.stars();

        // Création du tableau contenant les cordonnées des étoiles projetées dans le repère du canevas.
        double[] transformedStarCoordinates = tabTransformation(sky.starPositions(), transform);


        //Limites du canevas.
        Bounds canvasBounds = canvas.getBoundsInLocal();

        //Ensemble contenant tous les astérismes.
        Set < Asterism > asterismSet = sky.asterisms();


        /**
         * Dessin des astérismes.
         */

        for (Asterism asterism : asterismSet) {

            //List de l'ensemble des étoiles pour chaque astérisme.
            List < Star > asterismStars = asterism.stars();

            //List de l'ensemble des indice des étoiles de l'asterisme.
            List < Integer > indices = sky.asterismIndices(asterism);
            ctx.beginPath();

            int j = 0;

            //On définit ici le point (objet célèste de départ).
            double diameterOfStartingObject = diameterTransformationForOthers(transform, asterismStars.get(j), projection);
            Point2D startingPointCoordinatesForCanvas = new Point2D(transformedStarCoordinates[indices.get(j) * 2] - (diameterOfStartingObject / 2.0), transformedStarCoordinates[indices.get(j) * 2 + 1] - (diameterOfStartingObject / 2.0));

            while (j < asterismStars.size()) {

                j++;

                //On définit le point d'arrivée (objet célèste d'arrivée).
                double diameterOfArrivingObject = diameterTransformationForOthers(transform, asterismStars.get(j), projection);
                Point2D arrivingPointCoordinatesForCanvas = new Point2D(transformedStarCoordinates[indices.get(j) * 2] - (diameterOfArrivingObject / 2.0), transformedStarCoordinates[indices.get(j) * 2 + 1] - (diameterOfArrivingObject / 2.0));

                //Test si l'un des deux points se situent bien dans les limites du canvas.
                if (canvasBounds.contains(startingPointCoordinatesForCanvas) || canvasBounds.contains(arrivingPointCoordinatesForCanvas)) {

                    ctx.setStroke(Color.BLUE);
                    ctx.setLineWidth(1);

                    ctx.moveTo(startingPointCoordinatesForCanvas.getX(), startingPointCoordinatesForCanvas.getY());
                    ctx.lineTo(arrivingPointCoordinatesForCanvas.getX(), arrivingPointCoordinatesForCanvas.getY());

                    ctx.stroke();

                }
                //Le preccedent point d'arrivée devient le nouveau point de départ (du trait correspondant à l'astérisme).
                startingPointCoordinatesForCanvas = arrivingPointCoordinatesForCanvas;

                if (j == asterismStars.size() - 1) {
                    break;
                }

            }

        }

        /**
         * Dessin de l'ensemble des étoiles du ciel
         */

        int i = 0;

        //Itération sur toutes les étoiles et on obtient les coordonnées associées.
        for (Star star : stars) {
            double x = transformedStarCoordinates[i];
            i++;
            double y = transformedStarCoordinates[i];
            i++;

            double diameter = diameterTransformationForOthers(transform, star, projection);

            drawDisks(x, y, BlackBodyColor.colorForTemperature(star.colorTemperature()), diameter);


        }

    }

    /**
     * Méthode permettant de dessiner le soleil selon le ciel donné.
     *
     * @param sky        "photographie" à l'instant pour lequel on souhaite dessiner le ciel, contenant les modèles des objets célèstes.
     * @param projection projection stéréographique
     * @param transform  transformation vers le canevas
     */

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform transform) {

        Sun sun = sky.sun();

        // Coordonnées du soleil.
        double x = sky.sunPosition().x();
        double y = sky.sunPosition().y();

        //Calcul du diamètre des différents disques à dessiner.
        double diameterForWhiteDisk = diameterTransformationForSunMoon(transform, sun, projection);
        double diameterForYellowDisk = diameterForWhiteDisk + 2;
        double diameterForHaloDisk = diameterForWhiteDisk * 2.2;

        //Coordonnées sur le canevas du centre des différents disques à dessiner.
        Point2D coordinatesForWhiteDisk = coordinatesTransformation(x, y, transform, diameterForWhiteDisk);
        Point2D coordinatesForYellowDisk = coordinatesTransformation(x, y, transform, diameterForYellowDisk);
        Point2D coordinatesForHaloDisk = coordinatesTransformation(x, y, transform, diameterForHaloDisk);


        //Choix du disque à dessiner en premier selon leurs diamètres.

        if ((diameterForHaloDisk > diameterForYellowDisk)) {
            drawHaloDiskFirst(coordinatesForHaloDisk, diameterForHaloDisk, coordinatesForYellowDisk, diameterForYellowDisk);
        } else {
            drawYellowDiskFirst(coordinatesForHaloDisk, diameterForHaloDisk, coordinatesForYellowDisk, diameterForYellowDisk);
        }

        ctx.setFill(Color.WHITE);
        ctx.fillOval(coordinatesForWhiteDisk.getX(), coordinatesForWhiteDisk.getY(), diameterForWhiteDisk, diameterForWhiteDisk);

    }


    /**
     * Méthode permettant de dessiner les planètes selon le ciel donné.
     *
     * @param sky        "photographie" à l'instant pour lequel on souhaite dessiner le soleil, contenant les modèles des objets célèstes.
     * @param projection projection stéréographique
     * @param transform  transformation vers le canevas
     */

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform transform) {
        List < Planet > planets = sky.planets();

        double[] transformedPlanetCoordinates = tabTransformation(sky.planetsPositions(), transform);

        //Itération sur l'ensemble des planètes et on obtient les coordonnées asssociées.
        int i = 0;
        for (Planet planet : planets) {
            double x = transformedPlanetCoordinates[i];
            i++;
            double y = transformedPlanetCoordinates[i];
            i++;
            double diameter = diameterTransformationForOthers(transform, planet, projection);

            drawDisks(x, y, Color.LIGHTGRAY, diameter);

        }

    }


    /**
     * Méthode permettant de dessiner l'horizon selon le ciel donné.
     *
     * @param projection projection stéréographique
     * @param transform  transformation vers le canvas
     */

    public void drawHorizon(StereographicProjection projection, Transform transform) {

        //Dessin de la ligne d'horizon

        CartesianCoordinates horizonCenterCoordinates = projection.circleCenterForParallel(HorizontalCoordinates.of(0, 0));
        double horizonDiameter = projection.circleRadiusForParallel(HorizontalCoordinates.of(0, 0));


        Point2D projectedCoordinates = transform.transform(horizonCenterCoordinates.x(), horizonCenterCoordinates.y());
        Point2D projectedDiameter = transform.deltaTransform(horizonDiameter, 0);

        double diameter = Math.abs(projectedDiameter.getX() * 2.0);

        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.strokeOval(projectedCoordinates.getX() - (diameter / 2.0), projectedCoordinates.getY() - (diameter / 2.0), diameter, diameter);

        //Dessin des points cardinaux

        for (int i = 0; i < 8; i++) {

            CartesianCoordinates southCoordinates = projection.apply(HorizontalCoordinates.ofDeg(45 * i, -0.5));
            Point2D projectedSouthCoordinates = transform.transform(southCoordinates.x(), southCoordinates.y());
            ctx.setFill(Color.RED);
            ctx.setTextBaseline(VPos.TOP);
            ctx.fillText(HorizontalCoordinates.ofDeg(45 * i, 0).azOctantName("N", "E", "S", "O"), projectedSouthCoordinates.getX(), projectedSouthCoordinates.getY());
        }

    }

    /**
     * Méthode permettant de dessiner des ufos
     * @param sky
     * @param transform
     * @param dateTimeBean
     */

    public void drawUFOs (ObservedSky sky, Transform transform, DateTimeBean dateTimeBean) {
        List <UFO> ufos = sky.ufos();
        double[] transformedCoordinates = tabTransformation(sky.ufoPositions(), transform);
        int i =0;
        for (UFO ufo: ufos) {

            double x = transformedCoordinates[i];
            i++;
            double y = transformedCoordinates[i];
            i++;


            if (dateTimeBean.getZonedDateTime().isAfter(ufo.apparition()) && dateTimeBean.getZonedDateTime().isBefore(ufo.disparition())) {

                drawDisks(x, y, Color.GRAY.deriveColor(1, 1, 5.0, 1), 20);
                drawDisks(x, y, Color.VIOLET.deriveColor(1, 1, 1, 0.25), 35);
                drawDisks(x, y, Color.GREEN.deriveColor(3, 1, 3, 1), 10);
            }
        }
    }

    /**
     * Méthode permettant de dessiner la lune selon le ciel donné.
     *
     * @param sky        "photographie" à l'instant pour lequel on souhaite dessiner la lune, contenant les modèles des objets célèstes.
     * @param projection projection stéréographique
     * @param transform  transformation vers le canvas
     */

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform transform) {

        Moon moon = sky.moon();
        double diameter = diameterTransformationForSunMoon(transform, moon, projection);
        Point2D coordinatesForCanvas = coordinatesTransformation(sky.moonPosition().x(), sky.moonPosition().y(), transform, diameter);
        ctx.setFill(Color.WHITE);

        ctx.fillOval(coordinatesForCanvas.getX(), coordinatesForCanvas.getY(), diameter, diameter);


    }

    public void drawSatellites (ObservedSky sky, Transform transform) {
        List <ArtificialSatellite> satellites = sky.artificialSatellites();
        double[] transformedCoordinates = tabTransformation(sky.satellitePositions(), transform);

        int i = 0;
        for (ArtificialSatellite satellite : satellites) {
            double x = transformedCoordinates [i];
            i++;
            double y = transformedCoordinates [i];
            i++;
            drawDisks(x , y , Color.GOLD, 2);
            ctx.setFill(Color.GREEN);
            ctx.setFont(Font.font("Courier New", FontWeight.LIGHT, 12));
            ctx.fillText(satellite.getCountry(), x, y);


        }
    }


    /**
     * Méthodes permettant de dessiner tous les objets célestes en appelant toutes les méthodes de dessin situées ci dessus.
     *
     * @param sky        "photographie" à l'instant ,pour lequel on souhaite dessiner la lune , contenant les modèles des objets célèstes.
     * @param projection projection stéréographique
     * @param transform  transformation vers le canvas
     */

    public void drawAll(ObservedSky sky, StereographicProjection projection, Transform transform, List<ObjectsToDraw> objectsToDraw, boolean dayNightMode) {
        clear(sky, dayNightMode);

        // Dessin des objets célestes du plus éloigné au plus proche.
        for (ObjectsToDraw o : objectsToDraw) {
            switch (o) {
                case MOON :
                    drawMoon(sky, projection, transform);
                    break;
                case SUN :
                    drawSun(sky, projection, transform);
                    break;
                case PLANETS :
                    drawPlanets(sky, projection, transform);
                    break;
                case STARS :
                    drawStars(sky, projection, transform);
                    break;
                case SATELLITES:
                    drawSatellites(sky, transform);
                    break;
                case UFO :
                    drawUFOs(sky, transform, dateTimeBean);
                    break;
            }

        }

        //L'horizon et les points cardinaux et intercardinaux sont quant à eux dessinés en tout dernier, afin de ne jamais être masqués par quoi que ce soit.
        drawHorizon(projection, transform);
    }

    /**
     * Méthodes permettant d'appliquer des transformations sur des coordonées ou distance.
     */


    private Point2D coordinatesTransformation(double x, double y, Transform transform, double diameter) {

        Point2D projectedCoordinates = transform.transform(x, y);
        Point2D coordinatesForCanvas = new Point2D(projectedCoordinates.getX() - (diameter / 2.0), projectedCoordinates.getY() - (diameter / 2.0));
        return coordinatesForCanvas;

    }

    private double diameterTransformationForOthers(Transform transform, CelestialObject object, StereographicProjection projection) {

        Point2D projectedDiameter = transform.deltaTransform(object.magnitudeTransformation(object.magnitude(), projection), 0);
        double diameter = projectedDiameter.getX();

        return diameter;
    }

    private double diameterTransformationForSunMoon(Transform transform, CelestialObject object, StereographicProjection projection) {
        Point2D projectedDiameter = transform.deltaTransform(projection.applyToAngle(object.angularSize()), 0);
        double diameter = projectedDiameter.getX();

        return diameter;


    }

    private double[] tabTransformation(double[] celestialPositions, Transform transform) {
        double[] transformedCelestCoordinates = new double[celestialPositions.length];
        transform.transform2DPoints(celestialPositions, 0, transformedCelestCoordinates, 0, celestialPositions.length / 2);
        return transformedCelestCoordinates;


    }

    private Color skyColour (ObservedSky sky)  {
        HorizontalCoordinates sunHorizontalCoordinates = sky.equatorialToHorizontalConversion().apply(sky.eclipticToEquatorialConversion().apply(sky.sun().eclipticPos()));
        double alt = sunHorizontalCoordinates.altDeg();

        if (alt < 10)
            return Color.BLACK;


        if (10 <= alt && alt < 25 )
            return Color.MIDNIGHTBLUE;

        if (25 <= alt && alt < 40)
            return Color.MEDIUMBLUE;

        if (40<= alt && alt < 55)
            return Color.ROYALBLUE;

        if (alt <= 55 && alt < 70)
            return Color.DEEPSKYBLUE;

        if (alt <= 70 && alt < 120)
            return Color.SKYBLUE;

        if (alt <= 120 && alt < 135)
            return Color.DEEPSKYBLUE;

        if (135<= alt && alt < 150)
            return Color.ROYALBLUE;

        if (150 <= alt && alt < 165)
            return Color.MEDIUMBLUE;

        if  (165 <= alt && alt < 180)
            return Color.MIDNIGHTBLUE;

        else return Color.SKYBLUE;

    }


    /**
     * Méthodes permettant de dessiner différents objets dur le canvas.
     **/


    private void drawHaloDiskFirst(Point2D coordinatesForHaloDisk, double diameterForHaloDisk, Point2D coordinatesForYellowDisk, double diameterForYellowDisk) {
        ctx.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.25));
        ctx.fillOval(coordinatesForHaloDisk.getX(), coordinatesForHaloDisk.getY(), diameterForHaloDisk, diameterForHaloDisk);


        ctx.setFill(Color.YELLOW);
        ctx.fillOval(coordinatesForYellowDisk.getX(), coordinatesForYellowDisk.getY(), diameterForYellowDisk, diameterForYellowDisk);


    }

    private void drawYellowDiskFirst(Point2D coordinatesForHaloDisk, double diameterForHaloDisk, Point2D coordinatesForYellowDisk, double diameterForYellowDisk) {

        ctx.setFill(Color.YELLOW);
        ctx.fillOval(coordinatesForYellowDisk.getX(), coordinatesForYellowDisk.getY(), diameterForYellowDisk, diameterForYellowDisk);

        ctx.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.25));
        ctx.fillOval(coordinatesForHaloDisk.getX(), coordinatesForHaloDisk.getY(), diameterForHaloDisk, diameterForHaloDisk);


    }


    private void drawDisks(double x, double y, Color color, double diameter) {
        Point2D coordinatesForCanvas = new Point2D(x - (diameter / 2.0), y - (diameter / 2.0));
        ctx.setFill(color);
        ctx.fillOval(coordinatesForCanvas.getX(), coordinatesForCanvas.getY(), diameter, diameter);
    }

}


