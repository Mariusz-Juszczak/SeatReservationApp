package com.seatreservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.seatreservation.IntegrationTest;
import com.seatreservation.domain.Dimensions;
import com.seatreservation.repository.DimensionsRepository;
import com.seatreservation.service.dto.DimensionsDto;
import com.seatreservation.service.mapper.DimensionsMapper;
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
 * Integration tests for the {@link DimensionsController} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DimensionsControllerIT {

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final Double DEFAULT_WIDTH = 1D;
    private static final Double UPDATED_WIDTH = 2D;


    private static final String ENTITY_API_URL = "/api/dimensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DimensionsRepository dimensionsRepository;

    @Autowired
    private DimensionsMapper dimensionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDimensionsMockMvc;

    private Dimensions dimensions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dimensions createEntity(EntityManager em) {
        Dimensions dimensions = new Dimensions().height(DEFAULT_HEIGHT).width(DEFAULT_WIDTH);
        return dimensions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dimensions createUpdatedEntity(EntityManager em) {
        Dimensions dimensions = new Dimensions().height(UPDATED_HEIGHT).width(UPDATED_WIDTH);
        return dimensions;
    }

    @BeforeEach
    public void initTest() {
        dimensions = createEntity(em);
    }

    @Test
    @Transactional
    void createDimensions() throws Exception {
        int databaseSizeBeforeCreate = dimensionsRepository.findAll().size();
        // Create the Dimensions
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);
        restDimensionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isCreated());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeCreate + 1);
        Dimensions testDimensions = dimensionsList.get(dimensionsList.size() - 1);
        assertThat(testDimensions.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testDimensions.getWidth()).isEqualTo(DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void createDimensionsWithExistingId() throws Exception {
        // Create the Dimensions with an existing ID
        dimensions.setId(1L);
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        int databaseSizeBeforeCreate = dimensionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDimensionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = dimensionsRepository.findAll().size();
        // set the field null
        dimensions.setHeight(null);

        // Create the Dimensions, which fails.
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        restDimensionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isBadRequest());

        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWidthIsRequired() throws Exception {
        int databaseSizeBeforeTest = dimensionsRepository.findAll().size();
        // set the field null
        dimensions.setWidth(null);

        // Create the Dimensions, which fails.
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        restDimensionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isBadRequest());

        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeTest);
    }


    @Test
    @Transactional
    void getAllDimensions() throws Exception {
        // Initialize the database
        dimensionsRepository.saveAndFlush(dimensions);

        // Get all the dimensionsList
        restDimensionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dimensions.getId().intValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())));

    }

    @Test
    @Transactional
    void getDimensions() throws Exception {
        // Initialize the database
        dimensionsRepository.saveAndFlush(dimensions);

        // Get the dimensions
        restDimensionsMockMvc
            .perform(get(ENTITY_API_URL_ID, dimensions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dimensions.getId().intValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()))
           ;
    }

    @Test
    @Transactional
    void getNonExistingDimensions() throws Exception {
        // Get the dimensions
        restDimensionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDimensions() throws Exception {
        // Initialize the database
        dimensionsRepository.saveAndFlush(dimensions);

        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();

        // Update the dimensions
        Dimensions updatedDimensions = dimensionsRepository.findById(dimensions.getId()).get();
        // Disconnect from session so that the updates on updatedDimensions are not directly saved in db
        em.detach(updatedDimensions);
        updatedDimensions.height(UPDATED_HEIGHT).width(UPDATED_WIDTH);
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(updatedDimensions);

        restDimensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dimensionsDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isOk());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
        Dimensions testDimensions = dimensionsList.get(dimensionsList.size() - 1);
        assertThat(testDimensions.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testDimensions.getWidth()).isEqualTo(UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void putNonExistingDimensions() throws Exception {
        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();
        dimensions.setId(count.incrementAndGet());

        // Create the Dimensions
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDimensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dimensionsDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDimensions() throws Exception {
        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();
        dimensions.setId(count.incrementAndGet());

        // Create the Dimensions
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDimensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDimensions() throws Exception {
        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();
        dimensions.setId(count.incrementAndGet());

        // Create the Dimensions
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDimensionsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDimensionsWithPatch() throws Exception {
        // Initialize the database
        dimensionsRepository.saveAndFlush(dimensions);

        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();

        // Update the dimensions using partial update
        Dimensions partialUpdatedDimensions = new Dimensions();
        partialUpdatedDimensions.setId(dimensions.getId());

        partialUpdatedDimensions.height(UPDATED_HEIGHT);

        restDimensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDimensions.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDimensions))
            )
            .andExpect(status().isOk());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
        Dimensions testDimensions = dimensionsList.get(dimensionsList.size() - 1);
        assertThat(testDimensions.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testDimensions.getWidth()).isEqualTo(DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void fullUpdateDimensionsWithPatch() throws Exception {
        // Initialize the database
        dimensionsRepository.saveAndFlush(dimensions);

        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();

        // Update the dimensions using partial update
        Dimensions partialUpdatedDimensions = new Dimensions();
        partialUpdatedDimensions.setId(dimensions.getId());

        partialUpdatedDimensions.height(UPDATED_HEIGHT).width(UPDATED_WIDTH);

        restDimensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDimensions.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDimensions))
            )
            .andExpect(status().isOk());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
        Dimensions testDimensions = dimensionsList.get(dimensionsList.size() - 1);
        assertThat(testDimensions.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testDimensions.getWidth()).isEqualTo(UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void patchNonExistingDimensions() throws Exception {
        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();
        dimensions.setId(count.incrementAndGet());

        // Create the Dimensions
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDimensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dimensionsDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDimensions() throws Exception {
        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();
        dimensions.setId(count.incrementAndGet());

        // Create the Dimensions
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDimensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDimensions() throws Exception {
        int databaseSizeBeforeUpdate = dimensionsRepository.findAll().size();
        dimensions.setId(count.incrementAndGet());

        // Create the Dimensions
        DimensionsDto dimensionsDto = dimensionsMapper.toDto(dimensions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDimensionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dimensionsDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dimensions in the database
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDimensions() throws Exception {
        // Initialize the database
        dimensionsRepository.saveAndFlush(dimensions);

        int databaseSizeBeforeDelete = dimensionsRepository.findAll().size();

        // Delete the dimensions
        restDimensionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, dimensions.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dimensions> dimensionsList = dimensionsRepository.findAll();
        assertThat(dimensionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
