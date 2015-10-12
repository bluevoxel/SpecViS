package org.specvis.data;

/**
 * Created by pdzwiniel on 2015-05-27.
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

public class LuminanceScaleData {

    private String scaleID;
    private String scaleName;
    private String scaleHue;
    private String scaleSaturation;
    private String scaleB0;
    private String scaleB20;
    private String scaleB40;
    private String scaleB60;
    private String scaleB80;
    private String scaleB100;
    private String scaleAdditionalInfo;
    private double[] brightnessMeasureScale;
    private double[] luminanceMeasurements;
    private double[] brightnessVector;
    private double[] luminanceForBrightness;
    private String fixationPointMeasuredLuminance = "0";
    private String fixationPointChangeMeasuredLuminance = "0";

    public String getScaleID() {
        return scaleID;
    }

    public void setScaleID(String scaleID) {
        this.scaleID = scaleID;
    }

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }

    public String getScaleHue() {
        return scaleHue;
    }

    public void setScaleHue(String scaleHue) {
        this.scaleHue = scaleHue;
    }

    public String getScaleSaturation() {
        return scaleSaturation;
    }

    public void setScaleSaturation(String scaleSaturation) {
        this.scaleSaturation = scaleSaturation;
    }

    public String getScaleB0() {
        return scaleB0;
    }

    public void setScaleB0(String scaleB0) {
        this.scaleB0 = scaleB0;
    }

    public String getScaleB20() {
        return scaleB20;
    }

    public void setScaleB20(String scaleB20) {
        this.scaleB20 = scaleB20;
    }

    public String getScaleB40() {
        return scaleB40;
    }

    public void setScaleB40(String scaleB40) {
        this.scaleB40 = scaleB40;
    }

    public String getScaleB60() {
        return scaleB60;
    }

    public void setScaleB60(String scaleB60) {
        this.scaleB60 = scaleB60;
    }

    public String getScaleB80() {
        return scaleB80;
    }

    public void setScaleB80(String scaleB80) {
        this.scaleB80 = scaleB80;
    }

    public String getScaleB100() {
        return scaleB100;
    }

    public void setScaleB100(String scaleB100) {
        this.scaleB100 = scaleB100;
    }

    public String getScaleAdditionalInfo() {
        return scaleAdditionalInfo;
    }

    public void setScaleAdditionalInfo(String scaleAdditionalInfo) {
        this.scaleAdditionalInfo = scaleAdditionalInfo;
    }

    public double[] getBrightnessMeasureScale() {
        return brightnessMeasureScale;
    }

    public void setBrightnessMeasureScale(double[] brightnessMeasureScale) {
        this.brightnessMeasureScale = brightnessMeasureScale;
    }

    public double[] getLuminanceMeasurements() {
        return luminanceMeasurements;
    }

    public void setLuminanceMeasurements(double[] luminanceMeasurements) {
        this.luminanceMeasurements = luminanceMeasurements;
    }

    public double[] getBrightnessVector() {
        return brightnessVector;
    }

    public void setBrightnessVector(double[] brightnessVector) {
        this.brightnessVector = brightnessVector;
    }

    public double[] getLuminanceForBrightness() {
        return luminanceForBrightness;
    }

    public void setLuminanceForBrightness(double[] luminanceForBrightness) {
        this.luminanceForBrightness = luminanceForBrightness;
    }

    public String getFixationPointMeasuredLuminance() {
        return fixationPointMeasuredLuminance;
    }

    public void setFixationPointMeasuredLuminance(String fixationPointMeasuredLuminance) {
        this.fixationPointMeasuredLuminance = fixationPointMeasuredLuminance;
    }

    public String getFixationPointChangeMeasuredLuminance() {
        return fixationPointChangeMeasuredLuminance;
    }

    public void setFixationPointChangeMeasuredLuminance(String fixationPointChangeMeasuredLuminance) {
        this.fixationPointChangeMeasuredLuminance = fixationPointChangeMeasuredLuminance;
    }
}
