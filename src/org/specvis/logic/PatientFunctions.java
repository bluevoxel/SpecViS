package org.specvis.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.specvis.data.PatientInfoData;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pdzwiniel on 2015-05-25.
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

public class PatientFunctions {

    public String createPatientIndividualID(String currentDate, int numberOfUnspecifiedSymbols) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int l = 0; l < numberOfUnspecifiedSymbols; l++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String date = currentDate.replaceAll("-", "");
        String id = date + "_" + sb.toString();
        return id;
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
        String regDate = year + "-" + month + "-" + day;
        return regDate;
    }

    public int calculatePatientAge(String currentDate, String patientDateOfBirth) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date cd = null;
        Date dob = null;
        try {
            cd = sdf.parse(currentDate);
            dob = sdf.parse(patientDateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = cd.getTime() - dob.getTime();
        int diffDays = (int) (diff / (24 * 1000 * 60 * 60));
        return diffDays / 365;
    }

    public void saveNewPatientToFile(String id, String gender, String firstName, String lastName, String dateOfBirth,
                                     String age, String phone, String email, String city, String postalCode,
                                     String visualAcuityForLeftEye, String visualAcuityForRightEye,
                                     String additionalInformation) {
        File file = new File("patients.s");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));

            writer.write(id + '\t' + gender + '\t' + firstName + '\t' + lastName + '\t' + dateOfBirth + '\t' +
                    age + '\t' + phone + '\t' + email + '\t' + city + '\t' + postalCode + '\t' +
                    visualAcuityForLeftEye + '\t' + visualAcuityForRightEye + '\t' + additionalInformation + '\n');

            writer.close();

            Base64EncoderDecoder cipher = new Base64EncoderDecoder();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient was successfully saved.");
            alert.setContentText(cipher.decode(firstName) + " " + cipher.decode(lastName) + " with ID " +
                    cipher.decode(id) + " was successfully saved.");
            alert.showAndWait();
        } catch (IOException ex) {
            ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
            ed.setTitle("Error");
            ed.setHeaderText("Exception");
            ed.showAndWait();
        }
    }

    public PatientInfoData findExistingPatientByID(String patientID, ArrayList<String> existingPatientsList,
                                                   Base64EncoderDecoder cipher) {
        PatientInfoData patientInfoData = new PatientInfoData();
        if (existingPatientsList.size() > 0) {
            for (int i = 0; i < existingPatientsList.size(); i++) {
                String[] str = existingPatientsList.get(i).split("\t");
                if (cipher.decode(str[0]).equals(patientID)) {
                    patientInfoData.setPatientID(cipher.decode(str[0]));
                    patientInfoData.setPatientGender(cipher.decode(str[1]));
                    patientInfoData.setPatientFirstName(cipher.decode(str[2]));
                    patientInfoData.setPatientLastName(cipher.decode(str[3]));
                    patientInfoData.setPatientDateOfBirth(cipher.decode(str[4]));
                    patientInfoData.setPatientAge(cipher.decode(str[5]));
                    patientInfoData.setPatientPhone(cipher.decode(str[6]));
                    patientInfoData.setPatientEmail(cipher.decode(str[7]));
                    patientInfoData.setPatientCity(cipher.decode(str[8]));
                    patientInfoData.setPatientPostalCode(cipher.decode(str[9]));
                    patientInfoData.setPatientVisualAcuityLeftEye(cipher.decode(str[10]));
                    patientInfoData.setPatientVisualAcuityRightEye(cipher.decode(str[11]));
                    patientInfoData.setPatientAdditionalInformation(cipher.decode(str[12]));
                }
            }
        }
        return patientInfoData;
    }

    public void deleteSelectedPatient(String patientID, ArrayList<String> existingPatientsList,
                                      Base64EncoderDecoder cipher) {
        if (existingPatientsList.size() > 0) {
            for (int i = 0; i < existingPatientsList.size(); i++) {
                String[] str = existingPatientsList.get(i).split("\t");
                String pID = cipher.decode(str[0]);
                if (patientID.equals(pID)) {
                    existingPatientsList.remove(i);
                    BufferedWriter writer;
                    File file = new File("patients.s");
                    try {
                        writer = new BufferedWriter(new FileWriter(file));
                        for (int j = 0; j < existingPatientsList.size(); j++) {
                            writer.write(existingPatientsList.get(j) + "\n");
                        }
                        writer.close();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("Selected patient was removed.");
                        alert.setContentText(cipher.decode(str[2]) + " " + cipher.decode(str[3]) + " with ID " +
                                cipher.decode(str[0]) + " was successfully removed.");
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

    public void saveEditedPatient(String[] patientInfo, ArrayList<String> existingPatientsList) {
        if (existingPatientsList.size() > 0) {
            for (int i = 0; i < existingPatientsList.size(); i++) {
                BufferedWriter writer;
                File file = new File("patients.s");
                try {
                    writer = new BufferedWriter(new FileWriter(file));
                    for (int j = 0; j < existingPatientsList.size(); j++) {
                        writer.write(existingPatientsList.get(j) + "\n");
                    }
                    writer.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Patient information was edited.");
                    alert.setContentText(patientInfo[1] + " " + patientInfo[2] + " with ID " +
                            patientInfo[0] + " was successfully edited and saved.");
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

    public ArrayList<String> getExistingPatientsList(File file) {
        ArrayList<String> existingPatientsList = new ArrayList<>();
        if (file.exists()) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                while ((line = reader.readLine()) != null) {
                    existingPatientsList.add(line);
                }
                reader.close();
            } catch (IOException ex) {
                ExceptionDialog ed = new ExceptionDialog(Alert.AlertType.ERROR, ex);
                ed.setTitle("Error");
                ed.setHeaderText("Exception");
                ed.showAndWait();
            }
        }
        return existingPatientsList;
    }

    public void setTextForEmptyTextField(TextField textField, String text) {
        if (textField.getText().equals("")) {
            textField.setText(text);
        }
    }

    public void setTextForTextFieldDependOnGivenText(TextField textField, String givenText, String conditionText, String settingText) {
        if (givenText.equals(conditionText)) {
            textField.setText(settingText);
        } else {
            textField.setText(givenText);
        }
    }
}
