package org.specvis.procedure;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

/**
 * Created by pdzwiniel on 2015-06-10.
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

public class MonitorStimulusPreview extends Stage {

    private Data data;
    private Pane paneMonitorStimulusPreview;

    public Parent createContent() {

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> center */
        VBox vBoxCenter = new VBox(10);

        Label labelMonitorStimulusPreview = new Label("Monitor stimulus preview");

        paneMonitorStimulusPreview = new Pane();
        paneMonitorStimulusPreview.setMinSize(200, 200);
        paneMonitorStimulusPreview.setMaxSize(200, 200);

        vBoxCenter.getChildren().addAll(labelMonitorStimulusPreview, paneMonitorStimulusPreview);
        vBoxCenter.setAlignment(Pos.CENTER);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonClose = new Button("Close");
        buttonClose.setOnAction(event -> this.close());

        hBoxBottom.getChildren().addAll(buttonClose);
        hBoxBottom.setAlignment(Pos.CENTER);

        /* return layout */
        layout.setCenter(vBoxCenter);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(vBoxCenter, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        setPaneFixationPointPreviewBackgroundColor(Integer.valueOf(data.getLuminanceScaleData().getScaleHue()),
                Integer.valueOf(data.getLuminanceScaleData().getScaleSaturation()), data.getBackgroundBrightness());

        setMonitorStimulus(100, 100, data.getMonitorStimulusWidth(), data.getMonitorStimulusHeight(),
                Color.hsb(Integer.valueOf(data.getLuminanceScaleData().getScaleHue()),
                        Double.valueOf(data.getLuminanceScaleData().getScaleSaturation()) / 100,
                        data.getMonitorStimulusBrightness() / 100));

        return layout;
    }

    public MonitorStimulusPreview(Data data) {
        this.data = data;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public void setPaneFixationPointPreviewBackgroundColor(int hsb, int saturation, int brightness) {
        paneMonitorStimulusPreview.setStyle("-fx-background-color: hsb(" + hsb + ", " + saturation + "%, " + brightness + "%);");
    }

    public void setMonitorStimulus(double positionX, double positionY, double width, double height, Color color) {

        double screenResolutionX = data.getScreenResolutionX();
        double screenResolutionY = data.getScreenResolutionY();

        double visualFieldX = data.getVisualFieldX();
        double visualFieldY = data.getVisualFieldY();

        double pixelsForOneDegreeX = screenResolutionX / visualFieldX;
        double pixelsForOneDegreeY = screenResolutionY / visualFieldY;

        double radiusX = (width / 2) * pixelsForOneDegreeX;
        double radiusY = (height / 2) * pixelsForOneDegreeY;

        Ellipse monitorStimulus;
        monitorStimulus = new Ellipse(positionX, positionY, radiusX, radiusY);
        monitorStimulus.setFill(color);
        monitorStimulus.setStroke(color);

        paneMonitorStimulusPreview.getChildren().add(monitorStimulus);
    }
}
