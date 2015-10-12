package org.specvis.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.StringConverter;
import org.specvis.data.PatientInfoData;
import org.specvis.logic.Base64EncoderDecoder;
import org.specvis.logic.ExceptionDialog;
import org.specvis.logic.PatientFunctions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by pdzwiniel on 2015-05-25.
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

public class Step1_NewPatient extends Stage {

    private Step1_Patient step1_patient;

    public Parent createContent() {

        /* init logic */
        PatientFunctions patientFunctions = new PatientFunctions();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> center */
        int equalMinWidth = 100;

        Label labelID = new Label("ID:");

        TextField textFieldID = new TextField();
        textFieldID.setMaxWidth(Double.MAX_VALUE);
        textFieldID.setEditable(false);
        textFieldID.setStyle("-fx-background-color: rgb(215,215,215)");
        textFieldID.setText(patientFunctions.createPatientIndividualID(patientFunctions.getCurrentDateYYYYmmDD(), 4));

        Label labelGender = new Label("Gender:");

        ObservableList olGender = FXCollections.observableArrayList("Male", "Female", "Other");
        ComboBox cbGender = new ComboBox(olGender);
        cbGender.setMaxWidth(Double.MAX_VALUE);
        cbGender.getSelectionModel().select(0);

        Label labelFirstName = new Label("First name:");

        TextField textFieldFirstName = new TextField();
        textFieldFirstName.setMaxWidth(Double.MAX_VALUE);

        Label labelLastName = new Label("Last name:");
        labelLastName.setMinWidth(equalMinWidth / 1.7);

        TextField textFieldLastName = new TextField();
        textFieldLastName.setMaxWidth(Double.MAX_VALUE);

        Label labelDateOfBirth = new Label("Date of birth:");

        DatePicker datePickerDateOfBirth = new DatePicker();
        datePickerDateOfBirth.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        });
        datePickerDateOfBirth.setPromptText("yyyy-MM-dd");
        datePickerDateOfBirth.setMaxWidth(Double.MAX_VALUE);

        Label labelAge = new Label("Age:");

        TextField textFieldAge = new TextField();
        textFieldAge.setMaxWidth(Double.MAX_VALUE);

        Button buttonAge = new Button("=");
        buttonAge.setOnAction(event -> {
            try {
                String currentDate = patientFunctions.getCurrentDateYYYYmmDD();
                String patientDateOfBirth = datePickerDateOfBirth.getValue().toString();
                int patientAge = patientFunctions.calculatePatientAge(currentDate, patientDateOfBirth);
                textFieldAge.setText(String.valueOf(patientAge));
            } catch (NullPointerException ex) {
                ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
                ed.setTitle("Error");
                ed.setHeaderText("Exception");
                ed.showAndWait();
            }
        });

        Label labelPhone = new Label("Phone:");

        TextField textFieldPhone = new TextField();
        textFieldPhone.setMaxWidth(Double.MAX_VALUE);

        Label labelEmail = new Label("Email:");

        TextField textFieldEmail = new TextField();
        textFieldEmail.setMaxWidth(Double.MAX_VALUE);

        Label labelCity = new Label("City:");

        TextField textFieldCity = new TextField();
        textFieldCity.setMaxWidth(Double.MAX_VALUE);

        Label labelPostalCode = new Label("Postal code:");

        TextField textFieldPostalCode = new TextField();
        textFieldPostalCode.setMaxWidth(Double.MAX_VALUE);

        Label labelVisualAcuityLeft = new Label("Visual acuity for left eye");

        TextField textFieldVisualAcuityLeft = new TextField();
        textFieldVisualAcuityLeft.setMaxWidth(Double.MAX_VALUE);

        Label labelVisualAcuityRight = new Label("and right");

        TextField textFieldVisualAcuityRight = new TextField();
        textFieldVisualAcuityRight.setMaxWidth(Double.MAX_VALUE);

        HBox hBox_1 = new HBox(10);

        Label labelAdditionalInfoAboutPatient = new Label ("Additional information about patient");
        labelAdditionalInfoAboutPatient.setMinWidth(equalMinWidth);

        hBox_1.getChildren().addAll(labelAdditionalInfoAboutPatient);
        hBox_1.setAlignment(Pos.CENTER);

        HBox hBox_2 = new HBox(10);

        TextArea textAreaAdditionalInfoAboutPatient = new TextArea();
        textAreaAdditionalInfoAboutPatient.setMaxWidth(Double.MAX_VALUE);

        hBox_2.getChildren().addAll(textAreaAdditionalInfoAboutPatient);
        hBox_2.setAlignment(Pos.CENTER);
        HBox.setHgrow(textAreaAdditionalInfoAboutPatient, Priority.ALWAYS);

        HBox h1 = new HBox(10);
        HBox h2 = new HBox(10);
        HBox h3 = new HBox(10);
        HBox h4 = new HBox(10);
        HBox h5 = new HBox(10);
        HBox h6 = new HBox(10);

        h1.getChildren().addAll(labelID, textFieldID);
        h2.getChildren().addAll(labelFirstName, textFieldFirstName);
        h3.getChildren().addAll(labelDateOfBirth, datePickerDateOfBirth);
        h4.getChildren().addAll(labelPhone, textFieldPhone);
        h5.getChildren().addAll(labelCity, textFieldCity);
        h6.getChildren().addAll(labelVisualAcuityLeft, textFieldVisualAcuityLeft);

        HBox.setHgrow(textFieldID, Priority.ALWAYS);
        HBox.setHgrow(textFieldFirstName, Priority.ALWAYS);
        HBox.setHgrow(datePickerDateOfBirth, Priority.ALWAYS);
        HBox.setHgrow(textFieldPhone, Priority.ALWAYS);
        HBox.setHgrow(textFieldCity, Priority.ALWAYS);
        HBox.setHgrow(textFieldVisualAcuityLeft, Priority.ALWAYS);

        h1.setAlignment(Pos.CENTER_LEFT);
        h2.setAlignment(Pos.CENTER_LEFT);
        h3.setAlignment(Pos.CENTER_LEFT);
        h4.setAlignment(Pos.CENTER_LEFT);
        h5.setAlignment(Pos.CENTER_LEFT);
        h6.setAlignment(Pos.CENTER_LEFT);

        VBox vBoxLeft = new VBox(10);
        vBoxLeft.getChildren().addAll(h1, h2, h3, h4, h5, h6);

        HBox h7 = new HBox(10);
        HBox h8 = new HBox(10);
        HBox h9 = new HBox(10);
        HBox h10 = new HBox(10);
        HBox h11 = new HBox(10);
        HBox h12 = new HBox(10);

        h7.getChildren().addAll(labelGender, cbGender);
        h8.getChildren().addAll(labelLastName, textFieldLastName);
        h9.getChildren().addAll(labelAge, textFieldAge, buttonAge);
        h10.getChildren().addAll(labelEmail, textFieldEmail);
        h11.getChildren().addAll(labelPostalCode, textFieldPostalCode);
        h12.getChildren().addAll(labelVisualAcuityRight, textFieldVisualAcuityRight);

        HBox.setHgrow(cbGender, Priority.ALWAYS);
        HBox.setHgrow(textFieldLastName, Priority.ALWAYS);
        HBox.setHgrow(textFieldAge, Priority.ALWAYS);
        HBox.setHgrow(textFieldEmail, Priority.ALWAYS);
        HBox.setHgrow(textFieldPostalCode, Priority.ALWAYS);
        HBox.setHgrow(textFieldVisualAcuityRight, Priority.ALWAYS);

        h7.setAlignment(Pos.CENTER_LEFT);
        h8.setAlignment(Pos.CENTER_LEFT);
        h9.setAlignment(Pos.CENTER_LEFT);
        h10.setAlignment(Pos.CENTER_LEFT);
        h11.setAlignment(Pos.CENTER_LEFT);
        h12.setAlignment(Pos.CENTER_LEFT);

        VBox vBoxRight = new VBox(10);
        vBoxRight.getChildren().addAll(h7, h8, h9, h10, h11, h12);

        HBox hBoxPrime = new HBox(10);
        hBoxPrime.getChildren().addAll(vBoxLeft, vBoxRight);

        VBox vBoxPrime = new VBox(10);
        vBoxPrime.getChildren().addAll(hBoxPrime, hBox_1, hBox_2);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonAcceptAndSave = new Button("Accept & save");
        buttonAcceptAndSave.setOnAction(event -> {
            if (datePickerDateOfBirth.getValue() != null) {

                String textForEmptyFields = "#";
                patientFunctions.setTextForEmptyTextField(textFieldID, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldFirstName, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldLastName, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldAge, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldPhone, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldEmail, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldCity, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldPostalCode, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldVisualAcuityLeft, textForEmptyFields);
                patientFunctions.setTextForEmptyTextField(textFieldVisualAcuityRight, textForEmptyFields);
                if (textAreaAdditionalInfoAboutPatient.getText().equals("")) {
                    textAreaAdditionalInfoAboutPatient.setText(textForEmptyFields);
                }

                Base64EncoderDecoder cipher = new Base64EncoderDecoder();

                patientFunctions.saveNewPatientToFile(
                        cipher.encode(textFieldID.getText()),
                        cipher.encode(cbGender.getSelectionModel().getSelectedItem().toString()),
                        cipher.encode(textFieldFirstName.getText()),
                        cipher.encode(textFieldLastName.getText()),
                        cipher.encode(datePickerDateOfBirth.getValue().toString()),
                        cipher.encode(textFieldAge.getText()),
                        cipher.encode(textFieldPhone.getText()),
                        cipher.encode(textFieldEmail.getText()),
                        cipher.encode(textFieldCity.getText()),
                        cipher.encode(textFieldPostalCode.getText()),
                        cipher.encode(textFieldVisualAcuityLeft.getText()),
                        cipher.encode(textFieldVisualAcuityRight.getText()),
                        cipher.encode(textAreaAdditionalInfoAboutPatient.getText().replaceAll("[\\t\\n]", " "))
                );

                step1_patient.getTextFieldChosenPatientID().setText(textFieldID.getText());

                PatientInfoData patientInfo = step1_patient.getPatientInfo();
                patientInfo.setPatientID(textFieldID.getText());
                patientInfo.setPatientGender(cbGender.getSelectionModel().getSelectedItem().toString());
                patientInfo.setPatientFirstName(textFieldFirstName.getText());
                patientInfo.setPatientLastName(textFieldLastName.getText());
                patientInfo.setPatientDateOfBirth(datePickerDateOfBirth.getValue().toString());
                patientInfo.setPatientAge(textFieldAge.getText());
                patientInfo.setPatientPhone(textFieldPhone.getText());
                patientInfo.setPatientEmail(textFieldEmail.getText());
                patientInfo.setPatientCity(textFieldCity.getText());
                patientInfo.setPatientPostalCode(textFieldPostalCode.getText());
                patientInfo.setPatientVisualAcuityLeftEye(textFieldVisualAcuityLeft.getText());
                patientInfo.setPatientVisualAcuityRightEye(textFieldVisualAcuityRight.getText());
                patientInfo.setPatientAdditionalInformation(textAreaAdditionalInfoAboutPatient.getText()
                        .replaceAll("[\\t\\n]", " "));

                this.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Select patient date of birth.");
                alert.setContentText("You must select patient date of birth in order to continue.");
                alert.showAndWait();
            }
        });

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> this.close());

        hBoxBottom.getChildren().addAll(buttonAcceptAndSave, buttonCancel);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setCenter(vBoxPrime);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(vBoxPrime, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    public Step1_NewPatient(Step1_Patient step1_patient) {
        this.step1_patient = step1_patient;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }
}
