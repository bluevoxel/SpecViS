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
import org.specvis.data.ConfigurationData;
import org.specvis.data.ExistingLuminanceScaleData;
import org.specvis.data.LuminanceScaleData;
import org.specvis.logic.CustomTableView;
import org.specvis.logic.ScreenLuminanceFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by pdzwiniel on 2015-05-25.
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

public class Step2_ExistingScale extends Stage {

    private String scaleType; // "Stimulus" or "Background"

    private Step2_ScreenAndLuminanceScale step2_ScreenAndLuminanceScale;
    private ScreenLuminanceFunctions screenLuminanceFunctions;
    private ObservableList<ExistingLuminanceScaleData> tableData;
    private CustomTableView tableView;
    private ArrayList<String> existingLuminanceScalesList;

    public Parent createContent() {

        /* layout */
        screenLuminanceFunctions = new ScreenLuminanceFunctions();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> center */
        String[] tableColumnsNames = new String[] {"ID", "Scale name", "Hue", "Saturation", "B0", "B20", "B40", "B60", "B80", "B100"};
        String[] tableFieldsNames = ExistingLuminanceScaleData.getFieldsNames();
        tableData = getTableData();
        tableView = new CustomTableView(tableColumnsNames, tableFieldsNames, tableData);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(event -> {


            if (tableView.getSelectionModel().getSelectedItem() != null) {
                ExistingLuminanceScaleData existingLuminanceScaleData = (ExistingLuminanceScaleData) tableView.getSelectionModel().getSelectedItem();
                String scaleID = existingLuminanceScaleData.getScaleID();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Delete selected scale.");
                alert.setContentText("Are you sure you want to delete " + existingLuminanceScaleData.getScaleName() + " with ID " + scaleID + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    screenLuminanceFunctions.deleteSelectedScale(scaleID, existingLuminanceScalesList);
                    ObservableList<ExistingLuminanceScaleData> data = getTableData();
                    tableData = getTableData();
                    tableView.getItems().removeAll();
                    tableView.setItems(tableData);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Select scale first.");
                alert.setContentText("In order to remove desired scale, you have to select it first.");
                alert.showAndWait();
            }
        });

        Button buttonAccept = new Button("Accept");
        buttonAccept.setOnAction(event -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                ExistingLuminanceScaleData existingLuminanceScaleData = (ExistingLuminanceScaleData) tableView.getSelectionModel().getSelectedItem();
                String scaleID = existingLuminanceScaleData.getScaleID();

                LuminanceScaleData luminanceScaleData = screenLuminanceFunctions.findExistingLuminanceScaleByID(scaleID, existingLuminanceScalesList);

                luminanceScaleData.setBrightnessMeasureScale(new double[]{0, 20, 40, 60, 80, 100});
                luminanceScaleData.setLuminanceMeasurements(new double[]{
                        Double.valueOf(luminanceScaleData.getScaleB0()),
                        Double.valueOf(luminanceScaleData.getScaleB20()),
                        Double.valueOf(luminanceScaleData.getScaleB40()),
                        Double.valueOf(luminanceScaleData.getScaleB60()),
                        Double.valueOf(luminanceScaleData.getScaleB80()),
                        Double.valueOf(luminanceScaleData.getScaleB100())
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
                    ex.printStackTrace();
                }
                luminanceScaleData.setLuminanceForBrightness(luminanceForBrightness);

                switch (scaleType) {
                    case "Stimulus":
                        step2_ScreenAndLuminanceScale.setLuminanceScaleDataForStimuli(luminanceScaleData);
                        step2_ScreenAndLuminanceScale.getTextFieldStimulusScaleName().setText(luminanceScaleData.getScaleName());
                        break;
                    case "Background":
                        step2_ScreenAndLuminanceScale.setLuminanceScaleDataForBackground(luminanceScaleData);
                        step2_ScreenAndLuminanceScale.getTextFieldBackgroundScaleName().setText(luminanceScaleData.getScaleName());
                        break;
                }

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

    public Step2_ExistingScale(Step2_ScreenAndLuminanceScale step2_ScreenAndLuminanceScale, String scaleType) {
        this.step2_ScreenAndLuminanceScale = step2_ScreenAndLuminanceScale;
        this.scaleType = scaleType;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public ObservableList<ExistingLuminanceScaleData> getTableData() {
        ObservableList<ExistingLuminanceScaleData> data = FXCollections.observableArrayList();
        File file = new File("screen_luminance_scales.s");
        existingLuminanceScalesList = screenLuminanceFunctions.getExistingLuminanceScalesList(file);
        if (existingLuminanceScalesList.size() > 0) {
            ExistingLuminanceScaleData[] existingLuminanceScaleDatas = new ExistingLuminanceScaleData[existingLuminanceScalesList.size()];
            for (int i = 0; i < existingLuminanceScalesList.size(); i++) {
                String[] str = existingLuminanceScalesList.get(i).split("\t");
                existingLuminanceScaleDatas[i] = new ExistingLuminanceScaleData(str[0], str[1], str[2], str[3], str[4],
                        str[5], str[6], str[7], str[8], str[9]);
            }

            data = FXCollections.observableArrayList(existingLuminanceScaleDatas);
        }
        return data;
    }
}
