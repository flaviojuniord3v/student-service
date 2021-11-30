package dev.flaviojunior.web.rest;

import dev.flaviojunior.common.web.util.HeaderUtil;
import dev.flaviojunior.common.web.util.PaginationUtil;
import dev.flaviojunior.common.web.util.ResponseUtil;
import dev.flaviojunior.domain.StudentRelationship;
import dev.flaviojunior.repository.StudentRelationshipRepository;
import dev.flaviojunior.service.StudentRelationshipService;
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
 * REST controller for managing {@link StudentRelationship}.
 */
@RestController
@RequestMapping("/api")
public class StudentRelationshipResource {

    private static final String ENTITY_NAME = "studentRelationship";
    private final Logger log = LoggerFactory.getLogger(StudentRelationshipResource.class);
    private final StudentRelationshipService studentRelationshipService;
    private final StudentRelationshipRepository studentRelationshipRepository;
    @Value("${spring.application.name}")
    private String applicationName;

    public StudentRelationshipResource(
            StudentRelationshipService studentRelationshipService,
            StudentRelationshipRepository studentRelationshipRepository
    ) {
        this.studentRelationshipService = studentRelationshipService;
        this.studentRelationshipRepository = studentRelationshipRepository;
    }

    /**
     * {@code POST  /student-relationships} : Creates a new studentRelationship.
     *
     * @param studentRelationship A studentRelationship to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentRelationship, or with status {@code 400 (Bad Request)} if the studentRelationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-relationships")
    public ResponseEntity<StudentRelationship> createStudentRelationship(@Valid @RequestBody StudentRelationship studentRelationship)
            throws URISyntaxException {
        log.debug("REST request to save StudentRelationship : {}", studentRelationship);
        if (studentRelationship.getId() != null) {
            throw new BadRequestAlertException("A new studentRelationship cannot already have an ID", ENTITY_NAME);
        }
        StudentRelationship result = studentRelationshipService.save(studentRelationship);
        return ResponseEntity
                .created(new URI("/api/student-relationships/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /student-relationships/:id} : Updates an existing studentRelationship.
     *
     * @param id                  the id of the studentRelationship to save.
     * @param studentRelationship the studentRelationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentRelationship,
     * or with status {@code 400 (Bad Request)} if the studentRelationship is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentRelationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-relationships/{id}")
    public ResponseEntity<StudentRelationship> updateStudentRelationship(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody StudentRelationship studentRelationship
    ) throws URISyntaxException {
        log.debug("REST request to update StudentRelationship : {}, {}", id, studentRelationship);
        if (studentRelationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME);
        }
        if (!Objects.equals(id, studentRelationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME);
        }

        if (!studentRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME);
        }

        StudentRelationship result = studentRelationshipService.save(studentRelationship);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, ENTITY_NAME, studentRelationship.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /student-relationships} : get all the studentRelationships.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentRelationships in body.
     */
    @GetMapping("/student-relationships")
    public ResponseEntity<List<StudentRelationship>> getAllStudentRelationships(Pageable pageable) {
        log.debug("REST request to get a page of StudentRelationships");
        Page<StudentRelationship> page = studentRelationshipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-relationships/:id} : get the "id" studentRelationship.
     *
     * @param id the id of the studentRelationship to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentRelationship, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-relationships/{id}")
    public ResponseEntity<StudentRelationship> getStudentRelationship(@PathVariable Long id) {
        log.debug("REST request to get StudentRelationship : {}", id);
        Optional<StudentRelationship> studentRelationship = studentRelationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentRelationship);
    }

    /**
     * {@code DELETE  /student-relationships/:id} : delete the "id" studentRelationship.
     *
     * @param id the id of the studentRelationship to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-relationships/{id}")
    public ResponseEntity<Void> deleteStudentRelationship(@PathVariable Long id) {
        log.debug("REST request to delete StudentRelationship : {}", id);
        studentRelationshipService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, ENTITY_NAME, id.toString()))
                .build();
    }
}
