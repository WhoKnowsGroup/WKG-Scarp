package com.webscrapper.batch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
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
	
	private String RACE_URL;
	
	public RacingAustraliaSiteScrapper() {
		// TODO Auto-generated constructor stub
	}
	public RacingAustraliaSiteScrapper( RaceInfoService raceInfoService, RaceService raceService) {
		this.raceInfoService = raceInfoService;
		this.raceService = raceService;
	}
	 // all of this should come from property file for execution
    
	public void readWebsite(){
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
				  		
				  		String[] getting_date_from_url =  RACE_URL.split(",");
				        String[] getting_date = getting_date_from_url[0].split("=");     
				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMMd");
				        LocalDate raceDate= LocalDate.parse(getting_date[1].replaceAll("\\s+",""), formatter);
				  		
				  		Elements raceList = doc.select("table.race-title");
				  		String cityN = doc.select(".race-venue").select(".top").get(0).text();
				  		String cityName = cityN.substring(0, cityN.indexOf(":"));
				  		List<Race> raceDetailsList = new ArrayList<Race>();
				  		
				  		raceList.forEach(race ->{
				  			Race raceObj = new Race();
				  			String raceTitle = race.select("tbody > tr > th > span").first().text();
				  			
				  			Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(raceTitle);
		            		String next_race_length = "";
		            	    while(m.find()) {
		            	    	next_race_length = m.group(1);
		            	    }
		            	    
		            	    String nextRaceTime = raceTitle.substring(9, 15);
		            	    
				  			// retrieve the second tr
				  			Element raceDescription = race.select("tbody > tr ").get(1);
				  			log.info("Race description ==>"+raceDescription.text());
				  			
				  			String state = RACE_URL;
				  			state = state.substring(state.indexOf(",") + 1);
				  			state = state.substring(0, state.indexOf(","));
				  			System.err.println("Race state===========>" +state);
				  			
				  			raceObj.setCity(cityName);
				  			raceObj.setState(state);
				  			raceObj.setRaceDate(raceDate);
				  			raceObj.setRaceName(raceTitle);
				  			raceObj.setRaceDescription(raceDescription.text());
				  			raceObj.setRace_length(next_race_length);
				  			raceObj.setRace_time(nextRaceTime);
				  			raceObj = raceService.save(raceObj);
				  			raceDetailsList.add(raceObj);
				  		});
				        Elements raceTablesList = doc.select("table.race-strip-fields");
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
//				        		Elements cellValues = row.getAllElements();
				        		String finishValue = row.getElementsByIndexEquals(1).first().text();
				        		String startPosition = row.getElementsByIndexEquals(2).text();
				        		Element horsesName = row.getElementsByIndexEquals(3).first();
				        		
				        		if(horsesName.text().equalsIgnoreCase("horse")){
				        			// do nothing for header
				        		}else{
				        			Element trainerName = row.getElementsByIndexEquals(4).first();
				            		Element jockeyName = row.getElementsByIndexEquals(5).first();
				            		Element margin = row.getElementsByIndexEquals(6).get(0);
//				            		Element bar = row.getElementsByIndexEquals(7).get(0);
//				            		Element weight = row.getElementsByIndexEquals(8).get(0);
				            		Element penalty = row.getElementsByIndexEquals(9).get(0);
				            		Element startingPrice = row.getElementsByIndexEquals(10).get(0);
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
				            		info.setRace_name(raceDetailsList.get(raceTablesList.indexOf(raceTable)).getRaceName());
				            		info.setCity(raceDetailsList.get(raceTablesList.indexOf(raceTable)).getCity());
				            		info.setState(raceDetailsList.get(raceTablesList.indexOf(raceTable)).getState());
				            		info.setRace_id(raceDetailsList.get(raceTablesList.indexOf(raceTable)).getId());
				            		info.setNext_race_length(raceDetailsList.get(raceTablesList.indexOf(raceTable)).getRace_length());
				                    info.setNext_race_time(raceDetailsList.get(raceTablesList.indexOf(raceTable)).getRace_time());
				                    info.setNext_race_date(raceDetailsList.get(raceTablesList.indexOf(raceTable)).getRaceDate().toString());
				                    info.setSource(RACE_URL);
				                    info.setLast_margin_date(raceDate);
				            		//info.setRace(raceDetailsList.get(index));
				            		Long marginValue = null;
				            		if(margin.text()== null || margin.text().equalsIgnoreCase("")){

				            		} else {
//				            			log.info("Margin value ==>"+margin.text().trim().substring(0, 1));
				            			try {
				            				String marginn = margin.text();
				            				String marValue = marginn.substring(0, marginn.indexOf("L"));
				            				if(marValue.length() > 3) {
				            					info.setMargin("0");
				            				} else {
							            		info.setMargin(marValue);
					            				System.err.println("Margin value=================>"+marValue);
				            				}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											log.error("Error occured in processing margin value ==>"+marginValue);
										}
//				            			log.info("Margin value converted ==>"+margin.text());
				            		}
				            		info.setPenalty(penalty.text());
				            		info.setStartingPrice(startingPrice.text());
				            		raceInfoList.add(info);
				            		raceInfoService.save(info);
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
