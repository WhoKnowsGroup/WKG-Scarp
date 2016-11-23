package com.webscrapper.service;

import com.webscrapper.domain.RenamedHorses;

import java.util.List;

/**
 * Service Interface for managing RenamedHorses.
 */
public interface RenamedHorsesService {

    /**
     * Save a renamedHorses.
     *
     * @param renamedHorses the entity to save
     * @return the persisted entity
     */
    RenamedHorses save(RenamedHorses renamedHorses);

    /**
     *  Get all the renamedHorses.
     *  
     *  @return the list of entities
     */
    List<RenamedHorses> findAll();

    /**
     *  Get the "id" renamedHorses.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RenamedHorses findOne(Long id);

    /**
     *  Delete the "id" renamedHorses.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
