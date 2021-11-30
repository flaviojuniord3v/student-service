package dev.flaviojunior.service;

import dev.flaviojunior.domain.PersonAddress;
import dev.flaviojunior.repository.PersonAddressRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonAddress}.
 */
@Service
@Transactional
public class PersonAddressService {

    private final Logger log = LoggerFactory.getLogger(PersonAddressService.class);

    private final PersonAddressRepository personAddressRepository;

    public PersonAddressService(PersonAddressRepository personAddressRepository) {
        this.personAddressRepository = personAddressRepository;
    }

    /**
     * Save a personAddress.
     *
     * @param personAddress the entity to save.
     * @return the persisted entity.
     */
    public PersonAddress save(PersonAddress personAddress) {
        log.debug("Request to save PersonAddress : {}", personAddress);
        return personAddressRepository.save(personAddress);
    }

    /**
     * Get all the personAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonAddress> findAll(Pageable pageable) {
        log.debug("Request to get all PersonAddresses");
        return personAddressRepository.findAll(pageable);
    }

    /**
     * Get one personAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonAddress> findOne(Long id) {
        log.debug("Request to get PersonAddress : {}", id);
        return personAddressRepository.findById(id);
    }

    /**
     * Delete the personAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonAddress : {}", id);
        personAddressRepository.deleteById(id);
    }
}
