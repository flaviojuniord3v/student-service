package dev.flaviojunior.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.flaviojunior.IntegrationTest;
import dev.flaviojunior.domain.PersonAddress;
import dev.flaviojunior.domain.enumeration.TypeOfAddress;
import dev.flaviojunior.repository.PersonAddressRepository;
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
 * Integration tests for the {@link PersonAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class PersonAddressResourceIT {

    private static final TypeOfAddress DEFAULT_TYPE = TypeOfAddress.HOME;
    private static final TypeOfAddress UPDATED_TYPE = TypeOfAddress.WORK;

    private static final String ENTITY_API_URL = "/api/person-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonAddressRepository personAddressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonAddressMockMvc;

    private PersonAddress personAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonAddress createEntity(EntityManager em) {
        PersonAddress personAddress = new PersonAddress().type(DEFAULT_TYPE);
        return personAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonAddress createUpdatedEntity(EntityManager em) {
        PersonAddress personAddress = new PersonAddress().type(UPDATED_TYPE);
        return personAddress;
    }

    @BeforeEach
    public void initTest() {
        personAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonAddress() throws Exception {
        int databaseSizeBeforeCreate = personAddressRepository.findAll().size();
        // Create the PersonAddress
        restPersonAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAddress)))
            .andExpect(status().isCreated());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PersonAddress testPersonAddress = personAddressList.get(personAddressList.size() - 1);
        assertThat(testPersonAddress.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createPersonAddressWithExistingId() throws Exception {
        // Create the PersonAddress with an existing ID
        personAddress.setId(1L);

        int databaseSizeBeforeCreate = personAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAddress)))
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personAddressRepository.findAll().size();
        // set the field null
        personAddress.setType(null);

        // Create the PersonAddress, which fails.

        restPersonAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAddress)))
            .andExpect(status().isBadRequest());

        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonAddresses() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        // Get all the personAddressList
        restPersonAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getPersonAddress() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        // Get the personAddress
        restPersonAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, personAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personAddress.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonAddress() throws Exception {
        // Get the personAddress
        restPersonAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonAddress() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();

        // Update the personAddress
        PersonAddress updatedPersonAddress = personAddressRepository.findById(personAddress.getId()).get();
        // Disconnect from session so that the updates on updatedPersonAddress are not directly saved in db
        em.detach(updatedPersonAddress);
        updatedPersonAddress.type(UPDATED_TYPE);

        restPersonAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonAddress))
            )
            .andExpect(status().isOk());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
        PersonAddress testPersonAddress = personAddressList.get(personAddressList.size() - 1);
        assertThat(testPersonAddress.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAddress)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonAddress() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        int databaseSizeBeforeDelete = personAddressRepository.findAll().size();

        // Delete the personAddress
        restPersonAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, personAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
