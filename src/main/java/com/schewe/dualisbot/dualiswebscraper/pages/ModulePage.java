package com.schewe.dualisbot.dualiswebscraper.pages;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModulePage extends Page {

    private final String moduleId;
    private final String moduleName;
    private final String semester;
    private final List<Map<String, String>> attempts = new ArrayList<>();
    private final List<Map<String, String>> exams = new ArrayList<>();

    public ModulePage(String path, String cookie) throws IOException {
        super(path, cookie);

        String title = document.getElementsByTag("h1").first().text().trim();
        moduleId = title.split(" ")[0];
        semester = StringUtils.substringBetween(title,"(", ")");
        moduleName = StringUtils.remove(StringUtils.remove(title, moduleId), "(" + semester + ")").trim();

        String attemptNumber = "";
        for(Element row : document.getElementsByTag("tbody").first().children()){
            Elements cells = row.children();
            if(cells.size() > 1) {
                if (cells.get(0).text().contains("Versuch ")) {
                    attemptNumber = cells.get(0).text().trim().replace("Versuch ", "");
                } else if (cells.get(1).text().contains("Gesamt ")) {
                    Map<String, String> attempt = new HashMap<>();
                    attempt.put("module_id", moduleId);
                    attempt.put("attempt", cells.get(1).text().replace("Gesamt ", ""));
                    String rating = cells.get(3).text();
                    String grade;
                    String status;
                    if(rating.replace(" ", "").equals("")){
                        grade = "noch nicht gesetzt";
                        status = "";
                    }else{
                        grade = rating.split(" ")[0].replace(",", ".");
                        status = rating.split(" ")[1];
                    }
                    attempt.put("grade", grade);
                    attempt.put("status", status);
                    attempts.add(attempt);
                } else if (!cells.get(3).text().replace(" ", "").equals("") && cells.get(0).text().replace(" ", "").equals("")) {
                    Map<String, String> exam = new HashMap<>();
                    exam.put("module_id", moduleId);
                    exam.put("attempt", attemptNumber);
                    String nameWeighting = cells.get(1).text().trim();
                    String weighting = StringUtils.substringBetween(nameWeighting, "(", ")");
                    String name = StringUtils.remove(nameWeighting, "(" + weighting + ")").trim();
                    exam.put("exam_name", name);
                    exam.put("weighting", weighting);
                    String ratingString = cells.get(3).text();
                    if(!ratingString.equals("noch nicht gesetzt") && !ratingString.equals("b")) {
                        float rating = Float.parseFloat(ratingString.replace(",", "."));
                        if (rating > 6) {
                            ratingString = (int) rating + "%";
                        } else {
                            ratingString = String.valueOf(rating);
                        }
                        exam.put("rating", ratingString);
                        exams.add(exam);
                    }
                } else if(!cells.get(3).text().replace(" ", "").equals("") && !cells.get(3).text().replace(" ", "").equals("Datum") && !cells.get(0).text().replace(" ", "").equals("")){
                    Map<String, String> exam = new HashMap<>();
                    exam.put("module_id", moduleId);
                    exam.put("attempt", attemptNumber);
                    String nameWeighting = cells.get(1).text().trim();
                    String weighting = StringUtils.substringBetween(nameWeighting, "(", ")");
                    String name = StringUtils.remove(nameWeighting, "(" + weighting + ")").trim();
                    exam.put("exam_name", moduleName + " " + name);
                    exam.put("weighting", weighting);
                    String ratingString = cells.get(3).text();
                    if(!ratingString.equals("noch nicht gesetzt") && !ratingString.equals("b")) {
                        float rating = Float.parseFloat(cells.get(3).text().replace(",", "."));
                        if (rating > 6) {
                            ratingString = (int) rating + "%";
                        } else {
                            ratingString = String.valueOf(rating);
                        }
                        exam.put("rating", ratingString);
                        exams.add(exam);
                    }
                }
            }
        }
    }

    public String getModuleId() {
        return moduleId;
    }

    public List<Map<String, String>> getAttempts() {
        return attempts;
    }

    public List<Map<String, String>> getExams() {
        return exams;
    }
}
