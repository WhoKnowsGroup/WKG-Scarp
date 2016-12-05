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

import javax.inject.Inject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public List<Race> findAll() throws JSONException {
        log.debug("Request to get all Races");
        List<Race> result = raceRepository.findAll();
        
        JSONArray raceJson = new JSONArray();
        JSONArray races = new JSONArray();
       
        
        for (Race race : result) {
           
           Long race_id = race.getId();
           List<RaceInfo> raceInfo = raceInfoService.findByRaceId(race_id);
//           System.out.println("Race Id"+race_id);
//           System.out.println("Race Id"+raceInfo.size());
           
          // JSONObject eachRace = new JSONObject();
           JSONObject eachRace = new JSONObject();
           eachRace.put("raceCourseName", race.getRaceName());
           eachRace.put("scheduledDate", race.getRaceDate());  
           eachRace.put("numberOfRaces",result.size());
           eachRace.put("state", race.getState());
           
          
           
//           JSONObject racesObject = new JSONObject();
                      
           JSONObject horsesObject = new JSONObject();
           JSONArray horses = new JSONArray();
           
           
           
           for (RaceInfo raceinfo : raceInfo) {
        	  
        	   JSONObject horse = new JSONObject();
        	   horse.put("horseName",raceinfo.getHorseName());
        	   horse.put("margin",raceinfo.getMargin());
        	   horse.put("trainer",raceinfo.getTrainer());
        	   horse.put("jockey",raceinfo.getJockey());
        	   horse.put("source",raceinfo.getSource());
        	  
        	   horses.put(horse);
           }
           
           horsesObject.put("raceNumber",race.getId());
           horsesObject.put("horses",horses);
           
           races.put(horsesObject);
           
           eachRace.put("races", races);
           
           raceJson.put(eachRace);
          
        }
       
         //eachRace.put("races", races);
         //raceJson.put(eachRace);
//        System.out.println("races"+raceJson);
		
     
          try {
				FileWriter file = new FileWriter("D:/file.txt");
				file.write(raceJson.toString());
				System.err.println("File created!");
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Successfully Copied JSON Object to File...");

        return result;
    }
    
    
    
    @Transactional(readOnly = true) 
    public void findByState(String state) throws JSONException {
        log.debug("Request to get Race in service : {}", state);
        List<Race> raceByState = raceRepository.findByState(state);
        
        
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
          	 
          	   List<RaceInfo> lastTenraces = raceinfoRepository.findByLastTenRaces(raceinfo.getHorseName());
          	   List<RaceInfo> lastDate = raceinfoRepository.lastRaceDate(raceinfo.getHorseName());
          	   horse.put("lastRaceDate", lastDate.get(0).getCreatedDate());

          	         System.err.println("Margin"+raceinfo.getMargin());
		          	 /*for (String name : horseNames) {
		          		 
		          		 if(name.equals(raceinfo.getHorseName())){
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
        
             try {
            	    String fileLocation = "D:/"+state +".txt";
    				FileWriter file = new FileWriter(fileLocation);
    				file.write(mainObject.toString());
    				System.err.println("File created!");
    				file.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
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
