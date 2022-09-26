package com.schewe.dualisbot.dualiswebscraper.pages;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamResultsPage extends Page{

    private final List<String> semesterPaths = new ArrayList<>();

    private final List<Map<String, String>> semesters = new ArrayList<>();

    public ExamResultsPage(String path, String cookie) throws IOException {
        super(path, cookie);

        Element select = document.getElementById("semester");
        String[] onChangeParams = select.attr("onchange")
                .replace(" ", "")
                .replace("reloadpage.createUrlAndReload(", "")
                .replace(");", "")
                .split(",");
        String swParam1 = onChangeParams[3].replace("'", "");
        String swParam2 = onChangeParams[4].replace("'", "");
        Elements options = select.children();
        for(Element option : options) {
            String value = option.attr("value");
            String semesterPath = "/scripts/mgrqispi.dll?APPNAME=CampusNet&PRGNAME=COURSERESULTS&ARGUMENTS=-N" + swParam1 + ",-N" + swParam2 + ",-N" + value;
            semesterPaths.add(semesterPath);
            Map<String, String> semester = new HashMap<>();
            semester.put("semester", option.text());
            semesters.add(semester);
        }
    }

    public List<String> getSemesterPaths() {
        return semesterPaths;
    }

    public List<Map<String, String>> getSemesters() {
        return semesters;
    }
}
