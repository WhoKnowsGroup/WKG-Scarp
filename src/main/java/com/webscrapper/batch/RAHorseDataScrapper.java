package com.webscrapper.batch;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.webscrapper.domain.Horse;
import com.webscrapper.service.HorseService;

@Component
public class RAHorseDataScrapper {

	private static final Logger log = LoggerFactory.getLogger(RAHorseDataScrapper.class);

	private HorseService horseService;
	
	// base url - http://www.racingnsw.com.au/default.aspx?s=search
	// search above url with horse names and come to this page
	
	private static final String HORSE_URL="http://racing.racingnsw.com.au/InteractiveForm/HorseFullForm.aspx?HorseCode=NzQwOTE1MTc1MA%3d%3d&src=horsesearch";
	
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
  		
  		Element horseDetailsTop = doc.select("table.horse-search-details").get(0);
  		Element horseDetailsBottom = doc.select("table.horse-search-details").get(1);
  		
  		System.out.println("========>1"+horseDetailsTop.toString());
  		/*System.out.println("========>2"+horseDetailsBottom.toString());*/
  		
  		Elements HorseTitlePart = horseDetailsTop.select("tbody > tr > td");
  		System.out.println("HorseTitlePart"+HorseTitlePart.text());
  		
  		//horse name
  		Element HorseNamePart= 	HorseTitlePart.get(1).select("h2.first").first();
  		//dob -todo
  		String[] dob =HorseTitlePart.get(1).select("br").get(0).nextSibling().toString().split(":");
 
  		//status
  		String active = horseDetailsBottom.select("tbody > tr").get(0).select("td > strong").text();
        //owner
  		String owner = horseDetailsBottom.select("tbody > tr").get(1).select("td > strong").text();
  		//Stewards 
  		String Stewards  = horseDetailsBottom.select("tbody > tr").get(2).select("td").text();
  		//emergency_vaccination_record_url
  		String emergency_vaccination_record_url=horseDetailsBottom.select("tbody > tr").get(3).select("td > a").attr("href");
  		//last gear change -todo 
  		String last_gear_change=horseDetailsBottom.select("tbody > tr").get(4).select("td").get(0).text().split(",")[0];
  		//TRINAER
  		String trainer = horseDetailsBottom.select("tbody > tr").get(5).select("td > a").text();
  		//prize
  		String prize = horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(1).nextSibling().toString();
        //bonus
  		String bonus= horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(2).nextSibling().toString();
  		//max-min
  		String maxmin = horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(6).nextSibling().toString();
  		//1st up
  		String firststup =horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(7).nextSibling().toString();
  		//2nd up
  		String secondup =horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(8).nextSibling().toString();
  		//firm
  		String firm =horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(9).nextSibling().toString();
  		//good
  		String good =horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(10).nextSibling().toString();
  		//soft
  		String soft =horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(11).nextSibling().toString();
  		//heavy
  		String heavy =horseDetailsBottom.select("tbody > tr").get(7).select("td > b").get(12).nextSibling().toString();
  		
  		
  		
  		
  		Horse horseObject=new Horse();
  		
  		horseObject.setHorseName(HorseNamePart.text());
  		//dob here====todo
  		horseObject.setHorseStatus(active);
  		horseObject.setOwner(owner);
  		horseObject.setStewardsEmbargoes(Stewards);
  		horseObject.setEmergencyVaccinationRecordURL(emergency_vaccination_record_url);
        //last gear change date==todo
  		horseObject.setTrainer(trainer);
  		horseObject.setMimMaxDistWin(maxmin);
  		horseObject.setFirstUpData(firststup);
  		horseObject.setSecondUpData(secondup);
  		horseObject.setFirm(firm);
  		horseObject.setGood(good);
  		horseObject.setSoft(soft);
  		horseObject.setHeavy(heavy);
  		horseService.save(horseObject);
  		
  		
	}
}
