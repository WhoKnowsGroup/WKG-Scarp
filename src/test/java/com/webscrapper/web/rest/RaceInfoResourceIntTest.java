package com.webscrapper.web.rest;

import com.webscrapper.WebScrapperApp;

import com.webscrapper.domain.RaceInfo;
import com.webscrapper.repository.RaceInfoRepository;
import com.webscrapper.service.RaceInfoService;

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
 * Test class for the RaceInfoResource REST controller.
 *
 * @see RaceInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebScrapperApp.class)
public class RaceInfoResourceIntTest {

    private static final String DEFAULT_RACE_NAME = "AAAAA";
    private static final String UPDATED_RACE_NAME = "BBBBB";

    private static final String DEFAULT_RACE_DESCRIPTION = "AAAAA";
    private static final String UPDATED_RACE_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";

    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    private static final Integer DEFAULT_START_POSITION = 1;
    private static final Integer UPDATED_START_POSITION = 2;

    private static final Integer DEFAULT_FINISH_POSITION = 1;
    private static final Integer UPDATED_FINISH_POSITION = 2;

    private static final String DEFAULT_HORSE_NAME = "AAAAA";
    private static final String UPDATED_HORSE_NAME = "BBBBB";

    private static final String DEFAULT_TRAINER = "AAAAA";
    private static final String UPDATED_TRAINER = "BBBBB";

    private static final String DEFAULT_JOCKEY = "AAAAA";
    private static final String UPDATED_JOCKEY = "BBBBB";

    private static final String DEFAULT_MARGIN = "1L";
    private static final String UPDATED_MARGIN = "2L";

    private static final String DEFAULT_PENALTY = "AAAAA";
    private static final String UPDATED_PENALTY = "BBBBB";

    private static final String DEFAULT_STARTING_PRICE = "AAAAA";
    private static final String UPDATED_STARTING_PRICE = "BBBBB";

    @Inject
    private RaceInfoRepository raceInfoRepository;

    @Inject
    private RaceInfoService raceInfoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRaceInfoMockMvc;

    private RaceInfo raceInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceInfoResource raceInfoResource = new RaceInfoResource();
        ReflectionTestUtils.setField(raceInfoResource, "raceInfoService", raceInfoService);
        this.restRaceInfoMockMvc = MockMvcBuilders.standaloneSetup(raceInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaceInfo createEntity(EntityManager em) {
        RaceInfo raceInfo = new RaceInfo()
                .createdDate(DEFAULT_CREATED_DATE)
                .startPosition(DEFAULT_START_POSITION)
                .finishPosition(DEFAULT_FINISH_POSITION)
                .horseName(DEFAULT_HORSE_NAME)
                .trainer(DEFAULT_TRAINER)
                .jockey(DEFAULT_JOCKEY)
                .margin(DEFAULT_MARGIN)
                .penalty(DEFAULT_PENALTY)
                .startingPrice(DEFAULT_STARTING_PRICE);
        return raceInfo;
    }

    @Before
    public void initTest() {
        raceInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createRaceInfo() throws Exception {
        int databaseSizeBeforeCreate = raceInfoRepository.findAll().size();

        // Create the RaceInfo

        restRaceInfoMockMvc.perform(post("/api/race-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceInfo)))
                .andExpect(status().isCreated());

        // Validate the RaceInfo in the database
        List<RaceInfo> raceInfos = raceInfoRepository.findAll();
        assertThat(raceInfos).hasSize(databaseSizeBeforeCreate + 1);
        RaceInfo testRaceInfo = raceInfos.get(raceInfos.size() - 1);
        assertThat(testRaceInfo.getStartPosition()).isEqualTo(DEFAULT_START_POSITION);
        assertThat(testRaceInfo.getFinishPosition()).isEqualTo(DEFAULT_FINISH_POSITION);
        assertThat(testRaceInfo.getHorseName()).isEqualTo(DEFAULT_HORSE_NAME);
        assertThat(testRaceInfo.getTrainer()).isEqualTo(DEFAULT_TRAINER);
        assertThat(testRaceInfo.getJockey()).isEqualTo(DEFAULT_JOCKEY);
        assertThat(testRaceInfo.getMargin()).isEqualTo(DEFAULT_MARGIN);
        assertThat(testRaceInfo.getPenalty()).isEqualTo(DEFAULT_PENALTY);
        assertThat(testRaceInfo.getStartingPrice()).isEqualTo(DEFAULT_STARTING_PRICE);
    }

    @Test
    @Transactional
    public void getAllRaceInfos() throws Exception {
        // Initialize the database
        raceInfoRepository.saveAndFlush(raceInfo);

        // Get all the raceInfos
        restRaceInfoMockMvc.perform(get("/api/race-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(raceInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].raceName").value(hasItem(DEFAULT_RACE_NAME.toString())))
                .andExpect(jsonPath("$.[*].raceDescription").value(hasItem(DEFAULT_RACE_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].startPosition").value(hasItem(DEFAULT_START_POSITION)))
                .andExpect(jsonPath("$.[*].finishPosition").value(hasItem(DEFAULT_FINISH_POSITION)))
                .andExpect(jsonPath("$.[*].horseName").value(hasItem(DEFAULT_HORSE_NAME.toString())))
                .andExpect(jsonPath("$.[*].trainer").value(hasItem(DEFAULT_TRAINER.toString())))
                .andExpect(jsonPath("$.[*].jockey").value(hasItem(DEFAULT_JOCKEY.toString())))
                .andExpect(jsonPath("$.[*].margin").value(hasItem(DEFAULT_MARGIN.toString())))
                .andExpect(jsonPath("$.[*].penalty").value(hasItem(DEFAULT_PENALTY.toString())))
                .andExpect(jsonPath("$.[*].startingPrice").value(hasItem(DEFAULT_STARTING_PRICE.toString())));
    }

    @Test
    @Transactional
    public void getRaceInfo() throws Exception {
        // Initialize the database
        raceInfoRepository.saveAndFlush(raceInfo);

        // Get the raceInfo
        restRaceInfoMockMvc.perform(get("/api/race-infos/{id}", raceInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(raceInfo.getId().intValue()))
            .andExpect(jsonPath("$.raceName").value(DEFAULT_RACE_NAME.toString()))
            .andExpect(jsonPath("$.raceDescription").value(DEFAULT_RACE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.startPosition").value(DEFAULT_START_POSITION))
            .andExpect(jsonPath("$.finishPosition").value(DEFAULT_FINISH_POSITION))
            .andExpect(jsonPath("$.horseName").value(DEFAULT_HORSE_NAME.toString()))
            .andExpect(jsonPath("$.trainer").value(DEFAULT_TRAINER.toString()))
            .andExpect(jsonPath("$.jockey").value(DEFAULT_JOCKEY.toString()))
            .andExpect(jsonPath("$.margin").value(DEFAULT_MARGIN.toString()))
            .andExpect(jsonPath("$.penalty").value(DEFAULT_PENALTY.toString()))
            .andExpect(jsonPath("$.startingPrice").value(DEFAULT_STARTING_PRICE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRaceInfo() throws Exception {
        // Get the raceInfo
        restRaceInfoMockMvc.perform(get("/api/race-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRaceInfo() throws Exception {
        // Initialize the database
        raceInfoService.save(raceInfo);

        int databaseSizeBeforeUpdate = raceInfoRepository.findAll().size();

        // Update the raceInfo
        RaceInfo updatedRaceInfo = raceInfoRepository.findOne(raceInfo.getId());
        updatedRaceInfo
                .createdDate(UPDATED_CREATED_DATE)
                .startPosition(UPDATED_START_POSITION)
                .finishPosition(UPDATED_FINISH_POSITION)
                .horseName(UPDATED_HORSE_NAME)
                .trainer(UPDATED_TRAINER)
                .jockey(UPDATED_JOCKEY)
                .margin(UPDATED_MARGIN)
                .penalty(UPDATED_PENALTY)
                .startingPrice(UPDATED_STARTING_PRICE);

        restRaceInfoMockMvc.perform(put("/api/race-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceInfo)))
                .andExpect(status().isOk());

        // Validate the RaceInfo in the database
        List<RaceInfo> raceInfos = raceInfoRepository.findAll();
        assertThat(raceInfos).hasSize(databaseSizeBeforeUpdate);
        RaceInfo testRaceInfo = raceInfos.get(raceInfos.size() - 1);
        assertThat(testRaceInfo.getStartPosition()).isEqualTo(UPDATED_START_POSITION);
        assertThat(testRaceInfo.getFinishPosition()).isEqualTo(UPDATED_FINISH_POSITION);
        assertThat(testRaceInfo.getHorseName()).isEqualTo(UPDATED_HORSE_NAME);
        assertThat(testRaceInfo.getTrainer()).isEqualTo(UPDATED_TRAINER);
        assertThat(testRaceInfo.getJockey()).isEqualTo(UPDATED_JOCKEY);
        assertThat(testRaceInfo.getMargin()).isEqualTo(UPDATED_MARGIN);
        assertThat(testRaceInfo.getPenalty()).isEqualTo(UPDATED_PENALTY);
        assertThat(testRaceInfo.getStartingPrice()).isEqualTo(UPDATED_STARTING_PRICE);
    }

    @Test
    @Transactional
    public void deleteRaceInfo() throws Exception {
        // Initialize the database
        raceInfoService.save(raceInfo);

        int databaseSizeBeforeDelete = raceInfoRepository.findAll().size();

        // Get the raceInfo
        restRaceInfoMockMvc.perform(delete("/api/race-infos/{id}", raceInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceInfo> raceInfos = raceInfoRepository.findAll();
        assertThat(raceInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
