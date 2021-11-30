package dev.flaviojunior.service;

import dev.flaviojunior.domain.StudentSettings;
import dev.flaviojunior.repository.StudentSettingsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentSettings}.
 */
@Service
@Transactional
public class StudentSettingsService {

    private final Logger log = LoggerFactory.getLogger(StudentSettingsService.class);

    private final StudentSettingsRepository studentSettingsRepository;

    public StudentSettingsService(StudentSettingsRepository studentSettingsRepository) {
        this.studentSettingsRepository = studentSettingsRepository;
    }

    /**
     * Save a studentSettings.
     *
     * @param studentSettings the entity to save.
     * @return the persisted entity.
     */
    public StudentSettings save(StudentSettings studentSettings) {
        log.debug("Request to save StudentSettings : {}", studentSettings);
        return studentSettingsRepository.save(studentSettings);
    }

    /**
     * Get all the studentSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentSettings> findAll(Pageable pageable) {
        log.debug("Request to get all StudentSettings");
        return studentSettingsRepository.findAll(pageable);
    }

    /**
     * Get one studentSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentSettings> findOne(Long id) {
        log.debug("Request to get StudentSettings : {}", id);
        return studentSettingsRepository.findById(id);
    }

    /**
     * Delete the studentSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentSettings : {}", id);
        studentSettingsRepository.deleteById(id);
    }
}
