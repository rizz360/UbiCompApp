/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rizz.ucapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author iRizz
 */
public class LocationHandler {
    private static LocationHandler instance = null;
    private LocationHandler() {
        
    }
    public static LocationHandler getInstance() {
        if(instance == null) instance = new LocationHandler();
        return instance;
    }
    
    private static String woeid = "";
    
    public static String getWOEID() {
        return woeid;
    }
    
    public static String lookupWOEID(String query) {
        String output = "";
        String uri = "http://woeid.rosselliot.co.nz/lookup/" + query;
        try {
            Document doc = Jsoup.connect(uri).get();
            Elements matches = doc.getElementsByClass("woeid");
            if(matches.isEmpty()) return "";
            output = matches.get(1).html();
        }
        catch (IOException ex) {Logger.getLogger(WeatherHandler.class.getName()).log(Level.SEVERE, null, ex);}
        setWoeid(output);
        return output;
    }

    public static void setWoeid(String woeid) {
        LocationHandler.woeid = woeid;
    }    
}
