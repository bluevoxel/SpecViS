package org.specvis.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.specvis.data.ConfigurationData;
import org.specvis.data.LuminanceScaleData;
import org.specvis.logic.ChiSquared;
import org.specvis.logic.ScreenLuminanceFunctions;

/**
 * Created by pdzwiniel on 2015-05-28.
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

public class Step2_ScalePreview extends Stage {

    private LuminanceScaleData luminanceScaleData;

    public Parent createContent() {

        /* init logic */
        ScreenLuminanceFunctions screenLuminanceFunctions = new ScreenLuminanceFunctions();

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> top */
        VBox vBox = new VBox(10);

        HBox hBox_1 = new HBox(10);

        Label labelGoodnessOfFit = new Label("Goodness of fit:");
        Label labelChiSquared = new Label();
        Label labelMeanSpread = new Label();

        hBox_1.getChildren().addAll(labelChiSquared, labelMeanSpread);
        hBox_1.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().addAll(labelGoodnessOfFit, labelChiSquared, labelMeanSpread);

        /* layout -> chart */
        /* DEFINE AXES */
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("HSB Brightness (%)");
        yAxis.setLabel("Luminance (cd/m2)");

        /* INIT LINE CHART */
        LineChart<Number,Number> lineChart = new LineChart(xAxis,yAxis);
        lineChart.setTitle("Screen luminance scale");

        /* DEFINE DATA */
        double[] brightnessMeasureScale = luminanceScaleData.getBrightnessMeasureScale();
        double[] luminanceMeasurements = luminanceScaleData.getLuminanceMeasurements();
        double[] brightnessVector = luminanceScaleData.getBrightnessVector();
        double[] luminanceForBrightness = luminanceScaleData.getLuminanceForBrightness();

        /* set negative fitted luminance values to 0.1 */
        /* it is necessary for chi-squared analysis */
        for (int i = 0; i < luminanceForBrightness.length; i++) {
            if (luminanceForBrightness[i] < 0.0) {
                luminanceForBrightness[i] = Double.valueOf(ConfigurationData.getIfFittedValuesAreLessThanZeroSetThemTo());
            }
        }

        /* CALCULATE CORRELATION BETWEEN MEASURED DATA AND FITTED DATA */
        double[] luminanceFitted = new double[] {luminanceForBrightness[0], luminanceForBrightness[20],
                luminanceForBrightness[40], luminanceForBrightness[60], luminanceForBrightness[80],
                luminanceForBrightness[100]};

        /* CALCULATE SPREAD BETWEEN MEASURED AND FITTED DATA */
        double[] difference = new double[luminanceMeasurements.length];
        for (int i = 0; i < luminanceMeasurements.length; i++) {
            difference[i] = screenLuminanceFunctions.round(luminanceMeasurements[i] - luminanceFitted[i], 2);
        }
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        for (int i = 0; i < difference.length; i++) {
            descriptiveStatistics.addValue(difference[i]);
        }
        double std = screenLuminanceFunctions.round(descriptiveStatistics.getStandardDeviation(), 2);
        labelMeanSpread.setText("Spread (STD) between measured and fitted luminance \u00b1 " + std + " (cd/m2)");

        /* chi squared */
        double[] expected = luminanceFitted;
        double[] observed = luminanceScaleData.getLuminanceMeasurements();
        int degreesOfFreedom = observed.length - 1;

        ChiSquared chiSquared = new ChiSquared();
        double criticalValue = chiSquared.calculateCriticalValue(expected, observed);
        double pValue = chiSquared.calculateChiSquaredPValue(degreesOfFreedom, criticalValue);

        labelChiSquared.setText("Chi-squared: " + screenLuminanceFunctions.round(criticalValue, 4) + ", p-value: " + screenLuminanceFunctions.round(pValue, 4));

        /* DEFINE CHART SERIES -> SERIES "One" */
        XYChart.Series seriesOne = new XYChart.Series();
        seriesOne.setName("2nd order polynomial fit");

        /* DEFINE CHART SERIES -> SERIES "Two" */
        XYChart.Series seriesTwo = new XYChart.Series();
        seriesTwo.setName("Luminance measurements");

        /* NEST DATA IN SERIES */
        for (int i = 0; i < brightnessVector.length; i++) {
            seriesOne.getData().add(new XYChart.Data(brightnessVector[i], luminanceForBrightness[i]));
        }

        for (int i = 0; i < brightnessMeasureScale.length; i++) {
            seriesTwo.getData().add(new XYChart.Data(brightnessMeasureScale[i], luminanceMeasurements[i]));
        }

        lineChart.getData().addAll(seriesOne, seriesTwo);
        lineChart.getStylesheets().add(getClass().getResource("/org/specvis/css_styles/ScreenLuminanceLineChart.css")
                .toExternalForm());

        /* layout -> bottom */
        HBox hBoxBottom = new HBox(10);

        Button buttonClose = new Button("Close");
        buttonClose.setOnAction(event -> this.close());

        hBoxBottom.getChildren().addAll(buttonClose);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        /* return layout */
        layout.setTop(vBox);
        layout.setCenter(lineChart);
        layout.setBottom(hBoxBottom);

        BorderPane.setMargin(vBox, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(hBoxBottom, new Insets(10, 10, 10, 10));

        return layout;
    }

    public Step2_ScalePreview(LuminanceScaleData luminanceScaleData) {
        this.luminanceScaleData = luminanceScaleData;
        this.setScene(new Scene(createContent()));
        this.setWidth(600);
        this.setHeight(600);
        this.setTitle("Specvis");
        this.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
    }
}
