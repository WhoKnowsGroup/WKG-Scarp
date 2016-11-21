package com.webscrapper.service.impl;

import com.webscrapper.service.RaceService;
import com.webscrapper.domain.Race;
import com.webscrapper.repository.RaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Race.
 */
@Service
@Transactional
public class RaceServiceImpl implements RaceService{

    private final Logger log = LoggerFactory.getLogger(RaceServiceImpl.class);
    
    @Inject
    private RaceRepository raceRepository;

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
     */
    @Transactional(readOnly = true) 
    public List<Race> findAll() {
        log.debug("Request to get all Races");
        List<Race> result = raceRepository.findAll();

        return result;
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
}
