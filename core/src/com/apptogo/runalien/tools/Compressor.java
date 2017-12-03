package com.apptogo.runalien.tools;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.oracle.jrockit.jfr.ContentType.Bytes;

public class Compressor {
    public static String compress(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;

        for(int i = 0; i < byteArray.length; i++) {
            String mapped = map(byteArray[i]);
            if(mapped != null) {
                stringBuilder.append(counter);
                stringBuilder.append(mapped);
                counter = 0;
            }
            else {
                counter++;
            }
        }
        return stringBuilder.toString();
    }

    private static String map(byte b) {
        if(b > 0) {
            return "P";
        }
        else if(b < 0) {
            return "L";
        }
        else {
            return null;
        }
    }

    private static Byte map(String s) {
        if("P".equals(s)) {
            return 1;
        }
        else if("L".equals(s)) {
            return -1;
        }
        else {
            return null;
        }
    }

    private static byte[] convertByteListToByteArray(List<Byte> byteList) {
        if(byteList == null || byteList.size() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[byteList.size()];
        for(int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

    public static byte[] uncompress(String compressedString) {
        List<Byte> resultList = new ArrayList<>();
        int counter = 0;
        for(int i = 0; i < compressedString.length(); i++) {
            String character = String.valueOf(compressedString.charAt(i));
            if(map(character) != null) {
                for(int j = 0; j < counter; j++) {
                    resultList.add((byte) 0);
                }
                counter = 0;
                resultList.add(map(character));
            }
            else {
                System.out.println(counter);
                counter = (counter * 10) + Integer.parseInt(character);
                System.out.println(counter);
            }
        }
        return convertByteListToByteArray(resultList);
    }
}
