package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.List;
import java.util.Optional;

/**
 * Classe représentant un gestionnaire de canevas sur lequel le ciel est dessiné.
 *
 * @author Romain Berquet (316122)
 * @author Victor Gergaud (302860)
 */

public final class SkyCanvasManager {

    /**
     * Valeurs à incrémenter aux coordonnées de la direction du regard selon la flèche du clavier pressée.
     */
    private static final int LOOK_LEFT = -10;
    private static final int LOOK_RIGHT = 10;
    private static final int LOOK_UP = 5;
    private static final int LOOK_DOWN = -5;


    /**
     * Intervalles dans lesquels les coordonnées de la direction du regard doivent être contenues.
     */
    private static final ClosedInterval VIEWING_PARAMETER_ALT_INTERVAL = ClosedInterval.of(5, 90);
    private static final RightOpenInterval VIEWING_PARAMETER_AZ_INTERVAL = RightOpenInterval.of(0, 360);
    private static final RightOpenInterval SCROLL_INTERVAL = RightOpenInterval.of(30, 150);
    /**
     * Attributs privés : la distance maximum entre l'objet céleste défini comme le plus proche du curseur de la souris et ce dernier.
     */
    private static final double MAXIMUM_DISTANCE = 10.0;
    /**
     * Liens publiques et finaux : l'azimut et la hauteur de la souris et l'objet céleste le plus proche de la souris.
     */
    public final DoubleBinding mouseAzDeg;
    public final DoubleBinding mouseAltDeg;
    public final ObjectBinding < CelestialObject > objectUnderMouse;
    /**
     * Liens privés : la projection stéréographique, la transformation permettant de passer du repère du ciel au repère du canvas, le ciel observé, les coordonnées horizontales de la souris.
     */
    private final ObjectBinding < StereographicProjection > projection;
    private final ObjectBinding < Affine > planeToCanvas;
    private final ObjectBinding < ObservedSky > observedSky;
    private final ObjectBinding < HorizontalCoordinates > mouseHorizontalPosition;
    private final SkyCanvasPainter skyCanvasPainter;
    private final Canvas canvas;
    /**
     * Propriété privée: coordonnées cartésiennes de la souris.
     */

    private final ObjectProperty < Point2D > mousePosition;

    /**
     * Construit un gestionnaire de canevas sur lequel le ciel est dessiné.
     *
     * @param catalogue             le catalogue d'étoiles et d'astérismes à dessiner
     * @param dateTimeBean          bean JavaFx contenant l'instant d'observation
     * @param observerLocationBean  bean JavaFx contenant la position de l'observateur
     * @param viewingParametersBean bean JavaFx contenant les paramètres déterminant la portion du ciel visible sur l'image
     */


    public SkyCanvasManager(StarCatalogue catalogue, UFOCatalogue ufoCatalogue, ArtificialSatelliteCatalogue satelliteCatalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean, List<ObjectsToDraw> objectsToDraw, boolean dayNightMode) {

        //Définition des attributs et de la propriété.
        canvas = new Canvas(800, 600);

        skyCanvasPainter = new SkyCanvasPainter(canvas, dateTimeBean);


        //Définition de la projection.
        projection = Bindings.createObjectBinding(() ->
                new StereographicProjection(viewingParametersBean.getCenter()), viewingParametersBean.centerProperty()
        );

        //Définition de la transformation en utilisant la formule liée au champ de vue et le facteur de dilatation.
        planeToCanvas = transformationDefinition(viewingParametersBean);

        //Définition de la position de la souris sur le canvas.
        mousePosition = new SimpleObjectProperty<>(new Point2D(0, 0));

        //Définition des coordonnées horizontales de la souris.
        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
                    try {
                        Point2D coordinates = planeToCanvas.getValue().inverseTransform(mousePosition.getValue().getX(), mousePosition.getValue().getY());
                        CartesianCoordinates mouseCartesianCoordinates = CartesianCoordinates.of(coordinates.getX(), coordinates.getY());
                        return projection.getValue().inverseApply(mouseCartesianCoordinates);

                    } catch (NonInvertibleTransformException e) {
                        return HorizontalCoordinates.of(0, 0);
                    }
                }, planeToCanvas, mousePosition, projection
        );


        mouseAzDeg = Bindings.createDoubleBinding(() ->
                mouseHorizontalPosition.getValue().azDeg(), mouseHorizontalPosition);

        mouseAltDeg = Bindings.createDoubleBinding(() ->
                mouseHorizontalPosition.getValue().altDeg(), mouseHorizontalPosition);

        //Définition du ciel observé.
        observedSky = Bindings.createObjectBinding(() ->
                new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.getValue(), catalogue, satelliteCatalogue, ufoCatalogue), dateTimeBean.dateProperty(), dateTimeBean.zoneProperty(), dateTimeBean.timeProperty(), observerLocationBean.latDegProperty(), observerLocationBean.lonDegProperty(), projection
        );


        //Définition de l'objet céleste situé sous la souris.
        objectUnderMouse = setObjectUnderMouseProperty(viewingParametersBean);


        //Gestion des évenements claviers et souris.
        canvas.setOnMousePressed(even1t -> canvas.requestFocus());
        mouseManager(viewingParametersBean);

        canvas.requestFocus();
        keyBoardManager(viewingParametersBean);


        // On ajoute les auditeurs permettant de dessiner le ciel à chaque modification de la projection, de la transformation et du ciel observé.

        projection.addListener(observable ->
                skyCanvasPainter.drawAll(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue(), objectsToDraw, dayNightMode)
        );
        planeToCanvas.addListener(observable ->
                skyCanvasPainter.drawAll(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue(), objectsToDraw, dayNightMode)
        );
        observedSky.addListener(observable ->
                skyCanvasPainter.drawAll(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue(), objectsToDraw, dayNightMode)
        );


    }

    /**
     * @return le canevas
     */

    public Canvas canvas() {
        return canvas;
    }


    // Méthode privée définissant une transformation, le but étant de clarifier et d'alleger le constructeur.
    private ObjectBinding < Affine > transformationDefinition(ViewingParametersBean viewingParametersBean) {

        return Bindings.createObjectBinding(() -> {
                    double fieldOfView = Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg().doubleValue());
                    double dilatationFactor = canvas.getWidth() / projection.getValue().applyToAngle(fieldOfView);
                    return Transform.affine(dilatationFactor, 0, 0, -dilatationFactor, canvas.getWidth() / 2, canvas.getHeight() / 2);

                }, projection, viewingParametersBean.fieldOfViewDegProperty(), canvas.widthProperty(), canvas.heightProperty()

        );
    }

    // Méthode privée mettant en place les auditeurs de la souris, le but étant de clarifier et d'alleger le constructeur.
    private void mouseManager(ViewingParametersBean viewingParametersBean) {

        canvas.setOnMouseMoved(event -> {
            mousePosition.setValue(new Point2D(event.getX(), event.getY()));
            event.consume();
        });

        canvas.setOnScroll(event -> {
            double biggest = Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY()) ? event.getDeltaX() : event.getDeltaY();
            viewingParametersBean.setFieldOfViewDeg(SCROLL_INTERVAL.reduce(viewingParametersBean.getFieldOfViewDeg().doubleValue() + biggest));
            event.consume();

        });
    }

    // Méthode privée mettant en place les auditeurs du clavier, le but étant de clarifier et d'alleger le constructeur.
    private void keyBoardManager(ViewingParametersBean viewingParametersBean) {
        canvas.requestFocus();
        canvas.setOnKeyPressed(event1 -> {
            HorizontalCoordinates currentCoordinates = viewingParametersBean.getCenter();
            switch (event1.getCode()) {

                case LEFT:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(VIEWING_PARAMETER_AZ_INTERVAL.reduce(currentCoordinates.azDeg() + LOOK_LEFT), currentCoordinates.altDeg()));
                    break;
                case RIGHT:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(VIEWING_PARAMETER_AZ_INTERVAL.reduce(currentCoordinates.azDeg() + LOOK_RIGHT), currentCoordinates.altDeg()));
                    break;
                case UP:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(currentCoordinates.azDeg(), VIEWING_PARAMETER_ALT_INTERVAL.clip(currentCoordinates.altDeg() + LOOK_UP)));
                    break;
                case DOWN:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(currentCoordinates.azDeg(), VIEWING_PARAMETER_ALT_INTERVAL.clip(currentCoordinates.altDeg() + LOOK_DOWN)));
                    break;
                default:
                    break;

            }

            event1.consume();
        });

    }


    // Méthode privée définissant le lien qui représente l'objet sous le pointeur de la souris, le but étant de clarifier et d'alleger le constructeur.
    private ObjectBinding < CelestialObject > setObjectUnderMouseProperty(ViewingParametersBean viewingParametersBean) {

        return Bindings.createObjectBinding(() -> {
            try {

                double distanceOnCanvas = MAXIMUM_DISTANCE / (projection.getValue().applyToAngle(Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg().doubleValue())));

                Point2D mouseTransformedCoordinates = planeToCanvas.getValue().inverseTransform(mousePosition.getValue().getX(), mousePosition.getValue().getY());

                CartesianCoordinates coordinates = CartesianCoordinates.of(mouseTransformedCoordinates.getX(), mouseTransformedCoordinates.getY());

                Optional < CelestialObject > optionalCelestialObject = observedSky.getValue().objectClosestTo(coordinates, planeToCanvas.getValue().inverseDeltaTransform(distanceOnCanvas, 0).getX());

                return optionalCelestialObject.orElse(null);
            } catch (NonInvertibleTransformException e) {
                return null;
            }


        }, mousePosition, observedSky, planeToCanvas);


    }

}

