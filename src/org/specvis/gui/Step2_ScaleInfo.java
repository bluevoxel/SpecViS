package org.specvis.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.specvis.data.ConfigurationData;
import org.specvis.data.LuminanceScaleData;
import org.specvis.logic.ExceptionDialog;
import org.specvis.logic.ScreenLuminanceFunctions;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pdzwiniel on 2015-05-28.
 * Last update by pdzwiniel on 2015-11-12.
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

public class Step2_ScaleInfo extends Stage {

    private String scaleType; // "Stimulus" or "Background".

    private Step2_ScreenAndLuminanceScale step2_ScreenAndLuminanceScale;
    private LuminanceScaleData luminanceScaleData;

    private Label labelColor;
    private Label labelB0;
    private Label labelB20;
    private Label labelB40;
    private Label labelB60;
    private Label labelB80;
    private Label labelB100;

    public Parent createContent() {

        /* init logic */
        ScreenLuminanceFunctions screenLuminanceFunctions = new ScreenLuminanceFunctions();

        /* init data */
        switch (scaleType) {
            case "Stimulus":
                luminanceScaleData = step2_ScreenAndLuminanceScale.getLuminanceScaleDataForStimuli();
                break;
            case "Background":
                luminanceScaleData = step2_ScreenAndLuminanceScale.getLuminanceScaleDataForBackground();
                break;
        }

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> center */
        int prefWidth = 200;
        int equalMinWidth = 100;

        VBox vBox = new VBox(10);

        HBox hBox_9 = new HBox(10);

        Label labelHue = new Label("Hue:");

        Spinner spinnerHue = new Spinner(0, 360, 0);
        spinnerHue.setEditable(true);
        spinnerHue.getValueFactory().setValue(Integer.valueOf(luminanceScaleData.getScaleHue()));

        Label labelSaturation = new Label("Saturation:");

        Spinner spinnerSaturation = new Spinner(0, 100, 0);
        spinnerSaturation.setEditable(true);
        spinnerSaturation.getValueFactory().setValue(Integer.valueOf(luminanceScaleData.getScaleSaturation()));

        labelColor = new Label();
        labelColor.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #ffffff;");
        labelColor.setMinSize(prefWidth / 10, prefWidth / 10);

        spinnerHue.setOnKeyReleased(event -> setMultipleLabelsBackgroundColors(Integer.valueOf(spinnerHue.getValue().toString()),
                Integer.valueOf(spinnerSaturation.getValue().toString())));
        spinnerHue.setOnMouseClicked(event -> setMultipleLabelsBackgroundColors(Integer.valueOf(spinnerHue.getValue().toString()),
                Integer.valueOf(spinnerSaturation.getValue().toString())));
        spinnerSaturation.setOnKeyReleased(event -> setMultipleLabelsBackgroundColors(Integer.valueOf(spinnerHue.getValue().toString()),
                Integer.valueOf(spinnerSaturation.getValue().toString())));
        spinnerSaturation.setOnMouseClicked(event -> setMultipleLabelsBackgroundColors(Integer.valueOf(spinnerHue.getValue().toString()),
                Integer.valueOf(spinnerSaturation.getValue().toString())));

        hBox_9.getChildren().addAll(labelHue, spinnerHue, labelSaturation, spinnerSaturation, labelColor);
        hBox_9.setAlignment(Pos.CENTER_LEFT);

        HBox hBox_2 = new HBox(10);

        labelB0 = new Label();
        labelB0.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #000000;");
        labelB0.setMinSize(prefWidth, prefWidth);

        labelB20 = new Label();
        labelB20.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #000000;");
        labelB20.setMinSize(prefWidth, prefWidth);

        labelB40 = new Label();
        labelB40.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #000000;");
        labelB40.setMinSize(prefWidth, prefWidth);

        hBox_2.getChildren().addAll(labelB0, labelB20, labelB40);
        hBox_2.setAlignment(Pos.CENTER);

        HBox hBox_3 = new HBox(10);

        Spinner spinnerB0 = new Spinner(0.0, 500.0, 0.0, 0.1);
        spinnerB0.setPrefWidth(prefWidth);
        spinnerB0.setMaxWidth(Double.MAX_VALUE);
        spinnerB0.setEditable(true);
        spinnerB0.getValueFactory().setValue(Double.valueOf(luminanceScaleData.getScaleB0()));

        Spinner spinnerB20 = new Spinner(0.0, 500.0, 0.0, 0.1);
        spinnerB20.setPrefWidth(prefWidth);
        spinnerB20.setMaxWidth(Double.MAX_VALUE);
        spinnerB20.setEditable(true);
        spinnerB20.getValueFactory().setValue(Double.valueOf(luminanceScaleData.getScaleB20()));

        Spinner spinnerB40 = new Spinner(0.0, 500.0, 0.0, 0.1);
        spinnerB40.setPrefWidth(prefWidth);
        spinnerB40.setMaxWidth(Double.MAX_VALUE);
        spinnerB40.setEditable(true);
        spinnerB40.getValueFactory().setValue(Double.valueOf(luminanceScaleData.getScaleB40()));

        hBox_3.getChildren().addAll(spinnerB0, spinnerB20, spinnerB40);
        hBox_3.setAlignment(Pos.CENTER);
        HBox.setHgrow(spinnerB0, Priority.ALWAYS);
        HBox.setHgrow(spinnerB20, Priority.ALWAYS);
        HBox.setHgrow(spinnerB40, Priority.ALWAYS);

        HBox hBox_4 = new HBox(10);

        labelB60 = new Label();
        labelB60.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #000000;");
        labelB60.setMinSize(prefWidth, prefWidth);

        labelB80 = new Label();
        labelB80.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #000000;");
        labelB80.setMinSize(prefWidth, prefWidth);

        labelB100 = new Label();
        labelB100.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #000000;");
        labelB100.setMinSize(prefWidth, prefWidth);

        hBox_4.getChildren().addAll(labelB60, labelB80, labelB100);
        hBox_4.setAlignment(Pos.CENTER);

        HBox hBox_5 = new HBox(10);

        Spinner spinnerB60 = new Spinner(0.0, 500.0, 0.0, 0.1);
        spinnerB60.setPrefWidth(prefWidth);
        spinnerB60.setMaxWidth(Double.MAX_VALUE);
        spinnerB60.setEditable(true);
        spinnerB60.getValueFactory().setValue(Double.valueOf(luminanceScaleData.getScaleB60()));

        Spinner spinnerB80 = new Spinner(0.0, 500.0, 0.0, 0.1);
        spinnerB80.setPrefWidth(prefWidth);
        spinnerB80.setMaxWidth(Double.MAX_VALUE);
        spinnerB80.setEditable(true);
        spinnerB80.getValueFactory().setValue(Double.valueOf(luminanceScaleData.getScaleB80()));

        Spinner spinnerB100 = new Spinner(0.0, 500.0, 0.0, 0.1);
        spinnerB100.setPrefWidth(prefWidth);
        spinnerB100.setMaxWidth(Double.MAX_VALUE);
        spinnerB100.setEditable(true);
        spinnerB100.getValueFactory().setValue(Double.valueOf(luminanceScaleData.getScaleB100()));

        hBox_5.getChildren().addAll(spinnerB60, spinnerB80, spinnerB100);
        hBox_5.setAlignment(Pos.CENTER);
        HBox.setHgrow(spinnerB60, Priority.ALWAYS);
        HBox.setHgrow(spinnerB80, Priority.ALWAYS);
        HBox.setHgrow(spinnerB100, Priority.ALWAYS);

        HBox hBox_6 = new HBox(10);

        Label labelID = new Label("ID:");
        labelID.setMinWidth(equalMinWidth / 4);

        TextField textFieldID = new TextField();
        textFieldID.setMaxWidth(Double.MAX_VALUE);
        textFieldID.setEditable(false);
        textFieldID.setStyle("-fx-background-color: rgb(215,215,215)");
        textFieldID.setText(luminanceScaleData.getScaleID());

        Label labelScaleName = new Label("Scale name:");
        labelScaleName.setMinWidth(equalMinWidth / 3);

        TextField textFieldScaleName = new TextField();
        textFieldScaleName.setMaxWidth(Double.MAX_VALUE);
        textFieldScaleName.setText(luminanceScaleData.getScaleName());

        hBox_6.getChildren().addAll(labelID, textFieldID, labelScaleName, textFieldScaleName);
        hBox_6.setAlignment(Pos.CENTER);
        HBox.setHgrow(textFieldID, Priority.ALWAYS);
        HBox.setHgrow(textFieldScaleName, Priority.ALWAYS);

        HBox hBox_7 = new HBox(10);

        Label labelAdditionalInformation = new Label("Additional information");

        hBox_7.getChildren().addAll(labelAdditionalInformation);
        hBox_7.setAlignment(Pos.CENTER);

        HBox hBox_8 = new HBox(10);

        TextArea textAreaAdditionalInformation = new TextArea();
        textAreaAdditionalInformation.setWrapText(true);
        textAreaAdditionalInformation.setMaxWidth(Double.MAX_VALUE);
        textAreaAdditionalInformation.setMaxHeight(60);
        if (luminanceScaleData.getScaleAdditionalInfo().equals("#")) {
            textAreaAdditionalInformation.setText("");
        } else {
            textAreaAdditionalInformation.setText(luminanceScaleData.getScaleAdditionalInfo());
        }

        hBox_8.getChildren().addAll(textAreaAdditionalInformation);
        hBox_8.setAlignment(Pos.CENTER);
        HBox.setHgrow(textAreaAdditionalInformation, Priority.ALWAYS);

        setMultipleLabelsBackgroundColors(Integer.valueOf(spinnerHue.getValue().toString()), Integer.valueOf(spinnerSaturation.getValue().toString()));

        vBox.getChildren().addAll(hBox_9, hBox_2, hBox_3, hBox_4, hBox_5, hBox_6, hBox_7, hBox_8);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonAcceptAndSave = new Button("Accept & save");
        buttonAcceptAndSave.setOnAction(event -> {

            boolean check = checkIfB0ToB100ValuesAreNotZero(new double[] {
                    Double.valueOf(spinnerB0.getValue().toString()),
                    Double.valueOf(spinnerB20.getValue().toString()),
                    Double.valueOf(spinnerB40.getValue().toString()),
                    Double.valueOf(spinnerB60.getValue().toString()),
                    Double.valueOf(spinnerB80.getValue().toString()),
                    Double.valueOf(spinnerB100.getValue().toString()),
            });

            if (!check) {
                if (!textFieldScaleName.getText().equals("")) {

                    switch (scaleType) {
                        case "Stimulus":
                            step2_ScreenAndLuminanceScale.getTextFieldStimulusScaleName().setText(textFieldScaleName.getText());
                            break;
                        case "Background":
                            step2_ScreenAndLuminanceScale.getTextFieldBackgroundScaleName().setText(textFieldScaleName.getText());
                            break;
                    }

                    String textForEmptyFields = "#";
                    if (textAreaAdditionalInformation.getText().equals("")) {
                        textAreaAdditionalInformation.setText(textForEmptyFields);
                    }

                    LuminanceScaleData luminanceScaleData;

                    switch (scaleType) {
                        case "Stimulus":
                            luminanceScaleData = step2_ScreenAndLuminanceScale.getLuminanceScaleDataForStimuli();
                            break;
                        case "Background":
                            luminanceScaleData = step2_ScreenAndLuminanceScale.getLuminanceScaleDataForBackground();
                            break;
                        default:
                            luminanceScaleData = new LuminanceScaleData();
                            break;
                    }

                    luminanceScaleData.setScaleID(textFieldID.getText());
                    luminanceScaleData.setScaleName(textFieldScaleName.getText());
                    luminanceScaleData.setScaleHue(spinnerHue.getValue().toString());
                    luminanceScaleData.setScaleSaturation(spinnerSaturation.getValue().toString());
                    luminanceScaleData.setScaleB0(spinnerB0.getValue().toString());
                    luminanceScaleData.setScaleB20(spinnerB20.getValue().toString());
                    luminanceScaleData.setScaleB40(spinnerB40.getValue().toString());
                    luminanceScaleData.setScaleB60(spinnerB60.getValue().toString());
                    luminanceScaleData.setScaleB80(spinnerB80.getValue().toString());
                    luminanceScaleData.setScaleB100(spinnerB100.getValue().toString());
                    luminanceScaleData.setScaleAdditionalInfo(textAreaAdditionalInformation.getText().replaceAll("[\\t\\n]", " "));
                    luminanceScaleData.setBrightnessMeasureScale(new double[]{0, 20, 40, 60, 80, 100});
                    luminanceScaleData.setLuminanceMeasurements(new double[]{
                            Double.valueOf(spinnerB0.getValue().toString()),
                            Double.valueOf(spinnerB20.getValue().toString()),
                            Double.valueOf(spinnerB40.getValue().toString()),
                            Double.valueOf(spinnerB60.getValue().toString()),
                            Double.valueOf(spinnerB80.getValue().toString()),
                            Double.valueOf(spinnerB100.getValue().toString())
                    });

                    double[] polynomialFitCoefficients = screenLuminanceFunctions
                            .fitPolynomialToData(luminanceScaleData.getBrightnessMeasureScale(),
                                    luminanceScaleData.getLuminanceMeasurements(), Integer.valueOf(ConfigurationData.getLuminanceScaleFitPolynomialDegree()), true);

                    double[] brightnessVector = screenLuminanceFunctions.createBrightnessVector(101);
                    luminanceScaleData.setBrightnessVector(brightnessVector);

                    double[] luminanceForBrightness = new double[brightnessVector.length];
                    try {
                        luminanceForBrightness = screenLuminanceFunctions.poly2D(polynomialFitCoefficients, brightnessVector);
                    } catch (Exception ex) {
                        ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
                        ed.setTitle("Error");
                        ed.setHeaderText("Exception");
                        ed.showAndWait();
                    }
                    luminanceScaleData.setLuminanceForBrightness(luminanceForBrightness);

                    ArrayList<String> existingLuminanceScalesList = screenLuminanceFunctions.getExistingLuminanceScalesList(new File("screen_luminance_scales.s"));
                    String scaleID = luminanceScaleData.getScaleID();
                    for (int i = 0; i < existingLuminanceScalesList.size(); i++) {
                        String[] str = existingLuminanceScalesList.get(i).split("\t");
                        if (scaleID.equals(str[0])) {
                            String editedScaleData =
                                    luminanceScaleData.getScaleID() + "\t" +
                                            luminanceScaleData.getScaleName() + "\t" +
                                            luminanceScaleData.getScaleHue() + "\t" +
                                            luminanceScaleData.getScaleSaturation() + "\t" +
                                            luminanceScaleData.getScaleB0() + "\t" +
                                            luminanceScaleData.getScaleB20() + "\t" +
                                            luminanceScaleData.getScaleB40() + "\t" +
                                            luminanceScaleData.getScaleB60() + "\t" +
                                            luminanceScaleData.getScaleB80() + "\t" +
                                            luminanceScaleData.getScaleB100() + "\t" +
                                            luminanceScaleData.getScaleAdditionalInfo();

                            existingLuminanceScalesList.set(i, editedScaleData);
                            screenLuminanceFunctions.saveEditedScale(new String[] {luminanceScaleData.getScaleID(),
                                    luminanceScaleData.getScaleName()}, existingLuminanceScalesList);
                        }
                    }
                    this.close();

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Scale name.");
                    alert.setContentText("You can't accept & save this scale without providing its name. Name this scale " +
                            "and than try again.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("All measurements are equal to zero.");
                alert.setContentText("It is imposible for all luminance measurements to be equal to zero. " +
                        "Conduct the measurements and try again.");
                alert.showAndWait();
            }
        });

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> this.close());

        hBoxBottom.getChildren().addAll(buttonAcceptAndSave, buttonCancel);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setCenter(vBox);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(vBox, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    public Step2_ScaleInfo(Step2_ScreenAndLuminanceScale step2_ScreenAndLuminanceScale, String scaleType) {
        this.step2_ScreenAndLuminanceScale = step2_ScreenAndLuminanceScale;
        this.scaleType = scaleType;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public void setLabelBackgroundColor(int hue, int sat, int bright, Label label) {
        label.setStyle("-fx-border-width: 2; " +
                "-fx-border-color: black; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: hsb(" + hue + ", " + sat + "%, " + bright + "%);");
    }

    public void setMultipleLabelsBackgroundColors(int hue, int sat) {
        setLabelBackgroundColor(hue, sat, 100, labelColor);
        setLabelBackgroundColor(hue, sat, 0, labelB0);
        setLabelBackgroundColor(hue, sat, 20, labelB20);
        setLabelBackgroundColor(hue, sat, 40, labelB40);
        setLabelBackgroundColor(hue, sat, 60, labelB60);
        setLabelBackgroundColor(hue, sat, 80, labelB80);
        setLabelBackgroundColor(hue, sat, 100, labelB100);
    }

    public boolean checkIfB0ToB100ValuesAreNotZero(double[] values) {
        boolean allValuesAreEqualToZero = true;
        for (int i = 0; i < 6; i++) {
            if (values[i] > 0) {
                allValuesAreEqualToZero = false;
                break;
            }
        }
        return allValuesAreEqualToZero;
    }
}
