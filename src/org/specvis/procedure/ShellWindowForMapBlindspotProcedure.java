package org.specvis.procedure;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Created by pdzwiniel on 2015-10-02.
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

public class ShellWindowForMapBlindspotProcedure extends Stage {

    private TextArea textArea;
    private Label labelProcedureStatus;

    public Parent createContent() {

        /* layout */
        BorderPane layout = new BorderPane();

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

        HBox hBoxBottom = new HBox();
        hBoxBottom.getChildren().add(labelProcedureStatus);
        HBox.setHgrow(labelProcedureStatus, Priority.ALWAYS);
        hBoxBottom.setAlignment(Pos.CENTER);

        /* add items to layout */
        layout.setCenter(textArea);
        layout.setBottom(hBoxBottom);
        return layout;
    }

    public ShellWindowForMapBlindspotProcedure() {
        this.setScene(new Scene(createContent()));
        this.setWidth(450);
        this.setHeight(600);
        this.setTitle("specvis");
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
}
