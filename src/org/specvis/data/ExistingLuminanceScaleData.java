package org.specvis.data;

import java.lang.reflect.Field;

/**
 * Created by pdzwiniel on 2015-05-28.
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

public class ExistingLuminanceScaleData {

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

    public ExistingLuminanceScaleData(String scaleID, String scaleName, String scaleHue, String scaleSaturation,
                                      String scaleB0, String scaleB20, String scaleB40, String scaleB60,
                                      String scaleB80, String scaleB100) {
        this.scaleID = scaleID;
        this.scaleName = scaleName;
        this.scaleHue = scaleHue;
        this.scaleSaturation = scaleSaturation;
        this.scaleB0 = scaleB0;
        this.scaleB20 = scaleB20;
        this.scaleB40 = scaleB40;
        this.scaleB60 = scaleB60;
        this.scaleB80 = scaleB80;
        this.scaleB100 = scaleB100;
    }

    public static String[] getFieldsNames() {
        Field[] fields = ExistingLuminanceScaleData.class.getDeclaredFields();
        String[] fieldsNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldsNames[i] = fields[i].getName();
        }
        return fieldsNames;
    }

    public String getScaleID() {
        return scaleID;
    }

    public String getScaleName() {
        return scaleName;
    }

    public String getScaleHue() {
        return scaleHue;
    }

    public String getScaleSaturation() {
        return scaleSaturation;
    }

    public String getScaleB0() {
        return scaleB0;
    }

    public String getScaleB20() {
        return scaleB20;
    }

    public String getScaleB40() {
        return scaleB40;
    }

    public String getScaleB60() {
        return scaleB60;
    }

    public String getScaleB80() {
        return scaleB80;
    }

    public String getScaleB100() {
        return scaleB100;
    }
}
