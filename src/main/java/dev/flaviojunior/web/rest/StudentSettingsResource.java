package dev.flaviojunior.web.rest;

import dev.flaviojunior.common.web.util.HeaderUtil;
import dev.flaviojunior.common.web.util.PaginationUtil;
import dev.flaviojunior.common.web.util.ResponseUtil;
import dev.flaviojunior.domain.StudentSettings;
import dev.flaviojunior.repository.StudentSettingsRepository;
import dev.flaviojunior.service.StudentSettingsService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link StudentSettings}.
 */
@RestController
@RequestMapping("/api")
public class StudentSettingsResource {

    private static final String ENTITY_NAME = "studentSettings";
    private final Logger log = LoggerFactory.getLogger(StudentSettingsResource.class);
    private final StudentSettingsService studentSettingsService;
    private final StudentSettingsRepository studentSettingsRepository;
    @Value("${spring.application.name}")
    private String applicationName;

    public StudentSettingsResource(StudentSettingsService studentSettingsService, StudentSettingsRepository studentSettingsRepository) {
        this.studentSettingsService = studentSettingsService;
        this.studentSettingsRepository = studentSettingsRepository;
    }

    /**
     * {@code POST  /student-settings} : Creates a new studentSettings.
     *
     * @param studentSettings A studentSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentSettings, or with status {@code 400 (Bad Request)} if the studentSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-settings")
    public ResponseEntity<StudentSettings> createStudentSettings(@RequestBody StudentSettings studentSettings) throws URISyntaxException {
        log.debug("REST request to save StudentSettings : {}", studentSettings);
        if (studentSettings.getId() != null) {
            throw new BadRequestAlertException("A new studentSettings cannot already have an ID", ENTITY_NAME);
        }
        StudentSettings result = studentSettingsService.save(studentSettings);
        return ResponseEntity
                .created(new URI("/api/student-settings/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /student-settings/:id} : Updates an existing studentSettings.
     *
     * @param id              the id of the studentSettings to save.
     * @param studentSettings the studentSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentSettings,
     * or with status {@code 400 (Bad Request)} if the studentSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-settings/{id}")
    public ResponseEntity<StudentSettings> updateStudentSettings(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody StudentSettings studentSettings
    ) throws URISyntaxException {
        log.debug("REST request to update StudentSettings : {}, {}", id, studentSettings);
        if (studentSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME);
        }
        if (!Objects.equals(id, studentSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME);
        }

        if (!studentSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME);
        }

        StudentSettings result = studentSettingsService.save(studentSettings);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, ENTITY_NAME, studentSettings.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /student-settings} : get all the studentSettings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentSettings in body.
     */
    @GetMapping("/student-settings")
    public ResponseEntity<List<StudentSettings>> getAllStudentSettings(Pageable pageable) {
        log.debug("REST request to get a page of StudentSettings");
        Page<StudentSettings> page = studentSettingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-settings/:id} : get the "id" studentSettings.
     *
     * @param id the id of the studentSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-settings/{id}")
    public ResponseEntity<StudentSettings> getStudentSettings(@PathVariable Long id) {
        log.debug("REST request to get StudentSettings : {}", id);
        Optional<StudentSettings> studentSettings = studentSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentSettings);
    }

    /**
     * {@code DELETE  /student-settings/:id} : delete the "id" studentSettings.
     *
     * @param id the id of the studentSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-settings/{id}")
    public ResponseEntity<Void> deleteStudentSettings(@PathVariable Long id) {
        log.debug("REST request to delete StudentSettings : {}", id);
        studentSettingsService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, ENTITY_NAME, id.toString()))
                .build();
    }
}
