package com.seatreservation.service;

import com.seatreservation.domain.Address;
import com.seatreservation.repository.AddressRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.seatreservation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddressService {
    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    public Address save(Address address) {
        if (address.getId() != null) {
            throw new BadRequestAlertException("A new address cannot already have an ID", Address.class , "id exists");
        }
        log.debug("Request to save Address : {}", address);
        return addressRepository.save(address);
    }

    public Address update(Address address, Long id) {
        if (address.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Address.class, "id null");
        }
        if (!Objects.equals(id, address.getId())) {
            throw new BadRequestAlertException("Invalid ID", Address.class, "id invalid");
        }

        if (!addressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Address.class, "id not found");
        }
        log.debug("Request to save Address : {}", address);

        return addressRepository.save(address);

    }



    @Transactional(readOnly = true)
    public Page<Address> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Address> findAllWhereBuildingIsNull() {
        log.debug("Request to get all addresses where Building is null");
        return StreamSupport
            .stream(addressRepository.findAll().spliterator(), false)
            .filter(address -> address.getBuilding() == null)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<Address> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
    }

}
