package org.specvis.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.specvis.StartApplication;
import org.specvis.data.ConfigurationData;
import org.specvis.data.LuminanceScaleData;
import org.specvis.procedure.*;

/**
 * Created by pdzwiniel on 2015-05-20.
 * Last update by pdzwiniel on 2015-11-20.
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

public class Step4_FixationAndOther extends Stage {

    private ColorPicker colorPickerFixPointColor;
    private Spinner spinnerFixationPointWidth;
    private Spinner spinnerFixationPointHeight;
    private Pane paneFixationPointPreview;
    private ComboBox cbAnswerToStimulusKey;
    private ComboBox cbPauseProcedureKey;
    private ComboBox cbCancelProcedureKey;
    private ComboBox comboBoxFixationMonitor;
    private Spinner spinnerFixationCheckRate;
    private RadioButton radioButton_1;
    private RadioButton radioButton_2;
    private TitledPane titledPaneFixationMonitorSettings;
    private Spinner spinnerBlindspotDistFromFixPointHoriz;
    private Spinner spinnerBlindspotDistFromFixPointVert;
    private Spinner spinnerMonitorStimulusWidth;
    private Spinner spinnerMonitorStimulusHeight;
    private Spinner spinnerMonitorStimulusBrightness;
    private TextField textFieldMonitorStimulusLuminance;
    private Spinner spinnerFixPointChangeWidth;
    private Spinner spinnerFixPointChangeHeight;
    private ColorPicker colorPickerFixPointChangeColor;
    private VBox vBoxBlindspot;
    private VBox vBoxFixPointChange;
    private StartApplication startApplication;

    public Parent createContent() {

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> top -> step indicator */
        Text textProgressBar = new Text("Step 4/4 - Fixation & other");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(1);
        progressBar.setPrefHeight(30);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-opacity: 0.7;");
        StackPane stackPane = new StackPane();
        stackPane.setMaxWidth(Double.MAX_VALUE);
        stackPane.getChildren().setAll(progressBar, textProgressBar);

        /* layout -> center -> left */
        int equalMinWidth = 110;

        Label labelFixationPointBrightness = new Label("Fixation point color:");
        labelFixationPointBrightness.setMinWidth(equalMinWidth / 1.6);

        colorPickerFixPointColor = new ColorPicker();
        colorPickerFixPointColor.setMaxWidth(Double.MAX_VALUE);
        colorPickerFixPointColor.setOnAction(event -> setFixationPointRepresentationOnPreviewPane());
        colorPickerFixPointColor.setValue(Color.web(ConfigurationData.getFixationPointColor()));

        Button buttonMeasureChosenCustomColorLuminance = new Button("Measure luminance");
        buttonMeasureChosenCustomColorLuminance.setOnAction(event -> {

            setDataValues();

            MeasureFixationPointLuminanceStage stage = new MeasureFixationPointLuminanceStage(colorPickerFixPointColor.getValue(), "Fixation point");
            stage.show();
        });

        Label labelFixationPointWidth = new Label("Fixation point width (\u00b0)");

        spinnerFixationPointWidth = new Spinner(0.1, 100.0, 0.5, 0.1);
        spinnerFixationPointWidth.setPrefWidth(70);
        spinnerFixationPointWidth.setMaxWidth(Double.MAX_VALUE);
        spinnerFixationPointWidth.setEditable(true);
        spinnerFixationPointWidth.setOnKeyReleased(event -> setFixationPointRepresentationOnPreviewPane());
        spinnerFixationPointWidth.setOnMousePressed(event -> setFixationPointRepresentationOnPreviewPane());
        spinnerFixationPointWidth.getValueFactory().setValue(Double.valueOf(ConfigurationData.getFixationPointWidth()));

        Label labelFixationPointHeight = new Label("and height (\u00b0)");
        labelFixationPointHeight.setAlignment(Pos.CENTER);

        spinnerFixationPointHeight = new Spinner(0.1, 100.0, 0.5, 0.1);
        spinnerFixationPointHeight.setPrefWidth(70);
        spinnerFixationPointHeight.setMaxWidth(Double.MAX_VALUE);
        spinnerFixationPointHeight.setEditable(true);
        spinnerFixationPointHeight.setOnKeyReleased(event -> setFixationPointRepresentationOnPreviewPane());
        spinnerFixationPointHeight.setOnMousePressed(event -> setFixationPointRepresentationOnPreviewPane());
        spinnerFixationPointHeight.getValueFactory().setValue(Double.valueOf(ConfigurationData.getFixationPointHeight()));

        TitledPane titledPaneFixationPointPreview = new TitledPane();
        titledPaneFixationPointPreview.setText("Fixation point preview");

        paneFixationPointPreview = new Pane();
        paneFixationPointPreview.setMinSize(200, 200);
        paneFixationPointPreview.setMaxSize(200, 200);

        titledPaneFixationPointPreview.setContent(paneFixationPointPreview);

        Label labelAnswerToStimulusKey = new Label("Answer to stimulus key:");
        labelAnswerToStimulusKey.setMinWidth(equalMinWidth * 1.2);

        ObservableList<String> olListOfKeys = FXCollections.observableArrayList(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "DIGIT0", "DIGIT1", "DIGIT2", "DIGIT3", "DIGIT4", "DIGIT5",
                "DIGIT6", "DIGIT7", "DIGIT8", "DIGIT9", "NUMPAD0", "NUMPAD1",
                "NUMPAD2", "NUMPAD3", "NUMPAD4", "NUMPAD5", "NUMPAD6", "NUMPAD7",
                "NUMPAD8", "NUMPAD9", "ALT", "CONTROL", "SHIFT", "ENTER", "SPACE",
                "ESCAPE", "UP", "DOWN", "LEFT", "RIGHT");

        cbAnswerToStimulusKey = new ComboBox(olListOfKeys);
        cbAnswerToStimulusKey.setMaxWidth(Double.MAX_VALUE);
        cbAnswerToStimulusKey.getSelectionModel().select("ENTER");
        cbAnswerToStimulusKey.getSelectionModel().select(ConfigurationData.getAnswerToStimulusKey());

        Label labelPauseProcedureKey = new Label("Pause procedure key:");
        labelPauseProcedureKey.setMinWidth(equalMinWidth * 1.2);

        cbPauseProcedureKey = new ComboBox(olListOfKeys);
        cbPauseProcedureKey.setMaxWidth(Double.MAX_VALUE);
        cbPauseProcedureKey.getSelectionModel().select("SPACE");
        cbPauseProcedureKey.getSelectionModel().select(ConfigurationData.getPauseProcedureKey());

        Label labelCancelProcedureKey = new Label("Cancel procedure key:");
        labelCancelProcedureKey.setMinWidth(equalMinWidth * 1.2);

        cbCancelProcedureKey = new ComboBox(olListOfKeys);
        cbCancelProcedureKey.setMaxWidth(Double.MAX_VALUE);
        cbCancelProcedureKey.getSelectionModel().select("ESCAPE");
        cbCancelProcedureKey.getSelectionModel().select(ConfigurationData.getCancelProcedureKey());

        /* layout -> center -> right */
        Label labelFixationMonitor = new Label("Fixation monitor:");

        ObservableList<String> olFixationMonitor = FXCollections.observableArrayList("None", "Blindspot", "Fixation point change");
        comboBoxFixationMonitor = new ComboBox(olFixationMonitor);
        comboBoxFixationMonitor.setPrefWidth(160);
        comboBoxFixationMonitor.getSelectionModel().select(1);
        comboBoxFixationMonitor.setOnAction(event -> {
            String selectedItem = comboBoxFixationMonitor.getSelectionModel().getSelectedItem().toString();
            switch (selectedItem) {
                case "Blindspot":
                    titledPaneFixationMonitorSettings.setDisable(false);
                    titledPaneFixationMonitorSettings.setContent(vBoxBlindspot);
                    spinnerFixationCheckRate.setDisable(false);
                    radioButton_1.setDisable(false);
                    radioButton_2.setDisable(false);
                    break;
                case "Fixation point change":
                    titledPaneFixationMonitorSettings.setDisable(false);
                    titledPaneFixationMonitorSettings.setContent(vBoxFixPointChange);
                    spinnerFixationCheckRate.setDisable(false);
                    radioButton_1.setDisable(false);
                    radioButton_2.setDisable(false);
                    break;
                default:
                    titledPaneFixationMonitorSettings.setContent(null);
                    titledPaneFixationMonitorSettings.setExpanded(false);
                    titledPaneFixationMonitorSettings.setDisable(true);
                    spinnerFixationCheckRate.setDisable(true);
                    radioButton_1.setDisable(true);
                    radioButton_2.setDisable(true);
                    break;
            }
        });
        comboBoxFixationMonitor.getSelectionModel().select(ConfigurationData.getFixationMonitor());

        Label labelFixationCheckRate = new Label("Fixation check rate (X):");

        spinnerFixationCheckRate = new Spinner(1, 100, 4);
        spinnerFixationCheckRate.setPrefWidth(80);
        spinnerFixationCheckRate.setEditable(true);
        spinnerFixationCheckRate.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getFixationCheckRate()));

        ToggleGroup toggleGroup = new ToggleGroup();

        radioButton_1 = new RadioButton("Monitoring fixation every X stimuli");
        radioButton_1.setToggleGroup(toggleGroup);
        radioButton_1.setSelected(true);

        radioButton_2 = new RadioButton("Monitoring fixation every Y (random value from 1 to X) stimuli");
        radioButton_2.setToggleGroup(toggleGroup);

        titledPaneFixationMonitorSettings = new TitledPane();
        titledPaneFixationMonitorSettings.setText("Fixation monitor settings");
        titledPaneFixationMonitorSettings.setAlignment(Pos.CENTER);
        titledPaneFixationMonitorSettings.setExpanded(true);

        /* layout -> center -> right -> Fixation monitor: Blindspot */
        Button buttonMapBlindspot = new Button("Map blindspot");
        buttonMapBlindspot.setOnAction(event -> {

            setDataValues();

            ShellWindowForMapBlindspotProcedure shellWindowForMapBlindspotProcedure = new ShellWindowForMapBlindspotProcedure();
            shellWindowForMapBlindspotProcedure.show();

            MapBlindspotProcedure stage = new MapBlindspotProcedure(startApplication.getData(), shellWindowForMapBlindspotProcedure);
            stage.show();
        });

        Label labelBlindspotDistanceFromFixationPoint = new Label("Blindspot distance from fixation point (\u00b0)");

        Label labelBlindspotHorizontally = new Label("Horizontally:");

        spinnerBlindspotDistFromFixPointHoriz = new Spinner(-100, 100, 15, 0.1);
        spinnerBlindspotDistFromFixPointHoriz.setPrefWidth(80);
        spinnerBlindspotDistFromFixPointHoriz.setEditable(true);
        spinnerBlindspotDistFromFixPointHoriz.getValueFactory().setValue(Double.valueOf(ConfigurationData.getBlindspotDistanceFromFixationPointHorizontally()));

        Label labelBlindspotVertically = new Label("Vertically:");

        spinnerBlindspotDistFromFixPointVert = new Spinner(-100, 100, -1.5, 0.1);
        spinnerBlindspotDistFromFixPointVert.setPrefWidth(80);
        spinnerBlindspotDistFromFixPointVert.setEditable(true);
        spinnerBlindspotDistFromFixPointVert.getValueFactory().setValue(Double.valueOf(ConfigurationData.getBlindspotDistanceFromFixationPointVertically()));

        Label labelMonitorStimulusSize = new Label("Monitor stimulus characteristics");

        Button buttonMonitorStimulusPreview = new Button("Preview");
        buttonMonitorStimulusPreview.setOnAction(event -> {

            setDataValues();

            MonitorStimulusPreview stage = new MonitorStimulusPreview(startApplication.getData());
            stage.show();
        });

        Label labelMonitorStimulusWidth = new Label("Width (\u00b0):");

        spinnerMonitorStimulusWidth = new Spinner(0.1, 100, 0.5, 0.1);
        spinnerMonitorStimulusWidth.setPrefWidth(80);
        spinnerMonitorStimulusWidth.setEditable(true);
        spinnerMonitorStimulusWidth.getValueFactory().setValue(Double.valueOf(ConfigurationData.getMonitorStimulusWidth()));

        Label labelMonitorStimulusHeight = new Label("Height (\u00b0):");

        spinnerMonitorStimulusHeight = new Spinner(0.1, 100, 0.5, 0.1);
        spinnerMonitorStimulusHeight.setPrefWidth(80);
        spinnerMonitorStimulusHeight.setEditable(true);
        spinnerMonitorStimulusHeight.getValueFactory().setValue(Double.valueOf(ConfigurationData.getMonitorStimulusHeight()));

        Label labelMonitorStimulusBrightness = new Label("Brightness (%):");

        spinnerMonitorStimulusBrightness = new Spinner(0, 100, 0, 1);
        spinnerMonitorStimulusBrightness.setPrefWidth(80);
        spinnerMonitorStimulusBrightness.setEditable(true);
        spinnerMonitorStimulusBrightness.getValueFactory().setValue(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerStimulusMaxBrightness().getValue().toString()));
        spinnerMonitorStimulusBrightness.setOnKeyReleased(event -> setLuminanceForGivenTextFields(
                Integer.valueOf(spinnerMonitorStimulusBrightness.getValue().toString()),
                startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleDataForStimuli(),
                textFieldMonitorStimulusLuminance));
        spinnerMonitorStimulusBrightness.setOnMousePressed(event -> setLuminanceForGivenTextFields(
                Integer.valueOf(spinnerMonitorStimulusBrightness.getValue().toString()),
                startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleDataForStimuli(),
                textFieldMonitorStimulusLuminance));
        spinnerMonitorStimulusBrightness.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getMonitorStimulusBrightness()));

        Label labelMonitorStimulusLuminance = new Label("Luminance (cd/m2):");

        textFieldMonitorStimulusLuminance = new TextField();
        textFieldMonitorStimulusLuminance.setPrefWidth(70);
        textFieldMonitorStimulusLuminance.setEditable(false);
        textFieldMonitorStimulusLuminance.setStyle("-fx-background-color: rgb(215,215,215)");

        /* layout -> center -> right -> Fixation monitor: Fixation point change */
        Label labelFixPointChangeCharacteristics = new Label("Fixation point change characteristics");

        Label labelFixPointChangeWidth = new Label("Width (\u00b0)");

        spinnerFixPointChangeWidth = new Spinner(0.1, 100, 0.5, 0.1);
        spinnerFixPointChangeWidth.setPrefWidth(80);
        spinnerFixPointChangeWidth.setEditable(true);
        spinnerFixPointChangeWidth.getValueFactory().setValue(Double.valueOf(ConfigurationData.getFixationPointChangeWidth()));

        Label labelFixPointChangeHeight = new Label("Height (\u00b0)");

        spinnerFixPointChangeHeight = new Spinner(0.1, 100, 0.5, 0.1);
        spinnerFixPointChangeHeight.setPrefWidth(80);
        spinnerFixPointChangeHeight.setEditable(true);
        spinnerFixPointChangeHeight.getValueFactory().setValue(Double.valueOf(ConfigurationData.getFixationPointChangeHeight()));

        Label labelFixPointChangeColor = new Label("Color:");

        colorPickerFixPointChangeColor = new ColorPicker();
        colorPickerFixPointChangeColor.setMaxWidth(Double.MAX_VALUE);
        colorPickerFixPointChangeColor.setValue(Color.GREEN);
        colorPickerFixPointChangeColor.setValue(Color.web(ConfigurationData.getFixationPointChangeColor()));

        Button buttonMeasureFixationPointChangeColor = new Button("Measure luminance");
        buttonMeasureFixationPointChangeColor.setOnAction(event -> {

            setDataValues();

            MeasureFixationPointLuminanceStage stage = new MeasureFixationPointLuminanceStage(colorPickerFixPointChangeColor.getValue(), "Fixation point change");
            stage.show();
        });

        Button buttonCompareFixationPointBeforeAndAfterChange = new Button("Compare fixation point before and after change");
        buttonCompareFixationPointBeforeAndAfterChange.setOnAction(event -> {

            setDataValues();

            CompareFixationPointBeforeAndAfterChange stage = new CompareFixationPointBeforeAndAfterChange(startApplication.getData());
            stage.show();
        });

        /* layout -> center -> left (add items) */
        HBox hBox_1 = new HBox(10);
        HBox hBox_2 = new HBox(10);
        HBox hBox_3 = new HBox(10);
        HBox hBox_4 = new HBox(10);
        HBox hBox_5 = new HBox(10);

        hBox_1.getChildren().addAll(labelFixationPointBrightness, colorPickerFixPointColor, buttonMeasureChosenCustomColorLuminance);
        hBox_1.setAlignment(Pos.CENTER_LEFT);
        hBox_1.setHgrow(colorPickerFixPointColor, Priority.ALWAYS);

        hBox_2.getChildren().addAll(labelFixationPointWidth, spinnerFixationPointWidth, labelFixationPointHeight, spinnerFixationPointHeight);
        hBox_2.setAlignment(Pos.CENTER_LEFT);
        hBox_2.setHgrow(spinnerFixationPointWidth, Priority.ALWAYS);
        hBox_2.setHgrow(spinnerFixationPointHeight, Priority.ALWAYS);

        hBox_3.getChildren().addAll(labelAnswerToStimulusKey, cbAnswerToStimulusKey);
        hBox_3.setAlignment(Pos.CENTER_LEFT);
        hBox_3.setHgrow(cbAnswerToStimulusKey, Priority.ALWAYS);

        hBox_4.getChildren().addAll(labelPauseProcedureKey, cbPauseProcedureKey);
        hBox_4.setAlignment(Pos.CENTER_LEFT);
        hBox_4.setHgrow(cbPauseProcedureKey, Priority.ALWAYS);

        hBox_5.getChildren().addAll(labelCancelProcedureKey, cbCancelProcedureKey);
        hBox_5.setAlignment(Pos.CENTER_LEFT);
        hBox_5.setHgrow(cbCancelProcedureKey, Priority.ALWAYS);

        /* layout -> center -> right (add items) */
        HBox hBox_7 = new HBox(10);
        HBox hBox_8 = new HBox(10);
        HBox hBox_9 = new HBox(10);
        HBox hBox_10 = new HBox(10);

        hBox_7.getChildren().addAll(labelFixationMonitor, comboBoxFixationMonitor);
        hBox_7.setAlignment(Pos.CENTER_LEFT);

        hBox_8.getChildren().addAll(labelFixationCheckRate, spinnerFixationCheckRate);
        hBox_8.setAlignment(Pos.CENTER_LEFT);

        hBox_9.getChildren().addAll(radioButton_1);
        hBox_9.setAlignment(Pos.CENTER_LEFT);

        hBox_10.getChildren().addAll(radioButton_2);
        hBox_10.setAlignment(Pos.CENTER_LEFT);

        /* layout -> center -> right -> Fixation monitor: Blindspot (add items) */
        HBox hBox_B_1 = new HBox(10);
        HBox hBox_B_2 = new HBox(10);
        HBox hBox_B_3 = new HBox(10);
        HBox hBox_B_4 = new HBox(10);
        HBox hBox_B_5 = new HBox(10);
        HBox hBox_B_6 = new HBox(10);
        HBox hBox_B_7 = new HBox(10);

        hBox_B_1.getChildren().addAll(buttonMapBlindspot);
        hBox_B_1.setAlignment(Pos.CENTER);

        hBox_B_2.getChildren().addAll(labelBlindspotDistanceFromFixationPoint);
        hBox_B_2.setAlignment(Pos.CENTER);

        hBox_B_3.getChildren().addAll(labelBlindspotHorizontally, spinnerBlindspotDistFromFixPointHoriz, labelBlindspotVertically, spinnerBlindspotDistFromFixPointVert);
        hBox_B_3.setAlignment(Pos.CENTER);

        hBox_B_4.getChildren().addAll(labelMonitorStimulusSize, buttonMonitorStimulusPreview);
        hBox_B_4.setAlignment(Pos.CENTER);

        hBox_B_5.getChildren().addAll(labelMonitorStimulusWidth, spinnerMonitorStimulusWidth, labelMonitorStimulusHeight, spinnerMonitorStimulusHeight);
        hBox_B_5.setAlignment(Pos.CENTER);

        hBox_B_6.getChildren().addAll(labelMonitorStimulusBrightness, spinnerMonitorStimulusBrightness);
        hBox_B_6.setAlignment(Pos.CENTER);

        hBox_B_7.getChildren().addAll(labelMonitorStimulusLuminance, textFieldMonitorStimulusLuminance);
        hBox_B_7.setAlignment(Pos.CENTER);

        vBoxBlindspot = new VBox(10);
        vBoxBlindspot.getChildren().addAll(hBox_B_1, new Separator(), hBox_B_2, hBox_B_3, new Separator(), hBox_B_4, hBox_B_5, hBox_B_6, hBox_B_7);

        if (comboBoxFixationMonitor.getSelectionModel().getSelectedItem().toString().equals("Blindspot")) {
            titledPaneFixationMonitorSettings.setDisable(false);
            titledPaneFixationMonitorSettings.setContent(vBoxBlindspot);
            spinnerFixationCheckRate.setDisable(false);
            radioButton_1.setDisable(false);
            radioButton_2.setDisable(false);
        } else if (comboBoxFixationMonitor.getSelectionModel().getSelectedItem().toString().equals("Fixation point change")) {
            titledPaneFixationMonitorSettings.setDisable(false);
            titledPaneFixationMonitorSettings.setContent(vBoxFixPointChange);
            spinnerFixationCheckRate.setDisable(false);
            radioButton_1.setDisable(false);
            radioButton_2.setDisable(false);
        } else {
            titledPaneFixationMonitorSettings.setContent(null);
            titledPaneFixationMonitorSettings.setExpanded(false);
            titledPaneFixationMonitorSettings.setDisable(true);
            spinnerFixationCheckRate.setDisable(true);
            radioButton_1.setDisable(true);
            radioButton_2.setDisable(true);
        }

        /* layout -> center -> right -> Fixation monitor: Fixation point change (add items) */
        HBox hBox_F_1 = new HBox(10);
        HBox hBox_F_2 = new HBox(10);
        HBox hBox_F_3 = new HBox(10);
        HBox hBox_F_4 = new HBox(10);

        hBox_F_1.getChildren().add(labelFixPointChangeCharacteristics);
        hBox_F_1.setAlignment(Pos.CENTER);

        hBox_F_2.getChildren().addAll(labelFixPointChangeWidth, spinnerFixPointChangeWidth, labelFixPointChangeHeight, spinnerFixPointChangeHeight);
        hBox_F_2.setAlignment(Pos.CENTER);

        hBox_F_3.getChildren().addAll(labelFixPointChangeColor, colorPickerFixPointChangeColor, buttonMeasureFixationPointChangeColor);
        hBox_F_3.setAlignment(Pos.CENTER);

        hBox_F_4.getChildren().add(buttonCompareFixationPointBeforeAndAfterChange);
        hBox_F_4.setAlignment(Pos.CENTER);

        vBoxFixPointChange = new VBox(10);
        vBoxFixPointChange.getChildren().addAll(hBox_F_1, hBox_F_2, hBox_F_3, hBox_F_4);

        /* layout -> center (add items) */
        VBox vBoxLeft = new VBox(10);
        vBoxLeft.getChildren().addAll(hBox_1, hBox_2, titledPaneFixationPointPreview, hBox_3, hBox_4, hBox_5);

        VBox vBoxRight = new VBox(10);
        vBoxRight.getChildren().addAll(hBox_7, hBox_8, hBox_9, hBox_10, titledPaneFixationMonitorSettings);

        HBox hBoxPrime = new HBox(10);
        hBoxPrime.getChildren().addAll(vBoxLeft, vBoxRight);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonBack = new Button("Back");
        buttonBack.setOnAction(event -> {
            this.hide();
            startApplication.getStageStep3Stimulus().show();
        });

        Button buttonPreviewStimuliDistribution = new Button("Preview stimuli distribution");
        buttonPreviewStimuliDistribution.setOnAction(event -> {
            setDataValues();
            PreviewStimuliDistribution stage = new PreviewStimuliDistribution(startApplication.getData());
            stage.show();
        });

        Button buttonRunProcedure = new Button("Run procedure");
        buttonRunProcedure.setOnAction(event -> {

            setDataValues();

            ShellWindowForMainProcedure shellWindowForMainProcedure = new ShellWindowForMainProcedure(startApplication.getData());
            shellWindowForMainProcedure.show();

            MainProcedure mainProcedure = new MainProcedure(startApplication.getData(), shellWindowForMainProcedure);
            mainProcedure.show();
        });

        hBoxBottom.getChildren().addAll(buttonBack, buttonPreviewStimuliDistribution, buttonRunProcedure);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setTop(stackPane);
        layout.setCenter(hBoxPrime);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(hBoxPrime, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    private class MeasureFixationPointLuminanceStage extends Stage {

        private Color color;
        private String target;

        public Parent createContent() {
            BorderPane layout = new BorderPane();

            VBox vBoxCenter = new VBox(10);

            Label labelInformation = new Label("Using a manual photometer, measure the luminance (expressed in " +
                    "cd/m2) of the color closed in the box below. The result of measurement type in the field " +
                    "below the box.");
            labelInformation.setPrefWidth(200);
            labelInformation.setWrapText(true);
            labelInformation.setTextAlignment(TextAlignment.CENTER);

            Label labelFixationPointLuminance = new Label();
            labelFixationPointLuminance.setStyle("-fx-border-width: 2; " +
                    "-fx-border-color: black; " +
                    "-fx-border-style: solid; " +
                    "-fx-background-color: hsb(" + color.getHue() + ", " + color.getSaturation() * 100 + "%, "
                    + color.getBrightness() * 100 + "%);");
            labelFixationPointLuminance.setMinSize(200, 200);

            Spinner spinnerFixationPointLuminance = new Spinner(0.0, 500.0, 0.0, 0.1);
            spinnerFixationPointLuminance.setPrefWidth(200);
            spinnerFixationPointLuminance.setMaxWidth(Double.MAX_VALUE);
            spinnerFixationPointLuminance.setEditable(true);
            switch (target) {
                case "Fixation point":
                    spinnerFixationPointLuminance.getValueFactory().setValue(Double.valueOf(startApplication.getData().getLuminanceScaleDataForStimuli().getFixationPointMeasuredLuminance()));
                    break;
                case "Fixation point change":
                    spinnerFixationPointLuminance.getValueFactory().setValue(Double.valueOf(startApplication.getData().getLuminanceScaleDataForStimuli().getFixationPointChangeMeasuredLuminance()));
                    break;
            }

            vBoxCenter.getChildren().addAll(labelInformation, labelFixationPointLuminance, spinnerFixationPointLuminance);
            vBoxCenter.setAlignment(Pos.CENTER);

            HBox hBoxBottom = new HBox(10);

            Button buttonAccept = new Button("Accept");
            buttonAccept.setOnAction(event -> {
                switch (target) {
                    case "Fixation point":
                        startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleDataForStimuli().setFixationPointMeasuredLuminance(spinnerFixationPointLuminance.getValue().toString());
                        break;
                    case "Fixation point change":
                        startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleDataForStimuli().setFixationPointChangeMeasuredLuminance(spinnerFixationPointLuminance.getValue().toString());
                        break;
                }

                this.close();
            });

            Button buttonCancel = new Button("Cancel");
            buttonCancel.setOnAction(event -> this.close());

            hBoxBottom.getChildren().addAll(buttonAccept, buttonCancel);
            hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

            layout.setCenter(vBoxCenter);
            layout.setBottom(hBoxBottom);

            BorderPane.setMargin(vBoxCenter, new Insets(10, 10, 10, 10));
            BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

            return layout;
        }

        private MeasureFixationPointLuminanceStage(Color color, String target) {
            this.color = color;
            this.target = target;
            this.setScene(new Scene(createContent()));
            this.setTitle("Specvis");
            this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
        }
    }

    public Step4_FixationAndOther(StartApplication startApplication) {
        this.startApplication = startApplication;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public void setLuminanceForGivenTextFields(int brightness, LuminanceScaleData lsd, TextField textFieldLuminance) {
        double luminance = lsd.getLuminanceForBrightness()[brightness];
        double value = startApplication.getStageStep2ScreenAndLuminance().getScreenLuminanceFunctions().round(luminance, 2);
        if (value < 0) {
            value = 0.0;
        }
        textFieldLuminance.setText(String.valueOf(value));
    }

    public void setPaneFixationPointPreviewBackgroundColor(int hsb, int saturation, int brightness) {
        paneFixationPointPreview.setStyle("-fx-background-color: hsb(" + hsb + ", " + saturation + "%, " + brightness + "%);");
    }

    public Ellipse createFixationPoint(double positionX, double positionY, double width, double height, Color color) {

        double screenResolutionX = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionX().getText());
        double screenResolutionY = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionY().getText());

        double visualFieldX = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldX().getText());
        double visualFieldY = Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldY().getText());

        double pixelsForOneDegreeX = screenResolutionX / visualFieldX;
        double pixelsForOneDegreeY = screenResolutionY / visualFieldY;

        double radiusX = (width / 2) * pixelsForOneDegreeX;
        double radiusY = (height / 2) * pixelsForOneDegreeY;

        Ellipse fixationPoint;
        fixationPoint = new Ellipse(positionX, positionY, radiusX, radiusY);
        fixationPoint.setFill(color);
        fixationPoint.setStroke(color);
        return fixationPoint;
    }

    public void setFixationPointRepresentationOnPreviewPane() {
        if (paneFixationPointPreview.getChildren().size() > 0) {
            paneFixationPointPreview.getChildren().remove(paneFixationPointPreview.getChildren().size() - 1);
        }
        Shape fixationPointRepresentation = createFixationPoint(100, 100,
                Double.valueOf(spinnerFixationPointWidth.getValue().toString()),
                Double.valueOf(spinnerFixationPointHeight.getValue().toString()),
                colorPickerFixPointColor.getValue());
        paneFixationPointPreview.getChildren().addAll(fixationPointRepresentation);
    }

    public void setDataValues() {
        // Step 1 - Patient
        startApplication.getData().setPatientInfoData(startApplication.getStageStep1Patient().getPatientInfo());
        startApplication.getData().setExaminedEye(startApplication.getStageStep1Patient().getCbExaminedEye().getSelectionModel().getSelectedItem().toString());

        // Step 2 - Luminance scale & screen
        startApplication.getData().setScreenLuminanceFunctions(startApplication.getStageStep2ScreenAndLuminance().getScreenLuminanceFunctions());
        startApplication.getData().setLuminanceScaleDataForStimuli(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleDataForStimuli());
        startApplication.getData().setLuminanceScaleDataForBackground(startApplication.getStageStep2ScreenAndLuminance().getLuminanceScaleDataForBackground());
        startApplication.getData().setActiveDisplayIndex(startApplication.getStageStep2ScreenAndLuminance().getCbActiveScreen().getSelectionModel().getSelectedIndex());
        startApplication.getData().setScreenResolutionX(Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionX().getText()));
        startApplication.getData().setScreenResolutionY(Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldScreenResolutionY().getText()));
        startApplication.getData().setVisualFieldX(Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldX().getText()));
        startApplication.getData().setVisualFieldY(Double.valueOf(startApplication.getStageStep2ScreenAndLuminance().getTextFieldInvolvedVisualFieldY().getText()));
        startApplication.getData().setScreenWidthInMm(Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getSpinnerScreenWidth().getValue().toString()));
        startApplication.getData().setScreenHeightInMm(Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getSpinnerScreenHeight().getValue().toString()));
        startApplication.getData().setPatientDistanceInMm(Integer.valueOf(startApplication.getStageStep2ScreenAndLuminance().getSpinnerPatientDistance().getValue().toString()));

        // Step 3 - Stimulus & background
        startApplication.getData().setStimulusMaxBrightness(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerStimulusMaxBrightness().getValue().toString()));
        startApplication.getData().setStimulusMinBrightness(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerStimulusMinBrightness().getValue().toString()));
        startApplication.getData().setStimulusShape(startApplication.getStageStep3Stimulus().getCbStimulusShape().getSelectionModel().getSelectedItem().toString());
        startApplication.getData().setStimulusInclination(Double.valueOf(startApplication.getStageStep3Stimulus().getSpinnerStimulusInclination().getValue().toString()));
        startApplication.getData().setStimulusWidthInDegrees(Double.valueOf(startApplication.getStageStep3Stimulus().getSpinnerStimulusWidth().getValue().toString()));
        startApplication.getData().setStimulusHeightInDegrees(Double.valueOf(startApplication.getStageStep3Stimulus().getSpinnerStimulusHeight().getValue().toString()));
        startApplication.getData().setStimulusDisplayTime(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerStimulusDisplayTime().getValue().toString()));
        startApplication.getData().setTimeIntervalBetweenStimuliConstantPart(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerIntervalBetweenStimuliConstantPart().getValue().toString()));
        startApplication.getData().setTimeIntervalBetweenStimuliRandomPart(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerIntervalBetweenStimuliRandomPart().getValue().toString()));
        startApplication.getData().setDistanceBetweenStimuliHorizontally(Double.valueOf(startApplication.getStageStep3Stimulus().getTextFieldDistanceBetweenStimuliHorizontally().getText()));
        startApplication.getData().setDistanceBetweenStimuliVertically(Double.valueOf(startApplication.getStageStep3Stimulus().getTextFieldDistanceBetweenStimuliVertically().getText()));
        startApplication.getData().setBackgroundBrightness(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerBackgroundBrightness().getValue().toString()));
        startApplication.getData().setQuarterGridResolutionX(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerQuarterGridResolutionX().getValue().toString()));
        startApplication.getData().setQuarterGridResolutionY(Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerQuarterGridResolutionY().getValue().toString()));

        // Step 4 - Fiation & other
        startApplication.getData().setFixPointHue((int) colorPickerFixPointColor.getValue().getHue());
        startApplication.getData().setFixPointSaturation((int) (colorPickerFixPointColor.getValue().getSaturation() * 100));
        startApplication.getData().setFixPointBrightness((int) (colorPickerFixPointColor.getValue().getBrightness() * 100));
        startApplication.getData().setFixPointWidth(Double.valueOf(spinnerFixationPointWidth.getValue().toString()));
        startApplication.getData().setFixPointHeight(Double.valueOf(spinnerFixationPointHeight.getValue().toString()));
        startApplication.getData().setFixationMonitor(comboBoxFixationMonitor.getSelectionModel().getSelectedItem().toString());
        startApplication.getData().setFixationCheckRate(Integer.valueOf(spinnerFixationCheckRate.getValue().toString()));
        startApplication.getData().setMonitoringFixationEvery_X_Stimuli(radioButton_1.isSelected());
        startApplication.getData().setMonitoringFixationEvery_Y_Stimuli(radioButton_2.isSelected());
        startApplication.getData().setBlindspotDistanceFromFixPointHorizontally(Double.valueOf(spinnerBlindspotDistFromFixPointHoriz.getValue().toString()));
        startApplication.getData().setBlindspotDistanceFromFixPointVertically(Double.valueOf(spinnerBlindspotDistFromFixPointVert.getValue().toString()));
        startApplication.getData().setMonitorStimulusWidth(Double.valueOf(spinnerMonitorStimulusWidth.getValue().toString()));
        startApplication.getData().setMonitorStimulusHeight(Double.valueOf(spinnerMonitorStimulusHeight.getValue().toString()));
        startApplication.getData().setMonitorStimulusBrightness(Integer.valueOf(spinnerMonitorStimulusBrightness.getValue().toString()));
        startApplication.getData().setFixPointChangeWidth(Double.valueOf(spinnerFixPointChangeWidth.getValue().toString()));
        startApplication.getData().setFixPointChangeHeight(Double.valueOf(spinnerFixPointChangeHeight.getValue().toString()));
        startApplication.getData().setFixPointChangeHue((int) colorPickerFixPointChangeColor.getValue().getHue());
        startApplication.getData().setFixPointChangeSaturation((int) (colorPickerFixPointChangeColor.getValue().getSaturation() * 100));
        startApplication.getData().setFixPointChangeBrightness((int) (colorPickerFixPointChangeColor.getValue().getBrightness() * 100));
        startApplication.getData().setAnswerToStimulusKey(cbAnswerToStimulusKey.getSelectionModel().getSelectedItem().toString());
        startApplication.getData().setPauseProcedureKey(cbPauseProcedureKey.getSelectionModel().getSelectedItem().toString());
        startApplication.getData().setCancelProcedureKey(cbCancelProcedureKey.getSelectionModel().getSelectedItem().toString());
    }

    public Spinner getSpinnerMonitorStimulusBrightness() {
        return spinnerMonitorStimulusBrightness;
    }

    public TextField getTextFieldMonitorStimulusLuminance() {
        return textFieldMonitorStimulusLuminance;
    }
}
