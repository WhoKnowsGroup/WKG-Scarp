package com.webscrapper.service.impl;

import com.webscrapper.service.RaceInfoService;
import com.webscrapper.domain.RaceInfo;
import com.webscrapper.repository.RaceInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing RaceInfo.
 */
@Service
@Transactional
public class RaceInfoServiceImpl implements RaceInfoService{

    private final Logger log = LoggerFactory.getLogger(RaceInfoServiceImpl.class);
    
    @Inject
    private RaceInfoRepository raceInfoRepository;

    /**
     * Save a raceInfo.
     *
     * @param raceInfo the entity to save
     * @return the persisted entity
     */
    public RaceInfo save(RaceInfo raceInfo) {
        log.debug("Request to save RaceInfo : {}", raceInfo);
        RaceInfo result = raceInfoRepository.save(raceInfo);
        return result;
    }

    /**
     *  Get all the raceInfos.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RaceInfo> findAll() {
        log.debug("Request to get all RaceInfos");
        List<RaceInfo> result = raceInfoRepository.findAll();

        return result;
    }

    /**
     *  Get one raceInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RaceInfo findOne(Long id) {
        log.debug("Request to get RaceInfo : {}", id);
        RaceInfo raceInfo = raceInfoRepository.findOne(id);
        return raceInfo;
    }

    /**
     *  Delete the  raceInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RaceInfo : {}", id);
        raceInfoRepository.delete(id);
    }
}
