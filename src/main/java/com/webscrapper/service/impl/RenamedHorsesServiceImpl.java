package com.webscrapper.service.impl;

import com.webscrapper.service.RenamedHorsesService;
import com.webscrapper.domain.RenamedHorses;
import com.webscrapper.repository.RenamedHorsesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing RenamedHorses.
 */
@Service
@Transactional
public class RenamedHorsesServiceImpl implements RenamedHorsesService{

    private final Logger log = LoggerFactory.getLogger(RenamedHorsesServiceImpl.class);
    
    @Inject
    private RenamedHorsesRepository renamedHorsesRepository;

    /**
     * Save a renamedHorses.
     *
     * @param renamedHorses the entity to save
     * @return the persisted entity
     */
    public RenamedHorses save(RenamedHorses renamedHorses) {
        log.debug("Request to save RenamedHorses : {}", renamedHorses);
        RenamedHorses result = renamedHorsesRepository.save(renamedHorses);
        return result;
    }

    /**
     *  Get all the renamedHorses.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RenamedHorses> findAll() {
        log.debug("Request to get all RenamedHorses");
        List<RenamedHorses> result = renamedHorsesRepository.findAll();

        return result;
    }

    /**
     *  Get one renamedHorses by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RenamedHorses findOne(Long id) {
        log.debug("Request to get RenamedHorses : {}", id);
        RenamedHorses renamedHorses = renamedHorsesRepository.findOne(id);
        return renamedHorses;
    }

    /**
     *  Delete the  renamedHorses by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RenamedHorses : {}", id);
        renamedHorsesRepository.delete(id);
    }
}
