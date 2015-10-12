package org.specvis.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.specvis.StartApplication;
import org.specvis.data.ConfigurationData;
import org.specvis.logic.ScreenLuminanceFunctions;
import org.specvis.procedure.PreviewStimuliDistribution;

/**
 * Created by pdzwiniel on 2015-05-20.
 */

/*
 * Copyright 2014-2015 Piotr Dzwiniel
 *
 * This file is part of Specvis.
 *
 * Specvis is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Specvis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Specvis; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

public class Step3_StimulusAndBackground extends Stage {

    private ScreenLuminanceFunctions screenLuminanceFunctions;
    private Spinner spinnerStimulusMaxBrightness;
    private TextField textFieldStimulusInitialLuminance;
    private ComboBox cbStimulusShape;
    private Spinner spinnerStimulusInclination;
    private Spinner spinnerStimulusWidth;
    private Spinner spinnerStimulusHeight;
    private Spinner spinnerBackgroundBrightness;
    private TextField textFieldBackgroundLuminance;
    private Pane paneStimulusPreview;
    private Spinner spinnerQuarterGridResolutionX;
    private Spinner spinnerQuarterGridResolutionY;
    private TextField textFieldDistanceBetweenStimuliHorizontally;
    private TextField textFieldDistanceBetweenStimuliVertically;
    private Spinner spinnerStimulusDisplayTime;
    private Spinner spinnerIntervalBetweenStimuliConstantPart;
    private Spinner spinnerIntervalBetweenStimuliRandomPart;
    private StartApplication startApplication;

    public Parent createContent() {

        /* init logic */
        screenLuminanceFunctions = startApplication.getStageStep2ScreenAndLuminance().getScreenLuminanceFunctions();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> top -> step indicator */
        Text textProgressBar = new Text("Step 3/4 - Stimulus & background");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0.75);
        progressBar.setPrefHeight(30);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-opacity: 0.7;");
        StackPane stackPane = new StackPane();
        stackPane.setMaxWidth(Double.MAX_VALUE);
        stackPane.getChildren().setAll(progressBar, textProgressBar);

        /* layout -> center */
        int equalMinWidth = 110;

        Label labelStimulusInitial = new Label("Stimulus max brightness (%):");
        labelStimulusInitial.setMinWidth(equalMinWidth * 1.5);

        spinnerStimulusMaxBrightness = new Spinner(0, 100, 100);
        spinnerStimulusMaxBrightness.setEditable(true);
        spinnerStimulusMaxBrightness.setOnKeyReleased(event -> {
            setLuminanceTextFieldsValues(Integer.valueOf(spinnerStimulusMaxBrightness.getValue().toString()),
                    textFieldStimulusInitialLuminance);
            setStimulusRepresentationOnPreviewPane();
        });
        spinnerStimulusMaxBrightness.setOnMouseClicked(event -> {
            setLuminanceTextFieldsValues(Integer.valueOf(spinnerStimulusMaxBrightness.getValue().toString()),
                    textFieldStimulusInitialLuminance);
            setStimulusRepresentationOnPreviewPane();
        });
        spinnerStimulusMaxBrightness.setPrefWidth(80);
        spinnerStimulusMaxBrightness.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getStimulusMaxBrightness()));

        Label labelStimulusInitialLuminance = new Label("Stimulus max lum. (cd/m2):");
        labelStimulusInitialLuminance.setMinWidth(equalMinWidth * 1.5);

        textFieldStimulusInitialLuminance = new TextField();
        textFieldStimulusInitialLuminance.setPrefWidth(80);
        textFieldStimulusInitialLuminance.setEditable(false);
        textFieldStimulusInitialLuminance.setStyle("-fx-background-color: rgb(215,215,215)");

        Label labelStimulusShape = new Label("Stimulus shape:");
        labelStimulusShape.setMinWidth(equalMinWidth);

        ObservableList olStimulusShape = FXCollections.observableArrayList("Ellipse", "Polygon");
        cbStimulusShape = new ComboBox(olStimulusShape);
        cbStimulusShape.setOnAction(event -> setStimulusRepresentationOnPreviewPane());
        cbStimulusShape.getSelectionModel().select(0);
        cbStimulusShape.setPrefWidth(100);
        cbStimulusShape.getSelectionModel().select(ConfigurationData.getStimulusShape());

        Label labelInclination = new Label("Inclination (\u00b0):");
        labelInclination.setMinWidth(equalMinWidth);

        spinnerStimulusInclination = new Spinner(0.0, 360.0, 0.0, 0.1);
        spinnerStimulusInclination.setEditable(true);
        spinnerStimulusInclination.setOnKeyReleased(event -> setStimulusRepresentationOnPreviewPane());
        spinnerStimulusInclination.setOnMousePressed(event -> setStimulusRepresentationOnPreviewPane());
        spinnerStimulusInclination.setPrefWidth(100);
        spinnerStimulusInclination.getValueFactory().setValue(Double.valueOf(ConfigurationData.getStimulusInclination()));

        Label labelStimulusWidth = new Label("Stimulus width (\u00b0):");
        labelStimulusWidth.setMinWidth(equalMinWidth);

        spinnerStimulusWidth = new Spinner(0.1, 100.0, 0.5, 0.1);
        spinnerStimulusWidth.setEditable(true);
        spinnerStimulusWidth.setOnKeyReleased(event -> setStimulusRepresentationOnPreviewPane());
        spinnerStimulusWidth.setOnMousePressed(event -> setStimulusRepresentationOnPreviewPane());
        spinnerStimulusWidth.setPrefWidth(100);
        spinnerStimulusWidth.getValueFactory().setValue(Double.valueOf(ConfigurationData.getStimulusWidth()));

        Label labelStimulusHeight = new Label("Stimulus height (\u00b0):");
        labelStimulusHeight.setMinWidth(equalMinWidth);

        spinnerStimulusHeight = new Spinner(0.1, 100.0, 0.5, 0.1);
        spinnerStimulusHeight.setEditable(true);
        spinnerStimulusHeight.setOnKeyReleased(event -> setStimulusRepresentationOnPreviewPane());
        spinnerStimulusHeight.setOnMousePressed(event -> setStimulusRepresentationOnPreviewPane());
        spinnerStimulusHeight.setPrefWidth(100);
        spinnerStimulusHeight.getValueFactory().setValue(Double.valueOf(ConfigurationData.getStimulusHeight()));

        Label labelBackground = new Label("Background brightness (%):");
        labelBackground.setMinWidth(equalMinWidth * 1.5);

        spinnerBackgroundBrightness = new Spinner(0, 100, 30);
        spinnerBackgroundBrightness.setEditable(true);
        spinnerBackgroundBrightness.setOnKeyReleased(event -> {
            setLuminanceTextFieldsValues(Integer.valueOf(spinnerBackgroundBrightness.getValue().toString()),
                    textFieldBackgroundLuminance);
            setPaneStimulusPreviewBackgroundColor(
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleHue()),
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleSaturation()),
                    Integer.valueOf(spinnerBackgroundBrightness.getValue().toString()));
            setStimulusRepresentationOnPreviewPane();

            startApplication.getStageStep4General().setPaneFixationPointPreviewBackgroundColor(
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleHue()),
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleSaturation()),
                    Integer.valueOf(spinnerBackgroundBrightness.getValue().toString()));
        });
        spinnerBackgroundBrightness.setOnMouseClicked(event -> {
            setLuminanceTextFieldsValues(Integer.valueOf(spinnerBackgroundBrightness.getValue().toString()),
                    textFieldBackgroundLuminance);
            setPaneStimulusPreviewBackgroundColor(
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleHue()),
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleSaturation()),
                    Integer.valueOf(spinnerBackgroundBrightness.getValue().toString()));
            setStimulusRepresentationOnPreviewPane();

            startApplication.getStageStep4General().setPaneFixationPointPreviewBackgroundColor(
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleHue()),
                    Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleSaturation()),
                    Integer.valueOf(spinnerBackgroundBrightness.getValue().toString()));
        });
        spinnerBackgroundBrightness.setPrefWidth(80);
        spinnerBackgroundBrightness.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getBackgroundBrightness()));

        Label labelBackgroundLuminance = new Label("Background lum. (cd/m2):");
        labelBackgroundLuminance.setMinWidth(equalMinWidth * 1.5);

        textFieldBackgroundLuminance = new TextField();
        textFieldBackgroundLuminance.setPrefWidth(80);
        textFieldBackgroundLuminance.setMaxWidth(Double.MAX_VALUE);
        textFieldBackgroundLuminance.setEditable(false);
        textFieldBackgroundLuminance.setStyle("-fx-background-color: rgb(215,215,215)");

        TitledPane titledPaneStimulusPreview = new TitledPane();
        titledPaneStimulusPreview.setText("Stimulus preview");

        paneStimulusPreview = new Pane();
        paneStimulusPreview.setMinSize(200, 200);
        paneStimulusPreview.setMaxSize(200, 200);

        titledPaneStimulusPreview.setContent(paneStimulusPreview);

        Label labelQuarterGridResolution = new Label("Quarter grid resolution");

        Label labelQuarterGridResolutionX = new Label("X:");

        spinnerQuarterGridResolutionX = new Spinner(1, 20, 4);
        spinnerQuarterGridResolutionX.setPrefWidth(80);
        spinnerQuarterGridResolutionX.setOnKeyReleased(event -> setTextForDistanceBetweenStimuliTextFields());
        spinnerQuarterGridResolutionX.setOnMouseClicked(event -> setTextForDistanceBetweenStimuliTextFields());
        spinnerQuarterGridResolutionX.setEditable(true);
        spinnerQuarterGridResolutionX.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getQuarterGridResolutionX()));

        Label labelQuarterGridResolutionY = new Label("Y:");

        spinnerQuarterGridResolutionY = new Spinner(1, 20, 4);
        spinnerQuarterGridResolutionY.setPrefWidth(80);
        spinnerQuarterGridResolutionY.setOnKeyReleased(event -> setTextForDistanceBetweenStimuliTextFields());
        spinnerQuarterGridResolutionY.setOnMouseClicked(event -> setTextForDistanceBetweenStimuliTextFields());
        spinnerQuarterGridResolutionY.setEditable(true);
        spinnerQuarterGridResolutionY.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getQuarterGridResolutionY()));

        Label labelDistanceBetweenStimuli = new Label("Distance between stimuli (\u00b0)");
        labelDistanceBetweenStimuli.setAlignment(Pos.CENTER);
        labelDistanceBetweenStimuli.setTextAlignment(TextAlignment.CENTER);

        Label labelDistanceBetweenStimuliHorizontally = new Label("Horizontally:");

        textFieldDistanceBetweenStimuliHorizontally = new TextField();
        textFieldDistanceBetweenStimuliHorizontally.setEditable(false);
        textFieldDistanceBetweenStimuliHorizontally.setStyle("-fx-background-color: rgb(215,215,215)");
        textFieldDistanceBetweenStimuliHorizontally.setPrefWidth(70);

        Label labelDistanceBetweenStimuliVertically = new Label("Vertically:");

        textFieldDistanceBetweenStimuliVertically = new TextField();
        textFieldDistanceBetweenStimuliVertically.setEditable(false);
        textFieldDistanceBetweenStimuliVertically.setStyle("-fx-background-color: rgb(215,215,215)");
        textFieldDistanceBetweenStimuliVertically.setPrefWidth(70);

        Label labelStimulusDisplayTime = new Label("Stimulus display time (ms):");

        spinnerStimulusDisplayTime = new Spinner(10, 10000, 200, 1);
        spinnerStimulusDisplayTime.setPrefWidth(80);
        spinnerStimulusDisplayTime.setEditable(true);
        spinnerStimulusDisplayTime.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getStimulusDisplayTime()));

        Label labelTimeIntervalBetweenStimuli = new Label("Time interval between stimuli (ms)");

        Label labelConstantPart = new Label("Constant:");

        spinnerIntervalBetweenStimuliConstantPart = new Spinner(10, 10000, 800, 1);
        spinnerIntervalBetweenStimuliConstantPart.setPrefWidth(80);
        spinnerIntervalBetweenStimuliConstantPart.setEditable(true);
        spinnerIntervalBetweenStimuliConstantPart.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getConstantPartOfIntervalBetweenStimuli()));

        Label labelRandomPart = new Label("Random:");

        spinnerIntervalBetweenStimuliRandomPart = new Spinner(0, 10000, 200, 1);
        spinnerIntervalBetweenStimuliRandomPart.setPrefWidth(80);
        spinnerIntervalBetweenStimuliRandomPart.setEditable(true);
        spinnerIntervalBetweenStimuliRandomPart.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getRandomPartOfIntervalBetweenStimuli()));

        HBox hBoxPrime = new HBox(10);
        VBox vBoxLeft = new VBox(10);
        VBox vBoxRight = new VBox(10);

        HBox h1 = new HBox(10);
        HBox h2 = new HBox(10);
        HBox h3 = new HBox(10);
        HBox h4 = new HBox(10);
        HBox h5 = new HBox(10);
        HBox h6 = new HBox(10);
        HBox h7 = new HBox(10);
        HBox h8 = new HBox(10);
        HBox h9 = new HBox(10);
        HBox h10 = new HBox(10);
        HBox h11 = new HBox(10);
        HBox h14 = new HBox(10);
        HBox h12 = new HBox(10);
        HBox h13 = new HBox(10);
        HBox h15 = new HBox(10);

        h1.getChildren().addAll(labelStimulusInitial, spinnerStimulusMaxBrightness);
        h1.setAlignment(Pos.CENTER_LEFT);
        h1.setHgrow(spinnerStimulusMaxBrightness, Priority.ALWAYS);

        h2.getChildren().addAll(labelStimulusShape, cbStimulusShape);
        h2.setAlignment(Pos.CENTER_LEFT);

        h3.getChildren().addAll(labelStimulusWidth, spinnerStimulusWidth);
        h3.setAlignment(Pos.CENTER_LEFT);

        h4.getChildren().addAll(labelBackground, spinnerBackgroundBrightness);
        h4.setAlignment(Pos.CENTER_LEFT);

        h5.getChildren().addAll(labelStimulusInitialLuminance, textFieldStimulusInitialLuminance);
        h5.setAlignment(Pos.CENTER_LEFT);

        h6.getChildren().addAll(labelInclination, spinnerStimulusInclination);
        h6.setAlignment(Pos.CENTER_LEFT);

        h7.getChildren().addAll(labelStimulusHeight, spinnerStimulusHeight);
        h7.setAlignment(Pos.CENTER_LEFT);

        h8.getChildren().addAll(labelBackgroundLuminance, textFieldBackgroundLuminance);
        h8.setAlignment(Pos.CENTER_LEFT);

        h9.getChildren().addAll(labelStimulusDisplayTime, spinnerStimulusDisplayTime);
        h9.setAlignment(Pos.CENTER);

        h11.getChildren().addAll(labelTimeIntervalBetweenStimuli);
        h11.setAlignment(Pos.CENTER);

        h10.getChildren().addAll(labelConstantPart, spinnerIntervalBetweenStimuliConstantPart, labelRandomPart, spinnerIntervalBetweenStimuliRandomPart);
        h10.setAlignment(Pos.CENTER);

        h14.getChildren().addAll(labelDistanceBetweenStimuli);
        h14.setAlignment(Pos.CENTER);

        h13.getChildren().addAll(labelDistanceBetweenStimuliHorizontally, textFieldDistanceBetweenStimuliHorizontally,
                labelDistanceBetweenStimuliVertically, textFieldDistanceBetweenStimuliVertically);
        h13.setAlignment(Pos.CENTER_LEFT);

        h15.getChildren().addAll(labelQuarterGridResolution);
        h15.setAlignment(Pos.CENTER);

        h12.getChildren().addAll(labelQuarterGridResolutionX, spinnerQuarterGridResolutionX, labelQuarterGridResolutionY,
                spinnerQuarterGridResolutionY);
        h12.setAlignment(Pos.CENTER);

        vBoxLeft.getChildren().addAll(h1, h5, new Separator(), h2, h6, h3, h7,new Separator(), h9, h11, h10, new Separator(), h14, h13);
        vBoxRight.getChildren().addAll(h4, h8, new Separator(), h15, h12, titledPaneStimulusPreview);

        hBoxPrime.getChildren().addAll(vBoxLeft, new Separator(Orientation.VERTICAL), vBoxRight);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonBack = new Button("Back");
        buttonBack.setOnAction(event -> {
            this.hide();
            startApplication.getStageStep2ScreenAndLuminance().show();
        });

        Button buttonPreviewStimuliDistribution = new Button("Preview stimuli distribution");
        buttonPreviewStimuliDistribution.setOnAction(event -> {
            startApplication.getStageStep4General().setDataValues();
            PreviewStimuliDistribution stage = new PreviewStimuliDistribution(startApplication.getData());
            stage.show();
        });

        Button buttonNext = new Button("Next");
        buttonNext.setOnAction(event -> {

            int brightnessDifference = Integer.valueOf(spinnerStimulusMaxBrightness.getValue().toString()) - Integer.valueOf(spinnerBackgroundBrightness.getValue().toString());

            if (brightnessDifference >= 11) {
                this.hide();
                startApplication.getStageStep4General().show();
                startApplication.getStageStep4General().setLuminanceForGivenTextFields(Integer.valueOf(startApplication.getStageStep4General().getSpinnerMonitorStimulusBrightness().getValue().toString()),
                        startApplication.getStageStep4General().getTextFieldMonitorStimulusLuminance());
                startApplication.getStageStep4General().setPaneFixationPointPreviewBackgroundColor(
                        Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleHue()),
                        Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleSaturation()),
                        Integer.valueOf(spinnerBackgroundBrightness.getValue().toString())
                );
                startApplication.getStageStep4General().setFixationPointRepresentationOnPreviewPane();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Inappriopriate \"Stimulus max brightness\" and \"Background brightness\".");
                alert.setContentText("The difference between \"Stimulus max brightness\" and \"Background brightness\" must be >= than 11. " +
                        "Correct values and try again.");
                alert.showAndWait();
            }
        });

        hBoxBottom.getChildren().addAll(buttonBack, buttonPreviewStimuliDistribution, buttonNext);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setTop(stackPane);
        layout.setCenter(hBoxPrime);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(hBoxPrime, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    public Step3_StimulusAndBackground(StartApplication startApplication) {
        this.startApplication = startApplication;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public double[] calculateDistanceBetweenStimuliInDegrees(int screenResolutionX, int screenResolutionY, double visualFieldX, double visualFieldY, int quarterGridResolutionX, int quarterGridResolutionY) {
        double pixelsForOneDegreeX = (double) screenResolutionX / visualFieldX;
        double pixelsForOneDegreeY = (double) screenResolutionY / visualFieldY;
        double degreesPerQuarterGridCellX = ((double) screenResolutionX / ((double) quarterGridResolutionX) / 2) / pixelsForOneDegreeX;
        double degreesPerQuarterGridCellY = ((double) screenResolutionY / ((double) quarterGridResolutionY) / 2) / pixelsForOneDegreeY;
        return new double[] {degreesPerQuarterGridCellX, degreesPerQuarterGridCellY};
    }

    public void setTextForDistanceBetweenStimuliTextFields() {
        double[] distance = calculateDistanceBetweenStimuliInDegrees(
                Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionX().getText()),
                Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionY().getText()),
                Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldX().getText()),
                Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldY().getText()),
                Integer.valueOf(spinnerQuarterGridResolutionX.getValue().toString()), Integer.valueOf(spinnerQuarterGridResolutionY.getValue().toString()));
        textFieldDistanceBetweenStimuliHorizontally.setText(String.format("%.2f", distance[0]).replace(",", "."));
        textFieldDistanceBetweenStimuliVertically.setText(String.format("%.2f", distance[1]).replace(",", "."));
    }

    public double getFittedLuminanceForGivenBrightness(int brightness) {
        return startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getLuminanceForBrightness()[brightness];
    }

    public void setLuminanceTextFieldsValues(int brightness, TextField textFieldLuminance) {
        double luminance = getFittedLuminanceForGivenBrightness(brightness);
        double value = screenLuminanceFunctions.round(luminance, 2);
        if (value < 0) {
            value = 0.0;
        }
        textFieldLuminance.setText(String.valueOf(value));
    }

    public void fireSpinnerStimulusMaxBrightness() {
        setLuminanceTextFieldsValues(Integer.valueOf(spinnerStimulusMaxBrightness.getValue().toString()),
                textFieldStimulusInitialLuminance);
    }

    public void fireSpinnerBackgroundBrightness() {
        setLuminanceTextFieldsValues(Integer.valueOf(spinnerBackgroundBrightness.getValue().toString()),
                textFieldBackgroundLuminance);
    }

    public void setPaneStimulusPreviewBackgroundColor(int hsb, int saturation, int brigthness) {
        paneStimulusPreview.setStyle("-fx-background-color: hsb(" + hsb + ", " + saturation + "%, " + brigthness + "%);");
    }

    public void setScreenLuminanceFunctions(ScreenLuminanceFunctions screenLuminanceFunctions) {
        this.screenLuminanceFunctions = screenLuminanceFunctions;
    }

    public Spinner getSpinnerBackgroundBrightness() {
        return spinnerBackgroundBrightness;
    }

    public Ellipse createEllipseStimulus(double stimulusPositionX, double stimulusPositionY, double stimulusDiameterX, double stimulusDiameterY, Color stimulusColor) {

        double screenResolutionX = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionX().getText());
        double screenResolutionY = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionY().getText());

        double visualFieldX = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldX().getText());
        double visualFieldY = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldY().getText());

        double pixelsForOneDegreeX = screenResolutionX / visualFieldX;
        double pixelsForOneDegreeY = screenResolutionY / visualFieldY;

        double radiusX = (stimulusDiameterX / 2) * pixelsForOneDegreeX;
        double radiusY = (stimulusDiameterY / 2) * pixelsForOneDegreeY;

        Ellipse stimulus;
        stimulus = new Ellipse(stimulusPositionX, stimulusPositionY, radiusX, radiusY);
        stimulus.setFill(stimulusColor);
        stimulus.setStroke(stimulusColor);
        return stimulus;
    }

    public Polygon createPolygonStimulus(double stimulusPositionX, double stimulusPositionY, double stimulusSizeX, double stimulusSizeY, Color stimulusColor, double inclination) {

        double screenResolutionX = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionX().getText());
        double screenResolutionY = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionY().getText());

        double visualFieldX = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldX().getText());
        double visualFieldY = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldY().getText());

        double pixelsForOneDegreeX = screenResolutionX / visualFieldX;
        double pixelsForOneDegreeY = screenResolutionY / visualFieldY;

        double sizeX = stimulusSizeX * pixelsForOneDegreeX;
        double sizeY = stimulusSizeY * pixelsForOneDegreeY;

        Polygon polygon = new Polygon();

        double diagonal = Math.sqrt(Math.pow(sizeX, 2) + Math.pow(sizeY, 2));

        double x = stimulusPositionX + sizeX;
        double y = stimulusPositionY - sizeY;

        double polygonInnerAngle = Math.toDegrees(Math.atan2(x - stimulusPositionX, stimulusPositionY - y));

        double positionAx = stimulusPositionX + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + 270 + (90 - polygonInnerAngle) - 90)));
        double positionAy = stimulusPositionY + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + 270 + (90 - polygonInnerAngle) - 90)));

        double positionBx = stimulusPositionX + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + polygonInnerAngle - 90)));
        double positionBy = stimulusPositionY + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + polygonInnerAngle - 90)));

        double positionCx = stimulusPositionX + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + 90 + (90 - polygonInnerAngle) - 90)));
        double positionCy = stimulusPositionY + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + 90 + (90 - polygonInnerAngle) - 90)));

        double positionDx = stimulusPositionX + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + 180 + polygonInnerAngle - 90)));
        double positionDy = stimulusPositionY + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + 180 + polygonInnerAngle - 90)));

        polygon.getPoints().addAll(positionAx, positionAy, positionBx, positionBy, positionCx, positionCy, positionDx, positionDy);

        polygon.setFill(stimulusColor);

        return polygon;
    }

    public void setStimulusRepresentationOnPreviewPane() {
        if (paneStimulusPreview.getChildren().size() > 0) {
            paneStimulusPreview.getChildren().remove(paneStimulusPreview.getChildren().size() - 1);
        }
        Shape stimulusRepresentation;
        if (cbStimulusShape.getSelectionModel().getSelectedItem().equals("Ellipse")) {
            stimulusRepresentation = createEllipseStimulus(100, 100,
                    Double.valueOf(spinnerStimulusWidth.getValue().toString()),
                    Double.valueOf(spinnerStimulusHeight.getValue().toString()),
                    Color.hsb(Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleHue()),
                            Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleSaturation()) / 100,
                            Double.valueOf(spinnerStimulusMaxBrightness.getValue().toString()) / 100)
            );
        } else {
            stimulusRepresentation = createPolygonStimulus(100, 100,
                    Double.valueOf(spinnerStimulusWidth.getValue().toString()),
                    Double.valueOf(spinnerStimulusHeight.getValue().toString()),
                    Color.hsb(Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleHue()),
                            Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleData().getScaleSaturation()) / 100,
                            Double.valueOf(spinnerStimulusMaxBrightness.getValue().toString()) / 100),
                    Double.valueOf(spinnerStimulusInclination.getValue().toString()));
        }

        paneStimulusPreview.getChildren().addAll(stimulusRepresentation);
    }

    public Spinner getSpinnerStimulusMaxBrightness() {
        return spinnerStimulusMaxBrightness;
    }

    public ComboBox getCbStimulusShape() {
        return cbStimulusShape;
    }

    public Spinner getSpinnerStimulusInclination() {
        return spinnerStimulusInclination;
    }

    public Spinner getSpinnerStimulusWidth() {
        return spinnerStimulusWidth;
    }

    public Spinner getSpinnerStimulusHeight() {
        return spinnerStimulusHeight;
    }

    public Spinner getSpinnerQuarterGridResolutionX() {
        return spinnerQuarterGridResolutionX;
    }

    public Spinner getSpinnerQuarterGridResolutionY() {
        return spinnerQuarterGridResolutionY;
    }

    public TextField getTextFieldDistanceBetweenStimuliHorizontally() {
        return textFieldDistanceBetweenStimuliHorizontally;
    }

    public TextField getTextFieldDistanceBetweenStimuliVertically() {
        return textFieldDistanceBetweenStimuliVertically;
    }

    public Spinner getSpinnerStimulusDisplayTime() {
        return spinnerStimulusDisplayTime;
    }

    public Spinner getSpinnerIntervalBetweenStimuliConstantPart() {
        return spinnerIntervalBetweenStimuliConstantPart;
    }

    public Spinner getSpinnerIntervalBetweenStimuliRandomPart() {
        return spinnerIntervalBetweenStimuliRandomPart;
    }
}
