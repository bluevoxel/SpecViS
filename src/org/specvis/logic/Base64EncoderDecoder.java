package org.specvis.logic;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

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

public class Base64EncoderDecoder {

    private static Encoder encoder = Base64.getEncoder();
    private static Decoder decoder = Base64.getDecoder();

    public String decode(String toDecode) {
        byte[] bytesDecoded = decoder.decode(toDecode.getBytes());
        String decoded = new String(bytesDecoded);
        return decoded;
    }

    public String encode(String toEncode) {
        byte[] bytesEncoded = encoder.encode(toEncode.getBytes());
        String encoded = new String(bytesEncoded);
        return encoded;
    }
}
