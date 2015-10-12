package org.specvis.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.specvis.data.ExistingPatientData;
import org.specvis.data.PatientInfoData;
import org.specvis.logic.Base64EncoderDecoder;
import org.specvis.logic.CustomTableView;
import org.specvis.logic.PatientFunctions;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

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

public class Step1_ExistingPatient extends Stage {

    private Step1_Patient _step1_patient;
    private PatientFunctions patientManagementFunctions;
    private ObservableList<ExistingPatientData> tableData;
    private CustomTableView tableView;
    private ArrayList<String> existingPatientsList;
    private Base64EncoderDecoder cipher;

    public Parent createContent() {

        /* init logic */
        patientManagementFunctions = new PatientFunctions();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> center */
        String[] tableColumnsNames = new String[] {"ID", "First name", "Last name"};
        String[] tableFieldsNames = ExistingPatientData.getFieldsNames();
        tableData = getTableData();
        tableView = new CustomTableView(tableColumnsNames, tableFieldsNames, tableData);
        tableView.setPrefSize(350, 200);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(event -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                ExistingPatientData existingPatientData = (ExistingPatientData) tableView.getSelectionModel()
                        .getSelectedItem();
                String patientID = existingPatientData.getPatientID();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Delete selected patient.");
                alert.setContentText("Are you sure you want to delete " + existingPatientData.getPatientFirstName() +
                        " " + existingPatientData.getPatientLastName() + " with ID " + patientID + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    patientManagementFunctions.deleteSelectedPatient(patientID, existingPatientsList, cipher);
                    ObservableList<ExistingPatientData> data = getTableData();
                    tableData = getTableData();
                    tableView.getItems().removeAll();
                    tableView.setItems(tableData);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Select patient first.");
                alert.setContentText("In order to remove desired patient, you have to select it first.");
                alert.showAndWait();
            }
        });

        Button buttonAccept = new Button("Accept");
        buttonAccept.setOnAction(event -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                ExistingPatientData existingPatientData = (ExistingPatientData) tableView.getSelectionModel()
                        .getSelectedItem();
                String patientID = existingPatientData.getPatientID();
                PatientInfoData patientInfoData = patientManagementFunctions
                        .findExistingPatientByID(patientID, existingPatientsList, cipher);
                _step1_patient.setPatientInfo(patientInfoData);
                _step1_patient.getTextFieldChosenPatientID().setText(patientID);

                this.close();
            }
        });

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> this.close());

        hBoxBottom.getChildren().addAll(buttonDelete, buttonAccept, buttonCancel);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setCenter(tableView);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    public Step1_ExistingPatient(Step1_Patient step1_patient) {
        this._step1_patient = step1_patient;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public ObservableList<ExistingPatientData> getTableData() {
        ObservableList<ExistingPatientData> data = FXCollections.observableArrayList();
        File file = new File("patients_list.s");
        existingPatientsList = patientManagementFunctions.getExistingPatientsList(file);
        cipher = new Base64EncoderDecoder();
        if (existingPatientsList.size() > 0) {
            ExistingPatientData[] existingPatients = new ExistingPatientData[existingPatientsList.size()];
            String[] patient = new String[3];
            for (int i = 0; i < existingPatientsList.size(); i++) {
                String[] str = existingPatientsList.get(i).split("\t");

                String decodedID = cipher.decode(str[0]);
                String decodedFirstName = cipher.decode(str[2]);
                String decodedLastName = cipher.decode(str[3]);
                patient[0] = decodedID;
                patient[1] = decodedFirstName;
                patient[2] = decodedLastName;

                existingPatients[i] = new ExistingPatientData(patient[0], patient[1], patient[2]);
            }
            data = FXCollections.observableArrayList(existingPatients);
        }
        return data;
    }
}
