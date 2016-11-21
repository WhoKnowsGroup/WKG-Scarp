package com.webscrapper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webscrapper.domain.RaceInfo;
import com.webscrapper.service.RaceInfoService;
import com.webscrapper.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RaceInfo.
 */
@RestController
@RequestMapping("/api")
public class RaceInfoResource {

    private final Logger log = LoggerFactory.getLogger(RaceInfoResource.class);
        
    @Inject
    private RaceInfoService raceInfoService;

    /**
     * POST  /race-infos : Create a new raceInfo.
     *
     * @param raceInfo the raceInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new raceInfo, or with status 400 (Bad Request) if the raceInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/race-infos")
    @Timed
    public ResponseEntity<RaceInfo> createRaceInfo(@RequestBody RaceInfo raceInfo) throws URISyntaxException {
        log.debug("REST request to save RaceInfo : {}", raceInfo);
        if (raceInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("raceInfo", "idexists", "A new raceInfo cannot already have an ID")).body(null);
        }
        RaceInfo result = raceInfoService.save(raceInfo);
        return ResponseEntity.created(new URI("/api/race-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("raceInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /race-infos : Updates an existing raceInfo.
     *
     * @param raceInfo the raceInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated raceInfo,
     * or with status 400 (Bad Request) if the raceInfo is not valid,
     * or with status 500 (Internal Server Error) if the raceInfo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/race-infos")
    @Timed
    public ResponseEntity<RaceInfo> updateRaceInfo(@RequestBody RaceInfo raceInfo) throws URISyntaxException {
        log.debug("REST request to update RaceInfo : {}", raceInfo);
        if (raceInfo.getId() == null) {
            return createRaceInfo(raceInfo);
        }
        RaceInfo result = raceInfoService.save(raceInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("raceInfo", raceInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /race-infos : get all the raceInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of raceInfos in body
     */
    @GetMapping("/race-infos")
    @Timed
    public List<RaceInfo> getAllRaceInfos() {
        log.debug("REST request to get all RaceInfos");
        return raceInfoService.findAll();
    }

    /**
     * GET  /race-infos/:id : get the "id" raceInfo.
     *
     * @param id the id of the raceInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the raceInfo, or with status 404 (Not Found)
     */
    @GetMapping("/race-infos/{id}")
    @Timed
    public ResponseEntity<RaceInfo> getRaceInfo(@PathVariable Long id) {
        log.debug("REST request to get RaceInfo : {}", id);
        RaceInfo raceInfo = raceInfoService.findOne(id);
        return Optional.ofNullable(raceInfo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /race-infos/:id : delete the "id" raceInfo.
     *
     * @param id the id of the raceInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/race-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteRaceInfo(@PathVariable Long id) {
        log.debug("REST request to delete RaceInfo : {}", id);
        raceInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("raceInfo", id.toString())).build();
    }

}
