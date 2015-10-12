package org.specvis.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.specvis.StartApplication;
import org.specvis.data.ConfigurationData;
import org.specvis.data.LuminanceScaleData;
import org.specvis.logic.ScreenLuminanceFunctions;

import java.awt.*;

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

public class Step2_LuminanceScaleAndScreen extends Stage {

    private StartApplication startApplication;
    private ScreenLuminanceFunctions screenLuminanceFunctions;
    private LuminanceScaleData luminanceScaleData;
    private ComboBox cbActiveScreen;
    private TextField textFieldScreenResolutionX;
    private TextField textFieldScreenResolutionY;
    private Spinner spinnerScreenWidth;
    private Spinner spinnerScreenHeight;
    private Spinner spinnerPatientDistance;
    private TextField textFieldInvolvedVisualFieldX;
    private TextField textFieldInvolvedVisualFieldY;
    private TextField textFieldScreenLuminanceScale;
    private TextField textFieldB0;
    private TextField textFieldB20;
    private TextField textFieldB40;
    private TextField textFieldB60;
    private TextField textFieldB80;
    private TextField textFieldB100;

    public Parent createContent() {

        /* init logic */
        screenLuminanceFunctions = new ScreenLuminanceFunctions();

        /* init data */
        luminanceScaleData = new LuminanceScaleData();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> top -> step indicator */
        Text textProgressBar = new Text("Step 2/4 - Luminance scale & screen");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0.50);
        progressBar.setPrefHeight(30);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-opacity: 0.7;");
        StackPane stackPane = new StackPane();
        stackPane.setMaxWidth(Double.MAX_VALUE);
        stackPane.getChildren().setAll(progressBar, textProgressBar);

        /* layout -> center */
        int equalMinWidth = 110;

        Label labelActiveScreen = new Label("Active screen:");
        labelActiveScreen.setMinWidth(equalMinWidth * 1.1);

        cbActiveScreen = new ComboBox(screenLuminanceFunctions.getActiveDisplaysObservableList());
        cbActiveScreen.setOnAction(event -> {
            if (cbActiveScreen.getItems().size() > 0) {
                int index = cbActiveScreen.getSelectionModel().getSelectedIndex();
                GraphicsDevice activeDisplay = screenLuminanceFunctions.getActiveDisplaysGraphicsDevices()[index];
                int[] activeDisplayResolution = screenLuminanceFunctions.getAcriveDisplayResolution(activeDisplay);
                textFieldScreenResolutionX.setText(String.valueOf(activeDisplayResolution[0]));
                textFieldScreenResolutionY.setText(String.valueOf(activeDisplayResolution[1]));

                setInvolvedVisualFieldXY(screenLuminanceFunctions, 2);
            } else {
                event.consume();
            }
        });
        cbActiveScreen.getSelectionModel().select(0);

        Button buttonRefreshActiveScreenList = new Button("R");
        buttonRefreshActiveScreenList.setOnAction(event1 -> {
            cbActiveScreen.getItems().clear();
            cbActiveScreen.setItems(screenLuminanceFunctions.getActiveDisplaysObservableList());
            cbActiveScreen.getSelectionModel().select(0);

            setInvolvedVisualFieldXY(screenLuminanceFunctions, 2);
        });

        Label labelScreenResolution = new Label("Resolution:");
        labelScreenResolution.setMinWidth(equalMinWidth * 1.1);

        textFieldScreenResolutionX = new TextField();
        textFieldScreenResolutionX.setPrefWidth(equalMinWidth / 1.5);
        textFieldScreenResolutionX.setEditable(false);
        textFieldScreenResolutionX.setStyle("-fx-background-color: rgb(215,215,215)");

        Label labelScreenResolutionSpace = new Label("x");

        textFieldScreenResolutionY = new TextField();
        textFieldScreenResolutionY.setPrefWidth(equalMinWidth / 1.5);
        textFieldScreenResolutionY.setEditable(false);
        textFieldScreenResolutionY.setStyle("-fx-background-color: rgb(215,215,215)");

        int index = cbActiveScreen.getSelectionModel().getSelectedIndex();
        GraphicsDevice activeDisplay = screenLuminanceFunctions.getActiveDisplaysGraphicsDevices()[index];
        int[] activeDisplayResolution = screenLuminanceFunctions.getAcriveDisplayResolution(activeDisplay);
        textFieldScreenResolutionX.setText(String.valueOf(activeDisplayResolution[0]));
        textFieldScreenResolutionY.setText(String.valueOf(activeDisplayResolution[1]));

        Label labelScreenWidth = new Label("Width:");
        labelScreenWidth.setMinWidth(equalMinWidth * 1.1);

        spinnerScreenWidth = new Spinner(1, 10000, 535);
        spinnerScreenWidth.setEditable(true);
        spinnerScreenWidth.setPrefWidth(equalMinWidth / 1.5);
        spinnerScreenWidth.setOnKeyReleased(event -> setInvolvedVisualFieldXY(screenLuminanceFunctions, 2));
        spinnerScreenWidth.setOnMousePressed(event -> setInvolvedVisualFieldXY(screenLuminanceFunctions, 2));
        spinnerScreenWidth.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getScreenWidth()));

        Label labelScreenHeight = new Label("Height:");
        labelScreenHeight.setAlignment(Pos.CENTER);

        spinnerScreenHeight = new Spinner(1, 10000, 300);
        spinnerScreenHeight.setEditable(true);
        spinnerScreenHeight.setPrefWidth(equalMinWidth / 1.5);
        spinnerScreenHeight.setMaxWidth(Double.MAX_VALUE);
        spinnerScreenHeight.setOnKeyReleased(event -> setInvolvedVisualFieldXY(screenLuminanceFunctions, 2));
        spinnerScreenHeight.setOnMousePressed(event -> setInvolvedVisualFieldXY(screenLuminanceFunctions, 2));
        spinnerScreenHeight.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getScreenHeight()));

        Label labelScreenWidthHeightMM = new Label("(mm)");

        Label labelPatientDistance = new Label("Patient distance (mm):");
        labelPatientDistance.setMinWidth(equalMinWidth * 1.1);

        spinnerPatientDistance = new Spinner(1, 10000, 500);
        spinnerPatientDistance.setEditable(true);
        spinnerPatientDistance.setPrefWidth(equalMinWidth / 1.5);
        spinnerPatientDistance.setOnKeyReleased(event -> setInvolvedVisualFieldXY(screenLuminanceFunctions, 2));
        spinnerPatientDistance.setOnMousePressed(event -> setInvolvedVisualFieldXY(screenLuminanceFunctions, 2));
        spinnerPatientDistance.getValueFactory().setValue(Integer.valueOf(ConfigurationData.getPatientDistance()));

        Label labelInvolvedVisualField = new Label("Involved visual field (\u00b0):");
        labelInvolvedVisualField.setMinWidth(equalMinWidth * 1.1);

        textFieldInvolvedVisualFieldX = new TextField();
        textFieldInvolvedVisualFieldX.setPrefWidth(equalMinWidth / 1.5);
        textFieldInvolvedVisualFieldX.setEditable(false);
        textFieldInvolvedVisualFieldX.setStyle("-fx-background-color: rgb(215,215,215)");

        textFieldInvolvedVisualFieldY = new TextField();
        textFieldInvolvedVisualFieldY.setPrefWidth(equalMinWidth / 1.5);
        textFieldInvolvedVisualFieldY.setEditable(false);
        textFieldInvolvedVisualFieldY.setStyle("-fx-background-color: rgb(215,215,215)");

        setInvolvedVisualFieldXY(screenLuminanceFunctions, 2);

        Label labelScreenLuminanceScale = new Label("Screen luminance scale name:");
        labelScreenLuminanceScale.setMinWidth(equalMinWidth * 1.4);

        textFieldScreenLuminanceScale = new TextField();
        textFieldScreenLuminanceScale.setPrefWidth(equalMinWidth / 2);
        textFieldScreenLuminanceScale.setMaxWidth(Double.MAX_VALUE);
        textFieldScreenLuminanceScale.setEditable(false);
        textFieldScreenLuminanceScale.setStyle("-fx-background-color: rgb(215,215,215)");

        Button buttonInfoAboutScale = new Button("?");
        buttonInfoAboutScale.setOnAction(event -> {
            if (!textFieldScreenLuminanceScale.getText().equals("")) {
                Step2_ScaleInfo stage = new Step2_ScaleInfo(this);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Choose scale first.");
                alert.setContentText("You can't open screen luminance scale info window without selecting a scale. " +
                        "Create new scale or choose existing one and than try again.");
                alert.showAndWait();
            }
        });

        Button buttonPreviewLuminanceBrightnessScale = new Button("P");
        buttonPreviewLuminanceBrightnessScale.setOnAction(event -> {
            if (!textFieldScreenLuminanceScale.getText().equals("")) {
                Step2_ScalePreview stage = new Step2_ScalePreview(luminanceScaleData);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Choose scale first.");
                alert.setContentText("You can't open screen luminance scale preview window without selecting a scale. " +
                        "Create new scale or choose existing one and than try again.");
                alert.showAndWait();
            }
        });

        Button buttonNewScale = new Button("New scale");
        buttonNewScale.setOnAction(event -> {
            Step2_NewScale stage = new Step2_NewScale(this);
            stage.show();
        });
        buttonNewScale.setMaxWidth(Double.MAX_VALUE);

        Button buttonExistingScale = new Button("Existing scale");
        buttonExistingScale.setOnAction(event -> {
            Step2_ExistingScale stage = new Step2_ExistingScale(this);
            stage.show();
        });
        buttonExistingScale.setMaxWidth(Double.MAX_VALUE);

        Label labelLuminance = new Label("Screen luminance (cd/m2) for specific brightness (%)");

        Label labelB0 = new Label("B0:");
        labelB0.setMinWidth(equalMinWidth/3.5);

        textFieldB0 = new TextField();
        textFieldB0.setPrefWidth(equalMinWidth / 2);
        textFieldB0.setEditable(false);
        textFieldB0.setStyle("-fx-background-color: rgb(215,215,215)");

        Label labelB20 = new Label("B20:");
        labelB20.setMinWidth(equalMinWidth/3.5);

        textFieldB20 = new TextField();
        textFieldB20.setPrefWidth(equalMinWidth / 2);
        textFieldB20.setEditable(false);
        textFieldB20.setStyle("-fx-background-color: rgb(215,215,215)");

        Label labelB40 = new Label("B40:");
        labelB40.setMinWidth(equalMinWidth/3.5);

        textFieldB40 = new TextField();
        textFieldB40.setPrefWidth(equalMinWidth / 2);
        textFieldB40.setEditable(false);
        textFieldB40.setStyle("-fx-background-color: rgb(215,215,215)");

        Label labelB60 = new Label("B60:");
        labelB60.setMinWidth(equalMinWidth/3.5);

        textFieldB60 = new TextField();
        textFieldB60.setPrefWidth(equalMinWidth / 2);
        textFieldB60.setEditable(false);
        textFieldB60.setStyle("-fx-background-color: rgb(215,215,215)");

        Label labelB80 = new Label("B80:");
        labelB80.setMinWidth(equalMinWidth/3.5);

        textFieldB80 = new TextField();
        textFieldB80.setPrefWidth(equalMinWidth / 2);
        textFieldB80.setEditable(false);
        textFieldB80.setStyle("-fx-background-color: rgb(215,215,215)");

        Label labelB100 = new Label("B100:");
        labelB100.setMinWidth(equalMinWidth / 3.5);

        textFieldB100 = new TextField();
        textFieldB100.setPrefWidth(equalMinWidth / 2);
        textFieldB100.setEditable(false);
        textFieldB100.setStyle("-fx-background-color: rgb(215,215,215)");

        /* layout -> center (add items) */
        HBox h1 = new HBox(10);
        HBox h2 = new HBox(10);
        HBox h3 = new HBox(10);
        HBox h4 = new HBox(10);
        HBox h5 = new HBox(10);

        h1.getChildren().addAll(labelActiveScreen, cbActiveScreen, buttonRefreshActiveScreenList);
        h1.setAlignment(Pos.CENTER_LEFT);

        h2.getChildren().addAll(labelScreenResolution, textFieldScreenResolutionX, labelScreenResolutionSpace, textFieldScreenResolutionY);
        h2.setAlignment(Pos.CENTER_LEFT);

        h3.getChildren().addAll(labelScreenWidth, spinnerScreenWidth, labelScreenHeight, spinnerScreenHeight, labelScreenWidthHeightMM);
        h3.setAlignment(Pos.CENTER_LEFT);

        h4.getChildren().addAll(labelPatientDistance, spinnerPatientDistance);
        h4.setAlignment(Pos.CENTER_LEFT);

        h5.getChildren().addAll(labelInvolvedVisualField, textFieldInvolvedVisualFieldX, textFieldInvolvedVisualFieldY);
        h5.setAlignment(Pos.CENTER_LEFT);

        VBox vBoxScreen = new VBox(10);
        vBoxScreen.getChildren().addAll(h1, h2, h3, h4, h5);

        TitledPane titledPaneScreen = new TitledPane("Screen", vBoxScreen);

        HBox h6 = new HBox(10);
        HBox h7 = new HBox(10);
        HBox h8 = new HBox(10);
        HBox h9 = new HBox(10);
        VBox h9_vBox1 = new VBox(5);
        VBox h9_vBox2 = new VBox(5);
        HBox h9_vBox1_h1 = new HBox(10);
        HBox h9_vBox1_h2 = new HBox(10);
        HBox h9_vBox1_h3 = new HBox(10);
        HBox h9_vBox2_h1 = new HBox(10);
        HBox h9_vBox2_h2 = new HBox(10);
        HBox h9_vBox2_h3 = new HBox(10);

        h6.getChildren().addAll(labelScreenLuminanceScale, textFieldScreenLuminanceScale, buttonInfoAboutScale, buttonPreviewLuminanceBrightnessScale);
        h6.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textFieldScreenLuminanceScale, Priority.ALWAYS);

        h7.getChildren().addAll(buttonNewScale, buttonExistingScale);
        h7.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(buttonNewScale, Priority.ALWAYS);
        HBox.setHgrow(buttonExistingScale, Priority.ALWAYS);

        h8.getChildren().addAll(labelLuminance);
        h8.setAlignment(Pos.CENTER);

        h9_vBox1_h1.getChildren().addAll(labelB0, textFieldB0);
        h9_vBox1_h1.setAlignment(Pos.CENTER_LEFT);

        h9_vBox1_h2.getChildren().addAll(labelB20, textFieldB20);
        h9_vBox1_h2.setAlignment(Pos.CENTER_LEFT);

        h9_vBox1_h3.getChildren().addAll(labelB40, textFieldB40);
        h9_vBox1_h3.setAlignment(Pos.CENTER_LEFT);

        h9_vBox1.getChildren().addAll(h9_vBox1_h1, h9_vBox1_h2, h9_vBox1_h3);

        h9_vBox2_h1.getChildren().addAll(labelB60, textFieldB60);
        h9_vBox2_h1.setAlignment(Pos.CENTER_LEFT);

        h9_vBox2_h2.getChildren().addAll(labelB80, textFieldB80);
        h9_vBox2_h2.setAlignment(Pos.CENTER_LEFT);

        h9_vBox2_h3.getChildren().addAll(labelB100, textFieldB100);
        h9_vBox2_h3.setAlignment(Pos.CENTER_LEFT);

        h9_vBox2.getChildren().addAll(h9_vBox2_h1, h9_vBox2_h2, h9_vBox2_h3);

        h9.getChildren().addAll(h9_vBox1, h9_vBox2);
        h9.setAlignment(Pos.CENTER);

        VBox vBoxScale = new VBox(10);
        vBoxScale.getChildren().addAll(h6, h7, h8, h9);

        TitledPane titledPaneScale = new TitledPane("Luminance scale", vBoxScale);

        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(titledPaneScale, titledPaneScreen);
        accordion.setExpandedPane(titledPaneScale);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonBack = new Button("Back");
        buttonBack.setOnAction(event -> {
            this.hide();
            startApplication.getStageStep1Patient().show();
        });

        Button buttonNext = new Button("Next");
        buttonNext.setOnAction(event -> {
            if (!textFieldScreenLuminanceScale.getText().equals("")) {
                this.hide();
                startApplication.getStageStep3Stimulus().show();
                startApplication.getStageStep3Stimulus().fireSpinnerStimulusMaxBrightness();
                startApplication.getStageStep3Stimulus().setTextForDistanceBetweenStimuliTextFields();
                startApplication.getStageStep3Stimulus().fireSpinnerBackgroundBrightness();
                startApplication.getStageStep3Stimulus().setScreenLuminanceFunctions(screenLuminanceFunctions);
                startApplication.getStageStep3Stimulus().setPaneStimulusPreviewBackgroundColor(
                        Integer.valueOf(luminanceScaleData.getScaleHue()),
                        Integer.valueOf(luminanceScaleData.getScaleSaturation()),
                        Integer.valueOf(startApplication.getStageStep3Stimulus().getSpinnerBackgroundBrightness()
                                .getValue().toString()));
                startApplication.getStageStep3Stimulus().setStimulusRepresentationOnPreviewPane();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Choose scale first.");
                alert.setContentText("You can't go further without selecting a scale. " +
                        "Create new scale or choose existing one and than try again.");
                alert.showAndWait();
            }
        });

        hBoxBottom.getChildren().addAll(buttonBack, buttonNext);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setTop(stackPane);
        layout.setCenter(accordion);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    public Step2_LuminanceScaleAndScreen(StartApplication startApplication) {
        this.startApplication = startApplication;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public void setInvolvedVisualFieldXY(ScreenLuminanceFunctions screenLuminanceFunctions, int round) {
        int scrX = Integer.valueOf(spinnerScreenWidth.getValue().toString());
        int scrY = Integer.valueOf(spinnerScreenHeight.getValue().toString());
        int patientDist = Integer.valueOf(spinnerPatientDistance.getValue().toString());

        double[] visField = screenLuminanceFunctions.getInvolvedVisualField(scrX, scrY, patientDist);
        double visX = screenLuminanceFunctions.round(visField[0], round);
        double visY = screenLuminanceFunctions.round(visField[1], round);

        textFieldInvolvedVisualFieldX.setText(String.valueOf(visX));
        textFieldInvolvedVisualFieldY.setText(String.valueOf(visY));
    }

    public TextField getTextFieldB0() {
        return textFieldB0;
    }

    public TextField getTextFieldB20() {
        return textFieldB20;
    }

    public TextField getTextFieldB40() {
        return textFieldB40;
    }

    public TextField getTextFieldB60() {
        return textFieldB60;
    }

    public TextField getTextFieldB80() {
        return textFieldB80;
    }

    public TextField getTextFieldB100() {
        return textFieldB100;
    }

    public TextField getTextFieldScreenLuminanceScale() {
        return textFieldScreenLuminanceScale;
    }

    public LuminanceScaleData getLuminanceScaleData() {
        return luminanceScaleData;
    }

    public void setLuminanceScaleData(LuminanceScaleData luminanceScaleData) {
        this.luminanceScaleData = luminanceScaleData;
    }

    public ScreenLuminanceFunctions getScreenLuminanceFunctions() {
        return screenLuminanceFunctions;
    }

    public TextField getTextFieldScreenResolutionX() {
        return textFieldScreenResolutionX;
    }

    public TextField getTextFieldScreenResolutionY() {
        return textFieldScreenResolutionY;
    }

    public TextField getTextFieldInvolvedVisualFieldX() {
        return textFieldInvolvedVisualFieldX;
    }

    public TextField getTextFieldInvolvedVisualFieldY() {
        return textFieldInvolvedVisualFieldY;
    }

    public Spinner getSpinnerScreenWidth() {
        return spinnerScreenWidth;
    }

    public Spinner getSpinnerScreenHeight() {
        return spinnerScreenHeight;
    }

    public Spinner getSpinnerPatientDistance() {
        return spinnerPatientDistance;
    }

    public ComboBox getCbActiveScreen() {
        return cbActiveScreen;
    }
}
