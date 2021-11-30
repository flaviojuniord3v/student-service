package dev.flaviojunior.web.rest;

import dev.flaviojunior.common.web.util.HeaderUtil;
import dev.flaviojunior.common.web.util.PaginationUtil;
import dev.flaviojunior.common.web.util.ResponseUtil;
import dev.flaviojunior.domain.PersonAddress;
import dev.flaviojunior.repository.PersonAddressRepository;
import dev.flaviojunior.service.PersonAddressService;
import dev.flaviojunior.common.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link PersonAddress}.
 */
@RestController
@RequestMapping("/api")
public class PersonAddressResource {

    private static final String ENTITY_NAME = "personAddress";
    private final Logger log = LoggerFactory.getLogger(PersonAddressResource.class);
    private final PersonAddressService personAddressService;
    private final PersonAddressRepository personAddressRepository;
    @Value("${spring.application.name}")
    private String applicationName;

    public PersonAddressResource(PersonAddressService personAddressService, PersonAddressRepository personAddressRepository) {
        this.personAddressService = personAddressService;
        this.personAddressRepository = personAddressRepository;
    }

    /**
     * {@code POST  /person-addresses} : Creates a new personAddress.
     *
     * @param personAddress A personAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personAddress, or with status {@code 400 (Bad Request)} if the personAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-addresses")
    public ResponseEntity<PersonAddress> createPersonAddress(@Valid @RequestBody PersonAddress personAddress) throws URISyntaxException {
        log.debug("REST request to save PersonAddress : {}", personAddress);
        if (personAddress.getId() != null) {
            throw new BadRequestAlertException("A new personAddress cannot already have an ID", ENTITY_NAME);
        }
        PersonAddress result = personAddressService.save(personAddress);
        return ResponseEntity
                .created(new URI("/api/person-addresses/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /person-addresses/:id} : Updates an existing personAddress.
     *
     * @param id            the id of the personAddress to save.
     * @param personAddress the personAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personAddress,
     * or with status {@code 400 (Bad Request)} if the personAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-addresses/{id}")
    public ResponseEntity<PersonAddress> updatePersonAddress(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody PersonAddress personAddress
    ) throws URISyntaxException {
        log.debug("REST request to update PersonAddress : {}, {}", id, personAddress);
        if (personAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME);
        }
        if (!Objects.equals(id, personAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME);
        }

        if (!personAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME);
        }

        PersonAddress result = personAddressService.save(personAddress);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, ENTITY_NAME, personAddress.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /person-addresses} : get all the personAddresses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personAddresses in body.
     */
    @GetMapping("/person-addresses")
    public ResponseEntity<List<PersonAddress>> getAllPersonAddresses(Pageable pageable) {
        log.debug("REST request to get a page of PersonAddresses");
        Page<PersonAddress> page = personAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /person-addresses/:id} : get the "id" personAddress.
     *
     * @param id the id of the personAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-addresses/{id}")
    public ResponseEntity<PersonAddress> getPersonAddress(@PathVariable Long id) {
        log.debug("REST request to get PersonAddress : {}", id);
        Optional<PersonAddress> personAddress = personAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personAddress);
    }

    /**
     * {@code DELETE  /person-addresses/:id} : delete the "id" personAddress.
     *
     * @param id the id of the personAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-addresses/{id}")
    public ResponseEntity<Void> deletePersonAddress(@PathVariable Long id) {
        log.debug("REST request to delete PersonAddress : {}", id);
        personAddressService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, ENTITY_NAME, id.toString()))
                .build();
    }
}
