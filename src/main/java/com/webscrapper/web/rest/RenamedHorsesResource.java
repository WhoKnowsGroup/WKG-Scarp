package com.webscrapper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webscrapper.domain.RenamedHorses;
import com.webscrapper.service.RenamedHorsesService;
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
 * REST controller for managing RenamedHorses.
 */
@RestController
@RequestMapping("/api")
public class RenamedHorsesResource {

    private final Logger log = LoggerFactory.getLogger(RenamedHorsesResource.class);
        
    @Inject
    private RenamedHorsesService renamedHorsesService;

    /**
     * POST  /renamed-horses : Create a new renamedHorses.
     *
     * @param renamedHorses the renamedHorses to create
     * @return the ResponseEntity with status 201 (Created) and with body the new renamedHorses, or with status 400 (Bad Request) if the renamedHorses has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/renamed-horses")
    @Timed
    public ResponseEntity<RenamedHorses> createRenamedHorses(@RequestBody RenamedHorses renamedHorses) throws URISyntaxException {
        log.debug("REST request to save RenamedHorses : {}", renamedHorses);
        if (renamedHorses.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("renamedHorses", "idexists", "A new renamedHorses cannot already have an ID")).body(null);
        }
        RenamedHorses result = renamedHorsesService.save(renamedHorses);
        return ResponseEntity.created(new URI("/api/renamed-horses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("renamedHorses", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /renamed-horses : Updates an existing renamedHorses.
     *
     * @param renamedHorses the renamedHorses to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated renamedHorses,
     * or with status 400 (Bad Request) if the renamedHorses is not valid,
     * or with status 500 (Internal Server Error) if the renamedHorses couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/renamed-horses")
    @Timed
    public ResponseEntity<RenamedHorses> updateRenamedHorses(@RequestBody RenamedHorses renamedHorses) throws URISyntaxException {
        log.debug("REST request to update RenamedHorses : {}", renamedHorses);
        if (renamedHorses.getId() == null) {
            return createRenamedHorses(renamedHorses);
        }
        RenamedHorses result = renamedHorsesService.save(renamedHorses);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("renamedHorses", renamedHorses.getId().toString()))
            .body(result);
    }

    /**
     * GET  /renamed-horses : get all the renamedHorses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of renamedHorses in body
     */
    @GetMapping("/renamed-horses")
    @Timed
    public List<RenamedHorses> getAllRenamedHorses() {
        log.debug("REST request to get all RenamedHorses");
        return renamedHorsesService.findAll();
    }

    /**
     * GET  /renamed-horses/:id : get the "id" renamedHorses.
     *
     * @param id the id of the renamedHorses to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the renamedHorses, or with status 404 (Not Found)
     */
    @GetMapping("/renamed-horses/{id}")
    @Timed
    public ResponseEntity<RenamedHorses> getRenamedHorses(@PathVariable Long id) {
        log.debug("REST request to get RenamedHorses : {}", id);
        RenamedHorses renamedHorses = renamedHorsesService.findOne(id);
        return Optional.ofNullable(renamedHorses)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /renamed-horses/:id : delete the "id" renamedHorses.
     *
     * @param id the id of the renamedHorses to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/renamed-horses/{id}")
    @Timed
    public ResponseEntity<Void> deleteRenamedHorses(@PathVariable Long id) {
        log.debug("REST request to delete RenamedHorses : {}", id);
        renamedHorsesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("renamedHorses", id.toString())).build();
    }

}
