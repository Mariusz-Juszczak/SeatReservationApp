package com.seatreservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.seatreservation.IntegrationTest;
import com.seatreservation.domain.SeatReserved;
import com.seatreservation.domain.enumeration.ReservationStatus;
import com.seatreservation.repository.SeatReservedRepository;
import com.seatreservation.service.dto.SeatReservedDto;
import com.seatreservation.service.mapper.SeatReservedMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SeatReservedController} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatReservedControllerIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FROM_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FROM_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ReservationStatus DEFAULT_RESERVATION_STATUS = ReservationStatus.ACTIVE;
    private static final ReservationStatus UPDATED_RESERVATION_STATUS = ReservationStatus.CANCELLED;

    private static final String ENTITY_API_URL = "/api/reserved-seats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatReservedRepository seatReservedRepository;

    @Autowired
    private SeatReservedMapper seatReservedMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatReservedMockMvc;

    private SeatReserved seatReserved;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatReserved createEntity(EntityManager em) {
        SeatReserved seatReserved = new SeatReserved()
            .name(DEFAULT_NAME)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .reservationStatus(DEFAULT_RESERVATION_STATUS);
        return seatReserved;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatReserved createUpdatedEntity(EntityManager em) {
        SeatReserved seatReserved = new SeatReserved()
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .reservationStatus(UPDATED_RESERVATION_STATUS);
        return seatReserved;
    }

    @BeforeEach
    public void initTest() {
        seatReserved = createEntity(em);
    }

    @Test
    @Transactional
    void createSeatReserved() throws Exception {
        int databaseSizeBeforeCreate = seatReservedRepository.findAll().size();
        // Create the SeatReserved
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);
        restSeatReservedMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isCreated());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeCreate + 1);
        SeatReserved testSeatReserved = seatReservedList.get(seatReservedList.size() - 1);
        assertThat(testSeatReserved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeatReserved.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testSeatReserved.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testSeatReserved.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSeatReserved.getReservationStatus()).isEqualTo(DEFAULT_RESERVATION_STATUS);
    }

    @Test
    @Transactional
    void createSeatReservedWithExistingId() throws Exception {
        // Create the SeatReserved with an existing ID
        seatReserved.setId(1L);
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        int databaseSizeBeforeCreate = seatReservedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatReservedMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatReservedRepository.findAll().size();
        // set the field null
        seatReserved.setName(null);

        // Create the SeatReserved, which fails.
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        restSeatReservedMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatReservedRepository.findAll().size();
        // set the field null
        seatReserved.setFromDate(null);

        // Create the SeatReserved, which fails.
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        restSeatReservedMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatReservedRepository.findAll().size();
        // set the field null
        seatReserved.setCreatedDate(null);

        // Create the SeatReserved, which fails.
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        restSeatReservedMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReservationStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatReservedRepository.findAll().size();
        // set the field null
        seatReserved.setReservationStatus(null);

        // Create the SeatReserved, which fails.
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        restSeatReservedMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeatReserveds() throws Exception {
        // Initialize the database
        seatReservedRepository.saveAndFlush(seatReserved);

        // Get all the seatReservedList
        restSeatReservedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seatReserved.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_FROM_DATE))))
            .andExpect(jsonPath("$.[*].toDate").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_TO_DATE))))
            .andExpect(jsonPath("$.[*].createdDate").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].reservationStatus").value(hasItem(DEFAULT_RESERVATION_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSeatReserved() throws Exception {
        // Initialize the database
        seatReservedRepository.saveAndFlush(seatReserved);

        // Get the seatReserved
        restSeatReservedMockMvc
            .perform(get(ENTITY_API_URL_ID, seatReserved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seatReserved.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fromDate").value(TestUtil.sameInstant(DEFAULT_FROM_DATE)))
            .andExpect(jsonPath("$.toDate").value(TestUtil.sameInstant(DEFAULT_TO_DATE)))
            .andExpect(jsonPath("$.createdDate").value(TestUtil.sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.reservationStatus").value(DEFAULT_RESERVATION_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSeatReserved() throws Exception {
        // Get the seatReserved
        restSeatReservedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSeatReserved() throws Exception {
        // Initialize the database
        seatReservedRepository.saveAndFlush(seatReserved);

        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();

        // Update the seatReserved
        SeatReserved updatedSeatReserved = seatReservedRepository.findById(seatReserved.getId()).get();
        // Disconnect from session so that the updates on updatedSeatReserved are not directly saved in db
        em.detach(updatedSeatReserved);
        updatedSeatReserved
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .reservationStatus(UPDATED_RESERVATION_STATUS);
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(updatedSeatReserved);

        restSeatReservedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatReservedDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isOk());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
        SeatReserved testSeatReserved = seatReservedList.get(seatReservedList.size() - 1);
        assertThat(testSeatReserved.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeatReserved.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testSeatReserved.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testSeatReserved.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSeatReserved.getReservationStatus()).isEqualTo(UPDATED_RESERVATION_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSeatReserved() throws Exception {
        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();
        seatReserved.setId(count.incrementAndGet());

        // Create the SeatReserved
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatReservedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatReservedDto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeatReserved() throws Exception {
        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();
        seatReserved.setId(count.incrementAndGet());

        // Create the SeatReserved
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatReservedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeatReserved() throws Exception {
        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();
        seatReserved.setId(count.incrementAndGet());

        // Create the SeatReserved
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatReservedMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatReservedWithPatch() throws Exception {
        // Initialize the database
        seatReservedRepository.saveAndFlush(seatReserved);

        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();

        // Update the seatReserved using partial update
        SeatReserved partialUpdatedSeatReserved = new SeatReserved();
        partialUpdatedSeatReserved.setId(seatReserved.getId());

        partialUpdatedSeatReserved.fromDate(UPDATED_FROM_DATE).createdDate(UPDATED_CREATED_DATE);

        restSeatReservedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatReserved.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatReserved))
            )
            .andExpect(status().isOk());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
        SeatReserved testSeatReserved = seatReservedList.get(seatReservedList.size() - 1);
        assertThat(testSeatReserved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeatReserved.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testSeatReserved.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testSeatReserved.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSeatReserved.getReservationStatus()).isEqualTo(DEFAULT_RESERVATION_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSeatReservedWithPatch() throws Exception {
        // Initialize the database
        seatReservedRepository.saveAndFlush(seatReserved);

        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();

        // Update the seatReserved using partial update
        SeatReserved partialUpdatedSeatReserved = new SeatReserved();
        partialUpdatedSeatReserved.setId(seatReserved.getId());

        partialUpdatedSeatReserved
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .reservationStatus(UPDATED_RESERVATION_STATUS);

        restSeatReservedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatReserved.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatReserved))
            )
            .andExpect(status().isOk());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
        SeatReserved testSeatReserved = seatReservedList.get(seatReservedList.size() - 1);
        assertThat(testSeatReserved.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeatReserved.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testSeatReserved.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testSeatReserved.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSeatReserved.getReservationStatus()).isEqualTo(UPDATED_RESERVATION_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSeatReserved() throws Exception {
        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();
        seatReserved.setId(count.incrementAndGet());

        // Create the SeatReserved
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatReservedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seatReservedDto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeatReserved() throws Exception {
        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();
        seatReserved.setId(count.incrementAndGet());

        // Create the SeatReserved
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatReservedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeatReserved() throws Exception {
        int databaseSizeBeforeUpdate = seatReservedRepository.findAll().size();
        seatReserved.setId(count.incrementAndGet());

        // Create the SeatReserved
        SeatReservedDto seatReservedDto = seatReservedMapper.toDto(seatReserved);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatReservedMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatReservedDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatReserved in the database
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeatReserved() throws Exception {
        // Initialize the database
        seatReservedRepository.saveAndFlush(seatReserved);

        int databaseSizeBeforeDelete = seatReservedRepository.findAll().size();

        // Delete the seatReserved
        restSeatReservedMockMvc
            .perform(delete(ENTITY_API_URL_ID, seatReserved.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SeatReserved> seatReservedList = seatReservedRepository.findAll();
        assertThat(seatReservedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
