/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rizz.ucapp;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.Image;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author iRizz
 */
public class WeatherHandler {
    private static WeatherHandler instance = null;
    private WeatherHandler() {
    }
    public static WeatherHandler getInstance() {
        if(instance == null) instance = new WeatherHandler();
        return instance;
    }
    
    private static YahooWeatherService service;
    private static Channel channel;

    private static String location = "";
    private static String temperature = "";
    private static String condition = "";
    
    public static void updateWeather() {
        try {
            service = new YahooWeatherService();
            channel = service.getForecast(LocationHandler.getWOEID(), DegreeUnit.CELSIUS);
            location = channel.getLocation().getCity();// + ", " + channel.getLocation().getCountry().substring(0, 2);
            temperature = channel.getItem().getCondition().getTemp() + "°C";
            condition = channel.getItem().getCondition().getText();
        } catch (JAXBException | IOException ex) {
            Logger.getLogger(WeatherHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void printInfo() {    //debug
        System.out.println("Location: " + location);
        System.out.println("Temperature: " + temperature);
        System.out.println("Condition: " + condition);
    }
    
    public static String getWeatherIcon() {
    //http://www.alessioatzeni.com/meteocons/
    //http://www.webdesignerdepot.com/2012/12/how-to-harness-yahoos-weather-api/
        switch(condition.toLowerCase()) {
            case "fair":   return "A";
            case "mostly cloudy":
            case "partly cloudy": return "H";
            case "cloudy": return "N";
            case "showers" :
            case "drizzle": return "Q";
            case "snowing": return "W";
            case "hot" :
            case "sunny": return "B";
            case "thunderstorms":
            case "severe thunderstorms": return "Z";
            default: return "'";
        }
    }
    
    public static String getLocation() {
        return location;
    }

    public static String getTemperature() {
        return temperature;
    }

    public static String getCondition() {
        return condition;
    }
    
}
