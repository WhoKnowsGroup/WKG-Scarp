package com.webscrapper.service;

import com.webscrapper.domain.Race;

import java.util.List;

import org.json.JSONException;

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
     * @throws JSONException 
     */
    List<Race> findAll() throws JSONException;

    /**
     *  Get the "id" race.
     *
     *  @param id the id of the entity
     *  @return the entity
     * @throws JSONException 
     */
    void findByState(String state) throws JSONException;

    /**
     *  Delete the "id" race.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    void saveJson();
}
