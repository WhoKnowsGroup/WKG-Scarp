package com.webscrapper.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.webscrapper.service.RaceInfoService;
import com.webscrapper.service.RaceService;

/**
 * This class is a spring scheduler component that will run exactly at 10pm in the night.
 * It will check for all the journals that have been published since a day before and will 
 * collect the details and compile and send an email to all the users in the system
 * 
 * It is assumed that all user do have an email address
 * @author venka
 *
 */
@Component("scheduleTask")
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Inject
    private RaceInfoService service;
    
    @Inject
    private RaceService raceService;
    
    /**
     * Note the cron rejex to schedule to execute at 10 pm
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void sendDailyJournalSummaryEmail() {
        log.info("The scheduled task to send email notificaitons - begins @ ", dateFormat.format(new Date()));

        RacingAustraliaSiteScrapper scrap =new RacingAustraliaSiteScrapper(service, raceService);
        scrap.readWebsite();
       
        log.info("The scheduled task to send email notificaitons - ends @ ", dateFormat.format(new Date()));
    }
}