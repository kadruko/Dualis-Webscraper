package com.schewe.dualisbot.dualiswebscraper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVWriter {

    public static String SEPARATOR = ";";

    public static void write(File file, List<Map<String, String>> lines) throws IOException {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            if(file.exists()){
                file.delete();
            }
            List<String> header = new ArrayList<>();
            for(Map<String, String> line : lines){
                for(String key : line.keySet().toArray(new String[0])){
                    if(!header.contains(key)){
                        header.add(key);
                    }
                }
            }
            for(String key : header){
                br.write(key);
                br.write(SEPARATOR);
            }
            br.newLine();
            for(Map<String, String> line : lines){
                for(String key : header){
                    String value = line.get(key);
                    if(value == null){
                        value = "";
                    }
                    br.write(value);
                    br.write(SEPARATOR);
                }
                br.newLine();
            }
        }
    }

}
