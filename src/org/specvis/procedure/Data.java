package org.specvis.procedure;

import org.specvis.data.LuminanceScaleData;
import org.specvis.data.PatientInfoData;
import org.specvis.logic.ScreenLuminanceFunctions;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pdzwiniel on 2015-06-08.
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

public class Data {

    /* Step 1 - Patient */
    private String examinedEye;
    private PatientInfoData patientInfoData;

    /* Step 2 - Luminance scale & screen */
    private int activeDisplayIndex;
    private int screenResolutionX;
    private int screenResolutionY;
    private int screenWidthInMm;
    private int screenHeightInMm;
    private int patientDistanceInMm;
    private double visualFieldX;
    private double visualFieldY;
    private LuminanceScaleData luminanceScaleDataForStimuli;
    private LuminanceScaleData luminanceScaleDataForBackground;
    private ScreenLuminanceFunctions screenLuminanceFunctions;

    /* Step 3 - Stimulus & background */
    private int stimulusMaxBrightness;
    private int stimulusMinBrightness;
    private String stimulusShape;
    private double stimulusInclination;
    private double stimulusWidthInDegrees;
    private double stimulusHeightInDegrees;
    private int stimulusDisplayTime;
    private int timeIntervalBetweenStimuliConstantPart;
    private int timeIntervalBetweenStimuliRandomPart;
    private double distanceBetweenStimuliHorizontally;
    private double distanceBetweenStimuliVertically;
    private int backgroundBrightness;
    private int quarterGridResolutionX;
    private int quarterGridResolutionY;
    private int[] stimuliVerifiedBrightnessValues;

    /* Step 4 - Fixation & other */
    private int fixPointHue;
    private int fixPointSaturation;
    private int fixPointBrightness;
    private double fixPointWidth;
    private double fixPointHeight;
    private String answerToStimulusKey;
    private String pauseProcedureKey;
    private String cancelProcedureKey;
    private String fixationMonitor;
    private int fixationCheckRate;
    private boolean monitoringFixationEvery_X_Stimuli;
    private boolean monitoringFixationEvery_Y_Stimuli;
    private double blindspotDistanceFromFixPointHorizontally;
    private double blindspotDistanceFromFixPointVertically;
    private double monitorStimulusWidth;
    private double monitorStimulusHeight;
    private double monitorStimulusBrightness;
    private double fixPointChangeWidth;
    private double fixPointChangeHeight;
    private int fixPointChangeHue;
    private int fixPointChangeSaturation;
    private int fixPointChangeBrightness;

    /* MainProcedure data */
    private ArrayList<StimulusResults> mainProcedureStimuliResults;
    private boolean isMainProcedureFinished;

    public String getExaminedEye() {
        return examinedEye;
    }

    public void setExaminedEye(String examinedEye) {
        this.examinedEye = examinedEye;
    }

    public int getActiveDisplayIndex() {
        return activeDisplayIndex;
    }

    public void setActiveDisplayIndex(int activeDisplayIndex) {
        this.activeDisplayIndex = activeDisplayIndex;
    }

    public int getScreenResolutionX() {
        return screenResolutionX;
    }

    public void setScreenResolutionX(int screenResolutionX) {
        this.screenResolutionX = screenResolutionX;
    }

    public int getScreenResolutionY() {
        return screenResolutionY;
    }

    public void setScreenResolutionY(int screenResolutionY) {
        this.screenResolutionY = screenResolutionY;
    }

    public double getVisualFieldX() {
        return visualFieldX;
    }

    public void setVisualFieldX(double visualFieldX) {
        this.visualFieldX = visualFieldX;
    }

    public double getVisualFieldY() {
        return visualFieldY;
    }

    public void setVisualFieldY(double visualFieldY) {
        this.visualFieldY = visualFieldY;
    }

    public int getScreenWidthInMm() {
        return screenWidthInMm;
    }

    public void setScreenWidthInMm(int screenWidthInMm) {
        this.screenWidthInMm = screenWidthInMm;
    }

    public int getScreenHeightInMm() {
        return screenHeightInMm;
    }

    public void setScreenHeightInMm(int screenHeightInMm) {
        this.screenHeightInMm = screenHeightInMm;
    }

    public int getPatientDistanceInMm() {
        return patientDistanceInMm;
    }

    public void setPatientDistanceInMm(int patientDistanceInMm) {
        this.patientDistanceInMm = patientDistanceInMm;
    }

    public int getStimulusMaxBrightness() {
        return stimulusMaxBrightness;
    }

    public void setStimulusMaxBrightness(int stimulusInitialBrightness) {
        this.stimulusMaxBrightness = stimulusInitialBrightness;
    }

    public int getStimulusMinBrightness() {
        return stimulusMinBrightness;
    }

    public void setStimulusMinBrightness(int stimulusMinBrightness) {
        this.stimulusMinBrightness = stimulusMinBrightness;
    }

    public String getStimulusShape() {
        return stimulusShape;
    }

    public void setStimulusShape(String stimulusShape) {
        this.stimulusShape = stimulusShape;
    }

    public double getStimulusInclination() {
        return stimulusInclination;
    }

    public void setStimulusInclination(double stimulusInclination) {
        this.stimulusInclination = stimulusInclination;
    }

    public double getStimulusWidthInDegrees() {
        return stimulusWidthInDegrees;
    }

    public void setStimulusWidthInDegrees(double stimulusWidthInDegrees) {
        this.stimulusWidthInDegrees = stimulusWidthInDegrees;
    }

    public double getStimulusHeightInDegrees() {
        return stimulusHeightInDegrees;
    }

    public void setStimulusHeightInDegrees(double stimulusHeightInDegrees) {
        this.stimulusHeightInDegrees = stimulusHeightInDegrees;
    }

    public int getBackgroundBrightness() {
        return backgroundBrightness;
    }

    public void setBackgroundBrightness(int backgroundBrightness) {
        this.backgroundBrightness = backgroundBrightness;
    }

    public int getFixPointHue() {
        return fixPointHue;
    }

    public void setFixPointHue(int fixPointHue) {
        this.fixPointHue = fixPointHue;
    }

    public int getFixPointSaturation() {
        return fixPointSaturation;
    }

    public void setFixPointSaturation(int fixPointSaturation) {
        this.fixPointSaturation = fixPointSaturation;
    }

    public int getFixPointBrightness() {
        return fixPointBrightness;
    }

    public void setFixPointBrightness(int fixPointBrightness) {
        this.fixPointBrightness = fixPointBrightness;
    }

    public double getFixPointWidth() {
        return fixPointWidth;
    }

    public void setFixPointWidth(double fixPointWidthInDegrees) {
        this.fixPointWidth = fixPointWidthInDegrees;
    }

    public double getFixPointHeight() {
        return fixPointHeight;
    }

    public void setFixPointHeight(double fixPointHeightInDegrees) {
        this.fixPointHeight = fixPointHeightInDegrees;
    }

    public String getAnswerToStimulusKey() {
        return answerToStimulusKey;
    }

    public void setAnswerToStimulusKey(String answerToStimulusKey) {
        this.answerToStimulusKey = answerToStimulusKey;
    }

    public String getPauseProcedureKey() {
        return pauseProcedureKey;
    }

    public void setPauseProcedureKey(String pauseProcedureKey) {
        this.pauseProcedureKey = pauseProcedureKey;
    }

    public String getCancelProcedureKey() {
        return cancelProcedureKey;
    }

    public void setCancelProcedureKey(String cancelProcedureKey) {
        this.cancelProcedureKey = cancelProcedureKey;
    }

    public int getQuarterGridResolutionX() {
        return quarterGridResolutionX;
    }

    public void setQuarterGridResolutionX(int quarterGridResolutionX) {
        this.quarterGridResolutionX = quarterGridResolutionX;
    }

    public int getQuarterGridResolutionY() {
        return quarterGridResolutionY;
    }

    public void setQuarterGridResolutionY(int quarterGridResolutionY) {
        this.quarterGridResolutionY = quarterGridResolutionY;
    }

    public double getDistanceBetweenStimuliHorizontally() {
        return distanceBetweenStimuliHorizontally;
    }

    public void setDistanceBetweenStimuliHorizontally(double distanceBetweenStimuliHorizontally) {
        this.distanceBetweenStimuliHorizontally = distanceBetweenStimuliHorizontally;
    }

    public double getDistanceBetweenStimuliVertically() {
        return distanceBetweenStimuliVertically;
    }

    public void setDistanceBetweenStimuliVertically(double distanceBetweenStimuliVertically) {
        this.distanceBetweenStimuliVertically = distanceBetweenStimuliVertically;
    }

    public PatientInfoData getPatientInfoData() {
        return patientInfoData;
    }

    public void setPatientInfoData(PatientInfoData patientInfoData) {
        this.patientInfoData = patientInfoData;
    }

    public LuminanceScaleData getLuminanceScaleDataForStimuli() {
        return luminanceScaleDataForStimuli;
    }

    public void setLuminanceScaleDataForStimuli(LuminanceScaleData luminanceScaleDataForStimuli) {
        this.luminanceScaleDataForStimuli = luminanceScaleDataForStimuli;
    }

    public LuminanceScaleData getLuminanceScaleDataForBackground() {
        return luminanceScaleDataForBackground;
    }

    public void setLuminanceScaleDataForBackground(LuminanceScaleData luminanceScaleDataForBackground) {
        this.luminanceScaleDataForBackground = luminanceScaleDataForBackground;
    }

    public ScreenLuminanceFunctions getScreenLuminanceFunctions() {
        return screenLuminanceFunctions;
    }

    public void setScreenLuminanceFunctions(ScreenLuminanceFunctions screenLuminanceFunctions) {
        this.screenLuminanceFunctions = screenLuminanceFunctions;
    }

    public int getStimulusDisplayTime() {
        return stimulusDisplayTime;
    }

    public void setStimulusDisplayTime(int stimulusDisplayTime) {
        this.stimulusDisplayTime = stimulusDisplayTime;
    }

    public int getTimeIntervalBetweenStimuliConstantPart() {
        return timeIntervalBetweenStimuliConstantPart;
    }

    public void setTimeIntervalBetweenStimuliConstantPart(int timeIntervalBetweenStimuliConstantPart) {
        this.timeIntervalBetweenStimuliConstantPart = timeIntervalBetweenStimuliConstantPart;
    }

    public int getTimeIntervalBetweenStimuliRandomPart() {
        return timeIntervalBetweenStimuliRandomPart;
    }

    public void setTimeIntervalBetweenStimuliRandomPart(int timeIntervalBetweenStimuliRandomPart) {
        this.timeIntervalBetweenStimuliRandomPart = timeIntervalBetweenStimuliRandomPart;
    }

    public String getFixationMonitor() {
        return fixationMonitor;
    }

    public void setFixationMonitor(String fixationMonitor) {
        this.fixationMonitor = fixationMonitor;
    }

    public int getFixationCheckRate() {
        return fixationCheckRate;
    }

    public void setFixationCheckRate(int fixationCheckRate) {
        this.fixationCheckRate = fixationCheckRate;
    }

    public double getBlindspotDistanceFromFixPointHorizontally() {
        return blindspotDistanceFromFixPointHorizontally;
    }

    public void setBlindspotDistanceFromFixPointHorizontally(double blindspotDistanceFromFixPointHorizontally) {
        this.blindspotDistanceFromFixPointHorizontally = blindspotDistanceFromFixPointHorizontally;
    }

    public double getBlindspotDistanceFromFixPointVertically() {
        return blindspotDistanceFromFixPointVertically;
    }

    public void setBlindspotDistanceFromFixPointVertically(double blindspotDistanceFromFixPointVertically) {
        this.blindspotDistanceFromFixPointVertically = blindspotDistanceFromFixPointVertically;
    }

    public double getMonitorStimulusWidth() {
        return monitorStimulusWidth;
    }

    public void setMonitorStimulusWidth(double monitorStimulusWidth) {
        this.monitorStimulusWidth = monitorStimulusWidth;
    }

    public double getMonitorStimulusHeight() {
        return monitorStimulusHeight;
    }

    public void setMonitorStimulusHeight(double monitorStimulusHeight) {
        this.monitorStimulusHeight = monitorStimulusHeight;
    }

    public double getMonitorStimulusBrightness() {
        return monitorStimulusBrightness;
    }

    public void setMonitorStimulusBrightness(double monitorStimulusBrightness) {
        this.monitorStimulusBrightness = monitorStimulusBrightness;
    }

    public double getFixPointChangeWidth() {
        return fixPointChangeWidth;
    }

    public void setFixPointChangeWidth(double fixPointChangeWidth) {
        this.fixPointChangeWidth = fixPointChangeWidth;
    }

    public double getFixPointChangeHeight() {
        return fixPointChangeHeight;
    }

    public void setFixPointChangeHeight(double fixPointChangeHeight) {
        this.fixPointChangeHeight = fixPointChangeHeight;
    }

    public int getFixPointChangeHue() {
        return fixPointChangeHue;
    }

    public void setFixPointChangeHue(int fixPointChangeHue) {
        this.fixPointChangeHue = fixPointChangeHue;
    }

    public int getFixPointChangeSaturation() {
        return fixPointChangeSaturation;
    }

    public void setFixPointChangeSaturation(int fixPointChangeSaturation) {
        this.fixPointChangeSaturation = fixPointChangeSaturation;
    }

    public int getFixPointChangeBrightness() {
        return fixPointChangeBrightness;
    }

    public void setFixPointChangeBrightness(int fixPointChangeBrightness) {
        this.fixPointChangeBrightness = fixPointChangeBrightness;
    }

    public ArrayList<StimulusResults> getMainProcedureStimuliResults() {
        return mainProcedureStimuliResults;
    }

    public void setMainProcedureStimuliResults(ArrayList<StimulusResults> mainProcedureStimuliResults) {
        this.mainProcedureStimuliResults = mainProcedureStimuliResults;
    }

    public boolean isMainProcedureFinished() {
        return isMainProcedureFinished;
    }

    public void setIsMainProcedureFinished(boolean isMainProcedureFinished) {
        this.isMainProcedureFinished = isMainProcedureFinished;
    }

    public boolean isMonitoringFixationEvery_X_Stimuli() {
        return monitoringFixationEvery_X_Stimuli;
    }

    public void setMonitoringFixationEvery_X_Stimuli(boolean monitoringFixationEvery_X_Stimuli) {
        this.monitoringFixationEvery_X_Stimuli = monitoringFixationEvery_X_Stimuli;
    }

    public boolean isMonitoringFixationEvery_Y_Stimuli() {
        return monitoringFixationEvery_Y_Stimuli;
    }

    public void setMonitoringFixationEvery_Y_Stimuli(boolean monitoringFixationEvery_Y_Stimuli) {
        this.monitoringFixationEvery_Y_Stimuli = monitoringFixationEvery_Y_Stimuli;
    }

    public int[] getStimuliVerifiedBrightnessValues() {
        return stimuliVerifiedBrightnessValues;
    }

    public void setStimuliVerifiedBrightnessValues(int[] stimuliVerifiedBrightnessValues) {
        this.stimuliVerifiedBrightnessValues = stimuliVerifiedBrightnessValues;
    }
}
