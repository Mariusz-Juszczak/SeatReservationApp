package com.seatreservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.seatreservation.IntegrationTest;
import com.seatreservation.domain.Coordinates;
import com.seatreservation.repository.CoordinatesRepository;
import com.seatreservation.service.dto.CoordinatesDto;
import com.seatreservation.service.mapper.CoordinatesMapper;
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
 * Integration tests for the {@link CoordinatesController} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoordinatesControllerIT {

    private static final Double DEFAULT_FROM_TOP = 1D;
    private static final Double UPDATED_FROM_TOP = 2D;

    private static final Double DEFAULT_FROM_LEFT = 1D;
    private static final Double UPDATED_FROM_LEFT = 2D;

    private static final Double DEFAULT_ANGLE = 1D;

    private static final Double UPDATED_ANGLE = 2D;

    private static final String ENTITY_API_URL = "/api/coordinates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Autowired
    private CoordinatesMapper coordinatesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoordinatesMockMvc;

    private Coordinates coordinates;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinates createEntity(EntityManager em) {
        Coordinates coordinates = new Coordinates().fromTop(DEFAULT_FROM_TOP).fromLeft(DEFAULT_FROM_LEFT).angle(DEFAULT_ANGLE);
        return coordinates;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinates createUpdatedEntity(EntityManager em) {
        Coordinates coordinates = new Coordinates().fromTop(UPDATED_FROM_TOP).fromLeft(UPDATED_FROM_LEFT).angle(UPDATED_ANGLE);
        return coordinates;
    }

    @BeforeEach
    public void initTest() {
        coordinates = createEntity(em);
    }

    @Test
    @Transactional
    void createCoordinates() throws Exception {
        int databaseSizeBeforeCreate = coordinatesRepository.findAll().size();
        // Create the Coordinates
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);
        restCoordinatesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isCreated());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeCreate + 1);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getFromTop()).isEqualTo(DEFAULT_FROM_TOP);
        assertThat(testCoordinates.getFromLeft()).isEqualTo(DEFAULT_FROM_LEFT);
        assertThat(testCoordinates.getAngle()).isEqualTo(DEFAULT_ANGLE);
    }

    @Test
    @Transactional
    void createCoordinatesWithExistingId() throws Exception {
        // Create the Coordinates with an existing ID
        coordinates.setId(1L);
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        int databaseSizeBeforeCreate = coordinatesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoordinatesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromTopIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setFromTop(null);

        // Create the Coordinates, which fails.
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        restCoordinatesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFromLeftIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setFromLeft(null);

        // Create the Coordinates, which fails.
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        restCoordinatesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAngleIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setAngle(null);

        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        restCoordinatesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    @Transactional
    void getAllCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        // Get all the coordinatesList
        restCoordinatesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coordinates.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromTop").value(hasItem(DEFAULT_FROM_TOP.doubleValue())))
            .andExpect(jsonPath("$.[*].fromLeft").value(hasItem(DEFAULT_FROM_LEFT.doubleValue())))
            .andExpect(jsonPath("$.[*].angle").value(hasItem(DEFAULT_ANGLE.doubleValue())));
    }

    @Test
    @Transactional
    void getCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        // Get the coordinates
        restCoordinatesMockMvc
            .perform(get(ENTITY_API_URL_ID, coordinates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coordinates.getId().intValue()))
            .andExpect(jsonPath("$.fromTop").value(DEFAULT_FROM_TOP.doubleValue()))
            .andExpect(jsonPath("$.fromLeft").value(DEFAULT_FROM_LEFT.doubleValue()))
            .andExpect(jsonPath("$.angle").value(DEFAULT_ANGLE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCoordinates() throws Exception {
        // Get the coordinates
        restCoordinatesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();

        // Update the coordinates
        Coordinates updatedCoordinates = coordinatesRepository.findById(coordinates.getId()).get();
        // Disconnect from session so that the updates on updatedCoordinates are not directly saved in db
        em.detach(updatedCoordinates);
        updatedCoordinates.fromTop(UPDATED_FROM_TOP).fromLeft(UPDATED_FROM_LEFT).angle(UPDATED_ANGLE);
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(updatedCoordinates);

        restCoordinatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coordinatesDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isOk());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getFromTop()).isEqualTo(UPDATED_FROM_TOP);
        assertThat(testCoordinates.getFromLeft()).isEqualTo(UPDATED_FROM_LEFT);
            assertThat(testCoordinates.getAngle()).isEqualTo(UPDATED_ANGLE);
    }

    @Test
    @Transactional
    void putNonExistingCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // Create the Coordinates
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coordinatesDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // Create the Coordinates
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // Create the Coordinates
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoordinatesWithPatch() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();

        // Update the coordinates using partial update
        Coordinates partialUpdatedCoordinates = new Coordinates();
        partialUpdatedCoordinates.setId(coordinates.getId());

        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinates.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinates))
            )
            .andExpect(status().isOk());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getFromTop()).isEqualTo(DEFAULT_FROM_TOP);
        assertThat(testCoordinates.getFromLeft()).isEqualTo(DEFAULT_FROM_LEFT);
            assertThat(testCoordinates.getAngle()).isEqualTo(DEFAULT_ANGLE);
    }

    @Test
    @Transactional
    void fullUpdateCoordinatesWithPatch() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();

        // Update the coordinates using partial update
        Coordinates partialUpdatedCoordinates = new Coordinates();
        partialUpdatedCoordinates.setId(coordinates.getId());

        partialUpdatedCoordinates.fromTop(UPDATED_FROM_TOP).fromLeft(UPDATED_FROM_LEFT).angle(UPDATED_ANGLE);

        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinates.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinates))
            )
            .andExpect(status().isOk());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getFromTop()).isEqualTo(UPDATED_FROM_TOP);
        assertThat(testCoordinates.getFromLeft()).isEqualTo(UPDATED_FROM_LEFT);
            assertThat(testCoordinates.getAngle()).isEqualTo(UPDATED_ANGLE);
    }

    @Test
    @Transactional
    void patchNonExistingCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // Create the Coordinates
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coordinatesDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // Create the Coordinates
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // Create the Coordinates
        CoordinatesDto coordinatesDto = coordinatesMapper.toDto(coordinates);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeDelete = coordinatesRepository.findAll().size();

        // Delete the coordinates
        restCoordinatesMockMvc
            .perform(delete(ENTITY_API_URL_ID, coordinates.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
