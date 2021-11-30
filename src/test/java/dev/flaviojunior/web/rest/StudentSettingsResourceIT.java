package dev.flaviojunior.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.flaviojunior.IntegrationTest;
import dev.flaviojunior.domain.StudentSettings;
import dev.flaviojunior.repository.StudentSettingsRepository;
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
 * Integration tests for the {@link StudentSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class StudentSettingsResourceIT {

    private static final Boolean DEFAULT_RECEIVE_EMAIL_NOTIFICATIONS = false;
    private static final Boolean UPDATED_RECEIVE_EMAIL_NOTIFICATIONS = true;

    private static final String ENTITY_API_URL = "/api/student-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentSettingsRepository studentSettingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentSettingsMockMvc;

    private StudentSettings studentSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentSettings createEntity(EntityManager em) {
        StudentSettings studentSettings = new StudentSettings().receiveEmailNotifications(DEFAULT_RECEIVE_EMAIL_NOTIFICATIONS);
        return studentSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentSettings createUpdatedEntity(EntityManager em) {
        StudentSettings studentSettings = new StudentSettings().receiveEmailNotifications(UPDATED_RECEIVE_EMAIL_NOTIFICATIONS);
        return studentSettings;
    }

    @BeforeEach
    public void initTest() {
        studentSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentSettings() throws Exception {
        int databaseSizeBeforeCreate = studentSettingsRepository.findAll().size();
        // Create the StudentSettings
        restStudentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSettings))
            )
            .andExpect(status().isCreated());

        // Validate the StudentSettings in the database
        List<StudentSettings> studentSettingsList = studentSettingsRepository.findAll();
        assertThat(studentSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentSettings testStudentSettings = studentSettingsList.get(studentSettingsList.size() - 1);
        assertThat(testStudentSettings.getReceiveEmailNotifications()).isEqualTo(DEFAULT_RECEIVE_EMAIL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void createStudentSettingsWithExistingId() throws Exception {
        // Create the StudentSettings with an existing ID
        studentSettings.setId(1L);

        int databaseSizeBeforeCreate = studentSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSettings in the database
        List<StudentSettings> studentSettingsList = studentSettingsRepository.findAll();
        assertThat(studentSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentSettings() throws Exception {
        // Initialize the database
        studentSettingsRepository.saveAndFlush(studentSettings);

        // Get all the studentSettingsList
        restStudentSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].receiveEmailNotifications").value(hasItem(DEFAULT_RECEIVE_EMAIL_NOTIFICATIONS.booleanValue())));
    }

    @Test
    @Transactional
    void getStudentSettings() throws Exception {
        // Initialize the database
        studentSettingsRepository.saveAndFlush(studentSettings);

        // Get the studentSettings
        restStudentSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, studentSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentSettings.getId().intValue()))
            .andExpect(jsonPath("$.receiveEmailNotifications").value(DEFAULT_RECEIVE_EMAIL_NOTIFICATIONS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingStudentSettings() throws Exception {
        // Get the studentSettings
        restStudentSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentSettings() throws Exception {
        // Initialize the database
        studentSettingsRepository.saveAndFlush(studentSettings);

        int databaseSizeBeforeUpdate = studentSettingsRepository.findAll().size();

        // Update the studentSettings
        StudentSettings updatedStudentSettings = studentSettingsRepository.findById(studentSettings.getId()).get();
        // Disconnect from session so that the updates on updatedStudentSettings are not directly saved in db
        em.detach(updatedStudentSettings);
        updatedStudentSettings.receiveEmailNotifications(UPDATED_RECEIVE_EMAIL_NOTIFICATIONS);

        restStudentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudentSettings))
            )
            .andExpect(status().isOk());

        // Validate the StudentSettings in the database
        List<StudentSettings> studentSettingsList = studentSettingsRepository.findAll();
        assertThat(studentSettingsList).hasSize(databaseSizeBeforeUpdate);
        StudentSettings testStudentSettings = studentSettingsList.get(studentSettingsList.size() - 1);
        assertThat(testStudentSettings.getReceiveEmailNotifications()).isEqualTo(UPDATED_RECEIVE_EMAIL_NOTIFICATIONS);
    }

    @Test
    @Transactional
    void putNonExistingStudentSettings() throws Exception {
        int databaseSizeBeforeUpdate = studentSettingsRepository.findAll().size();
        studentSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSettings in the database
        List<StudentSettings> studentSettingsList = studentSettingsRepository.findAll();
        assertThat(studentSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentSettings() throws Exception {
        int databaseSizeBeforeUpdate = studentSettingsRepository.findAll().size();
        studentSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSettings in the database
        List<StudentSettings> studentSettingsList = studentSettingsRepository.findAll();
        assertThat(studentSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentSettings() throws Exception {
        int databaseSizeBeforeUpdate = studentSettingsRepository.findAll().size();
        studentSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSettings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentSettings in the database
        List<StudentSettings> studentSettingsList = studentSettingsRepository.findAll();
        assertThat(studentSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentSettings() throws Exception {
        // Initialize the database
        studentSettingsRepository.saveAndFlush(studentSettings);

        int databaseSizeBeforeDelete = studentSettingsRepository.findAll().size();

        // Delete the studentSettings
        restStudentSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentSettings> studentSettingsList = studentSettingsRepository.findAll();
        assertThat(studentSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
