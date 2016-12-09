package com.webscrapper.batch;

import io.advantageous.boon.core.Sys;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.webscrapper.domain.Race;
import com.webscrapper.domain.RaceInfo;
import com.webscrapper.repository.RaceInfoRepository;
import com.webscrapper.service.RaceInfoService;
import com.webscrapper.service.RaceService;

@Component
public class PuntersDataScraper {
	private static final Logger log = LoggerFactory.getLogger(PuntersDataScraper.class);
	private RaceService raceService;
	private RaceInfoService raceInfoService;
	private RaceInfoRepository raceInfo;
	private String RACE_URL;
	private String cityName;
	//private static final String RACE_URL="https://www.punters.com.au/racing-results/new-south-wales/Taree/2016-11-28/";
	//private static final Element doc = null;
	
	
	public PuntersDataScraper() {
		// TODO Auto-generated constructor stub
	}
	public PuntersDataScraper( RaceInfoService raceInfoService, RaceService raceService, RaceInfoRepository raceInfo) {
		this.raceInfoService = raceInfoService;
		this.raceService = raceService;
		this.raceInfo = raceInfo;
	}
	
     public void readWebsite() throws JSONException{
    	 Properties prop = new Properties();
 		try {
 			prop.load(new FileInputStream("D:/properties/url.properties"));
 			String[] values = prop.getProperty("site.url").toString().split(";");
 					for(int k = 0; k < values.length; k++) {
 						RACE_URL = values[k];
 						Document doc=null;
 				  		try {
 					  		doc = Jsoup.connect(RACE_URL).get();
 				  		} catch (IOException e) {
 				  			// TODO Auto-generated catch block
 				  			e.printStackTrace();
 				  		}
	/*
		Document doc=null;
  		try {
  			doc = Jsoup.connect(RACE_URL).get();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}*/
 		System.err.println("Race URL"+RACE_URL);
  		Elements ListOfTables = doc.select("table.results-table");
//  		System.out.println(ListOfTables.size());
  		List<Race> raceDetailsList = new ArrayList<Race>();
  		
  		/*String[] getting_date_from_url =  RACE_URL.split(",");
        String[] getting_date = getting_date_from_url[0].split("=");     
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMMd");
        LocalDate raceDate1 = LocalDate.parse(getting_date[1].replaceAll("\\s+",""), formatter);*/
  		
  		ListOfTables.forEach(raceTable -> {
  			    Race raceObj = new Race();
  			    Elements info=raceTable.select("thead > tr > th");
	        	String raceTitle = raceTable.select("thead > tr > th").first().text();
	        	/*Matcher m = Pattern.compile("$").matcher(
						raceTitle);*/
	        	//String raceTitle=raceHead.substring(, )
	        	String replace = RACE_URL.replace("https://www.punters.com.au/racing-results", "");
	        	String state=replace.split("/")[1];
	        	String cityName=replace.split("/")[2];
	        	String raceDatefromUrl=replace.split("/")[3];
	            
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
	            LocalDate raceDate= LocalDate.parse(raceDatefromUrl.replaceAll("\\s+",""), formatter);
	        	
	        	String next_race_length = info.select("div > span.distance > abbr").first().attr("data-value")+info.select("div > span.distance > abbr").first().attr("data-unit");
	        	//String nextRaceTime=info.select("div > abbr").get(0).attr("title");
	        	//String raceDescription = raceTable.select("tr").get(1).select("div.details-line").text();
//	        	System.out.println("next_race_length:"+next_race_length);
	        	//System.out.println("nextRaceTime:"+nextRaceTime);
	        	//System.out.println("raceDescription"+raceDescription);
//	        	System.err.println("State:"+state);
//	        	System.out.println("City:"+cityName);
//	        	System.out.println("Date:"+date);
	        	raceObj.setCity(cityName);
	  			raceObj.setState(state);
	  			raceObj.setRace_length(next_race_length);
	  			//raceObj.setRaceDescription(raceDescription);
	  			//raceObj.setRace_time(nextRaceTime);
	  			raceObj.setRaceDate(raceDate);
	  			raceObj.setRaceName(raceTitle);
	  			raceObj.setRace_time("time");
	  			raceObj.setRaceDescription("Description");
	  			raceObj = raceService.save(raceObj);
	  			raceDetailsList.add(raceObj);
  				});
  		
  		
  		/*Elements raceTablesList = ListOfTables.select("table.results-table > tbody > tr");*/
  		Elements raceTablesList = ListOfTables.select("table.results-table");
  		
//  		System.err.println("size"+raceTablesList.size());
        if(raceTablesList.size()==raceDetailsList.size()){
        	log.info("********* ON TRACK - RACE TITLE MATCHES RACE DETAILS ***********");
        }else{
        	log.error("********** PROBLEM IN PARSING -- RACE TITLE COUNT DOES NOT MATCH RACE DETAILS" );
        }

        raceTablesList.forEach(body -> {
        	Elements raceInfoHeader = body.select("tbody > tr");
//        	System.err.println("size2"+raceInfoHeader.size());
        	
        	List<RaceInfo> raceInfoList = new ArrayList<RaceInfo>();
        	
         	raceInfoHeader.forEach(row -> {
         		
        		
        	String horsesName = row.select("td").get(0).select("a").text();
        	if (horsesName.equalsIgnoreCase(
					"horse")) {
				// do nothing for header
			}
//        	System.err.println("name is"+horsesName);
        	RaceInfo info = new RaceInfo();
//        	System.out.println("horsesName"+horsesName);
        	String finishValue = row.select("td").get(0).text();
//        	System.out.println("finishValue"+finishValue);
            String startPosition = body.select("td").get(0).select("span.result-icon").text();
//            System.out.println("startPosition"+startPosition);
            String jockeyName = row.select("td").get(1).select("span.jockey-name").text();
//            System.out.println("jockeyName"+jockeyName);
            // String topTote = body.select("td").get(2).text();
            System.err.println("Race URL"+RACE_URL);
            String startingPrice = row.select("td").get(3).text();
//            System.out.println("startingPrice"+startingPrice);
            String margin = row.select("td").get(4).text();
//            System.out.println("margin"+margin);
          
               List<RaceInfo> lastDate = raceInfo.lastRaceDate(horsesName);
            
               if(lastDate.size() == 0){
         		info.setPrevious_margin("0");
         	   }else{
         	       info.setPrevious_margin(lastDate.get(0).getMargin());
         	   }
            
            
            
//        	System.err.println("==============================>"+raceDetailsList.get(raceTablesList.indexOf(body)).getRaceDate());
        	 info.setCreatedDate(raceDetailsList.get(raceTablesList.indexOf(body)).getRaceDate());
        	 info.setHorseName(horsesName);
      		 //info.setTrainer(startPosition);
      		 info.setJockey(jockeyName); 
      		 info.setSource(RACE_URL);
      		
			if (finishValue != null) {
				try {
					info.setFinishPosition(new Integer(
							finishValue));
				} catch (Exception e) {
					// TODO Auto-generated catch
					// block
					log.error("Excepiton occured in parsing finish position"
							+ e.getMessage());
				}
			}
			if (startPosition != null) {
				try {
					info.setStartPosition(new Integer(
							startPosition));
				} catch (Exception e) {
					// TODO Auto-generated catch
					// block
					log.error("Excepiton occured in parsing start position"
							+ e.getMessage());
				}
			}
      		 info.setRace_name(raceDetailsList.get(raceTablesList.indexOf(body)).getRaceName());
      	
      		 info.setCity(raceDetailsList.get(raceTablesList.indexOf(body)).getCity());
    		 info.setState(raceDetailsList.get(raceTablesList.indexOf(body)).getState());
    		 info.setLast_margin_date(LocalDate.now());
    		 info.setStartingPrice(startingPrice);
    		 info.setRace_id(raceDetailsList.get(raceTablesList.indexOf(body)).getId());
    		 info.setNext_race_length(raceDetailsList
						.get(raceTablesList
								.indexOf(body))
						.getRace_length());
    		 
     		if(margin== null || margin.equalsIgnoreCase("")){
     			info.setMargin("0");
     		} else {
     			info.setMargin(margin);
     			raceInfoList.add(info);
        		raceInfoService.save(info);
                cityName = info.getCity();
     		}
        	});
        }); 
        raceService.findByState(cityName);
       
 					}
 					raceService.saveJson();
 					} catch (FileNotFoundException e1) {
 						// TODO Auto-generated catch block
 						e1.printStackTrace();
 					} catch (IOException e1) {
 						// TODO Auto-generated catch block
 						e1.printStackTrace();
 					}
     
     }
        }
     


