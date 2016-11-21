package com.webscrapper.service;

import com.webscrapper.domain.RaceInfo;

import java.util.List;

/**
 * Service Interface for managing RaceInfo.
 */
public interface RaceInfoService {

    /**
     * Save a raceInfo.
     *
     * @param raceInfo the entity to save
     * @return the persisted entity
     */
    RaceInfo save(RaceInfo raceInfo);

    /**
     *  Get all the raceInfos.
     *  
     *  @return the list of entities
     */
    List<RaceInfo> findAll();

    /**
     *  Get the "id" raceInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RaceInfo findOne(Long id);

    /**
     *  Delete the "id" raceInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
