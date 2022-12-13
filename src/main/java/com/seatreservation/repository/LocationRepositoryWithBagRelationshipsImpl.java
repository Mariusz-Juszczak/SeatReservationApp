package com.seatreservation.repository;

import com.seatreservation.domain.Location;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class LocationRepositoryWithBagRelationshipsImpl implements LocationRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Location> fetchBagRelationships(Optional<Location> location) {
        return location.map(this::fetchLocationAdmins);
    }

    @Override
    public Page<Location> fetchBagRelationships(Page<Location> locations) {
        return new PageImpl<>(fetchBagRelationships(locations.getContent()), locations.getPageable(), locations.getTotalElements());
    }

    @Override
    public List<Location> fetchBagRelationships(List<Location> locations) {
        return Optional.of(locations).map(this::fetchLocationAdmins).orElse(Collections.emptyList());
    }

    Location fetchLocationAdmins(Location result) {
        return entityManager
            .createQuery(
                "select location from Location location left join fetch location.locationAdmins where location is :location",
                Location.class
            )
            .setParameter("location", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Location> fetchLocationAdmins(List<Location> locations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, locations.size()).forEach(index -> order.put(locations.get(index).getId(), index));
        List<Location> result = entityManager
            .createQuery(
                "select distinct location from Location location left join fetch location.locationAdmins where location in :locations",
                Location.class
            )
            .setParameter("locations", locations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
