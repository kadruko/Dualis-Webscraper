package com.schewe.dualisbot.dualiswebscraper.pages;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemesterPage extends Page {

    private final String semester;
    private final List<String> modulePaths = new ArrayList<>();

    private final List<Map<String, String>> modules = new ArrayList<>();

    public SemesterPage(String path, String cookie) throws IOException {
        super(path, cookie);

        Element select = document.getElementById("semester");
        semester = select.getElementsByAttribute("selected").first().text();
        for(Element row : document.getElementsByTag("tbody").first().children()){
            Elements cells = row.children();
            if(cells.get(0).attr("colspan").equals("")) {
                Map<String, String> module = new HashMap<>();
                String moduleId = cells.get(0).text();
                module.put("module_id", moduleId);
                module.put("module_name", cells.get(1).text());
                module.put("final_grade", cells.get(2).text().replace(",", "."));
                module.put("credits", " " + (int) Float.parseFloat(cells.get(3).text().replace(",", ".")));
                module.put("status", cells.get(4).text());
                module.put("semester", semester);
                modules.add(module);
                Element script = cells.get(5).getElementsByTag("script").first();
                String[] popupParams = StringUtils.substringBetween(script.html(), "dl_popUp(", ");").split("\"");
                String modulePath = popupParams[1];
                modulePaths.add(modulePath);
            }
        }
    }

    public String getSemester() {
        return semester;
    }

    public List<String> getModulePaths() {
        return modulePaths;
    }

    public List<Map<String, String>> getModules() {
        return modules;
    }
}
