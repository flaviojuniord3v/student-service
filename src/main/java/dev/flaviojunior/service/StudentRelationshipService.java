package dev.flaviojunior.service;

import dev.flaviojunior.domain.StudentRelationship;
import dev.flaviojunior.repository.StudentRelationshipRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentRelationship}.
 */
@Service
@Transactional
public class StudentRelationshipService {

    private final Logger log = LoggerFactory.getLogger(StudentRelationshipService.class);

    private final StudentRelationshipRepository studentRelationshipRepository;

    public StudentRelationshipService(StudentRelationshipRepository studentRelationshipRepository) {
        this.studentRelationshipRepository = studentRelationshipRepository;
    }

    /**
     * Save a studentRelationship.
     *
     * @param studentRelationship the entity to save.
     * @return the persisted entity.
     */
    public StudentRelationship save(StudentRelationship studentRelationship) {
        log.debug("Request to save StudentRelationship : {}", studentRelationship);
        return studentRelationshipRepository.save(studentRelationship);
    }

    /**
     * Get all the studentRelationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentRelationship> findAll(Pageable pageable) {
        log.debug("Request to get all StudentRelationships");
        return studentRelationshipRepository.findAll(pageable);
    }

    /**
     * Get one studentRelationship by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentRelationship> findOne(Long id) {
        log.debug("Request to get StudentRelationship : {}", id);
        return studentRelationshipRepository.findById(id);
    }

    /**
     * Delete the studentRelationship by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentRelationship : {}", id);
        studentRelationshipRepository.deleteById(id);
    }
}
