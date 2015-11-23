package org.specvis.procedure;

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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.specvis.data.ConfigurationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pdzwiniel on 2015-06-08.
 * Last update by pdzwiniel on 2015-11-20.
 *
 * INFO:
 *
 * NOTEPAD:
 * 1. removed.
 * 2. removed.
 * 3. removed.
 * 4. For each point in each quarter there is a specified algorithm of operations:
 *  4.1. Display stimulus of brightness equals to X[4] (50).
 *  4.2. If participant answered to the stimulus positevely, ignore brightness values above X[4], ie. 60, 70, 80 etc.
 *  4.3. Display stimulus of brightness equals to X[2] (30).
 *  4.4. If participant answered to the stimulus negatively, ignore brightness values below X[2], ie. 20 and 10.
 *  4.5. Display stimulus of brightness equals to X[3] (40).
 *  4.6. If participant answered to the stimulus negatively, the visual sensitivity in this point is equal X[4] (50),
 *  otherwise visual sensitivity in this point is equal X[2] (30).
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

public class MainProcedure extends Stage {

    private Data data;
    private ShellWindowForMainProcedure shellWindowForMainProcedure;
    private Pane displayPane;
    private int activeDisplayIndex;
    private int screenResolutionX;
    private int screenResolutionY;
    private int quarterGridResolutionX;
    private int quarterGridResolutionY;
    private int backgroundHue;
    private int backgroundSaturation;
    private int backgroundBrightness;
    private int stimulusMaxBrightness;
    private int stimulusMinBrightness;
    private double stimulusWidthInPixels;
    private double stimulusHeightInPixels;
    private String fixationMonitor;
    private int fixationCheckRate;
    private boolean monitoringFixationEvery_X_Stimuli;
    private boolean monitoringFixationEvery_Y_Stimuli;
    private double blindspotDistFromFixPointHorizontally;
    private double blindspotDistFromFixPointVertically;
    private int checkRateIterator;
    private ArrayList<Boolean> fixationMonitorAnswers;
    private Shape fixationMonitorShape;
    private double pixelsForOneDegreeX;
    private double pixelsForOneDegreeY;
    private ArrayList<MainProcedureStimulus> stimuliList;
    private ArrayList<Integer> activeStimuliIndices;
    private ArrayList<StimulusResults> stimuliAnswers;
    private Timeline procedureTimeline;
    private Timeline stimulusTimeline;
    private Timeline fixationMonitorTimeline;
    private boolean procedureIsStarted;
    private boolean procedureIsRunning;
    private boolean procedureIsFinished;
    private boolean permissionToAnswer;
    private MainProcedureStimulus currentlyDisplayedStimulus;
    private Random randomGenerator;
    private int[] brightnessVector;
    private long startOfTheProcedureTime;
    private long endOfProcedureTime;
    private int visualFieldTestBrightnessVectorLength;

    public Parent createContent() {

        /* init fields */
        activeDisplayIndex = data.getActiveDisplayIndex();
        screenResolutionX = data.getScreenResolutionX();
        screenResolutionY = data.getScreenResolutionY();
        quarterGridResolutionX = data.getQuarterGridResolutionX();
        quarterGridResolutionY = data.getQuarterGridResolutionY();
        backgroundHue = Integer.valueOf(data.getLuminanceScaleDataForBackground().getScaleHue());
        backgroundSaturation = Integer.valueOf(data.getLuminanceScaleDataForBackground().getScaleSaturation());
        backgroundBrightness = data.getBackgroundBrightness();
        stimulusMaxBrightness = data.getStimulusMaxBrightness();
        stimulusMinBrightness = data.getStimulusMinBrightness();
        fixationMonitor = data.getFixationMonitor();
        monitoringFixationEvery_X_Stimuli = data.isMonitoringFixationEvery_X_Stimuli();
        monitoringFixationEvery_Y_Stimuli = data.isMonitoringFixationEvery_Y_Stimuli();
        if (monitoringFixationEvery_X_Stimuli) {
            fixationCheckRate = data.getFixationCheckRate();
        } else {
            fixationCheckRate = data.getScreenLuminanceFunctions().randomInt(1, data.getFixationCheckRate());
        }
        if (!fixationMonitor.equals("None")) {
            fixationMonitorAnswers = new ArrayList();
        }
        blindspotDistFromFixPointHorizontally = data.getBlindspotDistanceFromFixPointHorizontally();
        blindspotDistFromFixPointVertically = data.getBlindspotDistanceFromFixPointVertically();
        checkRateIterator = 0;
        pixelsForOneDegreeX = data.getScreenResolutionX() / data.getVisualFieldX();
        pixelsForOneDegreeY = data.getScreenResolutionY() / data.getVisualFieldY();
        stimulusHeightInPixels = data.getStimulusHeightInDegrees() * pixelsForOneDegreeY;
        stimulusWidthInPixels = data.getStimulusWidthInDegrees() * pixelsForOneDegreeX;
        procedureIsFinished = false;
        data.setIsMainProcedureFinished(false);
        randomGenerator = new Random();

        visualFieldTestBrightnessVectorLength = Integer.valueOf(ConfigurationData.getVisualFieldTestBrightnessVectorLength());
        brightnessVector = new int[visualFieldTestBrightnessVectorLength];
        List<Double> linspace = data.getScreenLuminanceFunctions().linspace(stimulusMinBrightness, stimulusMaxBrightness, visualFieldTestBrightnessVectorLength, true);
        for (int i = 0; i < visualFieldTestBrightnessVectorLength; i++) {
            double d = linspace.get(i);
            brightnessVector[i] = (int) d;
        }
        data.setStimuliVerifiedBrightnessValues(brightnessVector);

        /* layout */
        BorderPane layout = new BorderPane();

        /* stimuli preparation etc. */
        stimuliList = new ArrayList();
        activeStimuliIndices = new ArrayList();
        stimuliAnswers = new ArrayList();
        MainProcedureStimulus stimulus;
        double squareX = screenResolutionX / (quarterGridResolutionX * 2);
        double squareY = screenResolutionY / (quarterGridResolutionY * 2);
        int stimulusAssignedIndex = 0;
        for (int i = 0; i < quarterGridResolutionX * 2; i++) {
            for (int j = 0; j < quarterGridResolutionY * 2; j++) {
                stimulus = new MainProcedureStimulus();

                // Assigned index.
                stimulus.setStimulusAssignedIndex(stimulusAssignedIndex);

                // Shape.
                Shape shape = createStimulus(
                        data.getStimulusShape(),
                        Color.hsb(Integer.valueOf(data.getLuminanceScaleDataForStimuli().getScaleHue()),
                                Double.valueOf(data.getLuminanceScaleDataForStimuli().getScaleSaturation()) / 100,
                                Double.valueOf(data.getStimulusMaxBrightness()) / 100),
                        data.getStimulusInclination());
                shape.setLayoutX((squareX / 2) + (i * squareX) - (stimulusWidthInPixels / 2));
                shape.setLayoutY((squareY / 2) + (j * squareY) - (stimulusHeightInPixels / 2));
                stimulus.setStimulusShape(shape);

                // Quarter.
                if (i <= quarterGridResolutionX && j <= quarterGridResolutionY) {
                    stimulus.setStimulusQuarter(4);
                } else if (i <= quarterGridResolutionX && j > quarterGridResolutionY) {
                    stimulus.setStimulusQuarter(3);
                } else if (i > quarterGridResolutionX && j <= quarterGridResolutionY) {
                    stimulus.setStimulusQuarter(1);
                } else if (i > quarterGridResolutionY && j > quarterGridResolutionY) {
                    stimulus.setStimulusQuarter(2);
                }

                // Cell coordinates XY.
                stimulus.setStimulusCellCoordinatesXY(new int[]{i, j});

                // Display time.
                stimulus.setStimulusDisplayTimeInMs(data.getStimulusDisplayTime());

                // Answers.
                stimulus.setStimulusAnswers(new int[]{0, 0, 0, 0});

                // ** Add stimulus to list.
                stimuliList.add(stimulus);

                // ** Add stimulus index to active stimuli list.
                activeStimuliIndices.add(stimulusAssignedIndex);

                /**
                "activeList" will serve as a pool for drawing with repetition stimuli indices.
                After such draw, eg. resulting in drawing number "10", the stimulus of index
                "10" will be drawn from "stimuliList" and displayed on the screen. When participant
                answered to all displays of one stimulus, its index number will be removed from
                "activeList" list. When "activeList" is empty the procedure is ended.
                 */

                stimulusAssignedIndex += 1;
            }
        }

        /* layout -> display pane */
        displayPane = new Pane();
        displayPane.setStyle("-fx-background-color: hsb(" + backgroundHue + ", " + backgroundSaturation + "%, " + backgroundBrightness + "%);");
        displayPane.setMinSize(screenResolutionX, screenResolutionY);
        displayPane.setMaxSize(screenResolutionX, screenResolutionY);

        /* call functions */
        drawFixationPoint();
        setInitialDataInTheShellWindow();

        /* return layout */
        layout.setCenter(displayPane);

        return layout;
    }

    public MainProcedure(Data data, ShellWindowForMainProcedure shellWindowForMainProcedure) {
        this.data = data;
        this.shellWindowForMainProcedure = shellWindowForMainProcedure;
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
                if (procedureTimeline != null) {
                    procedureTimeline.stop();
                }
                if (stimulusTimeline != null) {
                    stimulusTimeline.stop();
                }
                if (fixationMonitorTimeline != null) {
                    fixationMonitorTimeline.stop();
                }

                if (activeStimuliIndices.size() == 0) {
                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS FINISHED", "#FFFFFF");

                    close();
                } else {
                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS CANCELED", "#F70000");
                    close();
                }
            }
        });

        this.getScene().addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
            if (!procedureIsStarted) {  // if procedure is not started
                if (ke.getCode() == KeyCode.valueOf(data.getAnswerToStimulusKey())) {
                    startOfTheProcedureTime = System.currentTimeMillis();
                    procedureIsRunning = true;
                    procedureIsStarted = true;
                    runProcedureTimeline();
                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS RUNNING", "#22E600");
                }
            } else {                    // if procedure is started
                if (ke.getCode() == KeyCode.valueOf(data.getAnswerToStimulusKey())) { // if participant hit answer key
                    if (procedureIsRunning) {
                        if (permissionToAnswer) {
                            if (fixationMonitor.equals("None")) {
                                // Write POSITIVE answer to stimulus.
                                for (int i = 0; i < currentlyDisplayedStimulus.getStimulusAnswers().length; i++) {
                                    if (currentlyDisplayedStimulus.getStimulusAnswers()[i] == 0) {
                                        currentlyDisplayedStimulus.getStimulusAnswers()[i] = 2;
                                        break;
                                    }
                                }
                            } else {
                                if (checkRateIterator != fixationCheckRate) {
                                    // Write POSITIVE answer to stimulus.
                                    for (int i = 0; i < currentlyDisplayedStimulus.getStimulusAnswers().length; i++) {
                                        if (currentlyDisplayedStimulus.getStimulusAnswers()[i] == 0) {
                                            currentlyDisplayedStimulus.getStimulusAnswers()[i] = 2;
                                            break;
                                        }
                                    }
                                } else {
                                    // Write POSITIVE answer to fixation monitor.
                                    fixationMonitorAnswers.add(true);
                                }
                            }
                            permissionToAnswer = false;
                        }
                    }
                } else if (ke.getCode() == KeyCode.valueOf(data.getPauseProcedureKey())) { // if participant hit pause key
                    if (procedureIsRunning) {
                        procedureIsRunning = false;
                        if (fixationMonitor.equals("None")) {
                            if (stimulusTimeline != null) {
                                stimulusTimeline.pause();
                                shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS PAUSED", "#E3AA00");
                            }
                        } else {
                            if (checkRateIterator != fixationCheckRate) {
                                if (stimulusTimeline != null) {
                                    stimulusTimeline.pause();
                                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS PAUSED", "#E3AA00");
                                }
                            } else {
                                if (fixationMonitorTimeline != null) {
                                    fixationMonitorTimeline.play();
                                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS PAUSED", "#E3AA00");
                                }
                            }
                        }
                    } else {
                        procedureIsRunning = true;
                        if (fixationMonitor.equals("None")) {
                            if (stimulusTimeline != null) {
                                stimulusTimeline.play();
                                shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS RUNNING", "#22E600");
                            }
                        } else {
                            if (checkRateIterator != fixationCheckRate) {
                                if (stimulusTimeline != null) {
                                    stimulusTimeline.play();
                                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS RUNNING", "#22E600");
                                }
                            } else {
                                if (fixationMonitorTimeline != null) {
                                    fixationMonitorTimeline.play();
                                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS RUNNING", "#22E600");
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void deactivateStimulusIfBrightnessThresholdIsEvaluated(int[] answers) {
        if (answers[2] != 0) {
            boolean shouldStimulusBeDeactivated = false;

            boolean[] threshold = new boolean[visualFieldTestBrightnessVectorLength];
            for (int i = 0; i < visualFieldTestBrightnessVectorLength; i++) {
                threshold[i] = true;
            }
            int currentBrightnessIndex = visualFieldTestBrightnessVectorLength / 2; // the middle one
            int lastBrightnessIndex = visualFieldTestBrightnessVectorLength / 2;

            int currentAnswer;
            int lastAnswer = answers[0];

            for (int i = 0; i < answers.length; i++) {
                currentAnswer = answers[i];
                switch (answers[i]) {
                    case 1:
                        if ((currentBrightnessIndex != 1 && currentBrightnessIndex != (visualFieldTestBrightnessVectorLength - 2)) && currentAnswer == lastAnswer) {
                            for (int j = 0; j <= lastBrightnessIndex; j++) {
                                threshold[j] = false;
                            }
                            currentBrightnessIndex += 2;
                        } else {
                            for (int j = 0; j <= lastBrightnessIndex; j++) {
                                threshold[j] = false;
                            }
                            currentBrightnessIndex += 1;
                        }
                        break;
                    case 2:
                        if ((currentBrightnessIndex != 1 && currentBrightnessIndex != (visualFieldTestBrightnessVectorLength - 2)) && currentAnswer == lastAnswer) {
                            for (int j = (visualFieldTestBrightnessVectorLength - 1); j >= lastBrightnessIndex; j--) {
                                threshold[j] = false;
                            }
                            currentBrightnessIndex -= 2;
                        } else {
                            for (int j = (visualFieldTestBrightnessVectorLength - 1); j >= lastBrightnessIndex; j--) {
                                threshold[j] = false;
                            }
                            currentBrightnessIndex -= 1;
                        }
                        break;
                }

                if (currentBrightnessIndex > (visualFieldTestBrightnessVectorLength - 1) || currentBrightnessIndex < 0) {
                    currentlyDisplayedStimulus.setEvaluatedBrightnessThreshold(brightnessVector[lastBrightnessIndex]);
                    shouldStimulusBeDeactivated = true;
                    break;
                } else if (!threshold[currentBrightnessIndex]) {
                    if (brightnessVector[lastBrightnessIndex] > brightnessVector[currentBrightnessIndex]) {
                        currentlyDisplayedStimulus.setEvaluatedBrightnessThreshold(brightnessVector[lastBrightnessIndex]);
                    } else {
                        currentlyDisplayedStimulus.setEvaluatedBrightnessThreshold(brightnessVector[lastBrightnessIndex+1]);
                    }
                    shouldStimulusBeDeactivated = true;
                    break;
                }
                lastAnswer = answers[i];
                lastBrightnessIndex = currentBrightnessIndex;
            }

            if (shouldStimulusBeDeactivated) {
                activeStimuliIndices.remove(new Integer(currentlyDisplayedStimulus.getStimulusAssignedIndex()));

                // STIMULI RESULTS
                String s = shellWindowForMainProcedure.getTextProgressBar().getText();
                String[] string = s.split("/");
                int numberOfCurrentStimulus = Integer.valueOf(string[0]) + 1;
                int totalNumberOfStimulusToShow = Integer.valueOf(string[1]);

                shellWindowForMainProcedure.getTextProgressBar().setText(String.valueOf(numberOfCurrentStimulus) + "/" + String.valueOf(totalNumberOfStimulusToShow));
                shellWindowForMainProcedure.getProgressBar().setProgress(1.0 * (Double.valueOf(numberOfCurrentStimulus) / Double.valueOf(totalNumberOfStimulusToShow)));

                double stiMaxLum = data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[data.getStimulusMaxBrightness()];
                double bgLum = data.getLuminanceScaleDataForBackground().getLuminanceForBrightness()[data.getBackgroundBrightness()];
                int stiID = currentlyDisplayedStimulus.getStimulusAssignedIndex();
                int stibBrighThresh = currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold();
                double stiLumThresh = data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold()], 2);
                double stiDbThresh = data.getScreenLuminanceFunctions().decibelsValue(stiMaxLum, stiLumThresh, bgLum, 2);

                shellWindowForMainProcedure.addTextToTextArea(stiID + "\t" + stibBrighThresh + "\t" + stiLumThresh + "\t" + stiDbThresh + "\n");

                // Add answers to hashmap.
                StimulusResults stimulusResults = new StimulusResults();
                stimulusResults.setStimulusNumber(currentlyDisplayedStimulus.getStimulusAssignedIndex());
                stimulusResults.setStimulusGridCoordinatesXY(currentlyDisplayedStimulus.getStimulusCellCoordinatesXY());
                stimulusResults.setStimulusBrightnessThreshold(currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold());
                stimulusResults.setStimulusLuminanceThreshold(data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold()], 2));
                stimuliAnswers.add(stimulusResults);

                if (activeStimuliIndices.size() == 0) {
                    stimulusTimeline.stop();
                    procedureIsFinished = true;
                    data.setIsMainProcedureFinished(true);
                    data.setMainProcedureStimuliResults(stimuliAnswers);
                    shellWindowForMainProcedure.setProcedureStatus("PROCEDURE IS FINISHED", "#FFFFFF");

                    int occurrences;
                    switch (fixationMonitor) {

                        case "Blindspot":
                            occurrences = (int) fixationMonitorAnswers.stream().filter(f -> f == false).count();
                            shellWindowForMainProcedure.addTextToTextArea("\n" + "RESULTS (FIXATION MONITOR)" + "\n");
                            shellWindowForMainProcedure.addTextToTextArea("Total fixation checks:" + "\t" + fixationMonitorAnswers.size() + "\n");
                            shellWindowForMainProcedure.addTextToTextArea("Positive fixation checks:" + "\t" + occurrences + "\n");
                            shellWindowForMainProcedure.addTextToTextArea("Fixation accuracy rate (%):" + "\t" + data.getScreenLuminanceFunctions().round(((double) occurrences / fixationMonitorAnswers.size()) * 100, 2) + "\n");
                            break;
                        case "Fixation point change":

                            occurrences = (int) fixationMonitorAnswers.stream().filter(f -> f == true).count();
                            shellWindowForMainProcedure.addTextToTextArea("\n" + "RESULTS (FIXATION MONITOR)" + "\n");
                            shellWindowForMainProcedure.addTextToTextArea("Total fixation checks:" + "\t" + fixationMonitorAnswers.size() + "\n");
                            shellWindowForMainProcedure.addTextToTextArea("Positive fixation checks:" + "\t" + occurrences + "\n");
                            shellWindowForMainProcedure.addTextToTextArea("Fixation accuracy rate (%):" + "\t" + data.getScreenLuminanceFunctions().round(((double) occurrences / fixationMonitorAnswers.size()) * 100, 2) + "\n");
                            break;
                    }

                    shellWindowForMainProcedure.addTextToTextArea("\n" + "TOTAL TIME OF THE PROCEDURE" + "\n");
                    endOfProcedureTime = System.currentTimeMillis();
                    shellWindowForMainProcedure.addTextToTextArea(data.getScreenLuminanceFunctions().totalTime(startOfTheProcedureTime, endOfProcedureTime));
                }
            }
        }
    }

    public void runProcedureTimeline() {
        procedureTimeline = new Timeline();

        KeyFrame start = new KeyFrame(Duration.millis(1000), event -> {
            runStimulusTimeline();
            procedureTimeline.stop();
        });
        procedureTimeline.getKeyFrames().add(start);
        procedureTimeline.play();
    }

    public int whatBrightnessStimulusShouldHave(int[] answers) {

        int brightnessIndex = visualFieldTestBrightnessVectorLength / 2; // the middle one

        int currentAnswer;
        int lastAnswer = answers[0];

        for (int i = 0; i < answers.length; i++) {
            currentAnswer = answers[i];
            if (answers[i] == 0) {
                break;
            } else {
                switch (answers[i]) {
                    case 1:
                        if ((brightnessIndex != 1 && brightnessIndex != (visualFieldTestBrightnessVectorLength - 2)) && currentAnswer == lastAnswer) {
                            brightnessIndex += 2;
                        } else {
                            brightnessIndex += 1;
                        }
                        break;
                    case 2:
                        if ((brightnessIndex != 1 && brightnessIndex != (visualFieldTestBrightnessVectorLength - 2)) && currentAnswer == lastAnswer) {
                            brightnessIndex -= 2;
                        } else {
                            brightnessIndex -= 1;
                        }
                        break;
                }
            }
            lastAnswer = answers[i];
        }
        return brightnessVector[brightnessIndex];
    }

    public void runStimulusTimeline() {
        stimulusTimeline = new Timeline();

        int r = randomGenerator.nextInt(activeStimuliIndices.size());
        int index = activeStimuliIndices.get(r);
        currentlyDisplayedStimulus = stimuliList.get(index);

        // Ustawienie warto�ci jasno�ci na podstawie dotychczas udzielonych odpowiedzi.
        int brightnessLevel = whatBrightnessStimulusShouldHave(currentlyDisplayedStimulus.getStimulusAnswers());
        Color newColor = Color.hsb(Integer.valueOf(data.getLuminanceScaleDataForStimuli().getScaleHue()),
                Double.valueOf(data.getLuminanceScaleDataForStimuli().getScaleSaturation()) / 100,
                Double.valueOf(brightnessLevel) / 100);
        currentlyDisplayedStimulus.getStimulusShape().setFill(newColor);
        currentlyDisplayedStimulus.getStimulusShape().setStroke(newColor);

        KeyFrame displayStimulus = new KeyFrame(Duration.millis(0), event -> {
            permissionToAnswer = true;
            displayPane.getChildren().add(currentlyDisplayedStimulus.getStimulusShape());
        });
        stimulusTimeline.getKeyFrames().add(displayStimulus);

        KeyFrame removeStimulus = new KeyFrame(Duration.millis(currentlyDisplayedStimulus.getStimulusDisplayTimeInMs()), event -> {
            if (displayPane.getChildren().size() > 1) {
                displayPane.getChildren().remove(1);
            }
        });
        stimulusTimeline.getKeyFrames().add(removeStimulus);

        int interval = randomTimeInterval(data.getTimeIntervalBetweenStimuliConstantPart(), data.getTimeIntervalBetweenStimuliRandomPart());

        KeyFrame intervalBetweenStimuli = new KeyFrame(Duration.millis(interval), event -> {

            // Write NEGATIVE answer to stimulus.
            if (permissionToAnswer) {
                for (int i = 0; i < currentlyDisplayedStimulus.getStimulusAnswers().length; i++) {
                    if (currentlyDisplayedStimulus.getStimulusAnswers()[i] == 0) {
                        currentlyDisplayedStimulus.getStimulusAnswers()[i] = 1;
                        break;
                    }
                }
            }

            // Remove stimulus from active stimulus list if all posible answers were provided.
            deactivateStimulusIfBrightnessThresholdIsEvaluated(currentlyDisplayedStimulus.getStimulusAnswers());

            if (!fixationMonitor.equals("None")) {
                checkRateIterator += 1;
            }

            // Recursion.
            if (!procedureIsFinished) {
                if (!fixationMonitor.equals("None")) {
                    if (checkRateIterator != fixationCheckRate) {
                        stimulusTimeline.stop();
                        runStimulusTimeline();
                    } else {
                        stimulusTimeline.stop();
                        runFixationMonitorTimeline();
                    }
                } else {
                    stimulusTimeline.stop();
                    runStimulusTimeline();
                }
            }
        });
        stimulusTimeline.getKeyFrames().add(intervalBetweenStimuli);
        stimulusTimeline.play();
    }

    private void runFixationMonitorTimeline() {

        fixationMonitorTimeline = new Timeline();
        double radiusX;
        double radiusY;

        // Prepare stimulus.
        switch(fixationMonitor) {
            case "Blindspot":

                radiusX = (data.getMonitorStimulusWidth() / 2) * pixelsForOneDegreeX;
                radiusY = (data.getMonitorStimulusHeight() / 2) * pixelsForOneDegreeY;
                double centerX = screenResolutionX / 2;
                double centerY = screenResolutionY / 2;
                double blindspotDistanceX;
                double blindspotDistanceY;
                if (data.getExaminedEye().equals("Right")) {
                    blindspotDistanceX = pixelsForOneDegreeX * blindspotDistFromFixPointHorizontally;
                    blindspotDistanceY = pixelsForOneDegreeY * (-blindspotDistFromFixPointVertically);
                } else {
                    blindspotDistanceX = pixelsForOneDegreeX * (-blindspotDistFromFixPointHorizontally);
                    blindspotDistanceY = pixelsForOneDegreeY * (-blindspotDistFromFixPointVertically);
                }
                fixationMonitorShape = new Ellipse(centerX + blindspotDistanceX, centerY + blindspotDistanceY, radiusX, radiusY);

                Color fixationMonitorStimulusColor = Color.hsb(Integer.valueOf(data.getLuminanceScaleDataForStimuli().getScaleHue()),
                        Double.valueOf(data.getLuminanceScaleDataForStimuli().getScaleSaturation()) / 100,
                        Double.valueOf(data.getMonitorStimulusBrightness()) / 100);
                fixationMonitorShape.setFill(fixationMonitorStimulusColor);
                fixationMonitorShape.setStroke(fixationMonitorStimulusColor);

                break;
            case "Fixation point change":

                radiusX = (data.getFixPointChangeWidth() / 2) * pixelsForOneDegreeX;
                radiusY = (data.getFixPointChangeHeight() / 2) * pixelsForOneDegreeY;
                fixationMonitorShape = new Ellipse(screenResolutionX / 2, screenResolutionY / 2, radiusX, radiusY);
                Color fixPointChangeColor = Color.hsb(Integer.valueOf(data.getFixPointChangeHue()), Double.valueOf(data.getFixPointChangeSaturation()) / 100, Double.valueOf(data.getFixPointChangeBrightness()) / 100);
                fixationMonitorShape.setFill(fixPointChangeColor);
                fixationMonitorShape.setStroke(fixPointChangeColor);

                break;
        }

        KeyFrame displayFixationMonitorStimulus = new KeyFrame(Duration.millis(0), event -> {
            permissionToAnswer = true;


            switch (fixationMonitor) {
                case "Blindspot":
                    displayPane.getChildren().add(fixationMonitorShape);
                    break;
                case "Fixation point change":
                    if (displayPane.getChildren().size() == 1) {
                        displayPane.getChildren().remove(0);
                    }
                    displayPane.getChildren().add(fixationMonitorShape);
                    break;
            }
        });
        fixationMonitorTimeline.getKeyFrames().add(displayFixationMonitorStimulus);

        KeyFrame removeFixationMonitorStimulus = new KeyFrame(Duration.millis(data.getStimulusDisplayTime()), event -> {
            switch (fixationMonitor) {
                case "Blindspot":
                    if (displayPane.getChildren().size() > 1) {
                        displayPane.getChildren().remove(1);
                    }
                    break;
                case "Fixation point change":
                    if (displayPane.getChildren().size() == 1) {
                        displayPane.getChildren().remove(0);
                    }
                    drawFixationPoint();
                    break;
            }
        });
        fixationMonitorTimeline.getKeyFrames().add(removeFixationMonitorStimulus);

        int interval = randomTimeInterval(data.getTimeIntervalBetweenStimuliConstantPart(), data.getTimeIntervalBetweenStimuliRandomPart());

        KeyFrame intervalBetweenStimuli = new KeyFrame(Duration.millis(interval), event -> {

            // Write NEGATIVE answer to fixation monitor.
            if (permissionToAnswer) {
                fixationMonitorAnswers.add(false);
            }

            // Recursion.
            if (!procedureIsFinished) {
                checkRateIterator = 0;
                if (monitoringFixationEvery_Y_Stimuli) {
                    fixationCheckRate = data.getScreenLuminanceFunctions().randomInt(1, data.getFixationCheckRate());
                }
                fixationMonitorTimeline.stop();
                runStimulusTimeline();
            }
        });
        fixationMonitorTimeline.getKeyFrames().add(intervalBetweenStimuli);
        fixationMonitorTimeline.play();
    }

    public int randomTimeInterval(int constantPart, int randomPart) {
        return constantPart + randomGenerator.nextInt(randomPart + 1);
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

    public Shape createStimulus(String shape, Color color, double inclination) {
        if (shape.equals("Ellipse")) {
            double radiusX = stimulusWidthInPixels / 2;
            double radiusY = stimulusHeightInPixels / 2;
            Ellipse stimulus = new Ellipse(0, 0, radiusX, radiusY);
            stimulus.setFill(color);
            stimulus.setStroke(color);
            return stimulus;
        } else {
            double sizeX = stimulusWidthInPixels;
            double sizeY = stimulusHeightInPixels;
            Polygon stimulus = new Polygon();
            double diagonal = Math.sqrt(Math.pow(sizeX, 2) + Math.pow(sizeY, 2));
            double x = 0 + sizeX;
            double y = 0 - sizeY;
            double polygonInnerAngle = Math.toDegrees(Math.atan2(x - 0, 0 - y));

            double positionAx = 0 + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + 270 + (90 - polygonInnerAngle) - 90)));
            double positionAy = 0 + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + 270 + (90 - polygonInnerAngle) - 90)));

            double positionBx = 0 + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + polygonInnerAngle - 90)));
            double positionBy = 0 + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + polygonInnerAngle - 90)));

            double positionCx = 0 + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + 90 + (90 - polygonInnerAngle) - 90)));
            double positionCy = 0 + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + 90 + (90 - polygonInnerAngle) - 90)));

            double positionDx = 0 + ((diagonal / 2) * Math.cos(Math.toRadians(inclination + 180 + polygonInnerAngle - 90)));
            double positionDy = 0 + ((diagonal / 2) * Math.sin(Math.toRadians(inclination + 180 + polygonInnerAngle - 90)));

            stimulus.getPoints().addAll(positionAx, positionAy, positionBx, positionBy, positionCx, positionCy, positionDx, positionDy);

            stimulus.setFill(color);
            return stimulus;
        }
    }

    private void setInitialDataInTheShellWindow() {
        shellWindowForMainProcedure.addTextToTextArea("PATIENT INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("ID:" + "\t" + data.getPatientInfoData().getPatientID() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Name:" + "\t" + data.getPatientInfoData().getPatientFirstName() + "\t" + data.getPatientInfoData().getPatientLastName() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Examined eye:" + "\t" + data.getExaminedEye() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("SCREEN INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Resolution:" + "\t" + data.getScreenResolutionX() + "\t" + data.getScreenResolutionY() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Size (mm):" + "\t" + data.getScreenWidthInMm() + "\t" + data.getScreenHeightInMm() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Patient distance (mm):" + "\t" + data.getPatientDistanceInMm() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Involved visual field (\u00B0):" + "\t" + data.getVisualFieldX() + "\t" + data.getVisualFieldY() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("LUMINANCE SCALE FOR STIMULI INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("ID:" + "\t" + data.getLuminanceScaleDataForStimuli().getScaleID() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Name:" + "\t" + data.getLuminanceScaleDataForStimuli().getScaleName() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("LUMINANCE SCALE FOR BACKGROUND INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("ID:" + "\t" + data.getLuminanceScaleDataForBackground().getScaleID() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Name:" + "\t" + data.getLuminanceScaleDataForBackground().getScaleName() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("STIMULUS INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Max brightness (%):" + "\t" + data.getStimulusMaxBrightness() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Max luminance(cd/m2):" + "\t" + data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[data.getStimulusMaxBrightness()], 2) + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Min brightness (%):" + "\t" + data.getStimulusMinBrightness() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Min luminance(cd/m2):" + "\t" + data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[data.getStimulusMinBrightness()], 2) + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Shape:" + "\t" + data.getStimulusShape() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Inclination (\u00B0):" + "\t" + data.getStimulusInclination() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Size (\u00B0):" + "\t" + data.getStimulusWidthInDegrees() + "\t" + data.getStimulusHeightInDegrees() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Display time (ms):" + "\t" + data.getStimulusDisplayTime() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Inter-stimuli interval - constant part (ms):" + "\t" + data.getTimeIntervalBetweenStimuliConstantPart() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Inter-stimuli interval - random part (ms):" + "\t" + data.getTimeIntervalBetweenStimuliRandomPart() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Horizontal and vertical distance between stimuli (\u00B0):" + "\t" + data.getDistanceBetweenStimuliHorizontally() + "\t" + data.getDistanceBetweenStimuliVertically() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("BACKGROUND INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Brightness (%):" + "\t" + data.getBackgroundBrightness() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Luminance (cd/m2):" + "\t" + data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForBackground().getLuminanceForBrightness()[data.getBackgroundBrightness()], 2) + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Grid resolution (XY):" + "\t" + data.getQuarterGridResolutionX() + "\t" + data.getQuarterGridResolutionY() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("FIXATION POINT INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Hue:" + "\t" + data.getFixPointHue() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Saturation:" + "\t" + data.getFixPointSaturation() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Brightness:" + "\t" + data.getFixPointBrightness() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Measured luminance (cd/m2):" + "\t" + data.getLuminanceScaleDataForStimuli().getFixationPointMeasuredLuminance() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Width (\u00B0):" + "\t" + data.getFixPointWidth() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Height (\u00B0):" + "\t" + data.getFixPointHeight() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("FIXATION MONITOR INFO" + "\n");
        if (data.getFixationMonitor().equals("None")) {
            shellWindowForMainProcedure.addTextToTextArea("Fixation monitor:" + "\t" + data.getFixationMonitor() + "\n\n");
        } else if (data.getFixationMonitor().equals("Blindspot")) {
            shellWindowForMainProcedure.addTextToTextArea("Fixation monitor:" + "\t" + data.getFixationMonitor() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Fixation check rate (X):" + "\t" + data.getFixationCheckRate() + "\n");
            if (monitoringFixationEvery_X_Stimuli) {
                shellWindowForMainProcedure.addTextToTextArea("Monitoring fixation every X stimuli" + "\n");
            } else {
                shellWindowForMainProcedure.addTextToTextArea("Monitoring fixation every Y (random value from 1 to X) stimuli" + "\n");
            }
            shellWindowForMainProcedure.addTextToTextArea("Blindspot distance from fixation point (\u00B0)" + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Horizontally:" + "\t" + data.getBlindspotDistanceFromFixPointHorizontally() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Vertically:" + "\t" + data.getBlindspotDistanceFromFixPointVertically() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Monitor stimulus characteristics" + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Width (\u00B0):" + "\t" + data.getMonitorStimulusWidth() +"\n");
            shellWindowForMainProcedure.addTextToTextArea("Height (\u00B0):" + "\t" + data.getMonitorStimulusHeight() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Brightness (%):" + "\t" + data.getMonitorStimulusBrightness() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Luminance (cd/m2):" + "\t" + data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[(int) data.getMonitorStimulusBrightness()], 2) + "\n\n");

        } else {
            shellWindowForMainProcedure.addTextToTextArea("Fixation monitor:" + "\t" + data.getFixationMonitor() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Fixation check rate (X):" + "\t" + data.getFixationCheckRate() + "\n");
            if (monitoringFixationEvery_X_Stimuli) {
                shellWindowForMainProcedure.addTextToTextArea("Monitoring fixation every X stimuli" + "\n");
            } else {
                shellWindowForMainProcedure.addTextToTextArea("Monitoring fixation every Y (random value from 1 to X) stimuli" + "\n");
            }
            shellWindowForMainProcedure.addTextToTextArea("Fixation point change characteristics" + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Width (\u00B0):" + "\t" + data.getFixPointChangeWidth() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Height (\u00B0):" + "\t" + data.getFixPointChangeHeight() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Hue:" + "\t" + data.getFixPointChangeHue() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Saturation:" + "\t" + data.getFixPointChangeSaturation() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Brightness:" + "\t" + data.getFixPointChangeBrightness() + "\n");
            shellWindowForMainProcedure.addTextToTextArea("Luminance (cd/m2):" + "\t" + data.getLuminanceScaleDataForStimuli().getFixationPointChangeMeasuredLuminance() + "\n\n");
        }

        shellWindowForMainProcedure.addTextToTextArea("KEYS INFO" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Answer to stimulus:" + "\t" + data.getAnswerToStimulusKey() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Pause procedure:" + "\t" + data.getPauseProcedureKey() + "\n");
        shellWindowForMainProcedure.addTextToTextArea("Cancel procedure:" + "\t" + data.getCancelProcedureKey() + "\n\n");

        shellWindowForMainProcedure.addTextToTextArea("RESULTS (STIMULI THRESHOLDS)" + "\n");
        shellWindowForMainProcedure.addTextToTextArea("id" + "\t" + "%" + "\t" + "cd/m2" + "\t" + "dB" + "\n");
    }
}
