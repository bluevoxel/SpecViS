package org.specvis;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.specvis.data.ConfigurationData;
import org.specvis.gui.*;
import org.specvis.procedure.Data;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by pdzwiniel on 2015-05-20.
 * Last update by pdzwiniel on 2015-11-20.
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

public class StartApplication extends Application {

    private Data data;
    private Step1_Patient stageStep1Patient;
    private Step2_ScreenAndLuminanceScale stageStep2ScreenAndLuminance;
    private Step3_StimulusAndBackground stageStep3Stimulus;
    private Step4_FixationAndOther stageStep4General;

    private Timeline timeline;

    public Parent createContent() {

        /* init data */
        data = new Data();

        /* load configuration data from conf.s */
        loadDataFromConfigurationFile();

        /* initialize specvis settings windows */
        stageStep1Patient = new Step1_Patient(this);
        stageStep2ScreenAndLuminance = new Step2_ScreenAndLuminanceScale(this);
        stageStep3Stimulus = new Step3_StimulusAndBackground(this);
        stageStep4General = new Step4_FixationAndOther(this);

        /* functionality responsible for displaying specvis logo for fixed amount of time,
         * after which the first "step" specvis settings window is displayed */
        timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyFrame waitWithShowingSpecvisLogo = new KeyFrame(Duration.millis(3000), event -> {
            stageStep1Patient.show();
        });
        timeline.getKeyFrames().add(waitWithShowingSpecvisLogo);

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> Logo */
        Pane glassForLogo = new Pane();

        Label labelSpecvisLogo = new Label();
        String logoPath = StartApplication.class.getResource("/org/specvis/graphics/SpecvisLogo.jpg").toExternalForm();
        labelSpecvisLogo.setStyle(
                "-fx-background-image: url('"+logoPath+"'); "
                + "-fx-background-position: center  center; "
                + "-fx-background-repeat: stretch; "
        );
        labelSpecvisLogo.setPrefSize(720, 270);

        Label labelCopyright = new Label("Copyright \u00a9 2014-2015 Piotr Dzwiniel" + "\n" + "Version 0.2.2");
        labelCopyright.setStyle("-fx-font-size: 14px;");
        labelCopyright.setLayoutX(5);
        labelCopyright.setLayoutY(5);

        /* layout -> Logo (add items) */
        glassForLogo.getChildren().addAll(labelSpecvisLogo, labelCopyright);

        /* return layout */
        layout.setCenter(glassForLogo);
        return layout;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.setWidth(720);
        stage.setHeight(270);
        stage.getIcons().add(new Image("/org/specvis/graphics/SpecvisIcon.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.centerOnScreen();
        stage.show();

        timeline.play();
        timeline.setOnFinished(event -> stage.close());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Step1_Patient getStageStep1Patient() {
        return stageStep1Patient;
    }

    public Step2_ScreenAndLuminanceScale getStageStep2ScreenAndLuminance() {
        return stageStep2ScreenAndLuminance;
    }

    public Step3_StimulusAndBackground getStageStep3Stimulus() {
        return stageStep3Stimulus;
    }

    public Step4_FixationAndOther getStageStep4General() {
        return stageStep4General;
    }

    public Data getData() {
        return data;
    }

    private void loadDataFromConfigurationFile() {
        ArrayList<String> configurationValues = new ArrayList();
        File file = new File("conf.s");
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split("=|;");
                configurationValues.add(str[1]);
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ConfigurationData.setLuminanceScaleFitPolynomialDegree(configurationValues.get(0));
        ConfigurationData.setIfFittedValuesAreLessThanZeroSetThemTo(configurationValues.get(1));
        ConfigurationData.setScreenWidth(configurationValues.get(2));
        ConfigurationData.setScreenHeight(configurationValues.get(3));
        ConfigurationData.setPatientDistance(configurationValues.get(4));
        ConfigurationData.setStimulusMaxBrightness(configurationValues.get(5));
        ConfigurationData.setStimulusMinBrightness(configurationValues.get(6));
        ConfigurationData.setStimulusShape(configurationValues.get(7));
        ConfigurationData.setStimulusInclination(configurationValues.get(8));
        ConfigurationData.setStimulusWidth(configurationValues.get(9));
        ConfigurationData.setStimulusHeight(configurationValues.get(10));
        ConfigurationData.setStimulusDisplayTime(configurationValues.get(11));
        ConfigurationData.setConstantPartOfIntervalBetweenStimuli(configurationValues.get(12));
        ConfigurationData.setRandomPartOfIntervalBetweenStimuli(configurationValues.get(13));
        ConfigurationData.setBackgroundBrightness(configurationValues.get(14));
        ConfigurationData.setQuarterGridResolutionX(configurationValues.get(15));
        ConfigurationData.setQuarterGridResolutionY(configurationValues.get(16));
        ConfigurationData.setFixationPointColor(configurationValues.get(17));
        ConfigurationData.setFixationPointWidth(configurationValues.get(18));
        ConfigurationData.setFixationPointHeight(configurationValues.get(19));
        ConfigurationData.setAnswerToStimulusKey(configurationValues.get(20));
        ConfigurationData.setPauseProcedureKey(configurationValues.get(21));
        ConfigurationData.setCancelProcedureKey(configurationValues.get(22));
        ConfigurationData.setFixationMonitor(configurationValues.get(23));
        ConfigurationData.setFixationCheckRate(configurationValues.get(24));
        ConfigurationData.setBlindspotDistanceFromFixationPointHorizontally(configurationValues.get(25));
        ConfigurationData.setBlindspotDistanceFromFixationPointVertically(configurationValues.get(26));
        ConfigurationData.setMonitorStimulusWidth(configurationValues.get(27));
        ConfigurationData.setMonitorStimulusHeight(configurationValues.get(28));
        ConfigurationData.setMonitorStimulusBrightness(configurationValues.get(29));
        ConfigurationData.setFixationPointChangeWidth(configurationValues.get(30));
        ConfigurationData.setFixationPointChangeHeight(configurationValues.get(31));
        ConfigurationData.setFixationPointChangeColor(configurationValues.get(32));
        ConfigurationData.setBlindspotMappingRangeHorizontally_1(configurationValues.get(33));
        ConfigurationData.setBlindspotMappingRangeHorizontally_2(configurationValues.get(34));
        ConfigurationData.setBlindspotMappingRangeVertically_1(configurationValues.get(35));
        ConfigurationData.setBlindspotMappingRangeVertically_2(configurationValues.get(36));
        ConfigurationData.setBlindspotMappingStimulusDisplayRepetitions(configurationValues.get(37));
        ConfigurationData.setBlindspotMappingAccuracy(configurationValues.get(38));
        ConfigurationData.setVisualFieldTestBrightnessVectorLength(configurationValues.get(39));
    }
}