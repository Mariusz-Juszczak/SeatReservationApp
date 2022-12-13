package com.seatreservation.web.rest;

import static com.seatreservation.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.seatreservation.IntegrationTest;
import com.seatreservation.domain.Restrictions;
import com.seatreservation.domain.enumeration.RestrictionType;
import com.seatreservation.repository.RestrictionsRepository;
import com.seatreservation.service.dto.RestrictionsDto;
import com.seatreservation.service.mapper.RestrictionsMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RestrictionsController} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestrictionsControllerIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FROM_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FROM_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final RestrictionType DEFAULT_RESTRICTION_TYPE = RestrictionType.PERCENTAGE;
    private static final RestrictionType UPDATED_RESTRICTION_TYPE = RestrictionType.PER_SEATS_NUMBER;

    private static final Integer DEFAULT_RESTRICTION_VALUE = 1;
    private static final Integer UPDATED_RESTRICTION_VALUE = 2;

    private static final String ENTITY_API_URL = "/api/restrictions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestrictionsRepository restrictionsRepository;

    @Autowired
    private RestrictionsMapper restrictionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestrictionsMockMvc;

    private Restrictions restrictions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restrictions createEntity(EntityManager em) {
        Restrictions restrictions = new Restrictions()
            .name(DEFAULT_NAME)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .restrictionType(DEFAULT_RESTRICTION_TYPE)
            .restrictionValue(DEFAULT_RESTRICTION_VALUE);
        return restrictions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restrictions createUpdatedEntity(EntityManager em) {
        Restrictions restrictions = new Restrictions()
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .restrictionType(UPDATED_RESTRICTION_TYPE)
            .restrictionValue(UPDATED_RESTRICTION_VALUE);
        return restrictions;
    }

    @BeforeEach
    public void initTest() {
        restrictions = createEntity(em);
    }

    @Test
    @Transactional
    void createRestrictions() throws Exception {
        int databaseSizeBeforeCreate = restrictionsRepository.findAll().size();
        // Create the Restrictions
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);
        restRestrictionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isCreated());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeCreate + 1);
        Restrictions testRestrictions = restrictionsList.get(restrictionsList.size() - 1);
        assertThat(testRestrictions.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestrictions.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testRestrictions.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testRestrictions.getRestrictionType()).isEqualTo(DEFAULT_RESTRICTION_TYPE);
        assertThat(testRestrictions.getRestrictionValue()).isEqualTo(DEFAULT_RESTRICTION_VALUE);
    }

    @Test
    @Transactional
    void createRestrictionsWithExistingId() throws Exception {
        // Create the Restrictions with an existing ID
        restrictions.setId(1L);
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        int databaseSizeBeforeCreate = restrictionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestrictionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = restrictionsRepository.findAll().size();
        // set the field null
        restrictions.setName(null);

        // Create the Restrictions, which fails.
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        restRestrictionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isBadRequest());

        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = restrictionsRepository.findAll().size();
        // set the field null
        restrictions.setFromDate(null);

        // Create the Restrictions, which fails.
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        restRestrictionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isBadRequest());

        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestrictions() throws Exception {
        // Initialize the database
        restrictionsRepository.saveAndFlush(restrictions);

        // Get all the restrictionsList
        restRestrictionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restrictions.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(sameInstant(DEFAULT_FROM_DATE))))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(sameInstant(DEFAULT_TO_DATE))))
            .andExpect(jsonPath("$.[*].restrictionType").value(hasItem(DEFAULT_RESTRICTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].restrictionValue").value(hasItem(DEFAULT_RESTRICTION_VALUE)));
    }

    @Test
    @Transactional
    void getRestrictions() throws Exception {
        // Initialize the database
        restrictionsRepository.saveAndFlush(restrictions);

        // Get the restrictions
        restRestrictionsMockMvc
            .perform(get(ENTITY_API_URL_ID, restrictions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restrictions.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fromDate").value(sameInstant(DEFAULT_FROM_DATE)))
            .andExpect(jsonPath("$.toDate").value(sameInstant(DEFAULT_TO_DATE)))
            .andExpect(jsonPath("$.restrictionType").value(DEFAULT_RESTRICTION_TYPE.toString()))
            .andExpect(jsonPath("$.restrictionValue").value(DEFAULT_RESTRICTION_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingRestrictions() throws Exception {
        // Get the restrictions
        restRestrictionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestrictions() throws Exception {
        // Initialize the database
        restrictionsRepository.saveAndFlush(restrictions);

        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();

        // Update the restrictions
        Restrictions updatedRestrictions = restrictionsRepository.findById(restrictions.getId()).get();
        // Disconnect from session so that the updates on updatedRestrictions are not directly saved in db
        em.detach(updatedRestrictions);
        updatedRestrictions
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .restrictionType(UPDATED_RESTRICTION_TYPE)
            .restrictionValue(UPDATED_RESTRICTION_VALUE);
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(updatedRestrictions);

        restRestrictionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restrictionsDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isOk());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
        Restrictions testRestrictions = restrictionsList.get(restrictionsList.size() - 1);
        assertThat(testRestrictions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestrictions.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testRestrictions.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testRestrictions.getRestrictionType()).isEqualTo(UPDATED_RESTRICTION_TYPE);
        assertThat(testRestrictions.getRestrictionValue()).isEqualTo(UPDATED_RESTRICTION_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingRestrictions() throws Exception {
        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();
        restrictions.setId(count.incrementAndGet());

        // Create the Restrictions
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestrictionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restrictionsDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestrictions() throws Exception {
        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();
        restrictions.setId(count.incrementAndGet());

        // Create the Restrictions
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestrictionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestrictions() throws Exception {
        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();
        restrictions.setId(count.incrementAndGet());

        // Create the Restrictions
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestrictionsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestrictionsWithPatch() throws Exception {
        // Initialize the database
        restrictionsRepository.saveAndFlush(restrictions);

        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();

        // Update the restrictions using partial update
        Restrictions partialUpdatedRestrictions = new Restrictions();
        partialUpdatedRestrictions.setId(restrictions.getId());

        partialUpdatedRestrictions.restrictionValue(UPDATED_RESTRICTION_VALUE);

        restRestrictionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestrictions.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestrictions))
            )
            .andExpect(status().isOk());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
        Restrictions testRestrictions = restrictionsList.get(restrictionsList.size() - 1);
        assertThat(testRestrictions.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestrictions.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testRestrictions.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testRestrictions.getRestrictionType()).isEqualTo(DEFAULT_RESTRICTION_TYPE);
        assertThat(testRestrictions.getRestrictionValue()).isEqualTo(UPDATED_RESTRICTION_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateRestrictionsWithPatch() throws Exception {
        // Initialize the database
        restrictionsRepository.saveAndFlush(restrictions);

        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();

        // Update the restrictions using partial update
        Restrictions partialUpdatedRestrictions = new Restrictions();
        partialUpdatedRestrictions.setId(restrictions.getId());

        partialUpdatedRestrictions
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .restrictionType(UPDATED_RESTRICTION_TYPE)
            .restrictionValue(UPDATED_RESTRICTION_VALUE);

        restRestrictionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestrictions.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestrictions))
            )
            .andExpect(status().isOk());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
        Restrictions testRestrictions = restrictionsList.get(restrictionsList.size() - 1);
        assertThat(testRestrictions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestrictions.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testRestrictions.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testRestrictions.getRestrictionType()).isEqualTo(UPDATED_RESTRICTION_TYPE);
        assertThat(testRestrictions.getRestrictionValue()).isEqualTo(UPDATED_RESTRICTION_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingRestrictions() throws Exception {
        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();
        restrictions.setId(count.incrementAndGet());

        // Create the Restrictions
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestrictionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restrictionsDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestrictions() throws Exception {
        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();
        restrictions.setId(count.incrementAndGet());

        // Create the Restrictions
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestrictionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestrictions() throws Exception {
        int databaseSizeBeforeUpdate = restrictionsRepository.findAll().size();
        restrictions.setId(count.incrementAndGet());

        // Create the Restrictions
        RestrictionsDto restrictionsDto = restrictionsMapper.toDto(restrictions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestrictionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restrictionsDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restrictions in the database
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestrictions() throws Exception {
        // Initialize the database
        restrictionsRepository.saveAndFlush(restrictions);

        int databaseSizeBeforeDelete = restrictionsRepository.findAll().size();

        // Delete the restrictions
        restRestrictionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, restrictions.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restrictions> restrictionsList = restrictionsRepository.findAll();
        assertThat(restrictionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
