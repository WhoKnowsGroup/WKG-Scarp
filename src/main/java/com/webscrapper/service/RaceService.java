package com.webscrapper.service;

import com.webscrapper.domain.Race;

import java.util.List;

/**
 * Service Interface for managing Race.
 */
public interface RaceService {

    /**
     * Save a race.
     *
     * @param race the entity to save
     * @return the persisted entity
     */
    Race save(Race race);

    /**
     *  Get all the races.
     *  
     *  @return the list of entities
     */
    List<Race> findAll();

    /**
     *  Get the "id" race.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Race findOne(Long id);

    /**
     *  Delete the "id" race.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
