package com.webscrapper.web.rest;

import com.webscrapper.WebScrapperApp;

import com.webscrapper.domain.Horse;
import com.webscrapper.repository.HorseRepository;
import com.webscrapper.service.HorseService;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HorseResource REST controller.
 *
 * @see HorseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebScrapperApp.class)
public class HorseResourceIntTest {

    private static final String DEFAULT_HORSE_NAME = "AAAAA";
    private static final String UPDATED_HORSE_NAME = "BBBBB";

    private static final String DEFAULT_HORSE_STATUS = "AAAAA";
    private static final String UPDATED_HORSE_STATUS = "BBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OWNER = "AAAAA";
    private static final String UPDATED_OWNER = "BBBBB";

    private static final String DEFAULT_STEWARDS_EMBARGOES = "AAAAA";
    private static final String UPDATED_STEWARDS_EMBARGOES = "BBBBB";

    private static final String DEFAULT_EMERGENCY_VACCINATION_RECORD_URL = "AAAAA";
    private static final String UPDATED_EMERGENCY_VACCINATION_RECORD_URL = "BBBBB";

    private static final LocalDate DEFAULT_LAST_GEAR_CHANGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_GEAR_CHANGE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TRAINER = "AAAAA";
    private static final String UPDATED_TRAINER = "BBBBB";

    private static final BigDecimal DEFAULT_PRIZE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIZE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BONUS = new BigDecimal(1);
    private static final BigDecimal UPDATED_BONUS = new BigDecimal(2);

    private static final String DEFAULT_MIM_MAX_DIST_WIN = "AAAAA";
    private static final String UPDATED_MIM_MAX_DIST_WIN = "BBBBB";

    private static final String DEFAULT_FIRST_UP_DATA = "AAAAA";
    private static final String UPDATED_FIRST_UP_DATA = "BBBBB";

    private static final String DEFAULT_SECOND_UP_DATA = "AAAAA";
    private static final String UPDATED_SECOND_UP_DATA = "BBBBB";

    private static final String DEFAULT_HORSE_TRACK = "AAAAA";
    private static final String UPDATED_HORSE_TRACK = "BBBBB";

    private static final String DEFAULT_HORSE_DIST = "AAAAA";
    private static final String UPDATED_HORSE_DIST = "BBBBB";

    private static final String DEFAULT_HORSE_CLASS = "AAAAA";
    private static final String UPDATED_HORSE_CLASS = "BBBBB";

    private static final Integer DEFAULT_POSITION_IN_LAST_RACE = 1;
    private static final Integer UPDATED_POSITION_IN_LAST_RACE = 2;

    private static final String DEFAULT_FIRM = "AAAAA";
    private static final String UPDATED_FIRM = "BBBBB";

    private static final String DEFAULT_GOOD = "AAAAA";
    private static final String UPDATED_GOOD = "BBBBB";

    private static final String DEFAULT_SOFT = "AAAAA";
    private static final String UPDATED_SOFT = "BBBBB";

    private static final String DEFAULT_HEAVY = "AAAAA";
    private static final String UPDATED_HEAVY = "BBBBB";

    @Inject
    private HorseRepository horseRepository;

    @Inject
    private HorseService horseService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHorseMockMvc;

    private Horse horse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HorseResource horseResource = new HorseResource();
        ReflectionTestUtils.setField(horseResource, "horseService", horseService);
        this.restHorseMockMvc = MockMvcBuilders.standaloneSetup(horseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horse createEntity(EntityManager em) {
        Horse horse = new Horse()
                .horseName(DEFAULT_HORSE_NAME)
                .horseStatus(DEFAULT_HORSE_STATUS)
                .birthDate(DEFAULT_BIRTH_DATE)
                .owner(DEFAULT_OWNER)
                .stewardsEmbargoes(DEFAULT_STEWARDS_EMBARGOES)
                .emergencyVaccinationRecordURL(DEFAULT_EMERGENCY_VACCINATION_RECORD_URL)
                .lastGearChange(DEFAULT_LAST_GEAR_CHANGE)
                .trainer(DEFAULT_TRAINER)
                .prize(DEFAULT_PRIZE)
                .bonus(DEFAULT_BONUS)
                .mimMaxDistWin(DEFAULT_MIM_MAX_DIST_WIN)
                .firstUpData(DEFAULT_FIRST_UP_DATA)
                .secondUpData(DEFAULT_SECOND_UP_DATA)
                .horseTrack(DEFAULT_HORSE_TRACK)
                .horseDist(DEFAULT_HORSE_DIST)
                .horseClass(DEFAULT_HORSE_CLASS)
                .positionInLastRace(DEFAULT_POSITION_IN_LAST_RACE)
                .firm(DEFAULT_FIRM)
                .good(DEFAULT_GOOD)
                .soft(DEFAULT_SOFT)
                .heavy(DEFAULT_HEAVY);
        return horse;
    }

    @Before
    public void initTest() {
        horse = createEntity(em);
    }

    @Test
    @Transactional
    public void createHorse() throws Exception {
        int databaseSizeBeforeCreate = horseRepository.findAll().size();

        // Create the Horse

        restHorseMockMvc.perform(post("/api/horses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(horse)))
                .andExpect(status().isCreated());

        // Validate the Horse in the database
        List<Horse> horses = horseRepository.findAll();
        assertThat(horses).hasSize(databaseSizeBeforeCreate + 1);
        Horse testHorse = horses.get(horses.size() - 1);
        assertThat(testHorse.getHorseName()).isEqualTo(DEFAULT_HORSE_NAME);
        assertThat(testHorse.getHorseStatus()).isEqualTo(DEFAULT_HORSE_STATUS);
        assertThat(testHorse.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testHorse.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testHorse.getStewardsEmbargoes()).isEqualTo(DEFAULT_STEWARDS_EMBARGOES);
        assertThat(testHorse.getEmergencyVaccinationRecordURL()).isEqualTo(DEFAULT_EMERGENCY_VACCINATION_RECORD_URL);
        assertThat(testHorse.getLastGearChange()).isEqualTo(DEFAULT_LAST_GEAR_CHANGE);
        assertThat(testHorse.getTrainer()).isEqualTo(DEFAULT_TRAINER);
        assertThat(testHorse.getPrize()).isEqualTo(DEFAULT_PRIZE);
        assertThat(testHorse.getBonus()).isEqualTo(DEFAULT_BONUS);
        assertThat(testHorse.getMimMaxDistWin()).isEqualTo(DEFAULT_MIM_MAX_DIST_WIN);
        assertThat(testHorse.getFirstUpData()).isEqualTo(DEFAULT_FIRST_UP_DATA);
        assertThat(testHorse.getSecondUpData()).isEqualTo(DEFAULT_SECOND_UP_DATA);
        assertThat(testHorse.getHorseTrack()).isEqualTo(DEFAULT_HORSE_TRACK);
        assertThat(testHorse.getHorseDist()).isEqualTo(DEFAULT_HORSE_DIST);
        assertThat(testHorse.getHorseClass()).isEqualTo(DEFAULT_HORSE_CLASS);
        assertThat(testHorse.getPositionInLastRace()).isEqualTo(DEFAULT_POSITION_IN_LAST_RACE);
        assertThat(testHorse.getFirm()).isEqualTo(DEFAULT_FIRM);
        assertThat(testHorse.getGood()).isEqualTo(DEFAULT_GOOD);
        assertThat(testHorse.getSoft()).isEqualTo(DEFAULT_SOFT);
        assertThat(testHorse.getHeavy()).isEqualTo(DEFAULT_HEAVY);
    }

    @Test
    @Transactional
    public void getAllHorses() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        // Get all the horses
        restHorseMockMvc.perform(get("/api/horses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(horse.getId().intValue())))
                .andExpect(jsonPath("$.[*].horseName").value(hasItem(DEFAULT_HORSE_NAME.toString())))
                .andExpect(jsonPath("$.[*].horseStatus").value(hasItem(DEFAULT_HORSE_STATUS.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
                .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
                .andExpect(jsonPath("$.[*].stewardsEmbargoes").value(hasItem(DEFAULT_STEWARDS_EMBARGOES.toString())))
                .andExpect(jsonPath("$.[*].emergencyVaccinationRecordURL").value(hasItem(DEFAULT_EMERGENCY_VACCINATION_RECORD_URL.toString())))
                .andExpect(jsonPath("$.[*].lastGearChange").value(hasItem(DEFAULT_LAST_GEAR_CHANGE.toString())))
                .andExpect(jsonPath("$.[*].trainer").value(hasItem(DEFAULT_TRAINER.toString())))
                .andExpect(jsonPath("$.[*].prize").value(hasItem(DEFAULT_PRIZE.intValue())))
                .andExpect(jsonPath("$.[*].bonus").value(hasItem(DEFAULT_BONUS.intValue())))
                .andExpect(jsonPath("$.[*].mimMaxDistWin").value(hasItem(DEFAULT_MIM_MAX_DIST_WIN.toString())))
                .andExpect(jsonPath("$.[*].firstUpData").value(hasItem(DEFAULT_FIRST_UP_DATA.toString())))
                .andExpect(jsonPath("$.[*].secondUpData").value(hasItem(DEFAULT_SECOND_UP_DATA.toString())))
                .andExpect(jsonPath("$.[*].horseTrack").value(hasItem(DEFAULT_HORSE_TRACK.toString())))
                .andExpect(jsonPath("$.[*].horseDist").value(hasItem(DEFAULT_HORSE_DIST.toString())))
                .andExpect(jsonPath("$.[*].horseClass").value(hasItem(DEFAULT_HORSE_CLASS.toString())))
                .andExpect(jsonPath("$.[*].positionInLastRace").value(hasItem(DEFAULT_POSITION_IN_LAST_RACE)))
                .andExpect(jsonPath("$.[*].firm").value(hasItem(DEFAULT_FIRM.toString())))
                .andExpect(jsonPath("$.[*].good").value(hasItem(DEFAULT_GOOD.toString())))
                .andExpect(jsonPath("$.[*].soft").value(hasItem(DEFAULT_SOFT.toString())))
                .andExpect(jsonPath("$.[*].heavy").value(hasItem(DEFAULT_HEAVY.toString())));
    }

    @Test
    @Transactional
    public void getHorse() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        // Get the horse
        restHorseMockMvc.perform(get("/api/horses/{id}", horse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(horse.getId().intValue()))
            .andExpect(jsonPath("$.horseName").value(DEFAULT_HORSE_NAME.toString()))
            .andExpect(jsonPath("$.horseStatus").value(DEFAULT_HORSE_STATUS.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.stewardsEmbargoes").value(DEFAULT_STEWARDS_EMBARGOES.toString()))
            .andExpect(jsonPath("$.emergencyVaccinationRecordURL").value(DEFAULT_EMERGENCY_VACCINATION_RECORD_URL.toString()))
            .andExpect(jsonPath("$.lastGearChange").value(DEFAULT_LAST_GEAR_CHANGE.toString()))
            .andExpect(jsonPath("$.trainer").value(DEFAULT_TRAINER.toString()))
            .andExpect(jsonPath("$.prize").value(DEFAULT_PRIZE.intValue()))
            .andExpect(jsonPath("$.bonus").value(DEFAULT_BONUS.intValue()))
            .andExpect(jsonPath("$.mimMaxDistWin").value(DEFAULT_MIM_MAX_DIST_WIN.toString()))
            .andExpect(jsonPath("$.firstUpData").value(DEFAULT_FIRST_UP_DATA.toString()))
            .andExpect(jsonPath("$.secondUpData").value(DEFAULT_SECOND_UP_DATA.toString()))
            .andExpect(jsonPath("$.horseTrack").value(DEFAULT_HORSE_TRACK.toString()))
            .andExpect(jsonPath("$.horseDist").value(DEFAULT_HORSE_DIST.toString()))
            .andExpect(jsonPath("$.horseClass").value(DEFAULT_HORSE_CLASS.toString()))
            .andExpect(jsonPath("$.positionInLastRace").value(DEFAULT_POSITION_IN_LAST_RACE))
            .andExpect(jsonPath("$.firm").value(DEFAULT_FIRM.toString()))
            .andExpect(jsonPath("$.good").value(DEFAULT_GOOD.toString()))
            .andExpect(jsonPath("$.soft").value(DEFAULT_SOFT.toString()))
            .andExpect(jsonPath("$.heavy").value(DEFAULT_HEAVY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHorse() throws Exception {
        // Get the horse
        restHorseMockMvc.perform(get("/api/horses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHorse() throws Exception {
        // Initialize the database
        horseService.save(horse);

        int databaseSizeBeforeUpdate = horseRepository.findAll().size();

        // Update the horse
        Horse updatedHorse = horseRepository.findOne(horse.getId());
        updatedHorse
                .horseName(UPDATED_HORSE_NAME)
                .horseStatus(UPDATED_HORSE_STATUS)
                .birthDate(UPDATED_BIRTH_DATE)
                .owner(UPDATED_OWNER)
                .stewardsEmbargoes(UPDATED_STEWARDS_EMBARGOES)
                .emergencyVaccinationRecordURL(UPDATED_EMERGENCY_VACCINATION_RECORD_URL)
                .lastGearChange(UPDATED_LAST_GEAR_CHANGE)
                .trainer(UPDATED_TRAINER)
                .prize(UPDATED_PRIZE)
                .bonus(UPDATED_BONUS)
                .mimMaxDistWin(UPDATED_MIM_MAX_DIST_WIN)
                .firstUpData(UPDATED_FIRST_UP_DATA)
                .secondUpData(UPDATED_SECOND_UP_DATA)
                .horseTrack(UPDATED_HORSE_TRACK)
                .horseDist(UPDATED_HORSE_DIST)
                .horseClass(UPDATED_HORSE_CLASS)
                .positionInLastRace(UPDATED_POSITION_IN_LAST_RACE)
                .firm(UPDATED_FIRM)
                .good(UPDATED_GOOD)
                .soft(UPDATED_SOFT)
                .heavy(UPDATED_HEAVY);

        restHorseMockMvc.perform(put("/api/horses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHorse)))
                .andExpect(status().isOk());

        // Validate the Horse in the database
        List<Horse> horses = horseRepository.findAll();
        assertThat(horses).hasSize(databaseSizeBeforeUpdate);
        Horse testHorse = horses.get(horses.size() - 1);
        assertThat(testHorse.getHorseName()).isEqualTo(UPDATED_HORSE_NAME);
        assertThat(testHorse.getHorseStatus()).isEqualTo(UPDATED_HORSE_STATUS);
        assertThat(testHorse.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testHorse.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testHorse.getStewardsEmbargoes()).isEqualTo(UPDATED_STEWARDS_EMBARGOES);
        assertThat(testHorse.getEmergencyVaccinationRecordURL()).isEqualTo(UPDATED_EMERGENCY_VACCINATION_RECORD_URL);
        assertThat(testHorse.getLastGearChange()).isEqualTo(UPDATED_LAST_GEAR_CHANGE);
        assertThat(testHorse.getTrainer()).isEqualTo(UPDATED_TRAINER);
        assertThat(testHorse.getPrize()).isEqualTo(UPDATED_PRIZE);
        assertThat(testHorse.getBonus()).isEqualTo(UPDATED_BONUS);
        assertThat(testHorse.getMimMaxDistWin()).isEqualTo(UPDATED_MIM_MAX_DIST_WIN);
        assertThat(testHorse.getFirstUpData()).isEqualTo(UPDATED_FIRST_UP_DATA);
        assertThat(testHorse.getSecondUpData()).isEqualTo(UPDATED_SECOND_UP_DATA);
        assertThat(testHorse.getHorseTrack()).isEqualTo(UPDATED_HORSE_TRACK);
        assertThat(testHorse.getHorseDist()).isEqualTo(UPDATED_HORSE_DIST);
        assertThat(testHorse.getHorseClass()).isEqualTo(UPDATED_HORSE_CLASS);
        assertThat(testHorse.getPositionInLastRace()).isEqualTo(UPDATED_POSITION_IN_LAST_RACE);
        assertThat(testHorse.getFirm()).isEqualTo(UPDATED_FIRM);
        assertThat(testHorse.getGood()).isEqualTo(UPDATED_GOOD);
        assertThat(testHorse.getSoft()).isEqualTo(UPDATED_SOFT);
        assertThat(testHorse.getHeavy()).isEqualTo(UPDATED_HEAVY);
    }

    @Test
    @Transactional
    public void deleteHorse() throws Exception {
        // Initialize the database
        horseService.save(horse);

        int databaseSizeBeforeDelete = horseRepository.findAll().size();

        // Get the horse
        restHorseMockMvc.perform(delete("/api/horses/{id}", horse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Horse> horses = horseRepository.findAll();
        assertThat(horses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
