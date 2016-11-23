package com.webscrapper.batch;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.webscrapper.service.HorseService;
import com.webscrapper.service.RaceInfoService;
import com.webscrapper.service.RaceService;

@Component
public class RAHorseDataScrapper {

	private static final Logger log = LoggerFactory.getLogger(RAHorseDataScrapper.class);

	private HorseService horseService;
	
	// base url - http://www.racingnsw.com.au/default.aspx?s=search
	// search above url with horse names and come to this page
	
	private static final String HORSE_URL="http://racing.racingnsw.com.au/InteractiveForm/HorseFullForm.aspx?HorseCode=Mzk2MDUyOTM3Mjg%3d&src=horsesearch";
	
	public RAHorseDataScrapper() {
		// TODO Auto-generated constructor stub
	}
	public RAHorseDataScrapper( HorseService horseService) {
		this.horseService = horseService;
	}
	
	public void gatherHorseData(){
		Document doc=null;
  		try {
  			// http://www.racingaustralia.horse/FreeFields/Results.aspx?Key=2016Nov21,VIC,Sportsbet-Ballarat
  			// http://www.racingaustralia.horse/FreeFields/Results.aspx?Key=2016Nov21,NSW,Wagga
  			doc = Jsoup.connect(HORSE_URL).get();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		Elements raceList = doc.select("table.race-title");
  		
	}
}
