package dev.flaviojunior.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.flaviojunior.IntegrationTest;
import dev.flaviojunior.domain.StudentRelationship;
import dev.flaviojunior.domain.enumeration.TypeOfRelationship;
import dev.flaviojunior.repository.StudentRelationshipRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StudentRelationshipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class StudentRelationshipResourceIT {

    private static final TypeOfRelationship DEFAULT_TYPE = TypeOfRelationship.FATHER;
    private static final TypeOfRelationship UPDATED_TYPE = TypeOfRelationship.MOTHER;

    private static final String ENTITY_API_URL = "/api/student-relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentRelationshipRepository studentRelationshipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentRelationshipMockMvc;

    private StudentRelationship studentRelationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRelationship createEntity(EntityManager em) {
        StudentRelationship studentRelationship = new StudentRelationship().type(DEFAULT_TYPE);
        return studentRelationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRelationship createUpdatedEntity(EntityManager em) {
        StudentRelationship studentRelationship = new StudentRelationship().type(UPDATED_TYPE);
        return studentRelationship;
    }

    @BeforeEach
    public void initTest() {
        studentRelationship = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentRelationship() throws Exception {
        int databaseSizeBeforeCreate = studentRelationshipRepository.findAll().size();
        // Create the StudentRelationship
        restStudentRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRelationship))
            )
            .andExpect(status().isCreated());

        // Validate the StudentRelationship in the database
        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeCreate + 1);
        StudentRelationship testStudentRelationship = studentRelationshipList.get(studentRelationshipList.size() - 1);
        assertThat(testStudentRelationship.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createStudentRelationshipWithExistingId() throws Exception {
        // Create the StudentRelationship with an existing ID
        studentRelationship.setId(1L);

        int databaseSizeBeforeCreate = studentRelationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRelationship in the database
        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRelationshipRepository.findAll().size();
        // set the field null
        studentRelationship.setType(null);

        // Create the StudentRelationship, which fails.

        restStudentRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRelationship))
            )
            .andExpect(status().isBadRequest());

        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentRelationships() throws Exception {
        // Initialize the database
        studentRelationshipRepository.saveAndFlush(studentRelationship);

        // Get all the studentRelationshipList
        restStudentRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getStudentRelationship() throws Exception {
        // Initialize the database
        studentRelationshipRepository.saveAndFlush(studentRelationship);

        // Get the studentRelationship
        restStudentRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, studentRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentRelationship.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStudentRelationship() throws Exception {
        // Get the studentRelationship
        restStudentRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentRelationship() throws Exception {
        // Initialize the database
        studentRelationshipRepository.saveAndFlush(studentRelationship);

        int databaseSizeBeforeUpdate = studentRelationshipRepository.findAll().size();

        // Update the studentRelationship
        StudentRelationship updatedStudentRelationship = studentRelationshipRepository.findById(studentRelationship.getId()).get();
        // Disconnect from session so that the updates on updatedStudentRelationship are not directly saved in db
        em.detach(updatedStudentRelationship);
        updatedStudentRelationship.type(UPDATED_TYPE);

        restStudentRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudentRelationship))
            )
            .andExpect(status().isOk());

        // Validate the StudentRelationship in the database
        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeUpdate);
        StudentRelationship testStudentRelationship = studentRelationshipList.get(studentRelationshipList.size() - 1);
        assertThat(testStudentRelationship.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingStudentRelationship() throws Exception {
        int databaseSizeBeforeUpdate = studentRelationshipRepository.findAll().size();
        studentRelationship.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRelationship in the database
        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentRelationship() throws Exception {
        int databaseSizeBeforeUpdate = studentRelationshipRepository.findAll().size();
        studentRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRelationship in the database
        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentRelationship() throws Exception {
        int databaseSizeBeforeUpdate = studentRelationshipRepository.findAll().size();
        studentRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRelationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentRelationship in the database
        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentRelationship() throws Exception {
        // Initialize the database
        studentRelationshipRepository.saveAndFlush(studentRelationship);

        int databaseSizeBeforeDelete = studentRelationshipRepository.findAll().size();

        // Delete the studentRelationship
        restStudentRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentRelationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentRelationship> studentRelationshipList = studentRelationshipRepository.findAll();
        assertThat(studentRelationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
