package org.specvis.procedure;

import javafx.scene.shape.Shape;

/**
 * Created by pdzwiniel on 2015-06-14.
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

public class MainProcedureStimulus {

    private int stimulusAssignedIndex;
    private Shape stimulusShape;
    private int stimulusQuarter; // 0, 1, 2 or 3.
    private int[] stimulusCellCoordinatesXY;
    private int stimulusDisplayTimeInMs;
    private int[] stimulusAnswers;  // int [] {0, 0, 0, 0}; 0 - stimulus was not displayed yet;
                                    // 1 - Lack of answer; 2 - Positive answer.
    private int evaluatedBrightnessThreshold;

    public int getStimulusAssignedIndex() {
        return stimulusAssignedIndex;
    }

    public void setStimulusAssignedIndex(int stimulusAssignedIndex) {
        this.stimulusAssignedIndex = stimulusAssignedIndex;
    }

    public Shape getStimulusShape() {
        return stimulusShape;
    }

    public void setStimulusShape(Shape stimulusShape) {
        this.stimulusShape = stimulusShape;
    }

    public void setStimulusQuarter(int stimulusQuarter) {
        this.stimulusQuarter = stimulusQuarter;
    }

    public int getStimulusDisplayTimeInMs() {
        return stimulusDisplayTimeInMs;
    }

    public void setStimulusDisplayTimeInMs(int stimulusDisplayTimeInMs) {
        this.stimulusDisplayTimeInMs = stimulusDisplayTimeInMs;
    }

    public int[] getStimulusAnswers() {
        return stimulusAnswers;
    }

    public void setStimulusAnswers(int[] stimulusAnswers) {
        this.stimulusAnswers = stimulusAnswers;
    }

    public int[] getStimulusCellCoordinatesXY() {
        return stimulusCellCoordinatesXY;
    }

    public void setStimulusCellCoordinatesXY(int[] stimulusCellCoordinatesXY) {
        this.stimulusCellCoordinatesXY = stimulusCellCoordinatesXY;
    }

    public int getEvaluatedBrightnessThreshold() {
        return evaluatedBrightnessThreshold;
    }

    public void setEvaluatedBrightnessThreshold(int evaluatedBrightnessThreshold) {
        this.evaluatedBrightnessThreshold = evaluatedBrightnessThreshold;
    }
}


