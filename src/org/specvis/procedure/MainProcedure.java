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
import org.specvis.data.ConfigurationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pdzwiniel on 2015-06-08.
 * Last update by pdzwiniel on 2015-12-21.
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
    private int answersVectorLength;

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

        answersVectorLength = Integer.valueOf(ConfigurationData.getAnswersVectorLength());

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
                //stimulus.setStimulusAnswers(new int[]{0, 0, 0, 0});
                stimulus.setStimulusAnswers(new int[answersVectorLength]);
                for (int z = 0; z < stimulus.getStimulusAnswers().length; z++) {
                    stimulus.getStimulusAnswers()[z] = 0;
                }

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

    /**
     * Method responsible for deactivating currently displayed stimulus based on patient answers provided
     * to this stimulus and evaluating its brightness threshold.
     *
     * Lets consider situation with 9 element stimulus brightness vector. According to the equation
     * "x = (n + 7) / 4", for the stimulus with 9 element brightness vector patient can provide
     * maximum 4 answers, ie. "(9 + 7) / 4 = 4". Answer "YES" is see by Specvis as "2", "NO" as "1",
     * lack of the answer for brightness value not yet used is see as "0".
     *
     * Taking this into account, lets consider how Specvis will evaluate brightness threshold for stimulus
     * with 4 given answers, ie. [1, 1, 2, 2].
     *
     * 1. Specvis display stimulus with brightness value from the middle of the brightness vector (d).
     *
     * [ ] [ ] [ ] [ ] [d] [ ] [ ] [ ] [ ]
     *
     * 2. Patient answers "No" to this stimulus. All fields on the left of the current brightness value are
     * disabled (x) including current brightness value. Next stimulus is displayed with brightness value
     * two fields higher than last (d).
     *
     * [x] [x] [x] [x] [x] [ ] [d] [ ] [ ]
     *
     * 3. Patient answers "No" to this stimulus. All fields on the left of the current brightness value are
     * disabled (x) including current brightness value. Next stimulus is displayed with brightness value
     * two fields higher than last (d).
     *
     * [x] [x] [x] [x] [x] [x] [x] [ ] [d]
     *
     * 4. Patient answers "Yes" to this stimulus. All fields on the right of the current brightness value are
     * disabled (x) excluding current brightness value. Next stimulus is displayed with brightness value
     * one field lower than last (d).
     *
     * [x] [x] [x] [x] [x] [x] [x] [d] [ ]
     *
     * 5. Patient answers "Yes" to this stimulus. All fields on the right of the current brightness value are
     * disabled (x) excluding current brightness value. Next stimulus is displayed with brightness value
     * two field lower than last - but in this case all four answers were provided so evaluation is ended.
     * Brightness threshold is equal to the brightness vector element with index equal to the only not-disabled
     * field, ie. 7 (e).
     *
     * [x] [x] [x] [x] [x] [x] [x] [e] [x]
     *
     * @param answers
     */
    public void deactivateStimulusIfBrightnessThresholdIsEvaluated(int[] answers) {

        int[] answersToStimulus = answers;
        boolean[] brightnessVectorRepresentation = new boolean[visualFieldTestBrightnessVectorLength];
        for (int i = 0; i < brightnessVectorRepresentation.length; i++) {
            brightnessVectorRepresentation[i] = true;
        }
        int currentElementIndex = visualFieldTestBrightnessVectorLength / 2;
        int latestAnswer = -1;

        for (int i = 0; i < answersToStimulus.length; i++) {
            if (answersToStimulus[i] != 0) {
                switch (answersToStimulus[i]) {
                    case 1:
                        // Disable all fields to the left from current element index with inclusion of itself.
                        // ...
                        // i - current element index
                        // x - disabled field
                        // [ ] [ ] [ ] [ ] [i] [ ] [ ] [ ] [ ]
                        // [x] [x] [x] [x] [x] [ ] [ ] [ ] [ ]
                        for (int j = 0; j <= currentElementIndex; j++) {
                            brightnessVectorRepresentation[j] = false;
                        }

                        // Change current element index base on latest answer.
                        switch (latestAnswer) {
                            case 1:
                                currentElementIndex +=2;
                                break;
                            case 2:
                                currentElementIndex += 1;
                                break;
                            default:
                                currentElementIndex += 2;
                                break;
                        }
                        break;
                    case 2:
                        // Disable all fields to the right from current element index with exclusion of itself.
                        // ...
                        // i - current element index
                        // x - disabled field
                        // [ ] [ ] [ ] [ ] [i] [ ] [ ] [ ] [ ]
                        // [ ] [ ] [ ] [ ] [i] [x] [x] [x] [x]
                        if (currentElementIndex != brightnessVectorRepresentation.length - 1) {
                            for (int j = currentElementIndex + 1; j < brightnessVectorRepresentation.length; j++) {
                                brightnessVectorRepresentation[j] = false;
                            }
                        }

                        // Change current element index base on latest answer.
                        switch (latestAnswer) {
                            case 1:
                                currentElementIndex -= 1;
                                break;
                            case 2:
                                currentElementIndex -= 2;
                                break;
                            default:
                                currentElementIndex -= 2;
                                break;
                        }
                }

                // Set latest answer.
                latestAnswer = answersToStimulus[i];
            } else {

                // If there are no more answers, break the loop.
                break;
            }
        }

        // If there is only one not-disabled field,
        // deactivate current stimulus.
        int truesCount = 0;
        int indexOfTheOnlyTrue = -1;
        boolean deactivateThisStimulus = false;
        for (int i = 0; i < brightnessVectorRepresentation.length; i++) {
            if (brightnessVectorRepresentation[i]) {
                truesCount++;
                indexOfTheOnlyTrue = i;
            }
        }
        if (truesCount <= 1) {
            deactivateThisStimulus = true;
        }

        // 1. Set evaluated brightness threshold.
        // 2. Remove currently displayed stimulus from active stimuli list.
        // 3. Add answers to stimulus to StimulusResults.
        // 4. Set text for ShellWindow progress bar.
        // 5. Add appropriate text information about deactivated stimulus to ShellWindow.
        // 6. If active stimuli list is empty, finish the procedure.
        if (deactivateThisStimulus) {

            // Ad. 1.
            if (indexOfTheOnlyTrue == -1) {
                currentlyDisplayedStimulus.setEvaluatedBrightnessThreshold(indexOfTheOnlyTrue);
            } else {
                currentlyDisplayedStimulus.setEvaluatedBrightnessThreshold(brightnessVector[indexOfTheOnlyTrue]);
            }

            // Ad. 2.
            activeStimuliIndices.remove(new Integer(currentlyDisplayedStimulus.getStimulusAssignedIndex()));

            // Ad. 3.
            StimulusResults stimulusResults = new StimulusResults();
            stimulusResults.setStimulusNumber(currentlyDisplayedStimulus.getStimulusAssignedIndex());
            stimulusResults.setStimulusGridCoordinatesXY(currentlyDisplayedStimulus.getStimulusCellCoordinatesXY());
            stimulusResults.setStimulusBrightnessThreshold(currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold());
            if (indexOfTheOnlyTrue == -1) {
                stimulusResults.setStimulusLuminanceThreshold(indexOfTheOnlyTrue);
            } else {
                stimulusResults.setStimulusLuminanceThreshold(data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold()], 2));
            }
            stimuliAnswers.add(stimulusResults);

            // Ad. 4.
            String s = shellWindowForMainProcedure.getTextProgressBar().getText();
            String[] str = s.split("/");
            int numberOfCurrentStimulus = Integer.valueOf(str[0]) + 1;
            int totalNumberOfStimuliToShow = Integer.valueOf(str[1]);
            shellWindowForMainProcedure.getTextProgressBar().setText(String.valueOf(numberOfCurrentStimulus) + "/" + String.valueOf(totalNumberOfStimuliToShow));
            shellWindowForMainProcedure.getProgressBar().setProgress(1.0 * (Double.valueOf(numberOfCurrentStimulus) / Double.valueOf(totalNumberOfStimuliToShow)));

            // Ad. 5.
            double stimulusMaxLuminance = data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[data.getStimulusMaxBrightness()];
            double backgroundLuminance = data.getLuminanceScaleDataForBackground().getLuminanceForBrightness()[data.getBackgroundBrightness()];
            int stimulusId = currentlyDisplayedStimulus.getStimulusAssignedIndex();
            int stimulusBrightnessThreshold = currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold();
            double stimulusLuminanceThreshold = 0;
            double stimulusDecibelThreshold = 0;
            if (indexOfTheOnlyTrue == -1) {
                stimulusLuminanceThreshold = indexOfTheOnlyTrue;
                stimulusDecibelThreshold = indexOfTheOnlyTrue;
            } else {
                stimulusLuminanceThreshold = data.getScreenLuminanceFunctions().round(data.getLuminanceScaleDataForStimuli().getLuminanceForBrightness()[currentlyDisplayedStimulus.getEvaluatedBrightnessThreshold()], 2);
                stimulusDecibelThreshold = data.getScreenLuminanceFunctions().decibelsValue(stimulusMaxLuminance, stimulusLuminanceThreshold, backgroundLuminance, 2);
            }
            shellWindowForMainProcedure.addTextToTextArea(stimulusId + "\t" + stimulusBrightnessThreshold + "\t" + stimulusLuminanceThreshold + "\t" + stimulusDecibelThreshold + "\n");

            // Ad. 6.
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

            // TEST - output to console information about answers to stimuli.
            int[] a = currentlyDisplayedStimulus.getStimulusAnswers();
            String ans = "";
            for (int i = -1; i < a.length + 1; i++) {
                if (i == -1) {
                    ans += "[";
                } else if (i == a.length) {
                    ans += "]";
                } else {
                    ans += a[i];
                }
            }
            System.out.println("STI:" + "\t" + currentlyDisplayedStimulus.getStimulusAssignedIndex() + "\t" + "BR:" + "\t" +
                    indexOfTheOnlyTrue + "\t" + "A:" + "\t" + ans + "\t" + "DEACTIVATED");
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

    public int getIndexOfNextStimulusBrightness(int[] answers) {
        int[] answersToStimulus = answers;
        int brightnessIndex = visualFieldTestBrightnessVectorLength / 2;
        int latestAnswer = -1;

        for (int i = 0; i < answersToStimulus.length; i++) {
            if (answersToStimulus[i] != 0) {
                switch (answersToStimulus[i]) {
                    case 1:
                        switch (latestAnswer) {
                            case 1:
                                brightnessIndex += 2;
                                break;
                            case 2:
                                brightnessIndex += 1;
                                break;
                            default:
                                brightnessIndex += 2;
                                break;
                        }
                        break;
                    case 2:
                        switch (latestAnswer) {
                            case 1:
                                brightnessIndex -= 1;
                                break;
                            case 2:
                                brightnessIndex -= 2;
                                break;
                            default:
                                brightnessIndex -= 2;
                                break;
                        }
                        break;
                }
                latestAnswer = answersToStimulus[i];
            } else {
                break;
            }
        }

        // TEST - output to console information about answers to stimuli.
        int[] a = currentlyDisplayedStimulus.getStimulusAnswers();
        String ans = "";
        for (int i = -1; i < a.length + 1; i++) {
            if (i == -1) {
                ans += "[";
            } else if (i == a.length) {
                ans += "]";
            } else {
                ans += a[i];
            }
        }
        System.out.println("STI:" + "\t" + currentlyDisplayedStimulus.getStimulusAssignedIndex() + "\t" + "BR:" + "\t" +
                brightnessIndex + "\t" + "A:" + "\t" + ans);

        return brightnessVector[brightnessIndex];
    }

    public void runStimulusTimeline() {
        stimulusTimeline = new Timeline();

        int r = randomGenerator.nextInt(activeStimuliIndices.size());
        int index = activeStimuliIndices.get(r);
        currentlyDisplayedStimulus = stimuliList.get(index);

        KeyFrame displayStimulus = new KeyFrame(Duration.millis(0), event -> {
            permissionToAnswer = true;

            // Set stimulus brightness value based on provided answers.
            int brightnessLevel = getIndexOfNextStimulusBrightness(currentlyDisplayedStimulus.getStimulusAnswers());
            Color newColor = Color.hsb(Integer.valueOf(data.getLuminanceScaleDataForStimuli().getScaleHue()),
                    Double.valueOf(data.getLuminanceScaleDataForStimuli().getScaleSaturation()) / 100,
                    Double.valueOf(brightnessLevel) / 100);
            currentlyDisplayedStimulus.getStimulusShape().setFill(newColor);
            currentlyDisplayedStimulus.getStimulusShape().setStroke(newColor);

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
