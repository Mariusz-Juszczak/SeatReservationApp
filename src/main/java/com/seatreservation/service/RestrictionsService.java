package com.seatreservation.service;

import com.seatreservation.domain.Restrictions;
import com.seatreservation.repository.RestrictionsRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.seatreservation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestrictionsService {

    private final Logger log = LoggerFactory.getLogger(RestrictionsService.class);
    private final RestrictionsRepository restrictionsRepository;
    private final NewRestrictionTrigger newRestrictionTrigger;
    private final ApplicationNotificationService applicationNotificationService;


    public RestrictionsService(RestrictionsRepository restrictionsRepository, NewRestrictionTrigger newRestrictionTrigger, ApplicationNotificationService applicationNotificationService) {
        this.restrictionsRepository = restrictionsRepository;
        this.newRestrictionTrigger = newRestrictionTrigger;
        this.applicationNotificationService = applicationNotificationService;
    }

    public Restrictions save(Restrictions restrictions) {
        if (restrictions.getId() != null) {
            throw new BadRequestAlertException("A new restrictions cannot already have an ID", Restrictions.class, "idexists");
        }
        log.debug("Request to save Restrictions : {}", restrictions);
        restrictionsRepository.save(restrictions);
        newRestrictionTrigger.execute(restrictions);
        applicationNotificationService.createNewRestrictionNotifications(restrictions);
        return restrictions;
    }

    public Restrictions update(Restrictions restrictions, Long id) {
        if (restrictions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Restrictions.class, "id null");
        }
        if (!Objects.equals(id, restrictions.getId())) {
            throw new BadRequestAlertException("Invalid ID", Restrictions.class, "id invalid");
        }
        if (!restrictionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Restrictions.class, "id not found");
        }
        log.debug("Request to save Restrictions : {}", restrictions);
        return restrictionsRepository.save(restrictions);
    }


    @Transactional(readOnly = true)
    public Page<Restrictions> findAll(Pageable pageable) {
        log.debug("Request to get all Restrictions");
        return restrictionsRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Restrictions> findOne(Long id) {
        log.debug("Request to get Restrictions : {}", id);
        return restrictionsRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Restrictions> findCurrent() {
        log.debug("Request to get current Restrictions");
        ZonedDateTime now = ZonedDateTime.now();
        List<Restrictions> currentRestrictionsList = restrictionsRepository.findByToDateAfterAndFromDateBeforeOrderByFromDate(now, now);
        return currentRestrictionsList.stream()
            .findFirst();
    }

    @Transactional(readOnly = true)
    public List<Restrictions> findByPeriodTime(ZonedDateTime fromDate, ZonedDateTime toDate) {
        log.debug("Request to get Restrictions by period time: {} - {}", fromDate, toDate);
        return restrictionsRepository.findByToDateAfterAndFromDateBeforeOrderByFromDate(fromDate, toDate);
    }

    public void delete(Long id) {
        log.debug("Request to delete Restrictions : {}", id);
        restrictionsRepository.deleteById(id);
    }
}
