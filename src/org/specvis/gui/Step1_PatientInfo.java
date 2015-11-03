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

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by pdzwiniel on 2015-05-25.
 * Last update by pdzwiniel on 2015-11-03.
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

public class Step1_PatientInfo extends Stage {

    private Step1_Patient step1_patient;
    private PatientInfoData patientInfo;

    public Parent createContent() {

        /* init logic */
        PatientFunctions patientFunctions = new PatientFunctions();

        /* init data */
        patientInfo = step1_patient.getPatientInfo();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> center */
        int minHeight = 25;

        Label labelID = new Label("ID:");
        labelID.setMinHeight(minHeight);

        TextField textFieldID = new TextField();
        textFieldID.setEditable(false);
        textFieldID.setStyle("-fx-background-color: rgb(215,215,215)");
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldID, patientInfo.getPatientID(), "#", "");
        textFieldID.setMinHeight(minHeight);

        Label labelGender = new Label("Gender:");
        labelGender.setMinHeight(minHeight);

        ObservableList olGender = FXCollections.observableArrayList("Male", "Female", "Other");
        ComboBox cbGender = new ComboBox(olGender);
        cbGender.getSelectionModel().select(patientInfo.getPatientGender());
        cbGender.setMinHeight(minHeight);

        Label labelFirstName = new Label("First name:");
        labelFirstName.setMinHeight(minHeight);

        TextField textFieldFirstName = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldFirstName, patientInfo.getPatientFirstName(), "#", "");
        textFieldFirstName.setMinHeight(minHeight);

        Label labelLastName = new Label("Last name:");
        labelLastName.setMinHeight(minHeight);

        TextField textFieldLastName = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldLastName, patientInfo.getPatientLastName(), "#", "");
        textFieldLastName.setMinHeight(minHeight);

        Label labelDateOfBirth = new Label("Date of birth:");
        labelDateOfBirth.setMinHeight(minHeight);

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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld = LocalDate.parse(patientInfo.getPatientDateOfBirth(), dtf);
        datePickerDateOfBirth.setValue(ld);
        datePickerDateOfBirth.setMinHeight(minHeight);

        Label labelAge = new Label("Age:");
        labelAge.setMinHeight(minHeight);

        TextField textFieldAge = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldAge, patientInfo.getPatientAge(), "#", "");
        textFieldAge.setMinHeight(minHeight);

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
        labelPhone.setMinHeight(minHeight);

        TextField textFieldPhone = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldPhone, patientInfo.getPatientPhone(), "#", "");
        textFieldPhone.setMinHeight(minHeight);

        Label labelEmail = new Label("Email:");
        labelEmail.setMinHeight(minHeight);

        TextField textFieldEmail = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldEmail, patientInfo.getPatientEmail(), "#", "");
        textFieldEmail.setMinHeight(minHeight);

        Label labelCity = new Label("City:");
        labelCity.setMinHeight(minHeight);

        TextField textFieldCity = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldCity, patientInfo.getPatientCity(), "#", "");
        textFieldCity.setMinHeight(minHeight);

        Label labelPostalCode = new Label("Postal code:");
        labelPostalCode.setMinHeight(minHeight);

        TextField textFieldPostalCode = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldPostalCode, patientInfo.getPatientPostalCode(), "#", "");
        textFieldPostalCode.setMinHeight(minHeight);

        Label labelVisualAcuityLeft = new Label("Left eye VA:");
        labelVisualAcuityLeft.setMinHeight(minHeight);

        TextField textFieldVisualAcuityLeft = new TextField();
        textFieldVisualAcuityLeft.setMaxWidth(Double.MAX_VALUE);
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldVisualAcuityLeft, patientInfo.getPatientVisualAcuityLeftEye(), "#", "");

        Label labelVisualAcuityRight = new Label("Right eye VA:");
        labelVisualAcuityRight.setMinHeight(minHeight);

        TextField textFieldVisualAcuityRight = new TextField();
        patientFunctions.setTextForTextFieldDependOnGivenText(textFieldVisualAcuityRight, patientInfo.getPatientVisualAcuityRightEye(), "#", "");
        textFieldVisualAcuityLeft.setMinHeight(minHeight);

        HBox hBox_1 = new HBox(10);

        Label labelAdditionalInfoAboutPatient = new Label ("Additional information about patient");

        hBox_1.getChildren().addAll(labelAdditionalInfoAboutPatient);
        hBox_1.setAlignment(Pos.CENTER);

        HBox hBox_2 = new HBox(10);

        TextArea textAreaAdditionalInfoAboutPatient = new TextArea();
        textAreaAdditionalInfoAboutPatient.setMaxWidth(Double.MAX_VALUE);
        textAreaAdditionalInfoAboutPatient.setText(patientInfo.getPatientAdditionalInformation());
        if (patientInfo.getPatientAdditionalInformation().equals("#")) {
            textAreaAdditionalInfoAboutPatient.setText("");
        }

        hBox_2.getChildren().addAll(textAreaAdditionalInfoAboutPatient);
        hBox_2.setAlignment(Pos.CENTER);
        HBox.setHgrow(textAreaAdditionalInfoAboutPatient, Priority.ALWAYS);

        VBox v1 = new VBox(10);
        VBox v2 = new VBox(10);
        VBox v3 = new VBox(10);
        VBox v4 = new VBox(10);

        v1.getChildren().addAll(labelID, labelFirstName, labelDateOfBirth, labelPhone, labelCity, labelVisualAcuityLeft);
        v1.setAlignment(Pos.CENTER_LEFT);

        v2.getChildren().addAll(textFieldID, textFieldFirstName, datePickerDateOfBirth, textFieldPhone, textFieldCity, textFieldVisualAcuityLeft);
        v2.setAlignment(Pos.CENTER_LEFT);

        v3.getChildren().addAll(labelGender, labelLastName, labelAge, labelEmail, labelPostalCode, labelVisualAcuityRight);
        v3.setAlignment(Pos.CENTER_LEFT);

        HBox hBoxAge = new HBox(10);
        hBoxAge.getChildren().addAll(textFieldAge, buttonAge);
        hBoxAge.setAlignment(Pos.CENTER_LEFT);

        v4.getChildren().addAll(cbGender, textFieldLastName, hBoxAge, textFieldEmail, textFieldPostalCode, textFieldVisualAcuityRight);
        v4.setAlignment(Pos.CENTER_LEFT);

        HBox hBoxPrime = new HBox(10);
        hBoxPrime.getChildren().addAll(v1, v2, v3, v4);

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

                ArrayList<String> existingPatientsList = patientFunctions
                        .getExistingPatientsList(new File("patients.s"));
                String patientID = patientInfo.getPatientID();
                for (int i = 0; i < existingPatientsList.size(); i++) {
                    String[] str = existingPatientsList.get(i).split("\t");
                    String pID = cipher.decode(str[0]);
                    if (patientID.equals(pID)) {
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

                        String editedPatientData =
                                cipher.encode(patientInfo.getPatientID()) + "\t" +
                                        cipher.encode(patientInfo.getPatientGender()) + "\t" +
                                        cipher.encode(patientInfo.getPatientFirstName()) + "\t" +
                                        cipher.encode(patientInfo.getPatientLastName()) + "\t" +
                                        cipher.encode(patientInfo.getPatientDateOfBirth()) + "\t" +
                                        cipher.encode(patientInfo.getPatientAge()) + "\t" +
                                        cipher.encode(patientInfo.getPatientPhone()) + "\t" +
                                        cipher.encode(patientInfo.getPatientEmail()) + "\t" +
                                        cipher.encode(patientInfo.getPatientCity()) + "\t" +
                                        cipher.encode(patientInfo.getPatientPostalCode()) + "\t" +
                                        cipher.encode(patientInfo.getPatientVisualAcuityLeftEye()) + "\t" +
                                        cipher.encode(patientInfo.getPatientVisualAcuityRightEye()) + "\t" +
                                        cipher.encode(patientInfo.getPatientAdditionalInformation());

                        existingPatientsList.set(i, editedPatientData);
                        patientFunctions.saveEditedPatient(new String[] {patientInfo.getPatientID(),
                                patientInfo.getPatientFirstName(), patientInfo.getPatientLastName()}, existingPatientsList);
                    }
                }
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

    public Step1_PatientInfo(Step1_Patient step1_patient) {
        this.step1_patient = step1_patient;
        this.setScene(new Scene(createContent()));
        this.setWidth(570);
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }
}
