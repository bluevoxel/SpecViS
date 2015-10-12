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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.specvis.StartApplication;
import org.specvis.data.PatientInfoData;

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

public class Step1_Patient extends Stage {

    private StartApplication startApplication;
    private PatientInfoData patientInfo;
    private TextField textFieldChosenPatientID;
    private ComboBox cbExaminedEye;

    public Parent createContent() {

        /* init data */
        patientInfo = new PatientInfoData();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> top -> step indicator */
        Text textProgressBar = new Text("Step 1/4 - Patient");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0.25);
        progressBar.setPrefHeight(30);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-opacity: 0.7;");
        StackPane stackPane = new StackPane();
        stackPane.setMaxWidth(Double.MAX_VALUE);
        stackPane.getChildren().setAll(progressBar, textProgressBar);

        /* layout -> center */
        int equalMinWidth = 110;

        VBox vBox = new VBox(10);

        HBox hBox_1 = new HBox(10);

        Button buttonNewPatient = new Button("New patient");
        buttonNewPatient.setOnAction(event -> {
            Step1_NewPatient stage = new Step1_NewPatient(this);
            stage.show();
        });
        buttonNewPatient.setMaxWidth(Double.MAX_VALUE);

        Button buttonExistingPatient = new Button("Existing patient");
        buttonExistingPatient.setOnAction(event -> {
            Step1_ExistingPatient stage = new Step1_ExistingPatient(this);
            stage.show();
        });
        buttonExistingPatient.setMaxWidth(Double.MAX_VALUE);

        hBox_1.getChildren().addAll(buttonNewPatient, buttonExistingPatient);
        HBox.setHgrow(buttonNewPatient, Priority.ALWAYS);
        HBox.setHgrow(buttonExistingPatient, Priority.ALWAYS);

        HBox hBox_2 = new HBox(10);

        Label labelChosenPatientID = new Label("Chosen patient ID:");
        labelChosenPatientID.setMinWidth(equalMinWidth);

        textFieldChosenPatientID = new TextField();
        textFieldChosenPatientID.setMaxWidth(Double.MAX_VALUE);
        textFieldChosenPatientID.setEditable(false);
        textFieldChosenPatientID.setStyle("-fx-background-color: rgb(215,215,215)");

        Button buttonInfo = new Button("?");
        buttonInfo.setOnAction(event1 -> {
            if (!textFieldChosenPatientID.getText().equals("")) {
                Step1_PatientInfo stage = new Step1_PatientInfo(this);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Choose patient first.");
                alert.setContentText("You can't open patient info window without selecting a patient. " +
                        "Create new patient or choose existing one and than try again.");
                alert.showAndWait();
            }
        });

        hBox_2.getChildren().addAll(labelChosenPatientID, textFieldChosenPatientID, buttonInfo);
        hBox_2.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textFieldChosenPatientID, Priority.ALWAYS);

        HBox hBox_3 = new HBox(10);

        Label labelExaminedEye = new Label("Examined eye:");
        labelExaminedEye.setMinWidth(equalMinWidth);

        ObservableList<String> ol = FXCollections.observableArrayList("Left", "Right");

        cbExaminedEye = new ComboBox(ol);
        cbExaminedEye.getSelectionModel().select(0);
        cbExaminedEye.setMaxWidth(Double.MAX_VALUE);

        hBox_3.getChildren().addAll(labelExaminedEye, cbExaminedEye);
        hBox_3.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(cbExaminedEye, Priority.ALWAYS);

        vBox.getChildren().addAll(hBox_1, hBox_2, hBox_3);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonNext = new Button("Next");
        buttonNext.setOnAction(event -> {
            if (!textFieldChosenPatientID.getText().equals("")) {
                this.hide();
                startApplication.getStageStep2ScreenAndLuminance().show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Choose patient first.");
                alert.setContentText("You can't go further without selecting a patient. " +
                        "Create new patient or choose existing one and than try again.");
                alert.showAndWait();
            }
        });

        hBoxBottom.getChildren().add(buttonNext);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setTop(stackPane);
        layout.setCenter(vBox);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(vBox, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    public Step1_Patient(StartApplication startApplication) {
        this.startApplication = startApplication;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public TextField getTextFieldChosenPatientID() {
        return textFieldChosenPatientID;
    }

    public PatientInfoData getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoData patientInfo) {
        this.patientInfo = patientInfo;
    }

    public ComboBox getCbExaminedEye() {
        return cbExaminedEye;
    }
}
