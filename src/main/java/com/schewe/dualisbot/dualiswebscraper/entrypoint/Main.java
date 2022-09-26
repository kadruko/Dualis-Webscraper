package com.schewe.dualisbot.dualiswebscraper.entrypoint;

import com.schewe.dualisbot.dualiswebscraper.CSVWriter;
import com.schewe.dualisbot.dualiswebscraper.Credentials;
import com.schewe.dualisbot.dualiswebscraper.DualisClient;
import com.schewe.dualisbot.dualiswebscraper.pages.ModulePage;
import com.schewe.dualisbot.dualiswebscraper.pages.SemesterPage;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        try {
            DualisClient client = new DualisClient(new Credentials("schewe.konrad@dh-karlsruhe.de", "HCunter123"));

            List<Map<String, String>> semesters = client.getExamResultsPage().getSemesters();
            List<Map<String, String>> modules = new ArrayList<>();
            for(SemesterPage semesterPage : client.getSemesterPages()){
                modules = Stream.concat(modules.stream(), semesterPage.getModules().stream()).collect(Collectors.toList());
            }
            List<Map<String, String>> attempts = new ArrayList<>();
            List<Map<String, String>> exams = new ArrayList<>();
            for(ModulePage modulePage : client.getModulePages()){
                attempts = Stream.concat(attempts.stream(), modulePage.getAttempts().stream()).collect(Collectors.toList());
                exams = Stream.concat(exams.stream(), modulePage.getExams().stream()).collect(Collectors.toList());
            }

            semesters = new ArrayList<>(new HashSet<>(semesters));
            modules = new ArrayList<>(new HashSet<>(modules));
            attempts = new ArrayList<>(new HashSet<>(attempts));
            exams = new ArrayList<>(new HashSet<>(exams));

            CSVWriter.write(new File("test-results/semesters.csv"), semesters);
            CSVWriter.write(new File("test-results/modules.csv"), modules);
            CSVWriter.write(new File("test-results/attempts.csv"), attempts);
            CSVWriter.write(new File("test-results/exams.csv"), exams);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
