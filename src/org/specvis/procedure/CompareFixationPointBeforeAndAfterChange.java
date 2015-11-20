package org.specvis.procedure;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
 * Created by pdzwiniel on 2015-06-09.
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

public class CompareFixationPointBeforeAndAfterChange extends Stage {

    private Data data;
    private Pane paneFixPointBefore;
    private Pane paneFixPointAfter;

    public Parent createContent() {

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> center */
        HBox hBoxCenter = new HBox(10);

        VBox vBoxBefore = new VBox(10);

        Label labelFixPointBefore = new Label("Before");

        paneFixPointBefore = new Pane();
        paneFixPointBefore.setMinSize(200, 200);
        paneFixPointBefore.setMaxSize(200, 200);

        vBoxBefore.getChildren().addAll(labelFixPointBefore, paneFixPointBefore);
        vBoxBefore.setAlignment(Pos.CENTER);

        VBox vBoxAfter = new VBox(10);

        Label labelFixPointAfter = new Label("After");

        paneFixPointAfter = new Pane();
        paneFixPointAfter.setMinSize(200, 200);
        paneFixPointAfter.setMaxSize(200, 200);

        vBoxAfter.getChildren().addAll(labelFixPointAfter, paneFixPointAfter);
        vBoxAfter.setAlignment(Pos.CENTER);

        hBoxCenter.getChildren().addAll(vBoxBefore, vBoxAfter);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        /* return layout */
        layout.setCenter(hBoxCenter);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(hBoxCenter, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        setPanesBackgroundColor();
        setFixationPointsOnPanes();

        return layout;
    }

    public CompareFixationPointBeforeAndAfterChange(Data data) {
        this.data = data;
        this.setScene(new Scene(createContent()));
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public void setPanesBackgroundColor() {
        int hue = Integer.valueOf(data.getLuminanceScaleDataForBackground().getScaleHue());
        int saturation = Integer.valueOf(data.getLuminanceScaleDataForBackground().getScaleSaturation());
        int brightness = Integer.valueOf(data.getBackgroundBrightness());

        paneFixPointBefore.setStyle("-fx-background-color: hsb(" + hue + ", " + saturation + "%, " + brightness + "%);");
        paneFixPointAfter.setStyle("-fx-background-color: hsb(" + hue + ", " + saturation + "%, " + brightness + "%);");
    }

    public Ellipse createFixationPoint(double positionX, double positionY, double width, double height, Color color) {

        double screenResolutionX = data.getScreenResolutionX();
        double screenResolutionY = data.getScreenResolutionY();

        double visualFieldX = data.getVisualFieldX();
        double visualFieldY = data.getVisualFieldY();

        double pixelsForOneDegreeX = screenResolutionX / visualFieldX;
        double pixelsForOneDegreeY = screenResolutionY / visualFieldY;

        double radiusX = (width / 2) * pixelsForOneDegreeX;
        double radiusY = (height / 2) * pixelsForOneDegreeY;

        Ellipse fixationPoint;
        fixationPoint = new Ellipse(positionX, positionY, radiusX, radiusY);
        fixationPoint.setFill(color);
        fixationPoint.setStroke(color);
        return fixationPoint;
    }

    public void setFixationPointsOnPanes() {
        Ellipse fixPointBeforeChange = createFixationPoint(100, 100, data.getFixPointWidth(), data.getFixPointHeight(),
                Color.hsb(Integer.valueOf(data.getFixPointHue()), Double.valueOf(data.getFixPointSaturation()) / 100, Double.valueOf(data.getFixPointBrightness()) / 100));
        Ellipse fixPointAfterChange = createFixationPoint(100, 100, data.getFixPointChangeWidth(), data.getFixPointChangeHeight(),
                Color.hsb(Integer.valueOf(data.getFixPointChangeHue()), Double.valueOf(data.getFixPointChangeSaturation()) / 100, Double.valueOf(data.getFixPointChangeBrightness()) / 100));
        paneFixPointBefore.getChildren().add(fixPointBeforeChange);
        paneFixPointAfter.getChildren().add(fixPointAfterChange);
    }
}
