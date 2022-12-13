package com.seatreservation.web.rest;

import com.seatreservation.domain.Address;
import com.seatreservation.service.AddressService;
import com.seatreservation.service.dto.AddressDto;
import com.seatreservation.service.mapper.AddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.seatreservation.utils.EntityAlertUtil.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public AddressController(AddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @PostMapping()
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) throws URISyntaxException {
        log.debug("REST request to save Address : {}", addressDto);
        Address createdAddress = addressService.save(addressMapper.toEntity(addressDto));
        AddressDto result = addressMapper.toDto(createdAddress);
        return ResponseEntity.created(new URI("/api/addresses/" + result.getId())).headers(createEntityCreationAlert(Address.class, result.getId().toString())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody AddressDto addressDto) throws URISyntaxException {
        Address createdAddress = addressService.update(addressMapper.toEntity(addressDto), id);
        AddressDto result = addressMapper.toDto(createdAddress);
        log.debug("REST request to update Address : {}, {}", id, addressDto);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Address.class, addressDto.getId().toString())).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<AddressDto>> getAllAddresses(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @RequestParam(required = false) String filter) {
        if ("building-is-null".equals(filter)) {
            log.debug("REST request to get all Addresss where building is null");
            return new ResponseEntity<>(addressMapper.toDto(addressService.findAllWhereBuildingIsNull()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Addresses");
        Page<AddressDto> page = addressService.findAll(pageable).map(addressMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {
        log.debug("REST request to get Address : {}", id);
        Optional<AddressDto> addressDto = addressService.findOne(id).map(addressMapper::toDto);
        return ResponseUtil.wrapOrNotFound(addressDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.debug("REST request to delete Address : {}", id);
        addressService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Address.class, id.toString())).build();
    }
}
