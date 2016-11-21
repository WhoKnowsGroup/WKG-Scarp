package com.webscrapper.web.rest;

import com.webscrapper.WebScrapperApp;

import com.webscrapper.domain.Race;
import com.webscrapper.repository.RaceRepository;
import com.webscrapper.service.RaceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RaceResource REST controller.
 *
 * @see RaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebScrapperApp.class)
public class RaceResourceIntTest {

    private static final String DEFAULT_RACE_NAME = "AAAAA";
    private static final String UPDATED_RACE_NAME = "BBBBB";

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";

    private static final String DEFAULT_RACE_DESCRIPTION = "AAAAA";
    private static final String UPDATED_RACE_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    private static final LocalDate DEFAULT_RACE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RACE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private RaceRepository raceRepository;

    @Inject
    private RaceService raceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRaceMockMvc;

    private Race race;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceResource raceResource = new RaceResource();
        ReflectionTestUtils.setField(raceResource, "raceService", raceService);
        this.restRaceMockMvc = MockMvcBuilders.standaloneSetup(raceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Race createEntity(EntityManager em) {
        Race race = new Race()
                .raceName(DEFAULT_RACE_NAME)
                .city(DEFAULT_CITY)
                .raceDescription(DEFAULT_RACE_DESCRIPTION)
                .state(DEFAULT_STATE)
                .raceDate(DEFAULT_RACE_DATE);
        return race;
    }

    @Before
    public void initTest() {
        race = createEntity(em);
    }

    @Test
    @Transactional
    public void createRace() throws Exception {
        int databaseSizeBeforeCreate = raceRepository.findAll().size();

        // Create the Race

        restRaceMockMvc.perform(post("/api/races")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(race)))
                .andExpect(status().isCreated());

        // Validate the Race in the database
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeCreate + 1);
        Race testRace = races.get(races.size() - 1);
        assertThat(testRace.getRaceName()).isEqualTo(DEFAULT_RACE_NAME);
        assertThat(testRace.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testRace.getRaceDescription()).isEqualTo(DEFAULT_RACE_DESCRIPTION);
        assertThat(testRace.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testRace.getRaceDate()).isEqualTo(DEFAULT_RACE_DATE);
    }

    @Test
    @Transactional
    public void getAllRaces() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        // Get all the races
        restRaceMockMvc.perform(get("/api/races?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(race.getId().intValue())))
                .andExpect(jsonPath("$.[*].raceName").value(hasItem(DEFAULT_RACE_NAME.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].raceDescription").value(hasItem(DEFAULT_RACE_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].raceDate").value(hasItem(DEFAULT_RACE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRace() throws Exception {
        // Initialize the database
        raceRepository.saveAndFlush(race);

        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", race.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(race.getId().intValue()))
            .andExpect(jsonPath("$.raceName").value(DEFAULT_RACE_NAME.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.raceDescription").value(DEFAULT_RACE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.raceDate").value(DEFAULT_RACE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRace() throws Exception {
        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRace() throws Exception {
        // Initialize the database
        raceService.save(race);

        int databaseSizeBeforeUpdate = raceRepository.findAll().size();

        // Update the race
        Race updatedRace = raceRepository.findOne(race.getId());
        updatedRace
                .raceName(UPDATED_RACE_NAME)
                .city(UPDATED_CITY)
                .raceDescription(UPDATED_RACE_DESCRIPTION)
                .state(UPDATED_STATE)
                .raceDate(UPDATED_RACE_DATE);

        restRaceMockMvc.perform(put("/api/races")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRace)))
                .andExpect(status().isOk());

        // Validate the Race in the database
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeUpdate);
        Race testRace = races.get(races.size() - 1);
        assertThat(testRace.getRaceName()).isEqualTo(UPDATED_RACE_NAME);
        assertThat(testRace.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testRace.getRaceDescription()).isEqualTo(UPDATED_RACE_DESCRIPTION);
        assertThat(testRace.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRace.getRaceDate()).isEqualTo(UPDATED_RACE_DATE);
    }

    @Test
    @Transactional
    public void deleteRace() throws Exception {
        // Initialize the database
        raceService.save(race);

        int databaseSizeBeforeDelete = raceRepository.findAll().size();

        // Get the race
        restRaceMockMvc.perform(delete("/api/races/{id}", race.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeDelete - 1);
    }
}
