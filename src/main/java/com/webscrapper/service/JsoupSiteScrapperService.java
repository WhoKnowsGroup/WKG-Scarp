package com.webscrapper.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JsoupSiteScrapperService {

	private final Logger log = LoggerFactory.getLogger(JsoupSiteScrapperService.class);

	public void scrapSite(){
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.sportsbet.com.au/next-races?LeftNav").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements newsHeadlines = doc.select("#a1-body");
		log.debug("HTML ==> "+newsHeadlines.html());
	}
	
}
