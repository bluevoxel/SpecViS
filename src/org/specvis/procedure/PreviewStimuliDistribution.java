package org.specvis.procedure;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

/**
 * Created by pdzwiniel on 2015-06-10.
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

public class PreviewStimuliDistribution extends Stage {

    private Data data;
    private int activeDisplayIndex;
    private int screenResolutionX;
    private int screenResolutionY;
    private double visualFieldX;
    private double visualFieldY;
    private int quarterGridResolutionX;
    private int quarterGridResolutionY;
    private String fixationMonitor;
    private double blindspotDistFromFixPointHorizontally;
    private double blindspotDistFromFixPointVertically;
    private int backgroundHue;
    private int backgroundSaturation;
    private int backgroundBrightness;
    private Pane displayPane;

    public Parent createContent() {

        /* init fields */
        activeDisplayIndex = data.getActiveDisplayIndex();
        screenResolutionX = data.getScreenResolutionX();
        screenResolutionY = data.getScreenResolutionY();
        visualFieldX = data.getVisualFieldX();
        visualFieldY = data.getVisualFieldY();
        quarterGridResolutionX = data.getQuarterGridResolutionX();
        quarterGridResolutionY = data.getQuarterGridResolutionY();
        fixationMonitor = data.getFixationMonitor();
        blindspotDistFromFixPointHorizontally = data.getBlindspotDistanceFromFixPointHorizontally();
        blindspotDistFromFixPointVertically = data.getBlindspotDistanceFromFixPointVertically();
        backgroundHue = Integer.valueOf(data.getLuminanceScaleData().getScaleHue());
        backgroundSaturation = Integer.valueOf(data.getLuminanceScaleData().getScaleSaturation());
        backgroundBrightness = data.getBackgroundBrightness();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> display pane */
        displayPane = new Pane();
        displayPane.setStyle("-fx-background-color: hsb(" + backgroundHue + ", " + backgroundSaturation + "%, " + backgroundBrightness + "%);");
        displayPane.setMinSize(screenResolutionX, screenResolutionY);
        displayPane.setMaxSize(screenResolutionX, screenResolutionY);

        /* call painting functions */
        drawLines();
        drawMarkers(fixationMonitor);

        /* return layout */
        layout.setCenter(displayPane);

        return layout;
    }

    public PreviewStimuliDistribution(Data data) {
        this.data = data;
        this.setScene(new Scene(createContent()));

        List<Screen> displays = Screen.getScreens();
        Screen activeDisplay = displays.get(activeDisplayIndex);
        Rectangle2D activeDisplayBounds = activeDisplay.getVisualBounds();

        this.setX(activeDisplayBounds.getMinX());
        this.setY(activeDisplayBounds.getMinY());
        this.setWidth(activeDisplayBounds.getWidth());
        this.setHeight(activeDisplayBounds.getHeight());
        this.initStyle(StageStyle.UNDECORATED);

        this.getScene().addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
    }

    public void drawLines() {
        Line line;
        Color lineColor = Color.web("rgb(244, 244, 244)");
        double lineWidth = 1.5;

        int quarterResX = quarterGridResolutionX * 2;
        int quarterResY = quarterGridResolutionY * 2;

        double spaceX = (double) screenResolutionX / quarterResX;
        double spaceY = (double) screenResolutionY / quarterResY;

        /* vertical lines */
        for (int i = 1; i < quarterResX; i++) {

            line = new Line(i * spaceX, 0, i * spaceX, screenResolutionY);
            line.getStrokeDashArray().addAll(2d, 21d);
            line.setStroke(lineColor);
            line.setStrokeWidth(lineWidth);
            displayPane.getChildren().add(line);
        }

		/* horizontal lines */
        for (int i = 1; i < quarterResY; i++) {

            line = new Line(0, i * spaceY, screenResolutionX, i * spaceY);
            line.getStrokeDashArray().addAll(2d, 21d);
            line.setStroke(lineColor);
            line.setStrokeWidth(lineWidth);
            displayPane.getChildren().add(line);
        }

        /* middle cross */
        line = new Line(0, (double) screenResolutionY / 2, screenResolutionX, (double) screenResolutionY / 2);
        line.setStroke(lineColor);
        line.setStrokeWidth(lineWidth);
        displayPane.getChildren().add(line);

        line = new Line((double) screenResolutionX / 2, 0, (double) screenResolutionX / 2 , screenResolutionY);
        line.setStroke(lineColor);
        line.setStrokeWidth(lineWidth);
        displayPane.getChildren().add(line);
    }

    public void drawMarkers(String fixationMonitor) {
        Label labelStimulus;

        double squareX = screenResolutionX / (quarterGridResolutionX * 2);
        double squareY = screenResolutionY / (quarterGridResolutionY * 2);

        for (int i = 0; i < quarterGridResolutionX * 2; i++) {
            for (int j = 0; j < quarterGridResolutionY * 2; j++) {
                labelStimulus = new Label("S");
                labelStimulus.setMinSize(20, 20);
                labelStimulus.setMaxSize(20, 20);
                labelStimulus.setLayoutX((squareX / 2) + (i * squareX) - (labelStimulus.getMaxWidth() / 2));
                labelStimulus.setLayoutY((squareY / 2) + (j * squareY) - (labelStimulus.getMaxHeight() / 2));
                labelStimulus.setStyle("-fx-border-width: 1; " +
                        "-fx-border-color: white; " +
                        "-fx-border-style: solid; " +
                        "-fx-background-color: #FFF; " +
                        "-fx-font-size: 14px;");
                labelStimulus.setAlignment(Pos.CENTER);
                displayPane.getChildren().add(labelStimulus);
            }
        }

        Label labelFixationPoint;
        if (fixationMonitor.equals("None")) {
            labelFixationPoint = new Label("F");
        } else if (fixationMonitor.equals("Blindspot")) {
            labelFixationPoint = new Label("F");

            Label labelFixationMonitor = new Label("M");
            labelFixationMonitor.setMinSize(30, 30);
            labelFixationMonitor.setMaxSize(30, 30);

            double centerX = (screenResolutionX / 2) - (labelFixationMonitor.getMaxWidth() / 2);
            double centerY = (screenResolutionY / 2) - (labelFixationMonitor.getMaxHeight() / 2);

            double pixelsForOneDegreeX = screenResolutionX / visualFieldX;
            double pixelsForOneDegreeY = screenResolutionY / visualFieldY;

            double distanceX;
            double distanceY;
            if (data.getExaminedEye().equals("Right")) {
                distanceX = pixelsForOneDegreeX * blindspotDistFromFixPointHorizontally;
                distanceY = pixelsForOneDegreeY * (-blindspotDistFromFixPointVertically);
            } else {
                distanceX = pixelsForOneDegreeX * (-blindspotDistFromFixPointHorizontally);
                distanceY = pixelsForOneDegreeY * (-blindspotDistFromFixPointVertically);
            }

            labelFixationMonitor.setLayoutX(centerX + distanceX);
            labelFixationMonitor.setLayoutY(centerY + distanceY);
            labelFixationMonitor.setStyle("-fx-border-width: 1; " +
                    "-fx-border-color: white; " +
                    "-fx-border-style: solid; " +
                    "-fx-background-color: #FFF; " +
                    "-fx-font-size: 14px;");
            labelFixationMonitor.setAlignment(Pos.CENTER);
            displayPane.getChildren().add(labelFixationMonitor);
        } else {
            labelFixationPoint = new Label("F+M");
        }
        labelFixationPoint.setMinSize(40, 40);
        labelFixationPoint.setMaxSize(40, 40);
        labelFixationPoint.setLayoutX((screenResolutionX / 2) - (labelFixationPoint.getMaxWidth() / 2));
        labelFixationPoint.setLayoutY((screenResolutionY / 2) - (labelFixationPoint.getMaxHeight() / 2));
        labelFixationPoint.setStyle("-fx-border-width: 1; " +
                "-fx-border-color: white; " +
                "-fx-border-style: solid; " +
                "-fx-background-color: #FFF; " +
                "-fx-font-size: 14px;");
        labelFixationPoint.setAlignment(Pos.CENTER);

        displayPane.getChildren().add(labelFixationPoint);
    }
}
