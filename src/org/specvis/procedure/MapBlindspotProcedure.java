package org.specvis.procedure;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.specvis.data.ConfigurationData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by pdzwiniel on 2015-06-14.
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

public class MapBlindspotProcedure extends Stage {

    private Data data;
    private ShellWindowForMapBlindspotProcedure shellWindowForMapBlindspotProcedure;
    private Timeline timelineProcedureHorizontal;
    private Timeline timelineProcedureVertical;
    private Pane displayPane;

//    The values of "blindspot mapping range in degrees horizontal" describes the visual field range
//    measured in accordance to the fixation point. For instance, range from 8 to 22 describes
//    visual field range measured horizontaly to the left or to the right from fixation point depending
//    of examined eye. If examined eye is right, than range starts 8 degrees horizontaly to the right
//    from the fixation point.
//    When mapping of the blindspot horizontal boundaries is done, than the center of the resulted "horizontal
//    blindspot range" is used as a reference for "blindspot range in degrees vertical". That is,
//    when lastly mentioned range is closed in values of, say, -12 and +12, that means the blindspot vertical
//    mapping will be performed on the vertical "line" 12 degrees above the center of the blindspot horizontal
//    range and below it.
//    Simple diagram below represents horizontal (H[0, 1]) and vertical (V[0, 1]) blindspot ranges.

//    Examined eye : right
//
//                             V[0]
//                              |
//                              |
//                              |
//    FIX_POINT -----|H[0]|-----------|H[1]|-----
//                              |
//                              |
//                              |
//                             V[1]

    private int[] blindspotMappingRangeInDegreesHorizontal;
    private int[] blindspotMappingRangeInDegreesVertical;
    private int monitorStimulusDisplayRepetitions;
    private String examinedEye;
    private int activeDisplayIndex;
    private int screenResolutionX;
    private int screenResolutionY;
    private double visualFieldX;
    private double visualFieldY;
    private int backgroundHue;
    private int backgroundSaturation;
    private int backgroundBrightness;
    private double mappingAccuracy;
    private double pixelsForOneDegreeX;
    private double pixelsForOneDegreeY;
    private boolean procedureIsStarted;
    private boolean procedureIsRunning;
    private boolean procedureIsFinished;
    private Ellipse[] horizontalMonitorStimuli;
    private Ellipse[] verticalMonitorStimuli;
    private int diffHorizontal;
    private int diffVertical;
    private int[] answersToHorizontalStimuli;
    private int[] answersToVerticalStimuli;
    private boolean horizontalStimuliTurn;
    private boolean verticalStimuliTurn;
    private int indexOfCurrentlyDisplayedStimulus;
    private boolean permissionToAnswer;
    private String mappingData;

    public Parent createContent() {

        /* init fields */
        /**
         * Following ranges are related to the blind spot specification,
         * ie. https://en.wikipedia.org/wiki/Blind_spot_(vision).
         *
         * The blind spot is located about 12-15° temporally and 1.5° below the horizontal
         * and is roughly 7.5° high and 5.5° wide.
         */
        blindspotMappingRangeInDegreesHorizontal = new int[] {Integer.valueOf(ConfigurationData.getBlindspotMappingRangeHorizontally_1()),
                Integer.valueOf(ConfigurationData.getBlindspotMappingRangeHorizontally_2())};
        blindspotMappingRangeInDegreesVertical = new int[] {Integer.valueOf(ConfigurationData.getBlindspotMappingRangeVertically_1()),
                Integer.valueOf(ConfigurationData.getBlindspotMappingRangeVertically_2())};

        /**
         * If one want to make MapBlindspotProcedure more accurate increase
         * monitorStimulusDisplayRepetitions and mappingAccuracy values.
         */
        monitorStimulusDisplayRepetitions = Integer.valueOf(ConfigurationData.getBlindspotMappingStimulusDisplayRepetitions());
        mappingAccuracy = Double.valueOf(ConfigurationData.getBlindspotMappingAccuracy());

        examinedEye = data.getExaminedEye();
        activeDisplayIndex = data.getActiveDisplayIndex();
        screenResolutionX = data.getScreenResolutionX();
        screenResolutionY = data.getScreenResolutionY();
        visualFieldX = data.getVisualFieldX();
        visualFieldY = data.getVisualFieldY();
        backgroundHue = Integer.valueOf(data.getLuminanceScaleDataForBackground().getScaleHue());
        backgroundSaturation = Integer.valueOf(data.getLuminanceScaleDataForBackground().getScaleSaturation());
        backgroundBrightness = data.getBackgroundBrightness();
        pixelsForOneDegreeX = screenResolutionX / visualFieldX;
        pixelsForOneDegreeY = screenResolutionY / visualFieldY;
        procedureIsStarted = false;
        procedureIsRunning = false;
        procedureIsFinished = false;
        diffHorizontal = (int) (Math.abs(blindspotMappingRangeInDegreesHorizontal[0] - blindspotMappingRangeInDegreesHorizontal[1]) / mappingAccuracy);
        diffVertical = (int) (Math.abs(blindspotMappingRangeInDegreesVertical[0] - blindspotMappingRangeInDegreesVertical[1]) / mappingAccuracy);

        answersToHorizontalStimuli = new int[diffHorizontal];
        for (int i = 0; i < answersToHorizontalStimuli.length; i++) {
            answersToHorizontalStimuli[i] = 0;
        }

        answersToVerticalStimuli = new int[diffVertical];
        for (int i = 0; i < answersToVerticalStimuli.length; i++) {
            answersToVerticalStimuli[i] = 0;
        }

        horizontalStimuliTurn = false;
        verticalStimuliTurn = false;

        indexOfCurrentlyDisplayedStimulus = 0;

        permissionToAnswer = true;

        /* layout */
        BorderPane layout = new BorderPane();

        /* layout -> display pane */
        displayPane = new Pane();
        displayPane.setStyle("-fx-background-color: hsb(" + backgroundHue + ", " + backgroundSaturation + "%, " + backgroundBrightness + "%);");
        displayPane.setMinSize(screenResolutionX, screenResolutionY);
        displayPane.setMaxSize(screenResolutionX, screenResolutionY);

        /* call functions */
        drawFixationPoint();

        /* return layout */
        layout.setCenter(displayPane);

        return  layout;
    }

    public MapBlindspotProcedure(Data data, ShellWindowForMapBlindspotProcedure shellWindowForMapBlindspotProcedure) {
        this.data = data;
        this.shellWindowForMapBlindspotProcedure = shellWindowForMapBlindspotProcedure;
        this.setScene(new Scene(createContent()));

        List<Screen> displays = Screen.getScreens();
        Screen activeDisplay = displays.get(activeDisplayIndex);
        Rectangle2D activeDisplayBounds = activeDisplay.getVisualBounds();

        this.setX(activeDisplayBounds.getMinX());
        this.setY(activeDisplayBounds.getMinY());
        this.setWidth(activeDisplayBounds.getWidth());
        this.setHeight(activeDisplayBounds.getHeight());
        this.initStyle(StageStyle.UNDECORATED);
        this.initModality(Modality.APPLICATION_MODAL);

        this.getScene().addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode() == KeyCode.valueOf(data.getCancelProcedureKey())) {
                if (timelineProcedureHorizontal != null) {
                    timelineProcedureHorizontal.stop();
                }
                if (timelineProcedureVertical != null) {
                    timelineProcedureVertical.stop();
                }
                if (procedureIsFinished) {
                    shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS FINISHED", "#FFFFFF");

                    close();
                } else {
                    shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS CANCELED", "#F70000");
                    close();
                }
            }
        });

        this.getScene().addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
            if (!procedureIsStarted) {
                if (ke.getCode() == KeyCode.valueOf(data.getAnswerToStimulusKey())) {
                    procedureIsRunning = true;
                    procedureIsStarted = true;

                    timelineProcedureHorizontal = createTimelineProcedureHorizontal();
                    timelineProcedureHorizontal.play();

                    shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS RUNNING", "#22E600");
                }
            } else {
                if (ke.getCode() == KeyCode.valueOf(data.getAnswerToStimulusKey())) {
                    if (procedureIsRunning) {
                        if (permissionToAnswer) {
                            if (horizontalStimuliTurn) {
                                answersToHorizontalStimuli[indexOfCurrentlyDisplayedStimulus] += 1;
                                permissionToAnswer = false;
                            } else if (verticalStimuliTurn) {
                                answersToVerticalStimuli[indexOfCurrentlyDisplayedStimulus] += 1;
                                permissionToAnswer = false;
                            }
                        }
                    }
                } else if (ke.getCode() == KeyCode.valueOf(data.getPauseProcedureKey())) {
                    if (procedureIsRunning) {
                        procedureIsRunning = false;

                        if (timelineProcedureHorizontal != null) {
                            if (!timelineProcedureHorizontal.getStatus().equals(Animation.Status.STOPPED)) {
                                timelineProcedureHorizontal.pause();
                                shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS PAUSED", "#E3AA00");
                            } else {
                                timelineProcedureVertical.pause();
                                shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS PAUSED", "#E3AA00");
                            }
                        }
                    } else {
                        procedureIsRunning = true;

                        if (timelineProcedureHorizontal != null) {
                            if (!timelineProcedureHorizontal.getStatus().equals(Animation.Status.STOPPED)) {
                                timelineProcedureHorizontal.play();
                                shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS RUNNING", "#22E600");
                            } else {
                                timelineProcedureVertical.play();
                                shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS RUNNING", "#22E600");
                            }
                        }
                    }
                }
            }
        });
    }

    public void drawFixationPoint() {
        double radiusX = (data.getFixPointWidth() / 2) * pixelsForOneDegreeX;
        double radiusY = (data.getFixPointHeight() / 2) * pixelsForOneDegreeY;
        Ellipse fixPoint = new Ellipse(screenResolutionX / 2, screenResolutionY / 2, radiusX, radiusY);
        Color fixPointColor = Color.hsb(Integer.valueOf(data.getFixPointHue()), Double.valueOf(data.getFixPointSaturation()) / 100, Double.valueOf(data.getFixPointBrightness()) / 100);
        fixPoint.setFill(fixPointColor);
        fixPoint.setStroke(fixPointColor);
        displayPane.getChildren().add(fixPoint);
    }

    public Ellipse prepareBlindspotMonitorStimulus(String examEye, double horizontalDistanceFromFixationPointInDegrees, double verticalDistanceFromTheCenterOfHorizontalBlindspotRange) {

        int examinedEyeCoeff;
        if (examEye.equals("Left")) {
            examinedEyeCoeff = -1;
        } else {
            examinedEyeCoeff = 1;
        }
        double radiusX = (data.getMonitorStimulusWidth() / 2) * pixelsForOneDegreeX;
        double radiusY = (data.getMonitorStimulusHeight() / 2) * pixelsForOneDegreeY;

        double centerX = (screenResolutionX / 2);
        double centerY = (screenResolutionY / 2);

        Ellipse monitorStimulus = new Ellipse(centerX + ((pixelsForOneDegreeX * horizontalDistanceFromFixationPointInDegrees)
                * examinedEyeCoeff), centerY + (pixelsForOneDegreeY * verticalDistanceFromTheCenterOfHorizontalBlindspotRange),
                radiusX, radiusY);

        Color monitorStimulusColor = Color.hsb(Integer.valueOf(data.getLuminanceScaleDataForStimuli().getScaleHue()),
                Double.valueOf(data.getLuminanceScaleDataForStimuli().getScaleSaturation()) / 100,
                data.getMonitorStimulusBrightness() / 100);

        monitorStimulus.setFill(monitorStimulusColor);
        monitorStimulus.setStroke(monitorStimulusColor);

        return monitorStimulus;
    }

    public Ellipse[] prepareHorizontalStimuli() {

        /* prepare monitor stimuli */
        Ellipse[] monitorStimuliHorizontal = new Ellipse[diffHorizontal];

        /* prepare monitor stimuli -> create shuffled vector of horizontal distances */
        double[] horizontalDistances = new double[diffHorizontal];
        int iterator = 0;
        for (double i = blindspotMappingRangeInDegreesHorizontal[0]; i < blindspotMappingRangeInDegreesHorizontal[1]; i += mappingAccuracy) {
            horizontalDistances[iterator] = i;
            iterator += 1;
        }
        ArrayList<Double> distances = new ArrayList();
        for (int i = 0; i < horizontalDistances.length; i++) {
            distances.add(horizontalDistances[i]);
        }
        Collections.shuffle(distances);

        /* prepare monitor stimuli -> create ellipses */
        for (int i = 0; i < diffHorizontal; i++) {
            monitorStimuliHorizontal[i] = prepareBlindspotMonitorStimulus(examinedEye, distances.get(i), 0);
        }
        return monitorStimuliHorizontal;
    }

    public Ellipse[] prepareVerticalStimuli(double startLocation) {

        /* prepare monitor stimuli */
        Ellipse[] monitorStimuliVertical = new Ellipse[diffVertical];

        /* prepare monitor stimuli -> create shuffled vector of vertical distances */
        double[] verticalDistances = new double[diffVertical];
        int iterator = 0;
        for (double i = blindspotMappingRangeInDegreesVertical[0]; i < blindspotMappingRangeInDegreesVertical[1]; i += mappingAccuracy) {
            verticalDistances[iterator] = i;
            iterator += 1;
        }

        ArrayList<Double> distances = new ArrayList();
        for (int i = 0; i < verticalDistances.length; i++) {
            distances.add(verticalDistances[i]);
        }
        Collections.shuffle(distances);

        /* prepare monitor stimuli -> create ellipses */
        for (int i = 0; i < diffVertical; i++) {
            monitorStimuliVertical[i] = prepareBlindspotMonitorStimulus(examinedEye, startLocation, distances.get(i));
        }

        return monitorStimuliVertical;
    }

    private Timeline createTimelineProcedureHorizontal() {
        Timeline timeline = new Timeline();

        /* init horizontal monitor stimuli */
        horizontalMonitorStimuli = prepareHorizontalStimuli();

        /* create time intervals between horizontal stimuli */
        int[] timeIntervalsBetweenHorizontalStimuli = new int[horizontalMonitorStimuli.length];

        int constantPart = data.getTimeIntervalBetweenStimuliConstantPart();
        int[] randomPart = new int[] {0, data.getTimeIntervalBetweenStimuliRandomPart()};

        Random rand = new Random();
        for (int i = 0; i < timeIntervalsBetweenHorizontalStimuli.length; i++) {
            timeIntervalsBetweenHorizontalStimuli[i] = constantPart + rand.nextInt((randomPart[1] - randomPart[0]) + 1) + randomPart[0];
        }

        /* create key frames */
        long totalTime = 1000;

        KeyFrame start = new KeyFrame(Duration.millis(totalTime), event -> {
            horizontalStimuliTurn = true;
        });
        timeline.getKeyFrames().add(start);

        for (int i = 0; i < monitorStimulusDisplayRepetitions; i++) {

            for (int j = 0; j < horizontalMonitorStimuli.length; j++) {

                int jay = j;

                KeyFrame displayStimulus = new KeyFrame(Duration.millis(totalTime + timeIntervalsBetweenHorizontalStimuli[j]), event -> {
                    indexOfCurrentlyDisplayedStimulus = jay;
                    permissionToAnswer = true;
                    displayPane.getChildren().add(horizontalMonitorStimuli[jay]);
                });
                timeline.getKeyFrames().add(displayStimulus);

                KeyFrame removeStimulus = new KeyFrame(Duration.millis(totalTime + timeIntervalsBetweenHorizontalStimuli[j] + data.getStimulusDisplayTime()), event -> {
                    if (displayPane.getChildren().size() > 1) {
                        displayPane.getChildren().remove(1);
                    }
                });
                timeline.getKeyFrames().add(removeStimulus);

                totalTime += timeIntervalsBetweenHorizontalStimuli[j] + data.getStimulusDisplayTime();
            }
        }

        KeyFrame finish = new KeyFrame(Duration.millis(totalTime + 1000), event -> {
            horizontalStimuliTurn = false;

            SortedValues sortedValuesHorizontal = calculateHorizontalBlindspotMedian();
            double blindspotCenterX = sortedValuesHorizontal.getBlindspotMedian();
            double blindspotCenterDistanceFromFixPointInPixels = Math.abs((screenResolutionX / 2) - blindspotCenterX);
            double blindspotCenterDistanceFromFixPointInDegrees = blindspotCenterDistanceFromFixPointInPixels / pixelsForOneDegreeX;

            timelineProcedureVertical = createTimelineProcedureVertical(blindspotCenterDistanceFromFixPointInDegrees, sortedValuesHorizontal);
            timelineProcedureVertical.play();

            timeline.stop();
        });
        timeline.getKeyFrames().add(finish);

        return timeline;
    }

    private Timeline createTimelineProcedureVertical(double startPoint, SortedValues sortedValuesHor) {
        Timeline timeline = new Timeline();

        /* init vertical monitor stimuli */
        verticalMonitorStimuli = prepareVerticalStimuli(startPoint);

        /* create time intervals between vertical stimuli */
        int[] timeIntervalsBetweenVerticalStimuli = new int[verticalMonitorStimuli.length];

        int constantPart = data.getTimeIntervalBetweenStimuliConstantPart();
        int[] randomPart = new int[] {0, data.getTimeIntervalBetweenStimuliRandomPart()};

        Random rand = new Random();
        for (int i = 0; i < timeIntervalsBetweenVerticalStimuli.length; i++) {
            timeIntervalsBetweenVerticalStimuli[i] = constantPart + rand.nextInt((randomPart[1] - randomPart[0]) + 1) + randomPart[0];
        }

        /* create key frames */
        long totalTime = 1000;

        KeyFrame start = new KeyFrame(Duration.millis(totalTime), event -> {
            verticalStimuliTurn = true;
        });
        timeline.getKeyFrames().add(start);

        for (int i = 0; i < monitorStimulusDisplayRepetitions; i++) {

            for (int j = 0; j < verticalMonitorStimuli.length; j++) {

                int jay = j;

                KeyFrame displayStimulus = new KeyFrame(Duration.millis(totalTime + timeIntervalsBetweenVerticalStimuli[j]), event -> {
                    indexOfCurrentlyDisplayedStimulus = jay;
                    permissionToAnswer = true;
                    displayPane.getChildren().add(verticalMonitorStimuli[jay]);
                });
                timeline.getKeyFrames().add(displayStimulus);

                KeyFrame removeStimulus = new KeyFrame(Duration.millis(totalTime + timeIntervalsBetweenVerticalStimuli[j] + data.getStimulusDisplayTime()), event -> {
                    if (displayPane.getChildren().size() > 1) {
                        displayPane.getChildren().remove(1);
                    }
                });
                timeline.getKeyFrames().add(removeStimulus);

                totalTime += timeIntervalsBetweenVerticalStimuli[j] + data.getStimulusDisplayTime();
            }
        }

        KeyFrame finish = new KeyFrame(Duration.millis(totalTime + 1000), event -> {
           verticalStimuliTurn = false;

            SortedValues sortedValuesVertical = calculateVerticalBlindspotMedian();
            double blindspotCenterY = sortedValuesVertical.getBlindspotMedian();
            double distInPixels = Math.abs((screenResolutionY / 2) - blindspotCenterY);
            double distInDegrees = distInPixels / pixelsForOneDegreeY;

            ArrayList<Ellipse> horizontalStimuli = sortedValuesHor.getStimuli();
            ArrayList<Ellipse> verticalStimuli = sortedValuesVertical.getStimuli();

            ArrayList<Double> horizontalDistances = new ArrayList();
            ArrayList<Double> verticalDistances = new ArrayList();
            for (int i = 0; i < horizontalStimuli.size(); i++) {
                horizontalDistances.add(Math.abs((screenResolutionX / 2) - horizontalStimuli.get(i).getCenterX()) / pixelsForOneDegreeX);
            }
            for (int i = 0; i < verticalStimuli.size(); i++) {
                double vertDist;
                if (verticalStimuli.get(i).getCenterY() < (screenResolutionY / 2)) {
                    vertDist = -(Math.abs((screenResolutionY / 2) - verticalStimuli.get(i).getCenterY()) / pixelsForOneDegreeY);
                } else {
                    vertDist = Math.abs((screenResolutionY / 2) - verticalStimuli.get(i).getCenterY()) / pixelsForOneDegreeY;
                }
                verticalDistances.add(vertDist);
            }

            mappingData = "";
            mappingData += "Horizontal blindspot center = " + data.getScreenLuminanceFunctions().round(startPoint, 2) + "\n";
            mappingData += "Vertical blindspot center = " + data.getScreenLuminanceFunctions().round(distInDegrees, 2) + "\n\n";

            for (int i = 0; i < horizontalDistances.size(); i++) {
                mappingData += "Horizontal " + String.format("%.2f", horizontalDistances.get(i)) + " degrees : " + sortedValuesHor.getAnswersToStimuli().get(i) + " answers" + "\n";
            }
            mappingData += "\n";
            if ((screenResolutionY / 2) > blindspotCenterY) {
                distInDegrees *= -1;
            }
            for (int i = 0; i < verticalDistances.size(); i++) {
                mappingData += "Vertical " + String.format("%.2f", verticalDistances.get(i)) + " degrees : " + sortedValuesVertical.getAnswersToStimuli().get(i) + " answers" + "\n";
            }

            procedureIsFinished = true;
            shellWindowForMapBlindspotProcedure.setProcedureStatus("PROCEDURE IS FINISHED", "#FFFFFF");
            shellWindowForMapBlindspotProcedure.addTextToTextArea(mappingData);
        });
        timeline.getKeyFrames().add(finish);

        return timeline;
    }

    private class SortedValues {

        private ArrayList<Ellipse> stimuli;
        private ArrayList<Integer> answersToStimuli;
        private double blindspotMedian;

        private SortedValues(ArrayList<Ellipse> stimuli, ArrayList<Integer> answersToStimuli, double blindspotMedian) {
            this.stimuli = stimuli;
            this.answersToStimuli = answersToStimuli;
            this.blindspotMedian = blindspotMedian;
        }

        public ArrayList<Ellipse> getStimuli() {
            return stimuli;
        }

        public ArrayList<Integer> getAnswersToStimuli() {
            return answersToStimuli;
        }

        public double getBlindspotMedian() {
            return blindspotMedian;
        }
    }

    public SortedValues calculateHorizontalBlindspotMedian() {
        ArrayList<Ellipse> stimuli = new ArrayList();
        ArrayList<Integer> answersToStimuli = new ArrayList();

        for (int i = 0; i < horizontalMonitorStimuli.length; i++) {
            stimuli.add(horizontalMonitorStimuli[i]);
        }
        for (int i = 0; i < answersToHorizontalStimuli.length; i++) {
            answersToStimuli.add(answersToHorizontalStimuli[i]);
        }

        // Insertion sort algorithm.
        for (int i = 1; i < stimuli.size(); i++) {

            Ellipse tempStimulus = stimuli.get(i);
            double tempCenterX = stimuli.get(i).getCenterX();
            int tempAnswer = answersToHorizontalStimuli[i];
            int j = i - 1;
            while (j >= 0 && stimuli.get(j).getCenterX() > tempCenterX) {
                stimuli.set(j + 1, stimuli.get(j));
                answersToStimuli.set(j + 1, answersToStimuli.get(j));
                j--;
            }
            stimuli.set(j + 1, tempStimulus);
            answersToStimuli.set(j + 1, tempAnswer);
        }

        // Calculate blindspot boundaries.
        ArrayList<Double> blindspotLocations = new ArrayList();
        for (int i = 0; i < answersToStimuli.size(); i++) {
            if (answersToStimuli.get(i) < 2) {
                blindspotLocations.add(stimuli.get(i).getCenterX());
            }
        }

        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        for (int i = 0; i < blindspotLocations.size(); i++) {
            descriptiveStatistics.addValue(blindspotLocations.get(i));
        }

        double median = descriptiveStatistics.getPercentile(50);
        SortedValues sortedValues = new SortedValues(stimuli, answersToStimuli, median);
        return sortedValues;
    }

    public SortedValues calculateVerticalBlindspotMedian() {
        ArrayList<Ellipse> stimuli = new ArrayList();
        ArrayList<Integer> answersToStimuli = new ArrayList();

        for (int i = 0; i < verticalMonitorStimuli.length; i++) {
            stimuli.add(verticalMonitorStimuli[i]);
        }
        for (int i = 0; i < answersToVerticalStimuli.length; i++) {
            answersToStimuli.add(answersToVerticalStimuli[i]);
        }

        // Insertion sort algorithm.
        for (int i = 1; i < stimuli.size(); i++) {

            Ellipse tempStimulus = stimuli.get(i);
            double tempCenterY = stimuli.get(i).getCenterY();
            int tempAnswer = answersToVerticalStimuli[i];
            int j = i - 1;
            while (j >= 0 && stimuli.get(j).getCenterY() > tempCenterY) {
                stimuli.set(j + 1, stimuli.get(j));
                answersToStimuli.set(j + 1, answersToStimuli.get(j));
                j--;
            }
            stimuli.set(j + 1, tempStimulus);
            answersToStimuli.set(j + 1, tempAnswer);
        }

        // Calculate blindspot boundaries.
        ArrayList<Double> blindspotLocations = new ArrayList();
        for (int i = 0; i < answersToStimuli.size(); i++) {
            if (answersToStimuli.get(i) < 2) {
                blindspotLocations.add(stimuli.get(i).getCenterY());
            }
        }

        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        for (int i = 0; i < blindspotLocations.size(); i++) {
            descriptiveStatistics.addValue(blindspotLocations.get(i));
        }

        double median = descriptiveStatistics.getPercentile(50);
        SortedValues sortedValues = new SortedValues(stimuli, answersToStimuli, median);
        return sortedValues;
    }
}