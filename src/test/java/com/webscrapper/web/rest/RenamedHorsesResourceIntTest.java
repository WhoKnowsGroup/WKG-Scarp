package com.webscrapper.web.rest;

import com.webscrapper.WebScrapperApp;

import com.webscrapper.domain.RenamedHorses;
import com.webscrapper.repository.RenamedHorsesRepository;
import com.webscrapper.service.RenamedHorsesService;

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
 * Test class for the RenamedHorsesResource REST controller.
 *
 * @see RenamedHorsesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebScrapperApp.class)
public class RenamedHorsesResourceIntTest {

    private static final String DEFAULT_OLD_NAME = "AAAAA";
    private static final String UPDATED_OLD_NAME = "BBBBB";

    private static final String DEFAULT_NEW_NAME = "AAAAA";
    private static final String UPDATED_NEW_NAME = "BBBBB";

    private static final LocalDate DEFAULT_DATE_CHANGED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CHANGED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_AGE_AND_BREED = "AAAAA";
    private static final String UPDATED_AGE_AND_BREED = "BBBBB";

    @Inject
    private RenamedHorsesRepository renamedHorsesRepository;

    @Inject
    private RenamedHorsesService renamedHorsesService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRenamedHorsesMockMvc;

    private RenamedHorses renamedHorses;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RenamedHorsesResource renamedHorsesResource = new RenamedHorsesResource();
        ReflectionTestUtils.setField(renamedHorsesResource, "renamedHorsesService", renamedHorsesService);
        this.restRenamedHorsesMockMvc = MockMvcBuilders.standaloneSetup(renamedHorsesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RenamedHorses createEntity(EntityManager em) {
        RenamedHorses renamedHorses = new RenamedHorses()
                .oldName(DEFAULT_OLD_NAME)
                .newName(DEFAULT_NEW_NAME)
                .dateChanged(DEFAULT_DATE_CHANGED)
                .ageAndBreed(DEFAULT_AGE_AND_BREED);
        return renamedHorses;
    }

    @Before
    public void initTest() {
        renamedHorses = createEntity(em);
    }

    @Test
    @Transactional
    public void createRenamedHorses() throws Exception {
        int databaseSizeBeforeCreate = renamedHorsesRepository.findAll().size();

        // Create the RenamedHorses

        restRenamedHorsesMockMvc.perform(post("/api/renamed-horses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(renamedHorses)))
                .andExpect(status().isCreated());

        // Validate the RenamedHorses in the database
        List<RenamedHorses> renamedHorses = renamedHorsesRepository.findAll();
        assertThat(renamedHorses).hasSize(databaseSizeBeforeCreate + 1);
        RenamedHorses testRenamedHorses = renamedHorses.get(renamedHorses.size() - 1);
        assertThat(testRenamedHorses.getOldName()).isEqualTo(DEFAULT_OLD_NAME);
        assertThat(testRenamedHorses.getNewName()).isEqualTo(DEFAULT_NEW_NAME);
        assertThat(testRenamedHorses.getDateChanged()).isEqualTo(DEFAULT_DATE_CHANGED);
        assertThat(testRenamedHorses.getAgeAndBreed()).isEqualTo(DEFAULT_AGE_AND_BREED);
    }

    @Test
    @Transactional
    public void getAllRenamedHorses() throws Exception {
        // Initialize the database
        renamedHorsesRepository.saveAndFlush(renamedHorses);

        // Get all the renamedHorses
        restRenamedHorsesMockMvc.perform(get("/api/renamed-horses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(renamedHorses.getId().intValue())))
                .andExpect(jsonPath("$.[*].oldName").value(hasItem(DEFAULT_OLD_NAME.toString())))
                .andExpect(jsonPath("$.[*].newName").value(hasItem(DEFAULT_NEW_NAME.toString())))
                .andExpect(jsonPath("$.[*].dateChanged").value(hasItem(DEFAULT_DATE_CHANGED.toString())))
                .andExpect(jsonPath("$.[*].ageAndBreed").value(hasItem(DEFAULT_AGE_AND_BREED.toString())));
    }

    @Test
    @Transactional
    public void getRenamedHorses() throws Exception {
        // Initialize the database
        renamedHorsesRepository.saveAndFlush(renamedHorses);

        // Get the renamedHorses
        restRenamedHorsesMockMvc.perform(get("/api/renamed-horses/{id}", renamedHorses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(renamedHorses.getId().intValue()))
            .andExpect(jsonPath("$.oldName").value(DEFAULT_OLD_NAME.toString()))
            .andExpect(jsonPath("$.newName").value(DEFAULT_NEW_NAME.toString()))
            .andExpect(jsonPath("$.dateChanged").value(DEFAULT_DATE_CHANGED.toString()))
            .andExpect(jsonPath("$.ageAndBreed").value(DEFAULT_AGE_AND_BREED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRenamedHorses() throws Exception {
        // Get the renamedHorses
        restRenamedHorsesMockMvc.perform(get("/api/renamed-horses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRenamedHorses() throws Exception {
        // Initialize the database
        renamedHorsesService.save(renamedHorses);

        int databaseSizeBeforeUpdate = renamedHorsesRepository.findAll().size();

        // Update the renamedHorses
        RenamedHorses updatedRenamedHorses = renamedHorsesRepository.findOne(renamedHorses.getId());
        updatedRenamedHorses
                .oldName(UPDATED_OLD_NAME)
                .newName(UPDATED_NEW_NAME)
                .dateChanged(UPDATED_DATE_CHANGED)
                .ageAndBreed(UPDATED_AGE_AND_BREED);

        restRenamedHorsesMockMvc.perform(put("/api/renamed-horses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRenamedHorses)))
                .andExpect(status().isOk());

        // Validate the RenamedHorses in the database
        List<RenamedHorses> renamedHorses = renamedHorsesRepository.findAll();
        assertThat(renamedHorses).hasSize(databaseSizeBeforeUpdate);
        RenamedHorses testRenamedHorses = renamedHorses.get(renamedHorses.size() - 1);
        assertThat(testRenamedHorses.getOldName()).isEqualTo(UPDATED_OLD_NAME);
        assertThat(testRenamedHorses.getNewName()).isEqualTo(UPDATED_NEW_NAME);
        assertThat(testRenamedHorses.getDateChanged()).isEqualTo(UPDATED_DATE_CHANGED);
        assertThat(testRenamedHorses.getAgeAndBreed()).isEqualTo(UPDATED_AGE_AND_BREED);
    }

    @Test
    @Transactional
    public void deleteRenamedHorses() throws Exception {
        // Initialize the database
        renamedHorsesService.save(renamedHorses);

        int databaseSizeBeforeDelete = renamedHorsesRepository.findAll().size();

        // Get the renamedHorses
        restRenamedHorsesMockMvc.perform(delete("/api/renamed-horses/{id}", renamedHorses.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RenamedHorses> renamedHorses = renamedHorsesRepository.findAll();
        assertThat(renamedHorses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
