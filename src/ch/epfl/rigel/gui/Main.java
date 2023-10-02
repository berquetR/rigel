package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Classe représentant le programme principal
 *
 * @author Romain Berquet
 * @author Victor Gergaud
 */
public final class Main extends Application {

    /**
     * Constantes correspondantes aux styles des boutons permettant l'acceleration, au numberStringConverter et aux styles des paneaux, separateurs.
     */

    private static final String RESET_IDENTIFIER = "\uf0e2";
    private static final String PLAY_IDENTIFIER = "\uf04b";
    private static final String PAUSE_IDENTIFIER = "\uf04c";
    private static final String HOME_IDENTIFIER = "\uf015";
    private static final NumberStringConverter STRING_CONVERTER = new NumberStringConverter("#0.00");
    private static final String STYLE_1 = "-fx-padding: 4;\n" + "-fx-background-color: white;";
    private static final String STYLE_2 = "-fx-pref-width: 60;-fx-alignment: baseline-right;";
    private static final String STYLE_3 = "-fx-spacing: inherit; -fx-alignment: baseline-left;";
    private static final String STYLE_4 = "-fx-pref-width: 120;";
    private static final String STYLE_5 = "-fx-pref-width: 75;-fx-alignment: baseline-right;";
    private static final String STYLE_6 = "-fx-pref-width: 180;";
    private static final String STYLE_7 = "-fx-spacing: inherit;";
    private static final String STYLE_8 = "-fx-spacing: 4; -fx-padding: 4;";
    private static final Separator SEPARATOR = new Separator(Orientation.VERTICAL);
    private static final Separator SEPARATOR_1 = new Separator(Orientation.VERTICAL);
   

    public static void main(String[] args)

    {

                launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }


    public void startRigel(Stage Rigel, CityCatalogue cityCatalogue, City startingPoint, List<ObjectsToDraw> objectsToDraw, UFOCatalogue ufoCatalogue, boolean dayNightMode) throws Exception {

        //Chargement des différents catalogues contenant les astérismes et les étoiles.
        try (InputStream hs = resourceStream("/hygdata_v3.csv");
             InputStream ast = resourceStream("/asterisms.txt");
             InputStream sat = resourceStream("/datasets_152510_351347_UCS_Satellite_Database_12-1-2018-1.csv");) {

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(ast, AsterismLoader.INSTANCE)
                    .build();
            ArtificialSatelliteCatalogue satelliteCatalogue = new ArtificialSatelliteCatalogue.Builder().loadFrom(sat,ArtificialSatelliteDatabaseLoader.INSTANCE).build();



            //Initialisation de l'instant d'observation au moment actuel.
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());

            //Initialisation de la position de l'observateur au début de la simulation sur le campus de l'EPFL.
            ObserverLocationBean observerLocationBean =
                    new ObserverLocationBean();
            observerLocationBean.setLatDeg(46.52);
            observerLocationBean.setLonDeg(6.57);

            //Initialisation des paramètres de vue au début de la simulation.
            ViewingParametersBean viewingParametersBean =
                    new ViewingParametersBean();
            viewingParametersBean.setCenter(
                    HorizontalCoordinates.ofDeg(180.000000000001, 15.0));
            viewingParametersBean.setFieldOfViewDeg(100.0);

            //Création du gestionnaire de canevas.
            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    ufoCatalogue,
                    satelliteCatalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean,
                    objectsToDraw,
                    dayNightMode);

            //Animateur du temps, va permettre de simuler le temps plus rapidement.
            TimeAnimator animator = new TimeAnimator(dateTimeBean);


            //Partie centrale du panneau général. Cette partie contient le canevas sur lequel on dessine le ciel.
            Canvas sky = canvasManager.canvas();
            Pane canvas = new Pane(sky);
            sky.widthProperty().bind(canvas.widthProperty());
            sky.heightProperty().bind(canvas.heightProperty());


            //Parties inférieures et supérieures du panneau général correspondant à la barre d'information et la barre de contrôle.
            BorderPane informationBar = informationBarSetUp(canvasManager, viewingParametersBean, objectsToDraw);
            HBox controlBar = controlBarSetUp(observerLocationBean, dateTimeBean, animator, cityCatalogue, ufoCatalogue, Rigel, startingPoint, objectsToDraw);


            BorderPane root = new BorderPane();


            root.setBottom(informationBar);
            root.setTop(controlBar);
            root.setCenter(canvas);


            Rigel.setMinWidth(800);
            Rigel.setMinHeight(600);


            Rigel.setScene(new Scene(root));

            Rigel.show();

            sky.requestFocus();


        }
    }

    @Override
    public void start(Stage welcomeMenu) {

        CityCatalogue cityCatalogue;
        UFOCatalogue ufoCatalogue;
        try (InputStream cities = resourceStream("/worldcities.csv");
             InputStream ufo = resourceStream("/scrubbed 2.csv")
        ) {
            cityCatalogue = new CityCatalogue.Builder().loadFrom(cities, CityDataBaseLoader.INSTANCE).build();
            ufoCatalogue = new UFOCatalogue.Builder().loadFrom(ufo, UFODataBaseLoader.INSTANCE).build();
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        // MENU ACCUEIL
        // Description
        Label welcomeDescription = new Label("Le ciel\n" +
                "comme vous ne\n" +
                "l'avez jamais vu");
        welcomeDescription.setFont(Font.font("Trebuchet MS", FontPosture.REGULAR, 80));
        welcomeDescription.setTextFill(Color.WHITE);

        // Phrase d'accroche
        Label catchPhrase = new Label("Votre conquête spatiale commence ici.");
        catchPhrase.setFont(Font.font("Trebuchet MS", FontPosture.REGULAR, 25));
        catchPhrase.setTextFill(Color.WHITE);

        // Titre
        Label rigel = new Label("Rigel");
        rigel.setFont(Font.font("Trebuchet MS", FontPosture.REGULAR, 47));
        rigel.setTextFill(Color.WHITE);


        // Logo EPFL
        Image logo = new Image("EPFL_Logo_Digital_WHITE_PROD.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(logo.getWidth() / 16.0);
        logoView.setFitHeight(logo.getHeight() / 16.0);


        // Bouton permettant l'accès à la simulation du ciel
        Button startButton = new Button("Lancer Rigel");
        startButton.setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, new CornerRadii(3), new Insets(-6, -10, -6, -10))));
        startButton.setTextFill(Color.WHITE);
        startButton.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 20));

        // Panneau du haut du menu d'accueil
        BorderPane topPane = new BorderPane();
        topPane.setRight(startButton);
        BorderPane.setMargin(startButton, new Insets(30, 30, 0, 0));
        topPane.setLeft(logoView);
        BorderPane.setAlignment(logoView, Pos.CENTER);
        BorderPane.setMargin(logoView, new Insets(15, 0, 0, 30));
        topPane.setCenter(rigel);
        BorderPane.setAlignment(rigel, Pos.CENTER_LEFT);
        BorderPane.setMargin(rigel, new Insets(15, 0, 0, 0));


        // Définition de l'image de fond du menu
        Image image = new Image("milky-way-2881461_1920.jpeg");
        BackgroundImage bi = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);


        // Remplissage du panneau principal
        BorderPane rootMenu = new BorderPane();
        rootMenu.setBackground(new Background(bi));
        rootMenu.setTop(topPane);
        rootMenu.setCenter(welcomeDescription);
        BorderPane.setMargin(welcomeDescription, new Insets(140, 420, 0, 0));
        rootMenu.setBottom(catchPhrase);
        BorderPane.setMargin(catchPhrase, new Insets(0, 0, 70, 150));

        // Onglet du menu d'accueil
        Tab mainMenu = new Tab("Rigel", rootMenu);
        mainMenu.setClosable(false);

        // PARAMETRES
        Label settingsLabel = new Label("Paramètres");
        settingsLabel.setFont(Font.font("Trebuchet MS", FontPosture.REGULAR, 70));
        settingsLabel.setTextFill(Color.WHITE);

        // Choix de ville
        Label cityLabel = new Label("Choisir une ville:");
        cityLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 20));
        cityLabel.setTextFill(Color.WHITE);

        // ComboBox pour le choix de la ville d'apparition
        List < City > sortedCities = new ArrayList <>(cityCatalogue.getCities());
        Collections.sort(sortedCities, new CityComparator());
        ObservableList <City> cityData  = FXCollections.observableArrayList(sortedCities);
        ComboBox <City> cities = new ComboBox <>();
        cities.setStyle("-fx-font: 15px \"Trebuchet MS\";-fx-pref-width: 180;");
        cities.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(7), Insets.EMPTY)));
        cities.setItems(cityData);
        cities.setValue(new City("Lausanne",46.52, 6.57));


        // Choix des objets à dessiner dans la simulation
        CheckBox starsCheck = checkBoxesConstructor("Etoiles", true);
        CheckBox planetsCheck = checkBoxesConstructor("Planètes", true);
        CheckBox sunCheck = checkBoxesConstructor("Soleil", true);
        CheckBox moonCheck = checkBoxesConstructor("Lune", true);
        CheckBox satellitesCheck = checkBoxesConstructor("Satellites", false);
        CheckBox dayNightCheck = checkBoxesConstructor("Mode jour/nuit", false);
        CheckBox ufoCheck = checkBoxesConstructor("OVNI", false);

        // Panneau grille contenant les checkBoxes
        GridPane checkPane = new GridPane();
        checkPane.setVgap(12);
        checkPane.add(starsCheck, 0, 1);
        checkPane.add(planetsCheck, 0, 2);
        checkPane.add(sunCheck, 0, 3);
        checkPane.add(moonCheck, 0, 4);
        checkPane.add(satellitesCheck, 0, 5);
        checkPane.add(ufoCheck, 0, 6);
        checkPane.add(new Separator(), 0, 7);
        checkPane.add(dayNightCheck, 0, 8);
        checkPane.add(new Separator(), 0, 9);
        checkPane.add(cityLabel, 0, 10);
        checkPane.add(cities, 0, 11);


        // Objets celestes observables label
        Label objectLabel = new Label("Objets célestes observables:");
        objectLabel.setTextFill(Color.WHITE);
        objectLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 20));

        // Ajout du label dans le panneau grille
        checkPane.add(objectLabel, 0, 0);
        checkPane.setAlignment(Pos.CENTER);
        BorderPane.setMargin(checkPane, new Insets(15, 0, 0, 0));

        // Panneau principal du menu Paramètres
        BorderPane rootAdvancedSettings = new BorderPane();
        rootAdvancedSettings.setBackground(new Background(bi));
        rootAdvancedSettings.setCenter(checkPane);
        BorderPane.setMargin(checkPane, new Insets(0, 0, 100, 0));
        rootAdvancedSettings.setTop(settingsLabel);
        BorderPane.setAlignment(settingsLabel, Pos.CENTER);
        rootAdvancedSettings.setPadding(new Insets(100, 0, 0, 0));

        // Onglet contenant le menu des paramètres
        Tab advancesSettingsMenu = new Tab("Paramètres", rootAdvancedSettings);
        advancesSettingsMenu.setClosable(false);

        // INFORMATIONS
        // Panneau principal du menu Informations
        BorderPane infoPane = new BorderPane();
        infoPane.setBackground(new Background(bi));
        Label infoLabel = new Label("Informations");
        infoLabel.setFont(Font.font("Trebuchet MS", FontPosture.REGULAR, 70));
        infoLabel.setTextFill(Color.WHITE);
        infoPane.setTop(infoLabel);
        BorderPane.setAlignment(infoLabel, Pos.CENTER);
        infoPane.setPadding(new Insets(100, 0, 0, 0));

        // Texte d'informations
        Text infoText = new Text();
        infoText.setFill(Color.WHITE);
        infoText.setFont(Font.font("Trebuchet MS", 20));
        infoText.setText(
                "Avec Rigel, simulez le ciel depuis n'importe où sur Terre,\n" +
                "et n'importe quand.\n" +
                "\n" +
                "Choisissez de lancer une simulation avec les réglages par défaut\n" +
                "en cliquant sur 'Lancer Rigel', dans le menu principal.\n" +
                "Vous pourrez alors observer les étoiles, les planètes du système solaire,\n" +
                "la Lune, et le Soleil depuis Lausanne, Suisse.\n" +
                "\n" +
                "Dans l'onglet Paramètres, choisissez quels objets célestes vous souhaitez voir apparaitre\n" +
                "à l'écran. Ayez un aperçu de la puissance spatiale de différents pays basé sur\n " +
                "le nombre de satellites déployés. Un mode jour/nuit (par défaut désactivé) est aussi\n " +
                "disponible pour une simulation plus proche de la réalité.\n" +
                "\n"+
                "Lorsqu'une simulation est lancée, vous avez la possibilité d'accélerer le temps simulé,\n" +
                "de changer de position, de ville, de fuseau horaire...\n");
        infoText.setTextAlignment(TextAlignment.CENTER);
        infoPane.setCenter(infoText);
        BorderPane.setMargin(infoText, new Insets(0, 0, 20 , 0));

        // Label des auteurs
        Label creditLabel = new Label("Romain Berquet, Victor Gergaud - EPFL 2020");
        creditLabel.setFont(Font.font("Trebuchet MS", FontPosture.REGULAR, 15));
        creditLabel.setTextFill(Color.DARKGRAY);
        infoPane.setBottom(creditLabel);
        BorderPane.setAlignment(creditLabel, Pos.BASELINE_RIGHT);

        // Onglet contenant le menu d'informations
        Tab infoTab = new Tab("Informations", infoPane);
        infoTab.setClosable(false);

        // Panneau d'onglets contenant organisant les différents menus
        TabPane tp = new TabPane(mainMenu, advancesSettingsMenu, infoTab);
        tp.setStyle("-fx-font: 15px \"Trebuchet MS\";-fx-pref-width: 120;");
        tp.setSide(Side.TOP);

        // Fonctionnalité du bouton de départ
        startButton.setOnAction(e -> {
            try {
                List<ObjectsToDraw> objectsToDraw = checkBoxesHandler(starsCheck, planetsCheck, sunCheck, moonCheck, satellitesCheck, ufoCheck);
                cities.setEditable(false);
                startRigel(new Stage(), cityCatalogue, cities.getValue(), objectsToDraw, ufoCatalogue, dayNightCheck.isSelected());
                welcomeMenu.close();
            }
            catch (Exception ex) {}
            e.consume();
        });

        // Dimensions par défaut du menu
        welcomeMenu.setMinWidth(800);
        welcomeMenu.setMinHeight(600);
        welcomeMenu.setScene(new Scene(tp));

        welcomeMenu.show();
    }

    /**
     * Méthode privée modularisant la construction d'une checkBox dans le style, et avec le background employés dans le menu.
     * @param text
     * @param selected
     * @return une checkBox
     */

    private CheckBox checkBoxesConstructor(String text, boolean selected) {
        CheckBox cb = new CheckBox(text);
        cb.setStyle("-fx-font: 15px \"Trebuchet MS\";-fx-pref-width: 180;");
        cb.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(7), Insets.EMPTY)));
        cb.setSelected(selected);

        return cb;
    }

    /**
     * Méthode privée gérant l'ajout des types d'objets célestes à dessiner en fonction de la selection des checkBoxes
     * @param stars
     * @param planets
     * @param sun
     * @param moon
     * @param satellites
     * @return
     */

    private List<ObjectsToDraw> checkBoxesHandler(CheckBox stars, CheckBox planets, CheckBox sun, CheckBox moon, CheckBox satellites, CheckBox ufo) {
        List<ObjectsToDraw> objectsToDraw = new ArrayList<>();
        if(stars.isSelected()) objectsToDraw.add(ObjectsToDraw.STARS);
        if(planets.isSelected()) objectsToDraw.add(ObjectsToDraw.PLANETS);
        if(sun.isSelected()) objectsToDraw.add(ObjectsToDraw.SUN);
        if(moon.isSelected()) objectsToDraw.add(ObjectsToDraw.MOON);
        if(satellites.isSelected()) objectsToDraw.add(ObjectsToDraw.SATELLITES);
        if(ufo.isSelected()) objectsToDraw.add(ObjectsToDraw.UFO);

        return objectsToDraw;
    }
    /**
     * Méthode permettant de générer la barre d'information.
     */

    private BorderPane informationBarSetUp(SkyCanvasManager skyCanvasManager, ViewingParametersBean viewingParametersBean, List<ObjectsToDraw> objectsToDraw) {

        //Barre d'information à laquelle on assigne le style donné.
        BorderPane informationBar = new BorderPane();


        if(objectsToDraw.contains(ObjectsToDraw.UFO)) informationBar.setStyle("-fx-padding: 4;\n" + "-fx-background-color: green;");
        else informationBar.setStyle(STYLE_1);


        // Partie du panneau contenant le champs de vue.
        StringExpression fieldOfView = Bindings.format("Champ de vue: %.1f °", viewingParametersBean.fieldOfViewDegProperty());
        Text leftZone = new Text();
        leftZone.textProperty().bind(fieldOfView);
        informationBar.setLeft(leftZone);

        //Partie du panneau contenant l'objet situé sous la souris.
        skyCanvasManager.objectUnderMouse.addListener(observable -> {
            CelestialObject currentObject = skyCanvasManager.objectUnderMouse.getValue();
            String objectUnderMouse;
            Text centerZone;

            objectUnderMouse = currentObject == null ? "" : currentObject.info();

            centerZone = new Text(objectUnderMouse);
            informationBar.setCenter(centerZone);
        });

        //Partie du panneau contenant les coordonnées horizontales de la souris.
        StringExpression mouseExpression = Bindings.format("Azimut = %.2f °," + " hauteur= %.2f °", skyCanvasManager.mouseAzDeg, skyCanvasManager.mouseAltDeg);
        Text rightZone = new Text();
        rightZone.textProperty().bind(mouseExpression);
        informationBar.setRight(rightZone);


        return informationBar;


    }

    /**
     * Méthode permettatn de construire la barre de controle.
     */

    private HBox controlBarSetUp(ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean, TimeAnimator animator, CityCatalogue cityCatalogue, UFOCatalogue ufoCatalogue, Stage Rigel, City startingPoint, List < ObjectsToDraw > objectsToDraw) {


        /**
         * Sous panneau contenant l'interface graphique permettant de définir la position d'observation.
         */

        List < City > sortedCities = new ArrayList <>(cityCatalogue.getCities());
        Collections.sort(sortedCities, new CityComparator());
        ObservableList < City > cityData = FXCollections.observableArrayList(sortedCities);

        ComboBox < City > cities = new ComboBox <>();
        cities.setStyle(STYLE_6);

        cities.setItems(cityData);
        cities.setValue(startingPoint);
        cities.valueProperty().addListener(observable -> {
            observerLocationBean.setLonDeg(cities.getValue().getLon());
            observerLocationBean.setLatDeg(cities.getValue().getLat());
        });

        ObservableList < UFO > ufos = FXCollections.observableArrayList(ufoCatalogue.UFOs());
        ComboBox < UFO > ufosBox = new ComboBox <>();
        ufosBox.setStyle("-fx-pref-width: 100;");
        ufosBox.setItems(ufos);
        ufosBox.setPromptText("OVNI");


        //Définition de l'étiquette de texte pour la longitude et latitude.
        Label longitudeLabel = new Label("Longitude (°) :");
        Label latitudeLabel = new Label("Latitude (°) :");


        //Définition des champs de texte pour la longitude, la latitude et leur style.
        TextField longitudeTextField = textFieldForCoordinates(STRING_CONVERTER, coordinatesTypes.LONDEG, observerLocationBean, cities);
        TextField latitudeTextField = textFieldForCoordinates(STRING_CONVERTER, coordinatesTypes.LATDEG, observerLocationBean, cities);

        longitudeTextField.setStyle(STYLE_2);
        latitudeTextField.setStyle(STYLE_2);

        //Définition du panneau contenant la partie sur la position d'observation.
        HBox observerLocationControlBar = new HBox(longitudeLabel, longitudeTextField, latitudeLabel, latitudeTextField);
        observerLocationControlBar.setStyle(STYLE_3);


        /**
         * Sous panneau contenant l'instant d'observtion
         */

        //Définition de l'étiquette de texte pour la date.
        Label date = new Label(" Date : ");

        //Définition du noeud permettant de choisir la date et lien avec la date.
        DatePicker datePicker = new DatePicker(dateTimeBean.getDate());
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.setStyle(STYLE_4);

        //Definition du noeud permettant de choisir l'heure.
        Label hour = new Label("Heure : ");

        TextField hourField = textFieldForHour(dateTimeBean);
        hourField.setStyle(STYLE_5);

        //Définition du noeud permettant de choisir le fuseau horaire.
        ComboBox <ZoneId> timeZone = new ComboBox <> ();
        timeZone.setStyle(STYLE_6);

        List < String > items = new ArrayList<>((ZoneId.getAvailableZoneIds()));
        Collections.sort(items);
        List < ZoneId > listItems = new ArrayList <>();
        for (String s : items) {
            listItems.add(ZoneId.of(s));
        }

        ObservableList < ZoneId > ObItems = FXCollections.observableArrayList(listItems);
        timeZone.setItems(ObItems);
        timeZone.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());

        //Genere le tableau conteant la partie permettat de modifier le bean ZoneDateTime.
        HBox instantOfObservation = new HBox(date, datePicker, hour, hourField, timeZone);
        instantOfObservation.setStyle(STYLE_3);

        animator.getRunState().addListener(observable ->
                instantOfObservation.setDisable(animator.getRunState().getValue())
        );


        /**
         * Sous panneau contenant l'interface graphique permettant de choisir la vitese d'écoulement du temps
         */

        ChoiceBox < NamedTimeAccelerator > menu = new ChoiceBox <>();

        ObservableList < NamedTimeAccelerator > accelerators = FXCollections.observableArrayList(NamedTimeAccelerator.values());
        menu.setItems(accelerators);
        menu.setValue(NamedTimeAccelerator.TIMES_300);

        animator.acceleratorProperty().bind(Bindings.select(menu.valueProperty(), "accelerator"));


        HBox timingParameters = new HBox(menu);
        timingParameters.setStyle(STYLE_7);




        /**
         *  Sous panneau contenant la partie orrespondant aux bouttons.
         */

        // Buttons
        Button resetButton = new Button(RESET_IDENTIFIER);
        Button playPauseButton = new Button(PLAY_IDENTIFIER);
        Button homeButton = new Button (HOME_IDENTIFIER);


        //Acces aux schémas des bouttons.
        try (InputStream fontStream = getClass()
                .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            Font fontAwesome = Font.loadFont(fontStream, 15);
            resetButton.setFont(fontAwesome);
            playPauseButton.setFont(fontAwesome);
            homeButton.setFont(fontAwesome);


        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        //Gestion du bouton reset.
        resetButton.setOnAction(e -> {
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());
            e.consume();
        });

        //Gestion du button on.
        playPauseButton.setOnAction(e -> {
            if (animator.getRunState().getValue()) {
                animator.stop();
                playPauseButton.setText(PLAY_IDENTIFIER);
            } else {
                animator.start();
                playPauseButton.setText(PAUSE_IDENTIFIER);
            }
            e.consume();
        });

        homeButton.setOnAction(event -> {
            try {
                start(new Stage());
                Rigel.close();
            }
            catch (Exception ex) {}
            event.consume();
        });



        // Activation/désactivation de certains noeuds en fonction de l'état de l'animateur.
        animator.getRunState().addListener(observable -> {
            menu.setDisable(animator.getRunState().getValue());
            resetButton.setDisable(animator.getRunState().getValue());
        });

        /**
         * Panneau final.
         */

        //Génere le tableau de control final.
        HBox controlBar;

       if (objectsToDraw.contains(ObjectsToDraw.UFO))  controlBar = new HBox(observerLocationControlBar, SEPARATOR, instantOfObservation, SEPARATOR_1, timingParameters, resetButton, playPauseButton, cities, ufosBox, homeButton);
       else  controlBar = new HBox(observerLocationControlBar, SEPARATOR, instantOfObservation, SEPARATOR_1, timingParameters, resetButton, playPauseButton, cities, homeButton);


        if(objectsToDraw.contains(ObjectsToDraw.UFO)) controlBar.setStyle("-fx-padding: 4;\n" + "-fx-background-color: green;");
        else controlBar.setStyle(STYLE_8);


        return controlBar;

    }

    /**
     * Méthode permettant la mise en place du formatteur texte pour les coordonnées.
     */

    private TextField textFieldForCoordinates(NumberStringConverter stringConverter, coordinatesTypes type, ObserverLocationBean observerLocationBean, ComboBox<City> cities) {


        UnaryOperator < TextFormatter.Change > coordinateFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double coordinateDeg =
                        stringConverter.fromString(newText).doubleValue();


                switch (type) {
                    case LONDEG:
                        return GeographicCoordinates.isValidLonDeg(coordinateDeg)
                                ? change
                                : null;
                    case LATDEG:
                        return GeographicCoordinates.isValidLatDeg(coordinateDeg)
                                ? change
                                : null;
                    default:
                        return null;
                }

            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter < Number > coordinate =
                new TextFormatter <>(stringConverter, 0, coordinateFilter);

        switch (type) {
            case LONDEG:
                coordinate.valueProperty().addListener(observable -> {
                    observerLocationBean.setLonDeg(coordinate.getValue().doubleValue());
                    cities.valueProperty().setValue(new City("",observerLocationBean.getLatDeg(),coordinate.getValue().doubleValue()));
                });


                break;
            case LATDEG:

                coordinate.valueProperty().addListener(observable -> {
                    observerLocationBean.setLatDeg(coordinate.getValue().doubleValue());
                    cities.valueProperty().setValue(new City("",coordinate.getValue().doubleValue(), observerLocationBean.getLatDeg()));
                });
        }

        TextField coordinateField =
                new TextField();
        coordinateField.setTextFormatter(coordinate);

        return coordinateField;

    }

    /**
     * Méthode permettant la mise en place du texte pour l'heure.
     */

    private TextField textFieldForHour(DateTimeBean dateTimeBean) {
        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter < LocalTime > timeFormatter =
                new TextFormatter <>(stringConverter);

        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());
        TextField hour = new TextField();
        hour.setTextFormatter(timeFormatter);

        return hour;
    }

    // Type énuméré privé permettant un choix aisé des TextField en fonction de la coordonnée.
    private enum coordinatesTypes {
        LONDEG,
        LATDEG
    }


}




