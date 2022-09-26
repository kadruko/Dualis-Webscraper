package com.schewe.dualisbot.dualiswebscraper.pages;

import java.io.IOException;

public class MainPage extends Page {

    private final String examResultsPath;

    public MainPage(String url, String cookie) throws IOException {
        super(url, cookie);

        examResultsPath = document.selectFirst("a.navLink:contains(Prüfungsergebnisse)").attr("href");
    }

    public String getExamResultsPath() {
        return examResultsPath;
    }
}
