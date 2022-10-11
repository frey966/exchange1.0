package com.financia.gateway.filter;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CheckUrl1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<String> urls = new ArrayList<>();
        urls.add("/manage-business/test/string");
        urls.add("/app-exchange/test/string");
        urls.add("/app-websock/*");
        urls.add("/app-exchange/*/*");
        String url="/app-exchange/test/string1";
        String[] urlsArray=urls.toArray(new String[urls.size()]);
        //String[] excludeUrl = new String[]{"/abf/*", "/abd/**", "*.login", "*.html", "*.jsp", "/abg/login"};
        //System.out.println("excludeUrl="+excludeUrl);
        System.out.println("urlsArray="+urlsArray);
        //FilterUrl.checkWhiteList(excludeUrl, "/abf/test");
        System.out.println(FilterUrl.checkWhiteList(urlsArray, url));
    }
}

