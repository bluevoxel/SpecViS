package org.specvis.data;

/**
 * Created by pdzwiniel on 2015-10-09.
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

public class ConfigurationData {

    private static String luminanceScaleFitPolynomialDegree;
    private static String ifFittedValuesAreLessThanZeroSetThemTo;
    private static String screenWidth;
    private static String screenHeight;
    private static String patientDistance;
    private static String stimulusMaxBrightness;
    private static String stimulusMinBrightness;
    private static String stimulusShape;
    private static String stimulusInclination;
    private static String stimulusWidth;
    private static String stimulusHeight;
    private static String stimulusDisplayTime;
    private static String constantPartOfIntervalBetweenStimuli;
    private static String randomPartOfIntervalBetweenStimuli;
    private static String backgroundBrightness;
    private static String quarterGridResolutionX;
    private static String quarterGridResolutionY;
    private static String fixationPointColor;
    private static String fixationPointWidth;
    private static String fixationPointHeight;
    private static String answerToStimulusKey;
    private static String pauseProcedureKey;
    private static String cancelProcedureKey;
    private static String fixationMonitor;
    private static String fixationCheckRate;
    private static String blindspotDistanceFromFixationPointHorizontally;
    private static String blindspotDistanceFromFixationPointVertically;
    private static String monitorStimulusWidth;
    private static String monitorStimulusHeight;
    private static String monitorStimulusBrightness;
    private static String fixationPointChangeWidth;
    private static String fixationPointChangeHeight;
    private static String fixationPointChangeColor;
    private static String blindspotMappingRangeHorizontally_1;
    private static String blindspotMappingRangeHorizontally_2;
    private static String blindspotMappingRangeVertically_1;
    private static String blindspotMappingRangeVertically_2;
    private static String blindspotMappingStimulusDisplayRepetitions;
    private static String blindspotMappingAccuracy;
    private static String visualFieldTestBrightnessVectorLength;

    public static String getLuminanceScaleFitPolynomialDegree() {
        return luminanceScaleFitPolynomialDegree;
    }

    public static void setLuminanceScaleFitPolynomialDegree(String luminanceScaleFitPolynomialDegree) {
        ConfigurationData.luminanceScaleFitPolynomialDegree = luminanceScaleFitPolynomialDegree;
    }

    public static String getIfFittedValuesAreLessThanZeroSetThemTo() {
        return ifFittedValuesAreLessThanZeroSetThemTo;
    }

    public static void setIfFittedValuesAreLessThanZeroSetThemTo(String ifFittedValuesAreLessThanZeroSetThemTo) {
        ConfigurationData.ifFittedValuesAreLessThanZeroSetThemTo = ifFittedValuesAreLessThanZeroSetThemTo;
    }

    public static String getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(String screenWidth) {
        ConfigurationData.screenWidth = screenWidth;
    }

    public static String getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(String screenHeight) {
        ConfigurationData.screenHeight = screenHeight;
    }

    public static String getPatientDistance() {
        return patientDistance;
    }

    public static void setPatientDistance(String patientDistance) {
        ConfigurationData.patientDistance = patientDistance;
    }

    public static String getStimulusMaxBrightness() {
        return stimulusMaxBrightness;
    }

    public static void setStimulusMaxBrightness(String stimulusMaxBrightness) {
        ConfigurationData.stimulusMaxBrightness = stimulusMaxBrightness;
    }

    public static String getStimulusMinBrightness() {
        return stimulusMinBrightness;
    }

    public static void setStimulusMinBrightness(String stimulusMinBrightness) {
        ConfigurationData.stimulusMinBrightness = stimulusMinBrightness;
    }

    public static String getStimulusShape() {
        return stimulusShape;
    }

    public static void setStimulusShape(String stimulusShape) {
        ConfigurationData.stimulusShape = stimulusShape;
    }

    public static String getStimulusInclination() {
        return stimulusInclination;
    }

    public static void setStimulusInclination(String stimulusInclination) {
        ConfigurationData.stimulusInclination = stimulusInclination;
    }

    public static String getStimulusWidth() {
        return stimulusWidth;
    }

    public static void setStimulusWidth(String stimulusWidth) {
        ConfigurationData.stimulusWidth = stimulusWidth;
    }

    public static String getStimulusHeight() {
        return stimulusHeight;
    }

    public static void setStimulusHeight(String stimulusHeight) {
        ConfigurationData.stimulusHeight = stimulusHeight;
    }

    public static String getStimulusDisplayTime() {
        return stimulusDisplayTime;
    }

    public static void setStimulusDisplayTime(String stimulusDisplayTime) {
        ConfigurationData.stimulusDisplayTime = stimulusDisplayTime;
    }

    public static String getConstantPartOfIntervalBetweenStimuli() {
        return constantPartOfIntervalBetweenStimuli;
    }

    public static void setConstantPartOfIntervalBetweenStimuli(String constantPartOfIntervalBetweenStimuli) {
        ConfigurationData.constantPartOfIntervalBetweenStimuli = constantPartOfIntervalBetweenStimuli;
    }

    public static String getRandomPartOfIntervalBetweenStimuli() {
        return randomPartOfIntervalBetweenStimuli;
    }

    public static void setRandomPartOfIntervalBetweenStimuli(String randomPartOfIntervalBetweenStimuli) {
        ConfigurationData.randomPartOfIntervalBetweenStimuli = randomPartOfIntervalBetweenStimuli;
    }

    public static String getBackgroundBrightness() {
        return backgroundBrightness;
    }

    public static void setBackgroundBrightness(String backgroundBrightness) {
        ConfigurationData.backgroundBrightness = backgroundBrightness;
    }

    public static String getQuarterGridResolutionX() {
        return quarterGridResolutionX;
    }

    public static void setQuarterGridResolutionX(String quarterGridResolutionX) {
        ConfigurationData.quarterGridResolutionX = quarterGridResolutionX;
    }

    public static String getQuarterGridResolutionY() {
        return quarterGridResolutionY;
    }

    public static void setQuarterGridResolutionY(String quarterGridResolutionY) {
        ConfigurationData.quarterGridResolutionY = quarterGridResolutionY;
    }

    public static String getFixationPointColor() {
        return fixationPointColor;
    }

    public static void setFixationPointColor(String fixationPointColor) {
        ConfigurationData.fixationPointColor = fixationPointColor;
    }

    public static String getFixationPointWidth() {
        return fixationPointWidth;
    }

    public static void setFixationPointWidth(String fixationPointWidth) {
        ConfigurationData.fixationPointWidth = fixationPointWidth;
    }

    public static String getFixationPointHeight() {
        return fixationPointHeight;
    }

    public static void setFixationPointHeight(String fixationPointHeight) {
        ConfigurationData.fixationPointHeight = fixationPointHeight;
    }

    public static String getAnswerToStimulusKey() {
        return answerToStimulusKey;
    }

    public static void setAnswerToStimulusKey(String answerToStimulusKey) {
        ConfigurationData.answerToStimulusKey = answerToStimulusKey;
    }

    public static String getPauseProcedureKey() {
        return pauseProcedureKey;
    }

    public static void setPauseProcedureKey(String pauseProcedureKey) {
        ConfigurationData.pauseProcedureKey = pauseProcedureKey;
    }

    public static String getCancelProcedureKey() {
        return cancelProcedureKey;
    }

    public static void setCancelProcedureKey(String cancelProcedureKey) {
        ConfigurationData.cancelProcedureKey = cancelProcedureKey;
    }

    public static String getFixationMonitor() {
        return fixationMonitor;
    }

    public static void setFixationMonitor(String fixationMonitor) {
        ConfigurationData.fixationMonitor = fixationMonitor;
    }

    public static String getFixationCheckRate() {
        return fixationCheckRate;
    }

    public static void setFixationCheckRate(String fixationCheckRate) {
        ConfigurationData.fixationCheckRate = fixationCheckRate;
    }

    public static String getBlindspotDistanceFromFixationPointHorizontally() {
        return blindspotDistanceFromFixationPointHorizontally;
    }

    public static void setBlindspotDistanceFromFixationPointHorizontally(String blindspotDistanceFromFixationPointHorizontally) {
        ConfigurationData.blindspotDistanceFromFixationPointHorizontally = blindspotDistanceFromFixationPointHorizontally;
    }

    public static String getBlindspotDistanceFromFixationPointVertically() {
        return blindspotDistanceFromFixationPointVertically;
    }

    public static void setBlindspotDistanceFromFixationPointVertically(String blindspotDistanceFromFixationPointVertically) {
        ConfigurationData.blindspotDistanceFromFixationPointVertically = blindspotDistanceFromFixationPointVertically;
    }

    public static String getMonitorStimulusWidth() {
        return monitorStimulusWidth;
    }

    public static void setMonitorStimulusWidth(String monitorStimulusWidth) {
        ConfigurationData.monitorStimulusWidth = monitorStimulusWidth;
    }

    public static String getMonitorStimulusHeight() {
        return monitorStimulusHeight;
    }

    public static void setMonitorStimulusHeight(String monitorStimulusHeight) {
        ConfigurationData.monitorStimulusHeight = monitorStimulusHeight;
    }

    public static String getMonitorStimulusBrightness() {
        return monitorStimulusBrightness;
    }

    public static void setMonitorStimulusBrightness(String monitorStimulusBrightness) {
        ConfigurationData.monitorStimulusBrightness = monitorStimulusBrightness;
    }

    public static String getFixationPointChangeWidth() {
        return fixationPointChangeWidth;
    }

    public static void setFixationPointChangeWidth(String fixationPointChangeWidth) {
        ConfigurationData.fixationPointChangeWidth = fixationPointChangeWidth;
    }

    public static String getFixationPointChangeHeight() {
        return fixationPointChangeHeight;
    }

    public static void setFixationPointChangeHeight(String fixationPointChangeHeight) {
        ConfigurationData.fixationPointChangeHeight = fixationPointChangeHeight;
    }

    public static String getFixationPointChangeColor() {
        return fixationPointChangeColor;
    }

    public static void setFixationPointChangeColor(String fixationPointChangeColor) {
        ConfigurationData.fixationPointChangeColor = fixationPointChangeColor;
    }

    public static String getBlindspotMappingRangeHorizontally_1() {
        return blindspotMappingRangeHorizontally_1;
    }

    public static void setBlindspotMappingRangeHorizontally_1(String blindspotMappingRangeHorizontally_1) {
        ConfigurationData.blindspotMappingRangeHorizontally_1 = blindspotMappingRangeHorizontally_1;
    }

    public static String getBlindspotMappingRangeHorizontally_2() {
        return blindspotMappingRangeHorizontally_2;
    }

    public static void setBlindspotMappingRangeHorizontally_2(String blindspotMappingRangeHorizontally_2) {
        ConfigurationData.blindspotMappingRangeHorizontally_2 = blindspotMappingRangeHorizontally_2;
    }

    public static String getBlindspotMappingRangeVertically_1() {
        return blindspotMappingRangeVertically_1;
    }

    public static void setBlindspotMappingRangeVertically_1(String blindspotMappingRangeVertically_1) {
        ConfigurationData.blindspotMappingRangeVertically_1 = blindspotMappingRangeVertically_1;
    }

    public static String getBlindspotMappingRangeVertically_2() {
        return blindspotMappingRangeVertically_2;
    }

    public static void setBlindspotMappingRangeVertically_2(String blindspotMappingRangeVertically_2) {
        ConfigurationData.blindspotMappingRangeVertically_2 = blindspotMappingRangeVertically_2;
    }

    public static String getBlindspotMappingStimulusDisplayRepetitions() {
        return blindspotMappingStimulusDisplayRepetitions;
    }

    public static void setBlindspotMappingStimulusDisplayRepetitions(String blindspotMappingStimulusDisplayRepetitions) {
        ConfigurationData.blindspotMappingStimulusDisplayRepetitions = blindspotMappingStimulusDisplayRepetitions;
    }

    public static String getBlindspotMappingAccuracy() {
        return blindspotMappingAccuracy;
    }

    public static void setBlindspotMappingAccuracy(String blindspotMappingAccuracy) {
        ConfigurationData.blindspotMappingAccuracy = blindspotMappingAccuracy;
    }

    public static String getVisualFieldTestBrightnessVectorLength() {
        return visualFieldTestBrightnessVectorLength;
    }

    public static void setVisualFieldTestBrightnessVectorLength(String visualFieldTestBrightnessVectorLength) {
        ConfigurationData.visualFieldTestBrightnessVectorLength = visualFieldTestBrightnessVectorLength;
    }
}
