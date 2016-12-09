package com.webscrapper.service.impl;

import com.webscrapper.service.RaceInfoService;
import com.webscrapper.service.RaceService;
import com.webscrapper.domain.Race;
import com.webscrapper.domain.RaceInfo;
import com.webscrapper.repository.RaceInfoRepository;
import com.webscrapper.repository.RaceRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import io.advantageous.boon.core.Sys;

import javax.inject.Inject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Service Implementation for managing Race.
 */
@Service
@Transactional
public class RaceServiceImpl implements RaceService{

    private final Logger log = LoggerFactory.getLogger(RaceServiceImpl.class);
    
    @Inject
    private RaceRepository raceRepository;
    
    @Inject
    private RaceInfoService raceInfoService;
    
    @Inject
    private RaceInfoRepository raceinfoRepository;
    
    JSONArray fullJson = new JSONArray();
	
//    List<String> horseNames = getNextDayHorseName();

    /**
     * Save a race.
     *
     * @param race the entity to save
     * @return the persisted entity
     */
    public Race save(Race race) {
        log.debug("Request to save Race : {}", race);
        Race result = raceRepository.save(race);
        return result;
    }

    /**
     *  Get all the races.
     *  
     *  @return the list of entities
     * @throws JSONException 
     */
    @Transactional(readOnly = true) 
    public void findAll() throws JSONException {
        log.debug("Request to get all Races");
        List<Race> all_race_result = raceRepository.findAll();
        System.err.println("total size"+all_race_result.size());
        JSONObject mainObject = new JSONObject();
        JSONArray racesArray = new JSONArray();
        
        Map<String, List<Race>> test = new HashMap<String, List<Race>>();
        for(Race single_race :all_race_result){
        	
        	  mainObject.put("raceCourseName",single_race.getCity());
              mainObject.put("scheduledDate", single_race.getRaceDate());  
              //mainObject.put("numberOfRaces",raceByState.size());
              mainObject.put("state",single_race.getState() );
              
              StringBuilder result = new StringBuilder();
              result.append(single_race.getState());
              result.append(",");
              result.append(single_race.getCity());
              result.append(",");
              result.append(single_race.getRaceDate());
              mainObject.put("id",result);
              
              
              
            /*  for (Race race : raceByState) {*/
              	
              	 Long race_id = single_race.getId();
              	 List<RaceInfo> raceInfo = raceInfoService.findByRaceId(race_id);
              	 
              	 
              	 JSONArray horses = new JSONArray();
              	 
              	 for (RaceInfo raceinfo : raceInfo) {
                	   JSONObject horse = new JSONObject();
                	   horse.put("horseName",raceinfo.getHorseName());
                	   
                	   horse.put("margin",raceinfo.getMargin());
                	   horse.put("trainer",raceinfo.getTrainer());
                	   horse.put("jockey",raceinfo.getJockey());
                	   horse.put("previousRace",raceinfo.getSource().toString());
                     horse.put("previousMargin",raceinfo.getPrevious_margin());
                	 
                	   List<RaceInfo> lastTenraces = raceinfoRepository.findByLastTenRaces(raceinfo.getHorseName());
                	   List<RaceInfo> lastDate = raceinfoRepository.lastRaceDate(raceinfo.getHorseName());
              	   
                	   horse.put("lastRaceDate", lastDate.get(0).getCreatedDate());
            	          	   
                	   StringBuilder position = new StringBuilder();
                	   for (RaceInfo lastTenrace : lastTenraces) {
                		     if(lastTenrace.getFinishPosition() == null){
                		    	position.append("x");
                	          }else{  
                	        	position.append(lastTenrace.getFinishPosition());
                	          }
                	   } 
                	   
                     horse.put("lastTenRaces",position);
                	   horses.put(horse);
                   }
              	 
              	 
              	 
              	
               	 JSONObject raceObject = new JSONObject();
                 
               	 //raceObject.put("raceNumber",raceByState.indexOf(race)+1);
               	 raceObject.put("horses",horses);
              	
               	 racesArray.put(raceObject);
              /*}*/
             
              mainObject.put("races",racesArray );
              
        	
        }
      
        System.err.println("races array"+mainObject);
        
           /*  try {
            	    String fileLocation = "D:/"+state +".txt";
    				FileWriter file = new FileWriter(fileLocation);
    				file.write(mainObject.toString());
    				System.err.println("File created!");
    				file.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}*/
//    			System.out.println("Successfully Copied JSON Object to File...");
        fullJson.put(mainObject);
        
        
        
        return;
    }
    
    
    
    
    
    
    @Transactional(readOnly = true) 
    public void findByState(String state) throws JSONException {
        log.debug("Request to get Race in service : {}", state);
        List<Race> raceByState = raceRepository.findByState(state);
        
        System.err.println("ize=============>"+raceByState.size());
//        JSONArray mainArray = new JSONArray();
        JSONObject mainObject = new JSONObject();
        JSONArray racesArray = new JSONArray();
        
        mainObject.put("raceCourseName",state);
        mainObject.put("scheduledDate", raceByState.get(0).getRaceDate());  
        mainObject.put("numberOfRaces",raceByState.size());
        mainObject.put("state",raceByState.get(0).getState() );
        
        StringBuilder result = new StringBuilder();
        result.append(raceByState.get(0).getState());
        result.append(",");
        result.append(state);
        result.append(",");
        result.append(raceByState.get(0).getRaceDate());
        mainObject.put("id",result);
        
        
        
        for (Race race : raceByState) {
        	
        	 Long race_id = race.getId();
        	 List<RaceInfo> raceInfo = raceInfoService.findByRaceId(race_id);
        	 
        	 
        	 JSONArray horses = new JSONArray();
        	 
        	 for (RaceInfo raceinfo : raceInfo) {
          	   JSONObject horse = new JSONObject();
          	   horse.put("horseName",raceinfo.getHorseName());
          	   
          	   horse.put("margin",raceinfo.getMargin());
          	   horse.put("trainer",raceinfo.getTrainer());
          	   horse.put("jockey",raceinfo.getJockey());
          	   horse.put("previousRace",raceinfo.getSource().toString());
               horse.put("previousMargin",raceinfo.getPrevious_margin());
          	 
          	   List<RaceInfo> lastTenraces = raceinfoRepository.findByLastTenRaces(raceinfo.getHorseName());
          	   List<RaceInfo> lastDate = raceinfoRepository.lastRaceDate(raceinfo.getHorseName());
          	   
          	  // horse.put("previousmargin",lastTenraces.get(0).getMargin());
          	   
           	 /*  if(lastDate.size() <= 1){
          		 horse.put("previousMargin","0");
          	   }else{
          	       System.err.println("====>in construction f previous marfgni"+lastTenraces.get(1).getMargin());
          		   horse.put("previousMargin",lastTenraces.get(1).getMargin());
          	   }*/
          	   
          	   horse.put("lastRaceDate", lastDate.get(0).getCreatedDate());

          	         System.err.println("Margin"+raceinfo.getMargin());
		          	 /*for (String name : horseNames) {
		          		 
		          		 if(name.equals(raceinfo.getHorseName())){0
		          			horse.put("previousMargin",lastTenraces.get(0).getMargin());
		          			break;
		          		 }else{ 
		          			horse.put("previousMargin","0");
		          		 }	 
		          	 }*/
          	   
          	   
          	   StringBuilder position = new StringBuilder();
          	   for (RaceInfo lastTenrace : lastTenraces) {
          		     if(lastTenrace.getFinishPosition() == null){
          		    	position.append("x");
          	          }else{  
          	        	position.append(lastTenrace.getFinishPosition());
          	          }
          	   } 
          	   
               horse.put("lastTenRaces",position);
          	   horses.put(horse);
             }
        	 
        	 
        	 
        	
         	 JSONObject raceObject = new JSONObject();
           
         	 raceObject.put("raceNumber",raceByState.indexOf(race)+1);
         	 raceObject.put("horses",horses);
        	
         	 racesArray.put(raceObject);
        }
       
        mainObject.put("races",racesArray );
        
        //System.out.println("races array"+mainObject);
        
             /*try {
            	    String fileLocation = "D:/"+state +".txt";
    				FileWriter file = new FileWriter(fileLocation);
    				file.write(mainObject.toString());
    				System.err.println("File created!");
    				file.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}*/
//    			System.out.println("Successfully Copied JSON Object to File...");
        fullJson.put(mainObject);
        
        
        
        return;
    }

    /**
     *  Get one race by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Race findOne(Long id) {
        log.debug("Request to get Race : {}", id);
        Race race = raceRepository.findOne(id);
        return race;
    }
    
    
 

    /**
     *  Delete the  race by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Race : {}", id);
        raceRepository.delete(id);
    }
    
    public void saveJson(){
        
	     try {
	            String fileLocation = "D:/JSON.txt";
	      FileWriter file = new FileWriter(fileLocation);
	      file.write(fullJson.toString());
	      System.err.println("File created!");
	      file.close();
	     } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	     }
	        System.err.println(" json constructed.......");
        
       }
    
    public List<String> getNextDayHorseName() {
    	System.err.println("Inside gettng horse details.");
		Properties prop = new Properties();
		Document doc = null;
  		List<String> horseNames = new ArrayList<String>();
		try {
			prop.load(new FileInputStream("D:/properties/url.properties"));
			String[] values = prop.getProperty("site.nextDayUrl").toString().split(";");
					for(int k = 0; k < values.length; k++) {
						String RACE_URL = values[k];
				  		try {
					  		doc = Jsoup.connect(RACE_URL).get();
				  		} catch (IOException e) {
				  			// TODO Auto-generated catch block
				  			e.printStackTrace();
				  		}
				  		try {
							Document doc1 = Jsoup.connect(RACE_URL).get();
				  			Elements raceTablesList = doc1.select("table.race-strip-fields");
				  			raceTablesList.forEach(raceTable -> {
					        	Elements raceInfoHeader = raceTable.getElementsByClass("horse");
					        	
					        	raceInfoHeader.forEach(row -> {
					        		String horsesName = row.getElementsByIndexEquals(2).first().text();
//					        		System.err.println("Horse name===============>" +horsesName);
					        		horseNames.add(horsesName);
					        		
					        	});
					        });
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				  		
					}
		
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return horseNames;
		
		
	}

	
}
