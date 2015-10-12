package org.specvis.procedure;

/**
 * Created by pdzwiniel on 2015-10-01.
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

public class StimulusResults {

    private int stimulusNumber;
    private int[] stimulusGridCoordinatesXY;
    private int stimulusBrightnessThreshold;
    private double stimulusLuminanceThreshold;

    public StimulusResults() {

    }

    public int getStimulusNumber() {
        return stimulusNumber;
    }

    public void setStimulusNumber(int stimulusNumber) {
        this.stimulusNumber = stimulusNumber;
    }

    public int[] getStimulusGridCoordinatesXY() {
        return stimulusGridCoordinatesXY;
    }

    public void setStimulusGridCoordinatesXY(int[] stimulusGridCoordinatesXY) {
        this.stimulusGridCoordinatesXY = stimulusGridCoordinatesXY;
    }

    public int getStimulusBrightnessThreshold() {
        return stimulusBrightnessThreshold;
    }

    public void setStimulusBrightnessThreshold(int stimulusBrightnessThreshold) {
        this.stimulusBrightnessThreshold = stimulusBrightnessThreshold;
    }

    public double getStimulusLuminanceThreshold() {
        return stimulusLuminanceThreshold;
    }

    public void setStimulusLuminanceThreshold(double stimulusLuminanceThreshold) {
        this.stimulusLuminanceThreshold = stimulusLuminanceThreshold;
    }
}
