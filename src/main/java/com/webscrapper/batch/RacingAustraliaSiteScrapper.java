package com.webscrapper.batch;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.webscrapper.domain.Race;
import com.webscrapper.domain.RaceInfo;
import com.webscrapper.service.RaceInfoService;
import com.webscrapper.service.RaceService;

@Component
public class RacingAustraliaSiteScrapper {

	private static final Logger log = LoggerFactory.getLogger(RacingAustraliaSiteScrapper.class);

	private RaceService raceService;
	
	private RaceInfoService raceInfoService;
	
	public RacingAustraliaSiteScrapper() {
		// TODO Auto-generated constructor stub
	}
	public RacingAustraliaSiteScrapper( RaceInfoService raceInfoService, RaceService raceService) {
		this.raceInfoService = raceInfoService;
		this.raceService = raceService;
	}
	 // all of this should come from property file for execution
    private static final String RACE_URL="http://www.racingaustralia.horse/FreeFields/Results.aspx?Key=2016Nov21,VIC,Sportsbet-Ballarat";
    
    private static final String city ="VIC";
    
    private static final String state="Sportsbet-Ballarat";
    
	public void readWebsite(){
		 Document doc=null;
	  		try {
	  			// http://www.racingaustralia.horse/FreeFields/Results.aspx?Key=2016Nov21,VIC,Sportsbet-Ballarat
	  			// http://www.racingaustralia.horse/FreeFields/Results.aspx?Key=2016Nov21,NSW,Wagga
	  			doc = Jsoup.connect(RACE_URL).get();
	  		} catch (IOException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		Elements raceList = doc.select("table.race-title");
	  		
	  		List<Race> raceDetailsList = new ArrayList<Race>();
	  		
	  		raceList.forEach(race ->{
	  			Race raceObj = new Race();
	  			Element raceTitle = race.select("tbody > tr > th > span").first();
	  			log.info("Race head ==>"+raceTitle);
	  			// retrieve the second tr
	  			Element raceDescription = race.select("tbody > tr ").get(1);
	  			log.info("Race description ==>"+raceDescription.text());
	  			raceObj.setCity(city);
	  			raceObj.setState(state);
	  			raceObj.setRaceDate(LocalDate.now());
	  			raceObj.setRaceName(raceTitle.text());
	  			raceObj.setRaceDescription(raceDescription.text().substring(0, 255));
	  			raceObj = raceService.save(raceObj);
	  			raceDetailsList.add(raceObj);
	  		});
	        Elements raceTablesList = doc.select("table.race-strip-fields");
	        int index = 0;
	        if(raceTablesList.size()==raceDetailsList.size()){
	        	log.info("********* ON TRACK - RACE TITLE MATCHES RACE DETAILS ***********");
	        }else{
	        	log.error("********** PROBLEM IN PARSING -- RACE TITLE COUNT DOES NOT MATCH RACE DETAILS" );
	        }
	        raceTablesList.forEach(raceTable -> {
	        	Elements raceInfoHeader = raceTable.getElementsByTag("tr");
	        	List<RaceInfo> raceInfoList = new ArrayList<RaceInfo>();
	        	
	        	raceInfoHeader.forEach(row -> {
	        		RaceInfo info = new RaceInfo();
	        		Elements cellValues = row.getAllElements();
	        		String finishValue = row.getElementsByIndexEquals(1).first().text();
	        		String startPosition = row.getElementsByIndexEquals(2).text();
	        		Element horsesName = row.getElementsByIndexEquals(3).first();
	        		
	        		if(horsesName.text().equalsIgnoreCase("horse")){
	        			// do nothing for header
	        		}else{
	        			Element trainerName = row.getElementsByIndexEquals(4).first();
	            		Element jockeyName = row.getElementsByIndexEquals(5).first();
	            		Element margin = row.getElementsByIndexEquals(6).get(0);
	            		Element bar = row.getElementsByIndexEquals(7).get(0);
	            		Element weight = row.getElementsByIndexEquals(8).get(0);
	            		Element penalty = row.getElementsByIndexEquals(9).get(0);
	            		Element startingPrice = row.getElementsByIndexEquals(10).get(0);
	            		log.info("Race row finish details ==> "+finishValue);
	            		log.info("Race row start ==> "+startPosition);
	            		log.info("Race row start ==> "+horsesName);
	            		log.info("Race row horse name ==>  "+horsesName.text());
	            		log.info("Race row margin name ==>  "+margin.text());
	            		log.info("Race row margin  ==>  "+margin);
	            		log.info("Race row bar name ==>  "+bar.text());
	            		log.info("Race row weight name ==>  "+weight.text());
	            		log.info("Race row penalty name ==>  "+penalty.text());
	            		log.info("Race row jockey name ==>  "+jockeyName.text());
	            		log.info("Race row starting pc name ==>  "+startingPrice.text());
	            		info.setCreatedDate(LocalDate.now());
	            		if(finishValue!=null){
	            			try {
	    						info.setFinishPosition(new Integer(finishValue));
	    					} catch (Exception e) {
	    						// TODO Auto-generated catch block
	    						log.error("Excepiton occured in parsing finish position" +e.getMessage());
	    					}
	            		}
	            		if(startPosition!=null){
	            			try {
	    						info.setStartPosition(new Integer(startPosition));
	    					} catch (Exception e) {
	    						// TODO Auto-generated catch block
	    						log.error("Excepiton occured in parsing start position" +e.getMessage());
	    					}
	            		}
	            		
	            		info.setHorseName(horsesName.text());
	            		info.setTrainer(trainerName.text());
	            		info.setJockey(jockeyName.text());
	            		//info.setRace(raceDetailsList.get(index));
	            		Long marginValue = null;
	            		if(margin.text()== null || margin.text().equalsIgnoreCase("")){
	            			marginValue = 0L;
	            		}else{
	            			log.info("Margin value ==>"+margin.text().trim().substring(0, 1));
	            			try {
								marginValue = Long.parseLong(margin.text().trim().substring(0, 1));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								log.error("Error occured in processing margin value ==>"+marginValue);
							}
	            			log.info("Margin value converted ==>"+margin.text());
	            		}
	            		info.setMargin(marginValue);
	            		info.setPenalty(penalty.text());
	            		info.setStartingPrice(startingPrice.text());
	            		raceInfoList.add(info);
	            		raceInfoService.save(info);
	        		}
	        	
	        		
	        	});
	        });
	}
}
