package org.specvis.logic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.specvis.data.LuminanceScaleData;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by pdzwiniel on 2015-05-26.
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

public class ScreenLuminanceFunctions {

    public String totalTime(long start, long end) {
        long difference = end - start;
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(difference),
                TimeUnit.MILLISECONDS.toMinutes(difference) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)),
                TimeUnit.MILLISECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference)));
    }

    public double decibelsValue(double maxLuminance, double stimulusLuminance, double backgroundLuminance, int round) {
        double decibels = 10 * Math.log10(maxLuminance / (stimulusLuminance - backgroundLuminance));
        return round(decibels, round);
    }

    public int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public ObservableList<String> getActiveDisplaysObservableList() {
        ObservableList<String> observableListActiveDisplays = FXCollections.observableArrayList();
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
        for (GraphicsDevice i : graphicsDevices) {
            observableListActiveDisplays.add(i.toString());
        }
        return observableListActiveDisplays;
    }

    public GraphicsDevice[] getActiveDisplaysGraphicsDevices() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return graphicsEnvironment.getScreenDevices();
    }

    public int[] getAcriveDisplayResolution(GraphicsDevice activeDisplay) {
        int x = activeDisplay.getDisplayMode().getWidth();
        int y = activeDisplay.getDisplayMode().getHeight();
        return new int[] {x, y};
    }

    public double[] getInvolvedVisualField(int screenWidthInMM, int screenHeightInMM, int patientDistanceInMM) {
        double[] involvedVisualField = new double[2];
        double screenWidthInCM = (double) screenWidthInMM / 10;
        double screenHeightInCM = (double) screenHeightInMM / 10;
        double patientDistanceInCM = (double) patientDistanceInMM / 10;
        double visualFieldX = 2 * Math.atan2(screenWidthInCM, 2 * patientDistanceInCM) * (180 / Math.PI);
        double visualFieldY = 2 * Math.atan2(screenHeightInCM, 2 * patientDistanceInCM) * (180 / Math.PI);
        involvedVisualField[0] = visualFieldX;
        involvedVisualField[1] = visualFieldY;
        return involvedVisualField;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public java.util.List<Double> linspace(double start, double stop, int n, boolean roundToInt) {
        List<Double> result = new ArrayList();
        double step = (stop-start)/(n-1);
        for(int i = 0; i <= n-2; i++) {
            if (roundToInt) {
                BigDecimal bd = new BigDecimal(start + (i * step));
                bd = bd.setScale(0, RoundingMode.HALF_UP);
                result.add(bd.doubleValue());
            } else {
                result.add(start + (i * step));
            }
        }
        result.add(stop);
        return result;
    }

    public String createScaleIndividualID(String currentDate, int numberOfUnspecifiedSymbols) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int l = 0; l < numberOfUnspecifiedSymbols; l++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String date = currentDate.replaceAll("-", "");
        return date + "_" + sb.toString();
    }

    public String getCurrentDateYYYYmmDD() {
        Calendar date = new GregorianCalendar();
        String day = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(date.get(Calendar.MONTH) + 1);
        String year = Integer.toString(date.get(Calendar.YEAR));
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    public double[] fitPolynomialToData(double[] x, double[] y, int degree, boolean reverseCoefficients) {
        WeightedObservedPoints obs = new WeightedObservedPoints();
        for (int i = 0; i < x.length; i++) {
            obs.add(1, x[i], y[i]);
        }
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        double[] coefficients = fitter.fit(obs.toList());
        if (reverseCoefficients) {
            return reverse(coefficients);
        } else {
            return coefficients;
        }
    }

    public double[] reverse(double[] values) {
        for (int i = 0; i < values.length / 2; i++) {
            double temp = values[i];
            values[i] = values[values.length - i - 1];
            values[values.length - i - 1] = temp;
        }
        return values;
    }

    public double[] poly2D(double[] coeff, double[] x) throws Exception{
        if (coeff.length != 3) {
            throw new Exception("poly2D method can handle only 3 coefficients.");
        } else {
            double[] y = new double[x.length];
            for (int i = 0; i < y.length; i++) {
                y[i] = coeff[0] * Math.pow(x[i], 2) + coeff[1] * x[i] + coeff[2];
            }
            return y;
        }
    }

    public double[] createBrightnessVector(int vectorLength) {
        double step = 101 / (double) vectorLength;
        double[] brightnessVector = new double[vectorLength];
        for (int i = 0; i < vectorLength; i ++) {
            brightnessVector[i] = (double) i * step;
        }
        return brightnessVector;
    }

    public void saveNewScaleToFile(String id, String scaleName, String scaleHue, String scaleSaturation, String B0, String B20, String B40, String B60, String B80, String B100, String additionalInfo) {
        File file = new File("screen_luminance_scales.s");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(id + '\t' + scaleName + '\t' + scaleHue + '\t' + scaleSaturation + '\t' + B0 + '\t' +
                    B20 + '\t' + B40 + '\t' + B60 + '\t' + B80 + '\t' + B100 + '\t' + additionalInfo + '\n');
            writer.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Scale was successfully saved.");
            alert.setContentText(scaleName + " with ID " + id + " was successfully saved.");
            alert.showAndWait();
        } catch (IOException ex) {
            ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
            ed.setTitle("Error");
            ed.setHeaderText("Exception");
            ed.showAndWait();
        }
    }

    public void deleteSelectedScale(String scaleID, ArrayList<String> existingLuminanceScalesList) {
        if (existingLuminanceScalesList.size() > 0) {
            for (int i = 0; i < existingLuminanceScalesList.size(); i++) {
                String[] str = existingLuminanceScalesList.get(i).split("\t");
                if (scaleID.equals(str[0])) {
                    existingLuminanceScalesList.remove(i);
                    BufferedWriter writer;
                    File file = new File("screen_luminance_scales.s");
                    try {
                        writer = new BufferedWriter(new FileWriter(file));
                        for (int j = 0; j < existingLuminanceScalesList.size(); j++) {
                            writer.write(existingLuminanceScalesList.get(j) + "\n");
                        }
                        writer.close();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("Selected scale was removed.");
                        alert.setContentText(str[1] + " with ID " +
                                str[0] + " was successfully removed.");
                        alert.showAndWait();
                    } catch (IOException ex) {
                        ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
                        ed.setTitle("Error");
                        ed.setHeaderText("Exception");
                        ed.showAndWait();
                    }
                    break;
                }
            }
        }
    }

    public void saveEditedScale(String[] scaleInfo, ArrayList<String> existingLuminanceScalesList) {
        if (existingLuminanceScalesList.size() > 0) {
            for (int i = 0; i < existingLuminanceScalesList.size(); i++) {
                BufferedWriter writer;
                File file = new File("screen_luminance_scales.s");
                try {
                    writer = new BufferedWriter(new FileWriter(file));
                    for (int j = 0; j < existingLuminanceScalesList.size(); j++) {
                        writer.write(existingLuminanceScalesList.get(j) + "\n");
                    }
                    writer.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Scale was edited.");
                    alert.setContentText(scaleInfo[1] + " with ID " +
                            scaleInfo[0] + " was successfully edited and saved.");
                    alert.showAndWait();
                } catch (IOException ex) {
                    ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
                    ed.setTitle("Error");
                    ed.setHeaderText("Exception");
                    ed.showAndWait();
                }
            }
        }
    }

    public ArrayList<String> getExistingLuminanceScalesList(File file) {
        ArrayList<String> existingLuminanceScalesList = new ArrayList();
        if (file.exists()) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                while ((line = reader.readLine()) != null) {
                    existingLuminanceScalesList.add(line);
                }
                reader.close();
            } catch (IOException ex) {
                ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
                ed.setTitle("Error");
                ed.setHeaderText("Exception");
                ed.showAndWait();
            }
        }
        return existingLuminanceScalesList;
    }

    public LuminanceScaleData findExistingLuminanceScaleByID(String scaleID, ArrayList<String> existingLuminanceScalesList) {
        LuminanceScaleData luminanceScaleData = new LuminanceScaleData();
        if (existingLuminanceScalesList.size() > 0) {
            for (int i = 0; i < existingLuminanceScalesList.size(); i++) {
                String[] str = existingLuminanceScalesList.get(i).split("\t");
                if (scaleID.equals(str[0])) {
                    luminanceScaleData.setScaleID(str[0]);
                    luminanceScaleData.setScaleName(str[1]);
                    luminanceScaleData.setScaleHue(str[2]);
                    luminanceScaleData.setScaleSaturation(str[3]);
                    luminanceScaleData.setScaleB0(str[4]);
                    luminanceScaleData.setScaleB20(str[5]);
                    luminanceScaleData.setScaleB40(str[6]);
                    luminanceScaleData.setScaleB60(str[7]);
                    luminanceScaleData.setScaleB80(str[8]);
                    luminanceScaleData.setScaleB100(str[9]);
                    luminanceScaleData.setScaleAdditionalInfo(str[10]);
                }
            }
        }
        return luminanceScaleData;
    }
}
