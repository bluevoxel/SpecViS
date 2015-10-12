package org.specvis.procedure;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Iterator;

/**
 * Created by pdzwiniel on 2015-09-28.
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

public class ShellWindowForMainProcedure extends Stage {

    private Data data;
    private TextArea textArea;
    private Label labelProcedureStatus;
    private Text textProgressBar;
    private ProgressBar progressBar;

    public Parent createContent() {

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> top */
        MenuBar menuBar = new MenuBar();
        Menu menuSession = new Menu("Session");

        MenuItem menuItemShowResultsAsVisualFieldMap = new MenuItem("Show results as visual field map");
        menuItemShowResultsAsVisualFieldMap.setOnAction(event -> {
            if (data.isMainProcedureFinished()) {
                ShowResultsAsVisualFieldMap showResultsAsVisualFieldMap = new ShowResultsAsVisualFieldMap(data);
                showResultsAsVisualFieldMap.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("There are no results to show.");
                alert.setContentText("You can't show results as visual field map. It's because lack of data and propably " +
                        "due to the fact, that main procedure was not finished.");
                alert.showAndWait();
            }
        });

        MenuItem menuItemsaveSessionAsTxt = new MenuItem("Save session as *.txt");
        menuItemsaveSessionAsTxt.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().addAll(extensionFilter);
            fileChooser.setTitle("Save session as *.txt");
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                ObservableList<CharSequence> paragraph = textArea.getParagraphs();
                Iterator<CharSequence> iter = paragraph.iterator();
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                    while (iter.hasNext()) {
                        CharSequence charSequence = iter.next();
                        bufferedWriter.append(charSequence);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        menuSession.getItems().addAll(menuItemShowResultsAsVisualFieldMap, new SeparatorMenuItem(), menuItemsaveSessionAsTxt);
        menuBar.getMenus().add(menuSession);

        /* layout -> center */
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setId("textArea");
        String stylesheet = this.getClass().getResource("/org/specvis/css_styles/ShellWindowForMainProcedure.css").toExternalForm();
        textArea.getStylesheets().add(stylesheet);


        /* layout -> bottom */
        labelProcedureStatus = new Label("Press ANSWER KEY to START PROCEDURE");
        labelProcedureStatus.setMaxWidth(Double.MAX_VALUE);
        labelProcedureStatus.setMinHeight(30);
        labelProcedureStatus.setAlignment(Pos.CENTER);
        labelProcedureStatus.setStyle("-fx-background-color: white; " +
                "-fx-font-weight: bold;");

        textProgressBar = new Text("0/" + (data.getQuarterGridResolutionX() * 2) * (data.getQuarterGridResolutionY() * 2));
        progressBar = new ProgressBar();
        progressBar.setMinHeight(20);
        progressBar.setMaxHeight(20);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().setAll(progressBar, textProgressBar);

        HBox hBoxBottom = new HBox();
        hBoxBottom.getChildren().addAll(labelProcedureStatus);
        HBox.setHgrow(labelProcedureStatus, Priority.ALWAYS);
        hBoxBottom.setAlignment(Pos.CENTER);

        VBox vBoxBottom = new VBox();
        vBoxBottom.getChildren().addAll(hBoxBottom, stackPane);

        /* add items to layout */
        layout.setTop(menuBar);
        layout.setCenter(textArea);
        layout.setBottom(vBoxBottom);
        return layout;
    }

    public ShellWindowForMainProcedure(Data data) {
        this.data = data;
        this.setScene(new Scene(createContent()));
        this.setWidth(450);
        this.setHeight(600);
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public void addTextToTextArea(String text) {
        textArea.appendText(text);
    }

    public void setProcedureStatus(String status, String webColor) {
        labelProcedureStatus.setText(status);
        labelProcedureStatus.setStyle("-fx-background-color: \"" + webColor + "\"; " +
                "-fx-font-weight: bold;");
    }

    public Text getTextProgressBar() {
        return textProgressBar;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
