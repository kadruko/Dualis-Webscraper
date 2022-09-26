package com.schewe.dualisbot.dualiswebscraper;

import com.schewe.dualisbot.dualiswebscraper.pages.ExamResultsPage;
import com.schewe.dualisbot.dualiswebscraper.pages.MainPage;
import com.schewe.dualisbot.dualiswebscraper.pages.ModulePage;
import com.schewe.dualisbot.dualiswebscraper.pages.SemesterPage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class DualisClient {

    public static String BASE_URL = "https://dualis.dhbw.de";

    private final MainPage mainPage;
    private final ExamResultsPage examResultsPage;
    private final List<SemesterPage> semesterPages;
    private final List<ModulePage> modulePages;

    public DualisClient(Credentials credentials) throws Exception{
        String loginUrl = BASE_URL + "/scripts/mgrqispi.dll?usrname=" + credentials.getUsername() + "&pass=" + credentials.getPassword() + "&APPNAME=CampusNet&PRGNAME=LOGINCHECK&ARGUMENTS=clino,usrname,pass,menuno,menu_type,browser,platform&clino=000000000000001&menuno=000324&menu_type=classic&browser=&platform";
        Connection.Response res = Jsoup.connect(loginUrl).method(Connection.Method.GET).execute();
        String loginCookie = res.cookie("cnsc");
        if(loginCookie == null || loginCookie.equals("")){
            throw new Exception("Login failed");
        }
        String redirectURL = res.header("REFRESH").split(";")[1].replace(" URL=", "");
        Document docRedirection = Jsoup.connect(BASE_URL + redirectURL).cookie("cnsc", loginCookie).get();
        Element metaLink = docRedirection.selectFirst("meta[http-equiv='refresh']");
        String mainPageUrl = metaLink.attr("content").split(";")[1].replace("URL=", "");

        mainPage = new MainPage(mainPageUrl, loginCookie);
        examResultsPage = new ExamResultsPage(mainPage.getExamResultsPath(), loginCookie);
        semesterPages = new ArrayList<>();
        modulePages = new ArrayList<>();
        for(String semesterPath : examResultsPage.getSemesterPaths()){
            SemesterPage semesterPage = new SemesterPage(semesterPath, loginCookie);
            semesterPages.add(semesterPage);
            for(String modulePath : semesterPage.getModulePaths()){
                ModulePage modulePage = new ModulePage(modulePath, loginCookie);
                modulePages.add(modulePage);
            }
        }
    }

    public MainPage getMainPage() {
        return mainPage;
    }

    public ExamResultsPage getExamResultsPage() {
        return examResultsPage;
    }

    public List<SemesterPage> getSemesterPages() {
        return semesterPages;
    }

    public List<ModulePage> getModulePages() {
        return modulePages;
    }
}
