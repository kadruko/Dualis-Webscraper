package com.schewe.dualisbot.dualiswebscraper.pages;

import com.schewe.dualisbot.dualiswebscraper.DualisClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Page {

    protected String path;
    protected Document document;

    public Page(String path, String cookie) throws IOException {
        this.path = path;
        document = Jsoup.connect(DualisClient.BASE_URL + path).cookie("cnsc", cookie).get();
    }

}
