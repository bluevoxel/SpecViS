package org.specvis.procedure;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pdzwiniel on 2015-10-03.
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

public class ShowResultsAsVisualFieldMap extends Stage {

    private Data data;
    private HBox hBoxCenter;
    private CheckMenuItem checkMenuItemShowGraphicalAnswers;
    private CheckMenuItem checkMenuItemShowNumericalValues;
    private CheckMenuItem checkMenuItemShowDistanceMarkers;
    private CheckMenuItem checkMenuItemShowBlindspot;
    private CheckMenuItem checkMenuItemShowGrid;
    private CheckMenuItem checkMenuItemScaleBrightness;
    private CheckMenuItem checkMenuItemScaleLuminance;
    private CheckMenuItem checkMenuItemScaleDecibels;
    private Pane displayPane;
    private double lowestStimuliLuminance;
    private double backgroundLuminance;
    private double maxPossibleLuminance;
    private Label labelMaxValue;
    private Label labelMinValue;

    public Parent createContent() {

        /* init some data */
        lowestStimuliLuminance = data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[data.getStimuliVerifiedBrightnessValues()[0]], 2);
        backgroundLuminance = data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForBackground().getLuminanceForBrightness()[data.getBackgroundBrightness()], 2);
        maxPossibleLuminance = data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[100], 2);

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> top */
        MenuBar menuBar = new MenuBar();

        Menu menuFigure = new Menu("Figure");

        MenuItem menuItemSaveAsJPG = new MenuItem("Save as *.jpg");
        menuItemSaveAsJPG.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                WritableImage snapshot = hBoxCenter.snapshot(new SnapshotParameters(), null);
                BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
                BufferedImage bufferedImage = new BufferedImage(670, 602, BufferedImage.OPAQUE);
                try {
                    Graphics2D graphics2D = bufferedImage.createGraphics();
                    graphics2D.drawImage(image, 0, 0, null);
                    ImageIO.write(bufferedImage, "jpg", file);
                    graphics2D.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        checkMenuItemShowGraphicalAnswers = new CheckMenuItem("Show graphical answers");
        checkMenuItemShowGraphicalAnswers.setOnAction(event -> redrawVisualFieldMap());
        checkMenuItemShowGraphicalAnswers.setSelected(true);

        checkMenuItemShowNumericalValues = new CheckMenuItem("Show numerical values");
        checkMenuItemShowNumericalValues.setOnAction(event -> redrawVisualFieldMap());
        checkMenuItemShowNumericalValues.setSelected(true);

        checkMenuItemShowDistanceMarkers = new CheckMenuItem("Show x10\u00B0 distance markers");
        checkMenuItemShowDistanceMarkers.setOnAction(event -> redrawVisualFieldMap());
        checkMenuItemShowDistanceMarkers.setSelected(true);

        checkMenuItemShowBlindspot = new CheckMenuItem("Show blindspot");
        checkMenuItemShowBlindspot.setOnAction(event -> redrawVisualFieldMap());
        checkMenuItemShowBlindspot.setSelected(true);

        checkMenuItemShowGrid = new CheckMenuItem("Show grid");
        checkMenuItemShowGrid.setOnAction(event -> redrawVisualFieldMap());
        checkMenuItemShowGrid.setSelected(true);

        Menu menuFigureScale = new Menu("Scale");

        checkMenuItemScaleBrightness = new CheckMenuItem("Brightness (%)");
        checkMenuItemScaleBrightness.setOnAction(event -> {
            if (checkMenuItemScaleLuminance.isSelected()) {
                checkMenuItemScaleLuminance.setSelected(false);
            }
            if (checkMenuItemScaleDecibels.isSelected()) {
                checkMenuItemScaleDecibels.setSelected(false);
            }
            if (checkMenuItemScaleBrightness.isSelected()) {
                checkMenuItemScaleBrightness.setSelected(true);
            } else {
                checkMenuItemScaleBrightness.setSelected(true);
            }
            redrawVisualFieldMap();
        });
        checkMenuItemScaleBrightness.setSelected(false);

        checkMenuItemScaleLuminance = new CheckMenuItem("Luminance (cd/m2)");
        checkMenuItemScaleLuminance.setOnAction(event -> {
            if (checkMenuItemScaleBrightness.isSelected()) {
                checkMenuItemScaleBrightness.setSelected(false);
            }
            if (checkMenuItemScaleDecibels.isSelected()) {
                checkMenuItemScaleDecibels.setSelected(false);
            }
            if (checkMenuItemScaleLuminance.isSelected()) {
                checkMenuItemScaleLuminance.setSelected(true);
            } else {
                checkMenuItemScaleLuminance.setSelected(true);
            }
            redrawVisualFieldMap();
        });
        checkMenuItemScaleLuminance.setSelected(true);

        checkMenuItemScaleDecibels = new CheckMenuItem("Decibels (dB)");
        checkMenuItemScaleDecibels.setOnAction(event -> {
            if (checkMenuItemScaleBrightness.isSelected()) {
                checkMenuItemScaleBrightness.setSelected(false);
            }
            if (checkMenuItemScaleLuminance.isSelected()) {
                checkMenuItemScaleLuminance.setSelected(false);
            }
            if (checkMenuItemScaleDecibels.isSelected()) {
                checkMenuItemScaleDecibels.setSelected(true);
            } else {
                checkMenuItemScaleDecibels.setSelected(true);
            }
            redrawVisualFieldMap();
        });
        checkMenuItemScaleDecibels.setSelected(false);

        menuFigureScale.getItems().addAll(checkMenuItemScaleBrightness, checkMenuItemScaleLuminance, checkMenuItemScaleDecibels);
        menuFigure.getItems().addAll(menuItemSaveAsJPG, new SeparatorMenuItem(), checkMenuItemShowGraphicalAnswers,
                checkMenuItemShowNumericalValues, checkMenuItemShowBlindspot, checkMenuItemShowDistanceMarkers,
                checkMenuItemShowGrid, menuFigureScale);
        menuBar.getMenus().addAll(menuFigure);

        /* layout -> center */
        hBoxCenter = new HBox(10);

        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: #ffffff; -fx-border-width: 1; -fx-border-color: black;");
        displayPane = new Pane();
        displayPane.setMinSize(600, 600);
        displayPane.setMaxSize(600, 600);
        stackPane.getChildren().add(displayPane);

        VBox vBoxColorScale = new VBox(5);

        labelMaxValue = new Label(String.valueOf(maxPossibleLuminance));
        labelMaxValue.setMinSize(60, 20);
        labelMaxValue.setMaxSize(60, 20);
        labelMaxValue.setAlignment(Pos.CENTER);

        labelMinValue = new Label(String.valueOf(backgroundLuminance));
        labelMinValue.setMinSize(60, 20);
        labelMinValue.setMaxSize(60, 20);
        labelMinValue.setAlignment(Pos.CENTER);

        Image image = createColorScale(backgroundLuminance, maxPossibleLuminance, 40, 550, Orientation.VERTICAL);
        ImageView colorScale = new ImageView(image);

        VBox vBoxForScale = new VBox();
        vBoxForScale.setMaxSize(40, 550);
        vBoxForScale.setStyle("-fx-border-width: 1; -fx-border-color: black;");
        vBoxForScale.getChildren().add(colorScale);

        vBoxColorScale.getChildren().addAll(labelMaxValue, vBoxForScale, labelMinValue);
        vBoxColorScale.setAlignment(Pos.CENTER);

        hBoxCenter.getChildren().addAll(stackPane, vBoxColorScale);
        hBoxCenter.setAlignment(Pos.CENTER);

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonClose = new Button("Close");
        buttonClose.setOnAction(event -> close());

        hBoxBottom.getChildren().addAll(buttonClose);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* init funtions */
        redrawVisualFieldMap();

        /* add items to layout and return it */
        BorderPane.setMargin(hBoxCenter, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        layout.setStyle("-fx-background-color: #FFFFFF;");

        layout.setTop(menuBar);
        layout.setCenter(hBoxCenter);
        layout.setBottom(hBoxBottom);
        return layout;
    }

    public ShowResultsAsVisualFieldMap(Data data) {
        this.data = data;
        this.setScene(new Scene(createContent()));
        this.setMaxWidth(760);
        this.setMaxHeight(750);
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }

    public void redrawVisualFieldMap() {

        if (displayPane.getChildren().size() > 0) {
            displayPane.getChildren().clear();
        }

        ArrayList<StimulusResults> stimulusResultsArrayList = data.getMainProcedureStimuliResults();
        int gridResolutionX = data.getQuarterGridResolutionX() * 2;
        int gridResolutionY = data.getQuarterGridResolutionY() * 2;
        double spaceX = displayPane.getMaxWidth() / gridResolutionX;
        double spaceY = displayPane.getMaxHeight() / gridResolutionY;
        int stimulusTotalNumber = data.getMainProcedureStimuliResults().size();
        String scaleType;

        if (checkMenuItemScaleBrightness.isSelected()) {
            scaleType = "Brightness";

            int lowestBriStim = data.getStimuliVerifiedBrightnessValues()[0];
            labelMaxValue.setText(String.valueOf(maxPossibleLuminance));
            labelMinValue.setText(String.valueOf(lowestBriStim));

        } else if (checkMenuItemScaleLuminance.isSelected()) {
            scaleType = "Luminance";

            labelMaxValue.setText(String.valueOf(maxPossibleLuminance));
            labelMinValue.setText(String.valueOf(lowestStimuliLuminance));

        } else {
            scaleType = "Decibels";

            labelMaxValue.setText(String.valueOf(data.getScreenLuminanceFunctions().decibelsValue(maxPossibleLuminance, maxPossibleLuminance, backgroundLuminance, 2)));
            labelMinValue.setText(String.valueOf(data.getScreenLuminanceFunctions().decibelsValue(maxPossibleLuminance, lowestStimuliLuminance, backgroundLuminance, 2)));
        }

        /* DRAW RECTANGLES */
        if (checkMenuItemShowGraphicalAnswers.isSelected()) {
            for (int i = 0; i < stimulusTotalNumber; i++) {
                int[] stimulusGridCoordinatesXY = stimulusResultsArrayList.get(i).getStimulusGridCoordinatesXY();

                Color stimulusColor = getColorForSpecificValue(backgroundLuminance, maxPossibleLuminance, stimulusResultsArrayList.get(i).getStimulusLuminanceThreshold());

                Rectangle r = new Rectangle((spaceX * stimulusGridCoordinatesXY[0]), (spaceY * stimulusGridCoordinatesXY[1]), spaceX, spaceY);
                r.setFill(stimulusColor);
                displayPane.getChildren().add(r);
            }
        }

        /* DRAW GRID */
        if (checkMenuItemShowGrid.isSelected()) {
            /* vertical lines */
            for (int i = 1; i < gridResolutionX; i++) {
                Line lineVertical = new Line(i * spaceX, 0, i * spaceX, displayPane.getMaxHeight());
                displayPane.getChildren().add(lineVertical);
            }

            /* horizontal lines */
            for (int i = 1; i < gridResolutionY; i++) {
                Line lineHorizontal = new Line(0, i * spaceY, displayPane.getMaxWidth(), i * spaceY);
                displayPane.getChildren().add(lineHorizontal);
            }
        }

        /* DRAW DISTANCE MARKERS */
        if (checkMenuItemShowDistanceMarkers.isSelected()) {

            double halfOfTheVisualFieldX = data.getVisualFieldX() / 2;
            double halfOfTheVisualFieldY = data.getVisualFieldY() / 2;

            int howMany10DegreesInFieldX = (int) Math.floor(halfOfTheVisualFieldX / 10);
            int howMany10DegreesInFieldY = (int) Math.floor(halfOfTheVisualFieldY / 10);

            double pixelsForOneDegreeX = displayPane.getMaxWidth() / data.getVisualFieldX();
            double pixelsForOneDegreeY = displayPane.getMaxHeight() / data.getVisualFieldY();

            double distanceOf10DegreesInPixelsX = pixelsForOneDegreeX * 10;
            double distanceOf10DegreesInPixelsY = pixelsForOneDegreeY * 10;

            double centerX = displayPane.getMaxWidth() / 2;
            double centerY = displayPane.getMaxHeight() / 2;

            for (int i = 1; i <= howMany10DegreesInFieldX; i++) {
                Circle pointLeft = new Circle(centerX - (distanceOf10DegreesInPixelsX * i), centerY, 4);
                pointLeft.setFill(Color.BLACK);
                Circle pointRight = new Circle(centerX + (distanceOf10DegreesInPixelsX * i), centerY, 4);
                pointRight.setFill(Color.BLACK);
                displayPane.getChildren().addAll(pointLeft, pointRight);
            }

            for (int i = 1; i <= howMany10DegreesInFieldY; i++) {
                Circle pointUp = new Circle(centerX, centerY - (distanceOf10DegreesInPixelsY * i), 4);
                pointUp.setFill(Color.BLACK);
                Circle pointDown = new Circle(centerX, centerY + (distanceOf10DegreesInPixelsY * i), 4);
                pointDown.setFill(Color.BLACK);
                displayPane.getChildren().addAll(pointUp, pointDown);
            }
        }

        /* DRAW BLINDSPOT */
        if (checkMenuItemShowBlindspot.isSelected()) {
            //Label labelBlindspot = new Label("BS");
            Label labelBlindspot = new Label();
            labelBlindspot.setMinSize(15, 15);
            labelBlindspot.setMaxSize(15, 15);

            double centerOfTheLabelX = (displayPane.getMaxWidth() / 2) - (labelBlindspot.getMaxWidth() / 2);
            double centerOfTheLabelY = (displayPane.getMaxHeight() / 2) - (labelBlindspot.getMaxHeight() / 2);

            double pixelsForOneDegreeX = displayPane.getMaxWidth() / data.getVisualFieldX();
            double pixelsForOneDegreeY = displayPane.getMaxHeight() / data.getVisualFieldY();
            double distanceX;
            double distanceY;
            if (data.getExaminedEye().equals("Right")) {
                distanceX = pixelsForOneDegreeX * data.getBlindspotDistanceFromFixPointHorizontally();
                distanceY = pixelsForOneDegreeY * data.getBlindspotDistanceFromFixPointVertically();
            } else {
                distanceX = pixelsForOneDegreeX * (-data.getBlindspotDistanceFromFixPointHorizontally());
                distanceY = pixelsForOneDegreeY * (-data.getBlindspotDistanceFromFixPointVertically());
            }

            labelBlindspot.setLayoutX(centerOfTheLabelX + distanceX);
            labelBlindspot.setLayoutY(centerOfTheLabelY + distanceY);
            labelBlindspot.setStyle("-fx-border-width: 1; " +
                    "-fx-border-color: black; " +
                    "-fx-border-style: solid; " +
                    "-fx-background-color: #FFF;");
            labelBlindspot.setAlignment(Pos.CENTER);
            displayPane.getChildren().add(labelBlindspot);
        }

        /* DRAW VALUES */
        if (checkMenuItemShowNumericalValues.isSelected()) {
            switch (scaleType) {
                case "Brightness":
                    for (int i = 0; i < stimulusTotalNumber; i++) {
                        int[] stimulusGridCoordinatesXY = stimulusResultsArrayList.get(i).getStimulusGridCoordinatesXY();
                        Label labelValue = new Label(String.valueOf(stimulusResultsArrayList.get(i).getStimulusBrightnessThreshold()));
                        labelValue.setMinSize(spaceX, spaceY);
                        labelValue.setLayoutX(spaceX * stimulusGridCoordinatesXY[0]);
                        labelValue.setLayoutY(spaceY * stimulusGridCoordinatesXY[1]);
                        labelValue.setAlignment(Pos.CENTER);
                        displayPane.getChildren().add(labelValue);
                    }
                    break;
                case "Luminance":
                    for (int i = 0; i < stimulusTotalNumber; i++) {
                        int[] stimulusGridCoordinatesXY = stimulusResultsArrayList.get(i).getStimulusGridCoordinatesXY();
                        Label labelValue = new Label(String.valueOf(data.getScreenLuminanceFunctions().round(stimulusResultsArrayList.get(i).getStimulusLuminanceThreshold(), 2)));
                        labelValue.setMinSize(spaceX, spaceY);
                        labelValue.setLayoutX(spaceX * stimulusGridCoordinatesXY[0]);
                        labelValue.setLayoutY(spaceY * stimulusGridCoordinatesXY[1]);
                        labelValue.setAlignment(Pos.CENTER);
                        displayPane.getChildren().add(labelValue);
                    }
                    break;
                case "Decibels":
                    for (int i = 0; i < stimulusTotalNumber; i++) {
                        int[] stimulusGridCoordinatesXY = stimulusResultsArrayList.get(i).getStimulusGridCoordinatesXY();
                        double stimulusLuminance = stimulusResultsArrayList.get(i).getStimulusLuminanceThreshold();
                        int round = 2;
                        double decibels = data.getScreenLuminanceFunctions().decibelsValue(maxPossibleLuminance, stimulusLuminance, backgroundLuminance, round);
                        Label labelValue = new Label(String.valueOf(decibels));
                        labelValue.setMinSize(spaceX, spaceY);
                        labelValue.setLayoutX(spaceX * stimulusGridCoordinatesXY[0]);
                        labelValue.setLayoutY(spaceY * stimulusGridCoordinatesXY[1]);
                        labelValue.setAlignment(Pos.CENTER);
                        displayPane.getChildren().add(labelValue);
                    }
                    break;
            }
        }

        /* DRAW FIXATION POINT */
        double centerX = spaceX * (gridResolutionX / 2);
        double centerY = spaceY * (gridResolutionY / 2);
        Line lineFixX = new Line(centerX - 20, centerY, centerX + 20, centerY);
        Line lineFixY = new Line(centerX, centerY - 20, centerX, centerY + 20);
        lineFixX.setStrokeWidth(4);
        lineFixY.setStrokeWidth(4);
        displayPane.getChildren().addAll(lineFixX, lineFixY);
    }

    private Image createColorScale(double minValue, double maxValue, int width, int height, Orientation orientation) {
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        if (orientation == Orientation.HORIZONTAL) {
            for (int x = 0; x < width; x++) {
                double value = minValue + (maxValue - minValue) * x / width;
                Color color = getColorForSpecificValue(minValue, maxValue, value);
                for (int y = 0; y < height; y++) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                double value = maxValue - (maxValue - minValue) * y / height;
                Color color = getColorForSpecificValue(minValue, maxValue, value);
                for (int x = 0; x < width; x++) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
        return writableImage;
    }

    private Color getColorForSpecificValue(double minScaleValue, double maxScaleValue, double value) {
        double scaleRange = maxScaleValue - minScaleValue;
        double normalizedValue = value - minScaleValue;
        double finalBrightness = 1.0 * (normalizedValue / scaleRange);
        return Color.hsb(0, 0.0, 1.0 - finalBrightness);
    }
}
