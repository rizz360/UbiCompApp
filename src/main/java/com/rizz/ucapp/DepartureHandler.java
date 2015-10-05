/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rizz.ucapp;

import com.wordpress.loeper.kvv.EFA;
import com.wordpress.loeper.kvv.live.exception.StopNotFoundException;
import com.wordpress.loeper.kvv.live.model.Departure;
import com.wordpress.loeper.kvv.live.model.Departures;
import com.wordpress.loeper.kvv.live.model.GeoStop;
import com.wordpress.loeper.kvv.live.model.Stop;
import com.wordpress.loeper.kvv.live.model.Stops;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iRizz
 */
public class DepartureHandler {
    //https://github.com/MartinLoeper/KVV_JAVA_API
    private static EFA kvv;
    private static DepartureHandler instance = null;
    private DepartureHandler() {
        kvv = new EFA();
        
    }
    public static DepartureHandler getInstance() {
        if(instance == null) instance = new DepartureHandler();
        return instance;
    };
    
    public static EFA getKVV() {
        if(kvv == null)kvv = new EFA();
        return kvv;
    }
    
    public static List<Departure> getDepartures(String query) {
        List<Departure> departuresList = null;
        try {
            //mostProbableStop = getKVV().searchStopByName(query).getStops().get(0);
            Stop mostProbableStop = getKVV().getMostProbableMatch(getKVV().searchStopByName(query), query);
            departuresList = getKVV().getDeparturesByStopId(mostProbableStop).getDepartures();
        }
        catch (IOException | StopNotFoundException ex) {Logger.getLogger(DepartureHandler.class.getName()).log(Level.SEVERE, null, ex);}
        return departuresList;
    }
    
    public static List<Departure> getDepartures(double lon, double lat) {
        List<Departure> departuresList = null;
        try {
            //mostProbableStop = getKVV().searchStopByName(query).getStops().get(0);
            Stop stopByCoord = getKVV().searchStopByGeoLocation(lon, lat).getStops().get(0);
            departuresList = getKVV().getDeparturesByStopId(stopByCoord).getDepartures();
        }
        catch (IOException | StopNotFoundException ex) {Logger.getLogger(DepartureHandler.class.getName()).log(Level.SEVERE, null, ex);}
        return departuresList;
    }
    
    public static String getStopName(double lon, double lat) {
        try {
            return getKVV().getDeparturesByStopId(getKVV().searchStopByGeoLocation(lon, lat).getStops().get(0)).getStopName();
        } catch (IOException | StopNotFoundException ex) {
            Logger.getLogger(DepartureHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Error";
    }
    
    public static String getFormatedDepartures(List<Departure> list, int index) {
        if(index >= list.size()) return "Error retrieving info for this location";
        return list.get(index).getRoute() + " | " + list.get(index).getDestination() + " | " + getFormatedTime(list.get(index).getTime());
    }
    
    public static String getFormatedTime(long unixTime) {
        String output = "";
        long timeDiff = unixTime - Calendar.getInstance().getTime().getTime()/1000L;
        if(timeDiff > 60*10) {
            Date date = new Date(unixTime*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            output = sdf.format(date);
        }
        else
            if(timeDiff/60 == 1) output = "1 Minute";
            else output = timeDiff/60 + " Minuten";
        return output;
    }   
}
